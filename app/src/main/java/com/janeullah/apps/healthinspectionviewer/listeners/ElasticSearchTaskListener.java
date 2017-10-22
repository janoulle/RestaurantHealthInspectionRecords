package com.janeullah.apps.healthinspectionviewer.listeners;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.janeullah.apps.healthinspectionviewer.R;
import com.janeullah.apps.healthinspectionviewer.activities.BaseActivity;
import com.janeullah.apps.healthinspectionviewer.adapters.RestaurantsSearchListAdapter;
import com.janeullah.apps.healthinspectionviewer.constants.IntentNames;
import com.janeullah.apps.healthinspectionviewer.dtos.FlattenedRestaurant;
import com.janeullah.apps.healthinspectionviewer.interfaces.TaskListenable;
import com.janeullah.apps.healthinspectionviewer.models.elasticsearch.ElasticSearchResponse;
import com.janeullah.apps.healthinspectionviewer.models.elasticsearch.Hit;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jane Ullah
 * @date 9/30/2017.
 */

public class ElasticSearchTaskListener implements TaskListenable<Void, ElasticSearchResponse> {
    private static final String TAG = "EsSearchTaskListener";
    private Intent intent;
    private BaseActivity activity;
    private RecyclerView recyclerView;

    public void setIntent(Intent intent){
        this.intent = intent;
    }

    public void setActivity(BaseActivity activity){
        this.activity = activity;
    }

    public void setRecyclerView(RecyclerView view){
        this.recyclerView = view;
    }

    @Override
    public Void onSuccess(ElasticSearchResponse elasticSearchResponse) {
        ProgressBar progressBar = activity.findViewById(R.id.loadingModalForIndeterminateProgress);
        progressBar.setVisibility(View.INVISIBLE);
        if (elasticSearchResponse != null && elasticSearchResponse.getHits().getTotal() > 0) {
            intent.putExtra(IntentNames.AWS_ES_RESULTS, Parcels.wrap(elasticSearchResponse));
            List<FlattenedRestaurant> processedAwsResponse = processAwsResponse(elasticSearchResponse);
            //add adapter to recyclerview
            RestaurantsSearchListAdapter adapter = new RestaurantsSearchListAdapter(processedAwsResponse);
            recyclerView.setAdapter(adapter);
        }else {
            String message = "Failed to find a match for the query provided=" + intent.getStringExtra(SearchManager.QUERY);
            Log.d(TAG,message);
            activity.showToast(message, Toast.LENGTH_LONG);
            //show error message
            // turn off spinner
        }
        return null;
    }

    private List<FlattenedRestaurant> processAwsResponse(ElasticSearchResponse rawResponse){
        List<FlattenedRestaurant> results = new ArrayList<>();
        if (rawResponse != null && rawResponse.getHits() != null && rawResponse.getHits().getTotal() > 0){
            Log.i(TAG,"Received hits from Aws Search Service: " + rawResponse);
            for(Hit hit : rawResponse.getHits().getHits()){
                FlattenedRestaurant restaurant = new FlattenedRestaurant(hit.getSource());
                results.add(restaurant);
            }
        }
        return results;
    }
}
