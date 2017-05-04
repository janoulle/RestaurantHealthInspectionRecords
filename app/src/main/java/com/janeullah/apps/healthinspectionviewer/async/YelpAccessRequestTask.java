package com.janeullah.apps.healthinspectionviewer.async;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.janeullah.apps.healthinspectionviewer.BuildConfig;
import com.janeullah.apps.healthinspectionviewer.constants.YelpConstants;
import com.janeullah.apps.healthinspectionviewer.interfaces.YelpService;
import com.janeullah.apps.healthinspectionviewer.models.yelp.YelpAuthTokenResponse;
import com.janeullah.apps.healthinspectionviewer.services.FetchYelpDataService;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * https://medium.com/google-developer-experts/weakreference-in-android-dd1e66b9be9d
 * https://futurestud.io/tutorials/retrofit-synchronous-and-asynchronous-requests
 * @author Jane Ullah
 * @date 4/30/2017.
 */

public class YelpAccessRequestTask extends AsyncTask<Void,Integer,YelpAuthTokenResponse>  {
    private static final String TAG = "YelpAccessRequestTask";
    private Listener listener;

    @Override
    protected YelpAuthTokenResponse doInBackground(Void... params) {
        try {
            Log.i(TAG,"Initiated background processing...");
            YelpService yelpService = FetchYelpDataService.YELP_API_SERVICE;
            Map<String, String> accessRequestData = new HashMap<>();
            accessRequestData.put(YelpConstants.GRANT_TYPE, YelpConstants.DEFAULT_GRANT_TYPE);
            accessRequestData.put(YelpConstants.CLIENT_ID, BuildConfig.YELP_CLIENT_ID);
            accessRequestData.put(YelpConstants.CLIENT_SECRET, BuildConfig.YELP_CLIENT_SECRET);
            Call<YelpAuthTokenResponse> accessRequest = yelpService.getAuthToken(accessRequestData);
            return accessRequest.execute().body();
        }catch(Exception e){
            Log.e(TAG,"Errored out while checking Yelp for data",e);
            FirebaseCrash.report(e);
        }
        Log.e(TAG,"Failed to retrieve auth response");
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
