package com.janeullah.apps.healthinspectionviewer.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.android.gms.maps.model.LatLng;

import com.janeullah.apps.healthinspectionviewer.R;
import com.janeullah.apps.healthinspectionviewer.activities.MapsActivity;
import com.janeullah.apps.healthinspectionviewer.constants.GeocodeConstants;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class FetchAddressIntentService extends IntentService {
    public static final String TAG = "FetchAddressSvc";
    protected ResultReceiver mReceiver;
    public static final GeoApiContext geocodeContext = new GeoApiContext();

    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_GEOCODE = "com.janeullah.apps.healthinspectionviewer.services.action.geocode";
    public static final String ACTION_REVERSE_GEOCODE = "com.janeullah.apps.healthinspectionviewer.services.action.reversegeocode";

    // TODO: Rename parameters
    public static final String RECEIVER = "com.janeullah.apps.healthinspectionviewer.services.extra.geocoderesultsreceiver";
    public static final String ADDRESS_KEY = "com.janeullah.apps.healthinspectionviewer.services.extra.restaurantAddress";
    private static final String COORDINATES = "com.janeullah.apps.healthinspectionviewer.services.extra.coordinates";

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
            } else if (ACTION_REVERSE_GEOCODE.equals(action)) {
                processReverseGeocode(intent);
            }
        }
    }

    private void processGeocode(Intent intent) {
        String errorMessage = "";
        GeocodingResult[] geocodingResults = null;
        final String address = intent.getStringExtra(ADDRESS_KEY);
        try {
            geocodeContext
                    .setApiKey(fetchGoogleApiKey())
                    .setMaxRetries(3);
            geocodingResults = GeocodingApi.geocode(geocodeContext, address).await();
            Log.i(TAG,"Address: " + address + " latlng: " + geocodingResults[0].geometry.location.toString());
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
            Log.i(TAG, "Coordinates " + latLng + " for address " + address + " found!");
            deliverSuccessResultToReceiver(GeocodeConstants.SUCCESS_RESULT, latLng);
        }
    }

    /**
     * https://developer.android.com/training/location/display-address.html#start-intent
     * @param intent
     */
    protected void processReverseGeocode(Intent intent) {
        String errorMessage = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        // Get the location passed to this service through an extra.
        Location location = intent.getParcelableExtra(
                GeocodeConstants.LOCATION_DATA_EXTRA);

        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    // In this sample, get just a single address.
                    1);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = getString(R.string.service_not_available);
            Log.e(TAG, errorMessage, ioException);
            FirebaseCrash.report(ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = getString(R.string.invalid_lat_long_used);
            Log.e(TAG, errorMessage + ". " +
                    "Latitude = " + location.getLatitude() +
                    ", Longitude = " +
                    location.getLongitude(), illegalArgumentException);
            FirebaseCrash.report(illegalArgumentException);
        }
        processReverseGeocodeResult(errorMessage, addresses);
    }

    private void processReverseGeocodeResult(String errorMessage, List<Address> addresses) {
        // Handle case where no address was found.
        if (addresses == null || addresses.size()  == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.no_address_found);
                Log.e(TAG, errorMessage);
            }
            deliverResultToReceiver(GeocodeConstants.FAILURE_RESULT, errorMessage);
        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();

            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            for(int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            Log.i(TAG, getString(R.string.address_found));
            deliverResultToReceiver(GeocodeConstants.SUCCESS_RESULT,
                    TextUtils.join(System.getProperty("line.separator"),
                            addressFragments));
        }
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
