package com.janeullah.apps.healthinspectionviewer.async.aws;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.janeullah.apps.healthinspectionviewer.configuration.RetrofitConfiguration;
import com.janeullah.apps.healthinspectionviewer.interfaces.EsSearchTaskListener;
import com.janeullah.apps.healthinspectionviewer.models.aws.AwsSearchRequest;
import com.janeullah.apps.healthinspectionviewer.models.aws.ElasticSearchResponse;
import com.janeullah.apps.healthinspectionviewer.services.aws.AwsElasticSearchService;

import retrofit2.Call;
import retrofit2.Response;


/**
 * @author Jane Ullah
 * @date 9/29/2017.
 */

public class AwsSearchRequestTask extends AsyncTask<AwsSearchRequest,Integer,ElasticSearchResponse>{
    private static final String TAG = "AwsSearchTask";
    private EsSearchTaskListener esSearchTaskListener;

    @Override
    protected ElasticSearchResponse doInBackground(AwsSearchRequest... awsSearchRequests) {
        try{
            Log.i(TAG,"Initiated background processing...");
            AwsElasticSearchService elasticSearchService = RetrofitConfiguration.AWS_ELASTIC_SEARCH_SERVICE;
            Call<ElasticSearchResponse> searchRequest = elasticSearchService.findRestaurantsByName(awsSearchRequests[0].getHeaders(),awsSearchRequests[0].getPayload());
            Response<ElasticSearchResponse> response = searchRequest.execute();
            if (response.isSuccessful()) {
                Log.i(TAG,"Search results received for query="+awsSearchRequests[0]);
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
        if (esSearchTaskListener != null) {
            esSearchTaskListener.onSuccess(result);
        }
    }

    public void setAwsSearchTaskListener(EsSearchTaskListener listener){
        this.esSearchTaskListener = listener;
    }
}
