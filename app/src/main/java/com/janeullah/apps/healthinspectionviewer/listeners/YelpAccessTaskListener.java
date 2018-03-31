package com.janeullah.apps.healthinspectionviewer.listeners;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.janeullah.apps.healthinspectionviewer.activities.BaseActivity;
import com.janeullah.apps.healthinspectionviewer.async.yelp.YelpSearchBusinessesTask;
import com.janeullah.apps.healthinspectionviewer.constants.YelpConstants;
import com.janeullah.apps.healthinspectionviewer.dtos.GeocodedAddressComponent;
import com.janeullah.apps.healthinspectionviewer.interfaces.TaskListenable;
import com.janeullah.apps.healthinspectionviewer.models.yelp.YelpAuthTokenResponse;
import com.janeullah.apps.healthinspectionviewer.models.yelp.YelpSearchRequest;

/**
 * @author Jane Ullah
 * @date 9/30/2017.
 */
public class YelpAccessTaskListener implements TaskListenable<Void, YelpAuthTokenResponse> {
    private static final String TAG = "YelpAccessTaskListener";
    private SharedPreferences mSharedPreferences;
    private GeocodedAddressComponent mGeocodedAddressComponents;
    private YelpSearchBusinessesTask mYelpSearchRequestTask;
    private BaseActivity activity;
    private Intent intent;

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public void setActivity(BaseActivity activity) {
        this.activity = activity;
    }

    public void setSharedPreferences(SharedPreferences preferences) {
        this.mSharedPreferences = preferences;
    }

    public void setGeocodedComponents(GeocodedAddressComponent components) {
        this.mGeocodedAddressComponents = components;
    }

    @Override
    public Void onSuccess(YelpAuthTokenResponse authTokenResponse) {
        Log.i(TAG, "Received valid auth token response: " + authTokenResponse.getAccessToken());
        // TODO: figure out proper way to stash this token info
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(YelpConstants.SAVED_YELP_AUTH_TOKEN, authTokenResponse.getAccessToken());
        editor.putInt(YelpConstants.SAVED_YELP_TOKEN_EXPIRATION, authTokenResponse.getExpiresIn());
        editor.putString(YelpConstants.SAVED_YELP_TOKEN_TYPE, authTokenResponse.getTokenType());
        editor.apply();

        // make Yelp search
        if (mGeocodedAddressComponents != null) {
            YelpSearchRequest yelpSearchRequest =
                    new YelpSearchRequest(
                            authTokenResponse,
                            mGeocodedAddressComponents,
                            authTokenResponse.getRestaurantSelected());
            mYelpSearchRequestTask = new YelpSearchBusinessesTask();
            YelpSearchTaskListener yelpSearchTaskListener = new YelpSearchTaskListener();
            yelpSearchTaskListener.setIntent(intent);
            yelpSearchTaskListener.setActivity(activity);
            mYelpSearchRequestTask.setYelpSearchTaskListener(yelpSearchTaskListener);
            mYelpSearchRequestTask.execute(yelpSearchRequest);
        }
        return null;
    }
}
