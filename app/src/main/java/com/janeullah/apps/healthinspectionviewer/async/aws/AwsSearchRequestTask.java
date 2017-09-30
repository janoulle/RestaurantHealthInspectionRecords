package com.janeullah.apps.healthinspectionviewer.async.aws;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.janeullah.apps.healthinspectionviewer.configuration.RetrofitConfiguration;
import com.janeullah.apps.healthinspectionviewer.interfaces.AwsEsSearchTaskListener;
import com.janeullah.apps.healthinspectionviewer.models.aws.AwsElasticSearchResponse;
import com.janeullah.apps.healthinspectionviewer.models.aws.AwsSearchRequest;
import com.janeullah.apps.healthinspectionviewer.services.aws.AwsElasticSearchService;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

import static com.janeullah.apps.healthinspectionviewer.constants.AwsElasticSearchConstants.AWS_ES_HOST;
import static com.janeullah.apps.healthinspectionviewer.constants.AwsElasticSearchConstants.AWS_SEARCH_URL;

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
            }
            Log.e(TAG,"Search response not successful. Result="+response);
        }catch(Exception e){
            FirebaseCrash.report(e);
            Log.e(TAG,"Error fetching Search Results from AWS",e);
        }
        return null;
    }

    /**
     * Making a http call using OkHttp sample
     * @param awsSearchRequest
     * @throws IOException exception that
     */
    @Deprecated
    private void vanillaOkHttpCall(AwsSearchRequest awsSearchRequest) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, awsSearchRequest.getJsonPayload());
        Request request = new Request.Builder()
                .url(AWS_SEARCH_URL)
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("host", AWS_ES_HOST)
                .addHeader("x-amz-date", awsSearchRequest.getTimestampedDate())
                .addHeader("authorization", awsSearchRequest.getAuthorizationHeader())
                .addHeader("cache-control", "no-cache")
                .build();

        com.squareup.okhttp.Response response = client.newCall(request).execute();
        if (response.isSuccessful()){
            Log.i(TAG,"Search results received for query="+ awsSearchRequest);
        }
        Log.e(TAG,"Search response not successful. Result="+response);
    }

    public void setAwsSearchTaskListener(AwsEsSearchTaskListener listener){
        this.awsEsSearchTaskListener = listener;
    }
}
