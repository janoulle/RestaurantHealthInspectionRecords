package com.janeullah.apps.healthinspectionviewer.async;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.janeullah.apps.healthinspectionviewer.constants.YelpConstants;
import com.janeullah.apps.healthinspectionviewer.dtos.FlattenedRestaurant;
import com.janeullah.apps.healthinspectionviewer.dtos.GeocodedAddressComponent;
import com.janeullah.apps.healthinspectionviewer.interfaces.YelpService;
import com.janeullah.apps.healthinspectionviewer.models.yelp.YelpAuthTokenResponse;
import com.janeullah.apps.healthinspectionviewer.models.yelp.YelpResults;
import com.janeullah.apps.healthinspectionviewer.models.yelp.YelpSearchRequest;
import com.janeullah.apps.healthinspectionviewer.services.FetchYelpDataService;
import com.janeullah.apps.healthinspectionviewer.utils.YelpQueryParams;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

import static com.janeullah.apps.healthinspectionviewer.utils.YelpUtils.isFromCache;
import static com.janeullah.apps.healthinspectionviewer.utils.YelpUtils.isFromNetwork;

/**
 * @author Jane Ullah
 * @date 5/4/2017.
 */

public class YelpSearchBusinessesTask extends AsyncTask<YelpSearchRequest,Integer,YelpResults> {
    private static final String TAG = "YelpSearchTask";
    private Listener listener;

    @Override
    protected YelpResults doInBackground(YelpSearchRequest... params) {
        try {
            Log.i(TAG,"Initiated background processing...");
            YelpService yelpService = FetchYelpDataService.YELP_API_SERVICE;
            YelpAuthTokenResponse bearerToken = params[0].bearerToken;
            GeocodedAddressComponent restaurantMetadata = params[0].restaurantMetadata;
            //FlattenedRestaurant restaurantData = params[0].restaurant;

            //prepare query
            Map<String, Object> queryParams = new HashMap<>();
            queryParams.put(YelpQueryParams.DEFAULT_SEARCH_LOCALE.getKey(), YelpQueryParams.DEFAULT_SEARCH_LOCALE.getValue());
            queryParams.put(YelpQueryParams.DEFAULT_SORT.getKey(), YelpQueryParams.DEFAULT_SORT.getValue());
            queryParams.put(YelpConstants.LATITUDE, restaurantMetadata.coordinates.latitude);
            queryParams.put(YelpConstants.LONGITUDE, restaurantMetadata.coordinates.longitude);
            queryParams.put(YelpConstants.TERM, "food");
            Call<YelpResults> searchRequest = yelpService.searchBusinesses(bearerToken.getAccessToken(),queryParams);
            Response<YelpResults> response = searchRequest.execute();
            if (response.isSuccessful()) {
                if (isFromCache(response)){
                    Log.v(TAG,"Received Yelp results from cache");
                }else if (isFromNetwork(response)){
                    Log.v(TAG,"Received Yelp results from network");
                }
                Log.i(TAG,"Successful call to Yelp api for query params: " +queryParams);
                return response.body();
            }
            Log.i(TAG,"Unsuccessful api call to Yelp for query params: " + queryParams);
        }catch(IOException e){
            Log.d(TAG,"Error retrieving search results for Yelp api query",e);
            FirebaseCrash.report(e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(YelpResults result) {
        if (listener != null) {
            listener.onSuccess(result);
        }
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onSuccess(YelpResults object);
    }
}
