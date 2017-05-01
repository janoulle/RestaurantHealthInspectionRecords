package com.janeullah.apps.healthinspectionviewer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.janeullah.apps.healthinspectionviewer.R;
import com.janeullah.apps.healthinspectionviewer.constants.IntentNames;
import com.janeullah.apps.healthinspectionviewer.dtos.FlattenedRestaurant;
import com.janeullah.apps.healthinspectionviewer.services.FirebaseInitialization;
import com.janeullah.apps.healthinspectionviewer.viewholder.RestaurantViewHolder;

import org.parceler.Parcels;

import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants_in_county);
        ButterKnife.bind(this);
        showProgressDialog();
        mCountyName = getIntent().getStringExtra(IntentNames.COUNTY_SELECTED);
        if (mCountyName == null) {
            Log.e(TAG,"County not selected");
            throw new IllegalArgumentException("Must pass a county selection");
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(layoutManager);

        negaRestaurantsDatabaseReference = FirebaseInitialization.getInstance()
                .getNegaDatabaseReference()
                .child("restaurants");

        mQuery = negaRestaurantsDatabaseReference
                .orderByChild("county")
                .equalTo(mCountyName);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecycler.getContext(),
                layoutManager.getOrientation());
        mRecycler.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        mAdapter = new FirebaseRecyclerAdapter<FlattenedRestaurant, RestaurantViewHolder>(FlattenedRestaurant.class, R.layout.item_flattenedrestaurant,
                RestaurantViewHolder.class, mQuery) {
            @Override
            protected void populateViewHolder(final RestaurantViewHolder viewHolder, final FlattenedRestaurant model, int position) {
                final DatabaseReference queryRef = getRef(position);
                String key = queryRef.getKey();
                Log.v(TAG, "Key: " + key);
                viewHolder.bindData(model);
                hideProgressDialog();
                countOfRestaurants.incrementAndGet();
                addListenerToRestaurantItem(viewHolder, model);
            }
        };
        mRecycler.setAdapter(mAdapter);

        //https://gist.github.com/puf/f49a1b07e92952b44f2dc36d9af04e3c#file-mainactivity-java-L102
        //http://stackoverflow.com/questions/34982347/how-to-be-notified-when-firebaselistadapter-finishes
        updateTextViewHeader();
    }

    private void addListenerToRestaurantItem(RestaurantViewHolder viewHolder, final FlattenedRestaurant model) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(RestaurantsInCountyActivity.this, RestaurantDataActivity.class);
                intent.putExtra(IntentNames.RESTAURANT_KEY_SELECTED, model.getNameKey());
                intent.putExtra(IntentNames.COUNTY_SELECTED, mCountyName);
                intent.putExtra(IntentNames.RESTAURANT_SELECTED, Parcels.wrap(model));
                intent.putExtra(IntentNames.RESTAURANT_ADDRESS_SELECTED,model.address);
                startActivity(intent);
            }
        });
    }

    private void updateTextViewHeader() {
        mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // the initial data has been loaded, hide the progress bar
                Log.i(TAG, "Count of datasnapshot: " + dataSnapshot.getChildrenCount());
                headerForView.setText(mCountyName + " " + mAdapter.getItemCount());
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
}
