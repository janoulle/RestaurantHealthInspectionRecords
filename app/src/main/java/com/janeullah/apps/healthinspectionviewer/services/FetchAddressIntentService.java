package com.janeullah.apps.healthinspectionviewer.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.os.ResultReceiver;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
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

import org.parceler.Parcels;

import java.io.IOException;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class FetchAddressIntentService extends IntentService {
    public static final String TAG = "FetchAddressSvc";
    public static final GeoApiContext geocodeContext = new GeoApiContext();
    protected ResultReceiver mReceiver;
    private NotificationManager mNotificationManager;

    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
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
            Log.e(TAG, "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e(TAG, "Failed to load meta-data, NullPointer: " + e.getMessage());
        }
        return "";
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            mReceiver = intent.getParcelableExtra(FetchAddressIntentService.RECEIVER);
            final String action = intent.getAction();
            if (ACTION_GEOCODE.equals(action)) {
                processGeocode(intent);
            }
        }
    }

    private void processGeocode(Intent intent) {
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
        }
        // Handle case where no address was found.
        if (geocodingResults == null || geocodingResults.length  == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.no_geocoded_address_found);
                Log.e(TAG, errorMessage);
            }
            deliverResultToReceiver(GeocodeConstants.FAILURE_RESULT, errorMessage);
        } else {
            GeocodingResult geocodedAddress = geocodingResults[0];
            //http://stackoverflow.com/questions/30106507/pass-longitude-and-latitude-with-intent-to-another-class
            com.google.maps.model.LatLng originalLatLng = geocodedAddress.geometry.location;
            LatLng latLng = new LatLng(originalLatLng.lat,originalLatLng.lng);
            Log.i(TAG, "Coordinates " + latLng + " for address " + restaurantSelected.address + " found!");
            sendNotification("Geocoding completed for address " + restaurantSelected.address,restaurantSelected);
            deliverSuccessResultToReceiver(GeocodeConstants.SUCCESS_RESULT, latLng);
        }
    }

    /**
     * TODO replace with better icon
     * http://stackoverflow.com/questions/28698278/how-to-start-a-notification-in-intentservice
     * https://developer.android.com/guide/topics/ui/notifiers/notifications.html
     * @param msg
     */
    private void sendNotification(String msg, FlattenedRestaurant restaurant) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_greencheckmark)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentTitle("NEGA Restaurant Scores")
                        .setContentText(msg);

        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        //doing this to preserve data needed by RestaurantDataActivity
        Intent resultIntent = new Intent(this, RestaurantDataActivity.class);
        resultIntent.putExtra(IntentNames.RESTAURANT_SELECTED, Parcels.wrap(restaurant));
        resultIntent.putExtra(IntentNames.RESTAURANT_KEY_SELECTED, restaurant.getNameKey());
        resultIntent.putExtra(IntentNames.COUNTY_SELECTED, restaurant.county);
        resultIntent.putExtra(IntentNames.RESTAURANT_ADDRESS_SELECTED,restaurant.address);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(RestaurantDataActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void deliverSuccessResultToReceiver(int resultCode, LatLng coordinates) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(GeocodeConstants.RESULT_DATA_KEY, coordinates);
        mReceiver.send(resultCode, bundle);
    }

    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(GeocodeConstants.RESULT_DATA_KEY, message);
        mReceiver.send(resultCode, bundle);
    }
}
