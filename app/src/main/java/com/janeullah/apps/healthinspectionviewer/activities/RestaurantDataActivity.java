package com.janeullah.apps.healthinspectionviewer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v4.os.ResultReceiver;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.janeullah.apps.healthinspectionviewer.R;
import com.janeullah.apps.healthinspectionviewer.constants.GeocodeConstants;
import com.janeullah.apps.healthinspectionviewer.constants.IntentNames;
import com.janeullah.apps.healthinspectionviewer.dtos.FlattenedRestaurant;
import com.janeullah.apps.healthinspectionviewer.models.InspectionReport;
import com.janeullah.apps.healthinspectionviewer.services.FetchAddressIntentService;

import org.parceler.Parcels;

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
    private FlattenedRestaurant mRestaurantSelected;

    @BindView(R.id.item_map)
    protected MapView mMapView;

    @BindView(R.id.inspection_score)
    protected TextView mRestaurantScore;

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_data);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRestaurantSelected = Parcels.unwrap(getIntent().getParcelableExtra(IntentNames.RESTAURANT_SELECTED));
        if (mRestaurantSelected == null) {
            Log.e(TAG,"Restaurant not selected before launching RestaurantDataActivity");
            throw new IllegalArgumentException("Failed to pass a restaurant selection before viewing inspection report activity");
        }
        mRestaurantScore.setText(String.valueOf(mRestaurantSelected.score));

        initializeMapView(savedInstanceState);
        initializeReportData();
        startBackgroundGeocodeServiceLookup();
    }

    private void initializeReportData() {
        InspectionReport report = new InspectionReport(mRestaurantSelected);
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
                NavUtils.navigateUpFromSameTask(this);
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
                .title(mRestaurantSelected.name));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
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
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    public class GeocodingResultsReceiver extends ResultReceiver {
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
                mRestaurantCoordinates = resultData.getParcelable(GeocodeConstants.RESULT_DATA_KEY);
                Log.i(TAG,"Coordinates ("+ mRestaurantCoordinates +") received");
                mMapView.getMapAsync(RestaurantDataActivity.this);
            } else {
                String output = resultData.getString(GeocodeConstants.RESULT_DATA_KEY);
                Log.d(TAG,output);
                showToast(output, Toast.LENGTH_SHORT);
            }
        }
    }

    public void showToast(String message, int duration) {
        Toast.makeText(this, message, duration);
    }

    public void showToast(int resId, int duration) {
        Toast.makeText(this, resId, duration).show();
    }
}
