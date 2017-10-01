package com.janeullah.apps.healthinspectionviewer.interfaces;

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
import com.janeullah.apps.healthinspectionviewer.models.aws.AwsElasticSearchResponse;
import com.janeullah.apps.healthinspectionviewer.models.aws.Hit;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jane Ullah
 * @date 9/30/2017.
 */

public class AwsEsSearchTaskListener implements TaskListener<Void, AwsElasticSearchResponse> {
    private static final String TAG = "AwsEsSearchTaskListener";
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
    public Void onSuccess(AwsElasticSearchResponse awsElasticSearchResponse) {
        ProgressBar progressBar = activity.findViewById(R.id.loadingModalForIndeterminateProgress);
        progressBar.setVisibility(View.INVISIBLE);
        if (awsElasticSearchResponse != null) {
            intent.putExtra(IntentNames.AWS_ES_RESULTS, Parcels.wrap(awsElasticSearchResponse));
            List<FlattenedRestaurant> processedAwsResponse = processAwsResponse(awsElasticSearchResponse);
            //add adapter to recyclerview
            RestaurantsSearchListAdapter adapter = new RestaurantsSearchListAdapter(activity, processedAwsResponse);
            recyclerView.setAdapter(adapter);
        }else {
            activity.showToast("Failed to fetch search results.", Toast.LENGTH_SHORT);
            //show error message
            // turn off spinner
        }
        return null;
    }

    private List<FlattenedRestaurant> processAwsResponse(AwsElasticSearchResponse rawResponse){
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
