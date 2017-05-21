package com.janeullah.apps.healthinspectionviewer.async;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;
import com.janeullah.apps.healthinspectionviewer.configuration.RetrofitConfigurationForYelp;
import com.janeullah.apps.healthinspectionviewer.constants.YelpConstants;
import com.janeullah.apps.healthinspectionviewer.dtos.YelpMatch;
import com.janeullah.apps.healthinspectionviewer.models.yelp.Business;
import com.janeullah.apps.healthinspectionviewer.models.yelp.YelpResults;
import com.janeullah.apps.healthinspectionviewer.models.yelp.YelpSearchRequest;
import com.janeullah.apps.healthinspectionviewer.services.YelpService;
import com.janeullah.apps.healthinspectionviewer.utils.YelpQueryParams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private static final Gson gson = new Gson();
    private Listener listener;

    @Override
    protected YelpResults doInBackground(YelpSearchRequest... params) {
        try {
            Log.i(TAG,"Initiated background processing...");
            YelpService yelpService = RetrofitConfigurationForYelp.YELP_API_SERVICE;

            //prepare query
            Map<String, Object> queryParams = new HashMap<>();
            queryParams.put(YelpQueryParams.DEFAULT_SEARCH_LOCALE.getKey(), YelpQueryParams.DEFAULT_SEARCH_LOCALE.getValue());
            queryParams.put(YelpQueryParams.DEFAULT_SORT.getKey(), YelpQueryParams.DEFAULT_SORT.getValue());
            queryParams.put(YelpConstants.LATITUDE, params[0].getLatitude());
            queryParams.put(YelpConstants.LONGITUDE, params[0].getLongitude());
            queryParams.put(YelpConstants.TERM, "food");
            Call<YelpResults> searchRequest = yelpService.searchBusinesses(params[0].getBearerToken(),queryParams);
            Response<YelpResults> response = searchRequest.execute();
            if (response.isSuccessful()) {
                if (isFromCache(response)){
                    Log.v(TAG,"Received Yelp results from cache");
                }else if (isFromNetwork(response)){
                    Log.v(TAG,"Received Yelp results from network");
                }
                Log.i(TAG,"Successful call to Yelp api for query params: " +queryParams);
                return findFirstAndSetYelpRestaurantOfInterest(params[0],response.body());
            }
            Log.i(TAG,"Unsuccessful api call to Yelp for query params: " + queryParams);
        }catch(IOException e){
            Log.d(TAG,"Error retrieving search results for Yelp api query",e);
            FirebaseCrash.report(e);
        }
        return null;
    }

    private YelpResults findFirstAndSetYelpRestaurantOfInterest(YelpSearchRequest yelpRequest, YelpResults yelpResults){
        List<YelpMatch> listOfPotentialMatches = new ArrayList<>();
        for(Business business : yelpResults.getBusinesses()){
            if (yelpRequest.matchesCityStateZip(business)){
                YelpMatch scoredPotentialMatch = yelpRequest.scoreMatch(business);
                listOfPotentialMatches.add(scoredPotentialMatch);
            }
        }
        if (!listOfPotentialMatches.isEmpty()) {
            YelpMatch closestMatch = YelpMatch.YELP_MATCH_ORDERING.max(listOfPotentialMatches);
            if (closestMatch.isAtOrAboveTolerance()) {
                Log.i(TAG,"Found a match at or above tolerance levels (" + YelpMatch.getToleranceLevels() +") for yelp listings with following data: ");
                yelpResults.setMatchedBusiness(closestMatch.getCandidate());
            }else{
                Log.i(TAG,"YelpMatch found was below the tolerance level - " + YelpMatch.getToleranceLevels() + " Rejected closest match: " + gson.toJson(closestMatch));
            }
        }
        return yelpResults;
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
