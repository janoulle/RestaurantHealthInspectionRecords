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

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * @author Jane Ullah
 * @date 5/4/2017.
 */

public class YelpSearchBusinessesTask extends AsyncTask<YelpSearchRequest,Integer,YelpResults> {
    private static final String TAG = "YelpSearchTask";

    @Override
    protected YelpResults doInBackground(YelpSearchRequest... params) {
        try {
            YelpService yelpService = FetchYelpDataService.YELP_API_SERVICE;
            YelpAuthTokenResponse bearerToken = params[0].bearerToken;
            GeocodedAddressComponent restaurantMetadata = params[0].restaurantMetadata;
            FlattenedRestaurant restaurantData = params[0].restaurant;
            Map<String, Object> queryParams = new HashMap<>();
            queryParams.put(YelpQueryParams.DEFAULT_SEARCH_LOCALE.getKey(), YelpQueryParams.DEFAULT_SEARCH_LOCALE.getValue());
            queryParams.put(YelpQueryParams.DEFAULT_SORT.getKey(), YelpQueryParams.DEFAULT_SORT.getValue());
            queryParams.put(YelpConstants.LATITUDE, restaurantMetadata.coordinates.latitude);
            queryParams.put(YelpConstants.LONGITUDE, restaurantMetadata.coordinates.longitude);
            queryParams.put(YelpConstants.TERM, "food");
            Call<YelpResults> searchRequest = yelpService.searchBusinesses(bearerToken.getAccessToken(),queryParams);
            return searchRequest.execute().body();
        }catch(Exception e){
            Log.d(TAG,"Error retrieving search results for Yelp api query",e);
            FirebaseCrash.report(e);
        }
        return null;
    }

    protected void onPostExecute(Long result) {
        //showDialog("Downloaded " + result + " bytes");
    }
}
