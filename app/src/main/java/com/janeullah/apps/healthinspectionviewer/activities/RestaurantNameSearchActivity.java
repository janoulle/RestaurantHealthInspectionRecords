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

import com.google.firebase.analytics.FirebaseAnalytics;
import com.janeullah.apps.healthinspectionviewer.R;
import com.janeullah.apps.healthinspectionviewer.async.aws.AwsElasticSearchRequestTask;
import com.janeullah.apps.healthinspectionviewer.async.heroku.HerokuElasticSearchRequestTask;
import com.janeullah.apps.healthinspectionviewer.interfaces.ElasticSearchTaskListener;
import com.janeullah.apps.healthinspectionviewer.models.aws.AwsElasticSearchRequest;
import com.janeullah.apps.healthinspectionviewer.models.heroku.HerokuElasticSearchRequest;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static org.apache.commons.lang3.StringUtils.trim;

public class RestaurantNameSearchActivity extends BaseActivity {
    private static final String TAG = "RestaurantSearch";
    private AwsElasticSearchRequest awsSearchRequest = null;
    private HerokuElasticSearchRequest herokuSearchRequest = null;
    private AwsElasticSearchRequestTask awsAsyncTask = new AwsElasticSearchRequestTask();
    private HerokuElasticSearchRequestTask herokuAsyncTask = new HerokuElasticSearchRequestTask();

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

        //setting recylerview adapter in the awstasklistener when the search query is completed

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
        if (awsAsyncTask != null){
            awsAsyncTask.setElasticSearchListener(null);
        }
        if (herokuAsyncTask != null){
            herokuAsyncTask.setElasticSearchListener(null);
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
                //setup listener
                ElasticSearchTaskListener listener = new ElasticSearchTaskListener();
                listener.setIntent(getIntent());
                listener.setActivity(this);
                listener.setRecyclerView(mRecycler);

                //setup async task
                //awsSearchRequest = new AwsElasticSearchRequest(trim(query));
                //awsAsyncTask.setElasticSearchListener(listener);
                //awsAsyncTask.execute(awsSearchRequest);
                herokuSearchRequest = new HerokuElasticSearchRequest(trim(query));
                herokuAsyncTask.setElasticSearchListener(listener);
                herokuAsyncTask.execute(herokuSearchRequest);
            }
        }
    }
}
