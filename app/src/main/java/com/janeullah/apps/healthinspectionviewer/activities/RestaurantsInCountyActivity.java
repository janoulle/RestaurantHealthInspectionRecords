package com.janeullah.apps.healthinspectionviewer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.janeullah.apps.healthinspectionviewer.R;
import com.janeullah.apps.healthinspectionviewer.constants.AppConstants;
import com.janeullah.apps.healthinspectionviewer.constants.IntentNames;
import com.janeullah.apps.healthinspectionviewer.dtos.FlattenedRestaurant;
import com.janeullah.apps.healthinspectionviewer.services.FirebaseInitialization;
import com.janeullah.apps.healthinspectionviewer.viewholder.RestaurantViewHolder;

import org.parceler.Parcels;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * https://developer.android.com/training/implementing-navigation/ancestral.html
 * http://stackoverflow.com/questions/26435231/getactionbar-returns-null-appcompat-v7-21
 * http://stackoverflow.com/questions/5049852/android-drawing-separator-divider-line-in-layout
 * https://guides.codepath.com/android/using-parcelable
 * https://guides.codepath.com/android/Using-Parceler
 *
 * @author Jane Ullah
 * @date 4/22/2017.
 */

public class RestaurantsInCountyActivity extends BaseActivity {
    private static final String TAG = "RestaurantsListing";

    private DatabaseReference negaRestaurantsDatabaseReference;
    private Query mQuery;
    private FirebaseRecyclerAdapter<FlattenedRestaurant, RestaurantViewHolder> mAdapter;
    private String mCountyName;
    private AtomicInteger countOfRestaurants = new AtomicInteger(0);

    @BindView(R.id.restaurants_in_county_recyclerview)
    protected RecyclerView mRecycler;

    @BindView(R.id.titleForRestaurantsInCounty)
    protected TextView headerForView;

    @BindView(R.id.app_toolbar)
    public Toolbar mAppToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants_in_county);
        ButterKnife.bind(this);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mCountyName = getIntent().getStringExtra(IntentNames.COUNTY_SELECTED);
        if (mCountyName == null) {
            Log.e(TAG,"County not selected");
            throw new IllegalArgumentException("Must pass a county selection");
        }
        showProgressDialog(String.format(Locale.getDefault(),"Loading restaurants in %s", mCountyName));

        setSupportActionBar(mAppToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(String.format(Locale.getDefault(),"Restaurants In %s", mCountyName));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(layoutManager);

        negaRestaurantsDatabaseReference = FirebaseInitialization.getInstance()
                .getNegaDatabaseReference()
                .child("restaurants");
        negaRestaurantsDatabaseReference.keepSynced(true);

        mQuery = negaRestaurantsDatabaseReference
                .orderByChild("county")
                .equalTo(mCountyName);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecycler.getContext(),
                layoutManager.getOrientation());
        mRecycler.addItemDecoration(dividerItemDecoration);
        logViewEvent(TAG);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            loadActivity(this, AboutActivity.class);
            return true;
        } else if(id == R.id.action_legal){
            loadActivity(this, LegalActivity.class);
            return true;
        }else if (id == android.R.id.home) {
            Log.i(TAG, "Up clicked!");
            Intent upIntent = NavUtils.getParentActivityIntent(this);
            navigateUp(this,upIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        super.onResume();
        showProgressDialog("Loading restaurants in " + mCountyName);
    }

    @Override
    public void onStart() {
        super.onStart();

        mAdapter = new RestaurantsInCountyAdapter(FlattenedRestaurant.class, R.layout.item_flattenedrestaurant, RestaurantViewHolder.class, mQuery);
        mRecycler.setAdapter(mAdapter);

        //https://gist.github.com/puf/f49a1b07e92952b44f2dc36d9af04e3c#file-mainactivity-java-L102
        //http://stackoverflow.com/questions/34982347/how-to-be-notified-when-firebaselistadapter-finishes
        updateTextViewHeader();
    }

    private void updateTextViewHeader() {
        mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // the initial data has been loaded, hide the progress bar
                Log.i(TAG, "Count of datasnapshot: " + dataSnapshot.getChildrenCount());
                headerForView.setText(String.format(Locale.getDefault(),"%d found", mAdapter.getItemCount()));
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e(TAG, "Error setting restaurants in county count " + firebaseError.getDetails());
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

    private class RestaurantsInCountyAdapter extends FirebaseRecyclerAdapter<FlattenedRestaurant, RestaurantViewHolder>{
        private static final String TAG = "RICAdapter";
        /**
         * @param modelClass      Firebase will marshall the data at a location into
         *                        an instance of a class that you provide
         * @param modelLayout     This is the layout used to represent a single item in the list.
         *                        You will be responsible for populating an instance of the corresponding
         *                        view with the data from an instance of modelClass.
         * @param viewHolderClass The class that hold references to all sub-views in an instance modelLayout.
         * @param ref             The Firebase location to watch for data changes. Can also be a slice of a location,
         *                        using some combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
         */
        public RestaurantsInCountyAdapter(Class<FlattenedRestaurant> modelClass, int modelLayout, Class<RestaurantViewHolder> viewHolderClass, Query ref) {
            super(modelClass, modelLayout, viewHolderClass, ref);
        }

        @Override
        protected void populateViewHolder(RestaurantViewHolder viewHolder, FlattenedRestaurant model, int position) {
            final DatabaseReference queryRef = getRef(position);
            String key = queryRef.getKey();
            Log.v(TAG, "Key: " + key);
            viewHolder.bindData(model);
            hideProgressDialog();
            countOfRestaurants.incrementAndGet();
            addListenerToRestaurantItem(viewHolder, model);
        }

        private void addListenerToRestaurantItem(RestaurantViewHolder viewHolder, final FlattenedRestaurant model) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent intent = new Intent(RestaurantsInCountyActivity.this, RestaurantDataActivity.class);
                    Log.i(TAG,String.format(Locale.getDefault(),"%s selected",model.name));
                    intent.putExtra(IntentNames.RESTAURANT_KEY_SELECTED, model.getNameKey());
                    intent.putExtra(IntentNames.COUNTY_SELECTED, mCountyName);
                    intent.putExtra(IntentNames.RESTAURANT_SELECTED, Parcels.wrap(model));
                    intent.putExtra(IntentNames.RESTAURANT_ADDRESS_SELECTED,model.address);

                    logSelectionEvent(AppConstants.RESTAURANT_SELECTION,model.getNameKey(),TAG);
                    startActivity(intent);
                }
            });
        }
    }
}
