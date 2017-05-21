package com.janeullah.apps.healthinspectionviewer.services;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.janeullah.apps.healthinspectionviewer.R;
import com.janeullah.apps.healthinspectionviewer.activities.RestaurantDataActivity;
import com.janeullah.apps.healthinspectionviewer.constants.GeocodeConstants;
import com.janeullah.apps.healthinspectionviewer.constants.IntentNames;
import com.janeullah.apps.healthinspectionviewer.dtos.FlattenedRestaurant;
import com.janeullah.apps.healthinspectionviewer.dtos.GeocodedAddressComponent;
import com.janeullah.apps.healthinspectionviewer.models.NotificationBuilderPojo;
import com.janeullah.apps.healthinspectionviewer.utils.TaskCompleteNotification;

import org.parceler.Parcels;

import java.io.IOException;

import static com.janeullah.apps.healthinspectionviewer.utils.GeocodingUtils.convertGeocodingResultsToGeocodedAddressComponent;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public final class FetchAddressIntentService extends IntentService {
    public static final String TAG = "FetchAddressSvc";
    public static final GeoApiContext geocodeContext = new GeoApiContext();
    protected ResultReceiver mReceiver;

    public static final int NOTIFICATION_ID = 1;
    public static final String ACTION_GEOCODE = "com.janeullah.apps.healthinspectionviewer.services.action.geocode";
    public static final String RECEIVER = "com.janeullah.apps.healthinspectionviewer.services.extra.geocoderesultsreceiver";

    public FetchAddressIntentService() {
        super("FetchAddressIntentService");
    }

    /**
     * http://blog.iangclifton.com/2010/10/08/using-meta-data-in-an-androidmanifest/
     * @return Gmaps api key
     */
    private String fetchGoogleApiKey(){
        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            return bundle.getString("com.google.android.geo.API_KEY");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Failed to load meta-data, NameNotFound: " + e.getMessage(),e);
        } catch (NullPointerException e) {
            Log.e(TAG, "Failed to load meta-data, NullPointer: " + e.getMessage(),e);
        }
        return "";
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            mReceiver = intent.getParcelableExtra(FetchAddressIntentService.RECEIVER);
            final String action = intent.getAction();
            if (ACTION_GEOCODE.equals(action)) {
                performGeocoding(intent);
            }
        }
    }

    private void performGeocoding(Intent intent) {
        String errorMessage = "";
        GeocodingResult[] geocodingResults = null;
        FlattenedRestaurant restaurantSelected = null;
        try {
            restaurantSelected = Parcels.unwrap(intent.getParcelableExtra(IntentNames.RESTAURANT_SELECTED));
            geocodeContext
                    .setApiKey(fetchGoogleApiKey())
                    .setMaxRetries(3);
            geocodingResults = GeocodingApi.geocode(geocodeContext, restaurantSelected.address).await();
            Log.i(TAG,"Address: " + restaurantSelected.address + " latlng: " + geocodingResults[0].geometry.location.toString());
        }catch(ApiException | IOException e){
            errorMessage = getString(R.string.service_not_available);
            Log.e(TAG, errorMessage, e);
            FirebaseCrash.report(e);
        }catch(InterruptedException e){
            errorMessage = getString(R.string.operation_failure);
            Log.e(TAG, errorMessage, e);
            FirebaseCrash.report(e);
            Thread.currentThread().interrupt();
        }
        processResponse(errorMessage, geocodingResults, restaurantSelected);
    }

    private void processResponse(String errorMessage, GeocodingResult[] geocodingResults, FlattenedRestaurant restaurantSelected) {
        // Handle case where no address was found.
        String errMessage = errorMessage;
        if (geocodingResults == null || geocodingResults.length  == 0) {
            if (TextUtils.isEmpty(errMessage)) {
                errMessage = getString(R.string.no_geocoded_address_found);
                Log.e(TAG, errorMessage);
            }
            deliverResultToReceiver(GeocodeConstants.FAILURE_RESULT, errMessage);
        } else {
            handleGeocodedHappyPath(geocodingResults[0], restaurantSelected);
        }
    }

    private void handleGeocodedHappyPath(GeocodingResult geocodingResult, FlattenedRestaurant restaurantSelected) {
        if (geocodingResult.partialMatch){
            Log.d(TAG,"Inexact Geocoding results received on requested address (" + restaurantSelected.address + ")");
        }
        //http://stackoverflow.com/questions/30106507/pass-longitude-and-latitude-with-intent-to-another-class
        GeocodedAddressComponent geocodedAddressComponent = convertGeocodingResultsToGeocodedAddressComponent(geocodingResult);
        restaurantSelected.coordinates = geocodedAddressComponent.coordinates;
        Log.i(TAG, "Coordinates "+ geocodedAddressComponent.coordinates + " for restaurant " + restaurantSelected.name + " at address " + restaurantSelected.address +" found!");
        sendNotification("Geocoding completed for address " + restaurantSelected.address,restaurantSelected);
        deliverSuccessResultToReceiver(GeocodeConstants.SUCCESS_RESULT,geocodedAddressComponent);
    }

    private void sendNotification(String msg, FlattenedRestaurant restaurant){
        NotificationBuilderPojo dataNeededForNotification = new NotificationBuilderPojo();
        dataNeededForNotification.notificationId = NOTIFICATION_ID;
        dataNeededForNotification.bigText = msg;
        dataNeededForNotification.contentText = msg;
        dataNeededForNotification.contentTitle = "NEGA Restaurant Scores";
        dataNeededForNotification.smallIconResourceId = R.drawable.ic_greencheck;
        dataNeededForNotification.notificationStyle = new NotificationCompat.BigTextStyle()
                .setSummaryText("Geocoding complete: "+ restaurant.address)
                .bigText("Completed geocoding for " + restaurant.name +  " at address: " + restaurant.address)
                .setBigContentTitle(msg);
        TaskStackBuilder stackBuilder = preserveStack(restaurant);
        dataNeededForNotification.pendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        TaskCompleteNotification.notify(this,dataNeededForNotification);
    }

    @NonNull
    private TaskStackBuilder preserveStack(FlattenedRestaurant restaurant) {
        //doing this to preserve data needed by RestaurantDataActivity
        Intent resultIntent = new Intent(this, RestaurantDataActivity.class);
        resultIntent.putExtra(IntentNames.STARTED_BY_NOTIFICATION,true);
        resultIntent.putExtra(IntentNames.RESTAURANT_SELECTED, Parcels.wrap(restaurant));
        resultIntent.putExtra(IntentNames.RESTAURANT_KEY_SELECTED, restaurant.getNameKey());
        resultIntent.putExtra(IntentNames.COUNTY_SELECTED, restaurant.county);
        resultIntent.putExtra(IntentNames.RESTAURANT_ADDRESS_SELECTED,restaurant.address);
        resultIntent.putExtra(GeocodeConstants.RESULT_DATA_KEY,restaurant.coordinates);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(RestaurantDataActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        return stackBuilder;
    }

    private void deliverSuccessResultToReceiver(int resultCode, GeocodedAddressComponent addressComponents) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(GeocodeConstants.RESULT_DATA_KEY, Parcels.wrap(addressComponents));
        mReceiver.send(resultCode, bundle);
    }

    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(GeocodeConstants.RESULT_DATA_KEY, message);
        mReceiver.send(resultCode, bundle);
    }
}
