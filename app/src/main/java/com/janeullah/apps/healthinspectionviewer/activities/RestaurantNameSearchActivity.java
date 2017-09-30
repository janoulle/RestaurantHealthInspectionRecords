package com.janeullah.apps.healthinspectionviewer.activities;

import android.app.SearchManager;
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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.janeullah.apps.healthinspectionviewer.R;
import com.janeullah.apps.healthinspectionviewer.async.aws.AwsSearchRequestTask;
import com.janeullah.apps.healthinspectionviewer.constants.AppConstants;
import com.janeullah.apps.healthinspectionviewer.constants.IntentNames;
import com.janeullah.apps.healthinspectionviewer.dtos.FlattenedRestaurant;
import com.janeullah.apps.healthinspectionviewer.interfaces.AwsEsSearchTaskListener;
import com.janeullah.apps.healthinspectionviewer.models.aws.AwsSearchRequest;
import com.janeullah.apps.healthinspectionviewer.viewholder.RestaurantViewHolder;

import org.apache.commons.lang3.StringUtils;
import org.parceler.Parcels;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantNameSearchActivity extends BaseActivity {
    private static final String TAG = "RestaurantSearch";
    private AwsSearchRequest searchRequest = null;
    private AwsSearchRequestTask asyncTask = new AwsSearchRequestTask();

    @BindView(R.id.restaurants_search_listing_recyclerview)
    protected RecyclerView mRecycler;

    @BindView(R.id.toolbar)
    public Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_name_search);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        //view setup
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(layoutManager);

        //add divider to layout
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecycler.getContext(),
                layoutManager.getOrientation());
        mRecycler.addItemDecoration(dividerItemDecoration);

        // Get the intent, verify the action and get the query
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    @Override
    public void onDestroy(){
        if (asyncTask != null){
            asyncTask.setAwsSearchTaskListener(null);
        }
        super.onDestroy();
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

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            showProgressDialog(String.format(Locale.getDefault(),"Loading restaurants for query %s", query));
            if (StringUtils.isNotBlank(query)) {
                searchRequest = new AwsSearchRequest(query);
                AwsEsSearchTaskListener listener = new AwsEsSearchTaskListener();
                listener.setIntent(getIntent());
                listener.setActivity(this);
                listener.setRecyclerView(mRecycler);
                asyncTask.setAwsSearchTaskListener(listener);
                asyncTask.execute(searchRequest);
            }
        }
    }


    private class RestaurantsInCountyAdapter extends FirebaseRecyclerAdapter<FlattenedRestaurant, RestaurantViewHolder>{
        private String searchQuery;

        private static final String TAG = "RICAdapter2";
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

        public void setSearchQuery(String query){
            searchQuery = query;
        }

        @Override
        protected void populateViewHolder(RestaurantViewHolder viewHolder, FlattenedRestaurant model, int position) {
            final DatabaseReference queryRef = getRef(position);
            String key = queryRef.getKey();
            if (key.contains(searchQuery)) {
                Log.v(TAG, "Key: " + key);
                viewHolder.bindData(model);
                //addListenerToRestaurantItem(viewHolder, model);
            }
        }

        private void addListenerToRestaurantItem(RestaurantViewHolder viewHolder, final FlattenedRestaurant model) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent intent = new Intent(RestaurantNameSearchActivity.this, RestaurantDataActivity.class);
                    Log.i(TAG,String.format(Locale.getDefault(),"%s selected",model.name));
                    intent.putExtra(IntentNames.RESTAURANT_KEY_SELECTED, model.getNameKey());
                    intent.putExtra(IntentNames.RESTAURANT_SELECTED, Parcels.wrap(model));
                    intent.putExtra(IntentNames.RESTAURANT_ADDRESS_SELECTED,model.address);

                    logSelectionEvent(AppConstants.RESTAURANT_SELECTION,model.getNameKey(),TAG);
                    startActivity(intent);
                }
            });
        }
    }
}
