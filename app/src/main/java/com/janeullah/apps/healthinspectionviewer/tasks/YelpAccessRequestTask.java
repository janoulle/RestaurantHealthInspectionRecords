package com.janeullah.apps.healthinspectionviewer.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.janeullah.apps.healthinspectionviewer.BuildConfig;
import com.janeullah.apps.healthinspectionviewer.constants.YelpConstants;
import com.janeullah.apps.healthinspectionviewer.models.yelp.YelpAuthTokenResponse;
import com.janeullah.apps.healthinspectionviewer.network.interfaces.YelpApiInterface;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

import static com.janeullah.apps.healthinspectionviewer.utils.YelpUtils.isFromCache;
import static com.janeullah.apps.healthinspectionviewer.utils.YelpUtils.isFromNetwork;

/**
 * https://medium.com/google-developer-experts/weakreference-in-android-dd1e66b9be9d
 * https://futurestud.io/tutorials/retrofit-synchronous-and-asynchronous-requests
 * @author Jane Ullah
 * @date 4/30/2017.
 */

public class YelpAccessRequestTask extends AsyncTask<Void,Integer,YelpAuthTokenResponse>  {
    private static final String TAG = "YelpAccessRequestTask";
    private Listener listener;
    private YelpApiInterface yelpApiInterface;

    public YelpAccessRequestTask(){    }

    public YelpAccessRequestTask(YelpApiInterface yelpApiInterface){
        this.yelpApiInterface = yelpApiInterface;
    }

    @Override
    protected YelpAuthTokenResponse doInBackground(Void... params) {
        try {
            Log.i(TAG,"Initiated background processing...");
            Map<String, String> accessRequestData = new HashMap<>();
            accessRequestData.put(YelpConstants.GRANT_TYPE, YelpConstants.DEFAULT_GRANT_TYPE);
            accessRequestData.put(YelpConstants.CLIENT_ID, BuildConfig.YELP_CLIENT_ID);
            accessRequestData.put(YelpConstants.CLIENT_SECRET, BuildConfig.YELP_CLIENT_SECRET);
            Call<YelpAuthTokenResponse> accessRequest = yelpApiInterface.getAuthToken(accessRequestData);
            Response<YelpAuthTokenResponse> response = accessRequest.execute();
            if (response.isSuccessful()) {
                if (isFromCache(response)){
                    Log.v(TAG,"Received auth token from cache");
                }else if (isFromNetwork(response)){
                    Log.v(TAG,"Received yelp auth token from network");
                }
                return response.body();
            }
            Log.d(TAG,"Unsuccessful api call to Yelp token api");
        }catch(IOException e){
            Log.e(TAG,"Errored out while checking Yelp for data with message: " + e.getMessage(),e);
            FirebaseCrash.report(e);
        }
        return null;
    }


    @Override
    protected void onPostExecute(YelpAuthTokenResponse result) {
        if (listener != null) {
            listener.onSuccess(result);
        }
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onSuccess(YelpAuthTokenResponse object);
    }

}
