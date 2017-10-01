package com.janeullah.apps.healthinspectionviewer.async.aws;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.janeullah.apps.healthinspectionviewer.configuration.RetrofitConfiguration;
import com.janeullah.apps.healthinspectionviewer.interfaces.AwsEsSearchTaskListener;
import com.janeullah.apps.healthinspectionviewer.models.aws.AwsElasticSearchResponse;
import com.janeullah.apps.healthinspectionviewer.models.aws.AwsSearchRequest;
import com.janeullah.apps.healthinspectionviewer.services.aws.AwsElasticSearchService;

import retrofit2.Call;
import retrofit2.Response;


/**
 * @author Jane Ullah
 * @date 9/29/2017.
 */

public class AwsSearchRequestTask extends AsyncTask<AwsSearchRequest,Integer,AwsElasticSearchResponse>{
    private static final String TAG = "AwsSearchTask";
    private AwsEsSearchTaskListener awsEsSearchTaskListener;

    @Override
    protected AwsElasticSearchResponse doInBackground(AwsSearchRequest... awsSearchRequests) {
        try{
            Log.i(TAG,"Initiated background processing...");
            AwsElasticSearchService elasticSearchService = RetrofitConfiguration.ELASTIC_SEARCH_SERVICE;
            Call<AwsElasticSearchResponse> searchRequest = elasticSearchService.findRestaurantsByName(awsSearchRequests[0].getHeaders(),awsSearchRequests[0].getPayload());
            Response<AwsElasticSearchResponse> response = searchRequest.execute();
            if (response.isSuccessful()) {
                Log.i(TAG,"Search results received for query="+awsSearchRequests[0]);
                Log.e(TAG,"Search response is successful. Result="+response);
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
    protected void onPostExecute(AwsElasticSearchResponse result) {
        if (awsEsSearchTaskListener != null) {
            awsEsSearchTaskListener.onSuccess(result);
        }
    }

    public void setAwsSearchTaskListener(AwsEsSearchTaskListener listener){
        this.awsEsSearchTaskListener = listener;
    }
}
