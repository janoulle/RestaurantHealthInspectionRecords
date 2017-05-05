package com.janeullah.apps.healthinspectionviewer.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.os.ResultReceiver;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.janeullah.apps.healthinspectionviewer.R;
import com.janeullah.apps.healthinspectionviewer.async.YelpAccessRequestTask;
import com.janeullah.apps.healthinspectionviewer.async.YelpSearchBusinessesTask;
import com.janeullah.apps.healthinspectionviewer.constants.GeocodeConstants;
import com.janeullah.apps.healthinspectionviewer.constants.IntentNames;
import com.janeullah.apps.healthinspectionviewer.constants.YelpConstants;
import com.janeullah.apps.healthinspectionviewer.databinding.ActivityRestaurantDataBinding;
import com.janeullah.apps.healthinspectionviewer.dtos.FlattenedRestaurant;
import com.janeullah.apps.healthinspectionviewer.dtos.GeocodedAddressComponent;
import com.janeullah.apps.healthinspectionviewer.models.yelp.YelpAuthTokenResponse;
import com.janeullah.apps.healthinspectionviewer.models.yelp.YelpResults;
import com.janeullah.apps.healthinspectionviewer.models.yelp.YelpSearchRequest;
import com.janeullah.apps.healthinspectionviewer.services.FetchAddressIntentService;

import org.parceler.Parcels;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * https://developer.android.com/topic/libraries/data-binding/index.html
 * https://guides.codepath.com/android/Applying-Data-Binding-for-Views
 * https://github.com/johncarl81/parceler
 * https://github.com/codepath/android_guides/wiki/Using-Parcelable
 * https://github.com/googlemaps/google-maps-services-java
 * https://developer.android.com/training/permissions/requesting.html
 * https://github.com/googlemaps/android-samples
 * https://developers.google.com/maps/documentation/android-api/map
 */
public class RestaurantDataActivity extends BaseActivity implements OnMapReadyCallback {
    private static final String TAG = "RestaurantDataActivity";
    private GoogleMap mMap;
    private GeocodingResultsReceiver mGeocodeResultsReceiver;
    private LatLng mRestaurantCoordinates;
    private GeocodedAddressComponent mGeocodedAddressComponents;
    private FlattenedRestaurant mRestaurantSelected;
    public ActivityRestaurantDataBinding mDataBinding;
    private YelpAccessRequestTask mAccessRequestTask = new YelpAccessRequestTask();
    private YelpSearchBusinessesTask mYelpSearchRequestTask  = new YelpSearchBusinessesTask();
    private SharedPreferences mSharedPreferences;

    @BindView(R.id.item_map)
    protected MapView mMapView;

    @BindView(R.id.app_toolbar)
    public Toolbar mAppToolbar;

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_restaurant_data);

        ButterKnife.bind(this);

        setSupportActionBar(mAppToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        mSharedPreferences = this.getSharedPreferences(YelpConstants.YELP_PREFERENCES,Context.MODE_PRIVATE);
        mRestaurantSelected = Parcels.unwrap(getIntent().getParcelableExtra(IntentNames.RESTAURANT_SELECTED));
        if (mRestaurantSelected == null) {
            Log.e(TAG,"Restaurant not selected before launching RestaurantDataActivity");
            throw new IllegalArgumentException("Failed to pass a restaurant selection before viewing inspection report activity");
        }
        Log.v(TAG,"resource id = " + mRestaurantSelected.restaurantCheckMarkResourceId);
        mDataBinding.setRestaurantSelected(mRestaurantSelected);
        setTitle(mRestaurantSelected.name);

        initializeMapView(savedInstanceState);
        checkAndInitiateYelpTokenRequest();
        startBackgroundGeocodeServiceLookup();
    }

    private void checkAndInitiateYelpTokenRequest() {
        String yelpAuthToken = mSharedPreferences.getString(YelpConstants.SAVED_YELP_AUTH_TOKEN,"");
        if (TextUtils.isEmpty(yelpAuthToken)){
            Log.i(TAG,"Yelp token not found! Making api call");
            mAccessRequestTask.setListener(createYelpAccessTokenListener());
            mAccessRequestTask.execute();
        }
    }

    private void startBackgroundGeocodeServiceLookup() {
        mGeocodeResultsReceiver = new GeocodingResultsReceiver(new Handler());
        Intent intent = new Intent(FetchAddressIntentService.ACTION_GEOCODE, null, this, FetchAddressIntentService.class);
        intent.putExtra(IntentNames.RESTAURANT_SELECTED, Parcels.wrap(mRestaurantSelected));
        intent.putExtra(FetchAddressIntentService.RECEIVER, mGeocodeResultsReceiver);
        Log.i(TAG,"Started background service for geocoding functionality with address: " + mRestaurantSelected.address);
        startService(intent);
    }

    private void initializeMapView(Bundle savedInstanceState) {
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView.onCreate(mapViewBundle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Log.i(TAG,"Up clicked!");
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                upIntent.putExtra(IntentNames.COUNTY_SELECTED,mRestaurantSelected.county);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                            // Navigate up to the closest parent
                            .startActivities();
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions()
                .draggable(true)
                .position(mRestaurantCoordinates)
                .title(mRestaurantSelected.address))
                .showInfoWindow();
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mRestaurantCoordinates, 17));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mAccessRequestTask.setListener(null);
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    private YelpAccessRequestTask.Listener createYelpAccessTokenListener() {
        return new YelpAccessRequestTask.Listener() {
            @Override
            public void onSuccess(YelpAuthTokenResponse authTokenResponse) {
                Log.i(TAG,"Received valid auth token response: " + authTokenResponse.getAccessToken());
                Toast.makeText(getApplicationContext(),"Make it to Yelp and got token!",Toast.LENGTH_LONG).show();
                //TODO: figure out proper way to stash this token info
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString(YelpConstants.SAVED_YELP_AUTH_TOKEN, authTokenResponse.getAccessToken());
                editor.putInt(YelpConstants.SAVED_YELP_TOKEN_EXPIRATION,authTokenResponse.getExpiresIn());
                editor.putString(YelpConstants.SAVED_YELP_TOKEN_TYPE,authTokenResponse.getTokenType());
                editor.apply();
                if (mGeocodedAddressComponents != null){
                    YelpSearchRequest yelpSearchRequest = new YelpSearchRequest(authTokenResponse,mGeocodedAddressComponents,mRestaurantSelected);
                    mYelpSearchRequestTask.setListener(createYelpSearchListener());
                    mYelpSearchRequestTask.execute(yelpSearchRequest);
                }
            }
        };
    }

    private YelpSearchBusinessesTask.Listener createYelpSearchListener() {
        return new YelpSearchBusinessesTask.Listener() {
            @Override
            public void onSuccess(YelpResults yelpResults) {
                Intent intent = getIntent();
                intent.putExtra(IntentNames.YELP_RESULTS,Parcels.wrap(yelpResults));
                Log.v(TAG,"Received yelp results for " + mRestaurantSelected.name + ": " + yelpResults);
            }
        };
    }

    private void makeAsyncYelpSearchCall(GeocodedAddressComponent component){
        //TODO: expire token plan
        Log.i(TAG,"Saved auth token found");
        String yelpAuthToken = mSharedPreferences.getString(YelpConstants.SAVED_YELP_AUTH_TOKEN,"");
        String tokenType = mSharedPreferences.getString(YelpConstants.SAVED_YELP_TOKEN_TYPE,"Bearer");
        Integer expiry = mSharedPreferences.getInt(YelpConstants.SAVED_YELP_TOKEN_EXPIRATION,15462984);
        YelpAuthTokenResponse authTokenResponse = new YelpAuthTokenResponse();
        authTokenResponse.setAccessToken(yelpAuthToken);
        authTokenResponse.setExpiresIn(expiry);
        authTokenResponse.setTokenType(tokenType);
        YelpSearchRequest yelpSearchRequest = new YelpSearchRequest(authTokenResponse,component,mRestaurantSelected);
        mYelpSearchRequestTask.setListener(createYelpSearchListener());
        mYelpSearchRequestTask.execute(yelpSearchRequest);
    }

    private class GeocodingResultsReceiver extends ResultReceiver {
        private static final String TAG = "GeocodingReceiver";
        /**
         * Create a new ResultReceive to receive results.  Your
         * {@link #onReceiveResult} method will be called from the thread running
         * <var>handler</var> if given, or from an arbitrary thread if null.
         *
         * @param handler
         */
        public GeocodingResultsReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultCode == GeocodeConstants.SUCCESS_RESULT) {
                mGeocodedAddressComponents = Parcels.unwrap(resultData.getParcelable(GeocodeConstants.RESULT_DATA_KEY));
                mRestaurantCoordinates = mGeocodedAddressComponents.coordinates;
                Log.i(TAG, String.format(Locale.getDefault(),"Coordinates (%s) received", mRestaurantCoordinates));
                if (!TextUtils.isEmpty(mSharedPreferences.getString(YelpConstants.SAVED_YELP_AUTH_TOKEN,""))){
                    makeAsyncYelpSearchCall(mGeocodedAddressComponents);
                }
                mMapView.getMapAsync(RestaurantDataActivity.this);
            } else {
                String output = resultData.getString(GeocodeConstants.RESULT_DATA_KEY);
                Log.d(TAG,output);
                showToast(output, Toast.LENGTH_SHORT);
            }
        }
    }

    public void showToast(String message, int duration) {
        Toast.makeText(this, message, duration).show();
    }

    public void showToast(int resId, int duration) {
        Toast.makeText(this, resId, duration).show();
    }
}
