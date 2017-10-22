package com.janeullah.apps.healthinspectionviewer.async.aws;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.janeullah.apps.healthinspectionviewer.configuration.RetrofitConfiguration;
import com.janeullah.apps.healthinspectionviewer.interfaces.AsyncElasticSearchTaskable;
import com.janeullah.apps.healthinspectionviewer.listeners.ElasticSearchTaskListener;
import com.janeullah.apps.healthinspectionviewer.models.aws.AwsElasticSearchRequest;
import com.janeullah.apps.healthinspectionviewer.models.elasticsearch.ElasticSearchResponse;
import com.janeullah.apps.healthinspectionviewer.services.aws.AwsElasticSearchService;

import retrofit2.Call;
import retrofit2.Response;


/**
 * @author Jane Ullah
 * @date 9/29/2017.
 */

public class AwsElasticSearchRequestTask extends AsyncTask<AwsElasticSearchRequest,Integer,ElasticSearchResponse> implements AsyncElasticSearchTaskable {
    private static final String TAG = "AwsSearchTask";
    private ElasticSearchTaskListener elasticSearchTaskListener;

    @Override
    protected ElasticSearchResponse doInBackground(AwsElasticSearchRequest... awsElasticSearchRequests) {
        try{
            Log.i(TAG,"Initiated background processing...");
            AwsElasticSearchService elasticSearchService = RetrofitConfiguration.AWS_ELASTIC_SEARCH_SERVICE;
            Call<ElasticSearchResponse> searchRequest = elasticSearchService.findRestaurantsByName(awsElasticSearchRequests[0].getHeaders(), awsElasticSearchRequests[0].getPayload());
            Response<ElasticSearchResponse> response = searchRequest.execute();
            if (response.isSuccessful()) {
                Log.i(TAG,"Search results received for query="+ awsElasticSearchRequests[0]);
                Log.e(TAG,"Search response is successful. Result="+response.body());
                return response.body();
            }
            Log.e(TAG,"Search response not successful. Result="+response);
            Log.e(TAG,"Error response: " + response.errorBody().string());
        }catch(Exception e){
            FirebaseCrash.report(e);
            Log.e(TAG,"Error fetching Search Results from AWS",e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(ElasticSearchResponse result) {
        if (elasticSearchTaskListener != null) {
            elasticSearchTaskListener.onSuccess(result);
        }
    }

    @Override
    public void setElasticSearchListener(ElasticSearchTaskListener listener){
        this.elasticSearchTaskListener = listener;
    }
}
