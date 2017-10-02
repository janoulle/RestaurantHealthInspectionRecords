package com.janeullah.apps.healthinspectionviewer.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v4.os.ResultReceiver;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.perf.metrics.AddTrace;
import com.google.gson.Gson;
import com.janeullah.apps.healthinspectionviewer.R;
import com.janeullah.apps.healthinspectionviewer.async.yelp.YelpAccessRequestTask;
import com.janeullah.apps.healthinspectionviewer.async.yelp.YelpSearchBusinessesTask;
import com.janeullah.apps.healthinspectionviewer.callbacks.ViolationActivityCallBack;
import com.janeullah.apps.healthinspectionviewer.constants.AppConstants;
import com.janeullah.apps.healthinspectionviewer.constants.GeocodeConstants;
import com.janeullah.apps.healthinspectionviewer.constants.IntentNames;
import com.janeullah.apps.healthinspectionviewer.constants.YelpConstants;
import com.janeullah.apps.healthinspectionviewer.databinding.ActivityRestaurantDataBinding;
import com.janeullah.apps.healthinspectionviewer.dtos.FlattenedRestaurant;
import com.janeullah.apps.healthinspectionviewer.dtos.GeocodedAddressComponent;
import com.janeullah.apps.healthinspectionviewer.interfaces.YelpAccessTaskListener;
import com.janeullah.apps.healthinspectionviewer.interfaces.YelpSearchTaskListener;
import com.janeullah.apps.healthinspectionviewer.models.yelp.YelpAuthTokenResponse;
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
    private static final Gson gson = new Gson();
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
    @AddTrace(name = "onCreateTrace", enabled = true)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_restaurant_data);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        ButterKnife.bind(this);

        setSupportActionBar(mAppToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSharedPreferences = this.getSharedPreferences(YelpConstants.YELP_PREFERENCES,Context.MODE_PRIVATE);
        mRestaurantSelected = Parcels.unwrap(getIntent().getParcelableExtra(IntentNames.RESTAURANT_SELECTED));
        if (mRestaurantSelected == null) {
            Log.e(TAG,"Restaurant not selected before launching RestaurantDataActivity");
            throw new IllegalArgumentException("Failed to pass a restaurant selection before viewing inspection report activity");
        }
        Log.v(TAG,"resource id = " + mRestaurantSelected.restaurantCheckMarkResourceId);
        mDataBinding.setRestaurantSelected(mRestaurantSelected);
        setTitle(mRestaurantSelected.name);
        ViolationActivityCallBack violationActivityCallBack = new ViolationActivityCallBack() {
            @Override
            public void onClick(View view, FlattenedRestaurant restaurant) {
                final Intent intent = new Intent(RestaurantDataActivity.this, RestaurantViolations.class);
                intent.putExtra(IntentNames.RESTAURANT_SELECTED, Parcels.wrap(restaurant));

                logSelectionEvent(AppConstants.RESTAURANT_DETAIL,restaurant.getNameKey(),TAG);
                startActivity(intent);
            }
        };
        mDataBinding.setViolationButtonClickCallBack(violationActivityCallBack);

        initializeMapView(savedInstanceState);
        checkAndInitiateYelpTokenRequest();
        startBackgroundGeocodeServiceLookup();
        logViewEvent(TAG);
    }

    private void checkAndInitiateYelpTokenRequest() {
        String yelpAuthToken = mSharedPreferences.getString(YelpConstants.SAVED_YELP_AUTH_TOKEN,"");
        if (TextUtils.isEmpty(yelpAuthToken)){
            Log.i(TAG,"Yelp token not found! Making api call");
            YelpAccessTaskListener taskListener = new YelpAccessTaskListener();
            taskListener.setGeocodedComponents(mGeocodedAddressComponents);
            taskListener.setSharedPreferences(mSharedPreferences);
            taskListener.setActivity(this);
            taskListener.setIntent(getIntent());
            mAccessRequestTask.setYelpAccessTaskListener(taskListener);
            mAccessRequestTask.execute();
        }
    }

    private void startBackgroundGeocodeServiceLookup() {
        mGeocodeResultsReceiver = new GeocodingResultsReceiver(new Handler());
        mGeocodeResultsReceiver.setActivity(this);
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

    /*
    * Commenting out because this button may not always be visible
    @OnClick(R.id.viewViolationsButton)
    public void launchViolationsActivity(){
        final Intent intent = new Intent(this, RestaurantViolations.class);
        intent.putExtra(IntentNames.RESTAURANT_SELECTED, Parcels.wrap(mRestaurantSelected));
        startActivity(intent);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            loadActivity(this, AboutActivity.class);
            return true;
        } else if(id == R.id.action_legal){
            loadActivity(this, LegalActivity.class);
            return true;
        }else if (id == android.R.id.home) {
            Log.i(TAG, "Up clicked!");
            Intent upIntent = NavUtils.getParentActivityIntent(this);
            upIntent.putExtra(IntentNames.COUNTY_SELECTED, mRestaurantSelected.county);
            navigateUp(this,upIntent);
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
        MarkerOptions markerOption = new MarkerOptions()
                .draggable(true)
                .title(mRestaurantSelected.address)
                .snippet(getString(R.string.map_marker_snippet))
                .position(mRestaurantCoordinates);
        mMap.addMarker(markerOption)
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
        mAccessRequestTask.setYelpAccessTaskListener(null);
        mYelpSearchRequestTask.setYelpSearchTaskListener(null);
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    /**
     * Receiver implementation for Intent Service
     */
    private class GeocodingResultsReceiver extends ResultReceiver {
        private static final String TAG = "GeocodingReceiver";
        private BaseActivity activity;

        /**
         * Create a new ResultReceiver to receive results.  Your
         * {@link #onReceiveResult} method will be called from the thread running
         * <var>handler</var> if given, or from an arbitrary thread if null.
         *
         * @param handler
         */
        public GeocodingResultsReceiver(Handler handler) {
            super(handler);
        }

        public void setActivity(BaseActivity activity){
            this.activity = activity;
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultCode == GeocodeConstants.SUCCESS_RESULT) {
                mGeocodedAddressComponents = Parcels.unwrap(resultData.getParcelable(GeocodeConstants.RESULT_DATA_KEY));
                mRestaurantCoordinates = mGeocodedAddressComponents.coordinates;
                Log.i(TAG, String.format(Locale.getDefault(),"Coordinates (%s) received", mRestaurantCoordinates));
                if (!TextUtils.isEmpty(mSharedPreferences.getString(YelpConstants.SAVED_YELP_AUTH_TOKEN,""))){
                    YelpSearchRequest yelpSearchRequest = new YelpSearchRequest(constructYelpAuthTokenResponseFromPreferences(),mGeocodedAddressComponents,mRestaurantSelected);
                    YelpSearchTaskListener yelpSearchTaskListener = new YelpSearchTaskListener();
                    yelpSearchTaskListener.setIntent(getIntent());
                    yelpSearchTaskListener.setActivity(activity);
                    mYelpSearchRequestTask.setYelpSearchTaskListener(yelpSearchTaskListener);
                    mYelpSearchRequestTask.execute(yelpSearchRequest);
                }
                mMapView.getMapAsync(RestaurantDataActivity.this);
            } else {
                String output = resultData.getString(GeocodeConstants.RESULT_DATA_KEY);
                Log.d(TAG,output);
                showToast(output,Toast.LENGTH_SHORT);
            }
        }

        private YelpAuthTokenResponse constructYelpAuthTokenResponseFromPreferences(){
            //TODO: expire token plan
            Log.i(TAG,"Constructing Yelp response using saved auth token");
            String yelpAuthToken = mSharedPreferences.getString(YelpConstants.SAVED_YELP_AUTH_TOKEN,"");
            String tokenType = mSharedPreferences.getString(YelpConstants.SAVED_YELP_TOKEN_TYPE,"Bearer");
            Integer expiry = mSharedPreferences.getInt(YelpConstants.SAVED_YELP_TOKEN_EXPIRATION,15462984);
            YelpAuthTokenResponse authTokenResponse = new YelpAuthTokenResponse();
            authTokenResponse.setAccessToken(yelpAuthToken);
            authTokenResponse.setExpiresIn(expiry);
            authTokenResponse.setTokenType(tokenType);
            return authTokenResponse;
        }
    }
}
