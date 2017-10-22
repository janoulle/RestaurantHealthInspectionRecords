package com.janeullah.apps.healthinspectionviewer.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.facebook.stetho.Stetho;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.perf.metrics.AddTrace;
import com.janeullah.apps.healthinspectionviewer.R;
import com.janeullah.apps.healthinspectionviewer.constants.AppConstants;
import com.janeullah.apps.healthinspectionviewer.constants.IntentNames;
import com.janeullah.apps.healthinspectionviewer.dtos.County;
import com.janeullah.apps.healthinspectionviewer.services.firebase.FirebaseInitialization;
import com.janeullah.apps.healthinspectionviewer.utils.MessageDelayer;

import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;

//import android.widget.SearchView;

/**
 * https://developer.android.com/training/appbar/setting-up.html
 * https://www.bignerdranch.com/blog/a-view-divided-adding-dividers-to-your-recyclerview-with-itemdecoration/
 */
public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private DatabaseReference negaCountyDatabaseReference;
    private ArrayAdapter<CharSequence> cAdapter;

    @BindView(R.id.counties_spinner)
    public Spinner mCountySpinner;

    @BindView(R.id.app_toolbar)
    public Toolbar mAppToolbar;

    @BindView(R.id.titleForMainScreen)
    public TextView mTitleView;

    private ChildEventListener mCountyListener;
    private AtomicBoolean isDropDownInitialized = new AtomicBoolean(false);


    @Override
    @AddTrace(name = "onCreateTrace", enabled = true)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics(),new Answers())
                .debuggable(true)
                .build();
        Fabric.with(fabric);
        Stetho.initializeWithDefaults(this);
        ButterKnife.bind(this);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        setSupportActionBar(mAppToolbar);

        mTitleView.setText(R.string.select_county_main_activity);
        negaCountyDatabaseReference = FirebaseInitialization
                .getInstance()
                .getNegaDatabaseReference()
                .child("counties");

        // Initialize the Amazon Cognito credentials provider
        /* CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                AWS_COGNITO_POOL_ID,
                Regions.US_EAST_1
        );*/

        logViewEvent(TAG);
    }

    @OnClick(R.id.countySubmitButton)
    protected void attachListenerToCountySubmitButton() {
        final Intent intent = new Intent(MainActivity.this, RestaurantsInCountyActivity.class);
        if (mCountySpinner.getSelectedItem() != null){
            String countyChosen = mCountySpinner.getSelectedItem().toString();
            intent.putExtra(IntentNames.COUNTY_SELECTED, countyChosen);

            // Submit the message to the delayer.
            MessageDelayer.processMessage(countyChosen, this, mIdlingResource);

            logSelectionEvent(AppConstants.COUNTY_SELECTION, countyChosen, TAG,mFirebaseAnalytics);
            startActivity(intent);
        }else{
            showToast("Please make a selection",Toast.LENGTH_SHORT);
        }
    }

    /**
     * https://developer.android.com/training/search/setup.html#create-sa
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        searchItem.setVisible(true);
        SearchView searchView = (SearchView) searchItem.getActionView();
        // Assumes current activity is the searchable activity
        searchView.setVisibility(View.VISIBLE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            loadActivity(this, AboutActivity.class);
            return true;
        } else if(id == R.id.action_legal) {
            loadActivity(this, LegalActivity.class);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        cAdapter = new ArrayAdapter<>(this,R.layout.item_spinner,R.id.countyItemSpinnerView);

        ChildEventListener countyListener = getChildEventListener();
        negaCountyDatabaseReference.addChildEventListener(countyListener);
        mCountyListener = countyListener;

        cAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
    }

    @NonNull
    private ChildEventListener getChildEventListener() {
        return new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                County county = dataSnapshot.getValue(County.class);
                Log.i(TAG, "County: " + county.name);
                Log.i(TAG, "Child Count: " + dataSnapshot.getChildrenCount());
                Log.i(TAG, "County Restaurant Size: " + county.restaurants.size());
                Log.i(TAG, "Snapshot Key: " + dataSnapshot.getKey());
                cAdapter.add(dataSnapshot.getKey());
                if (!isDropDownInitialized.get()){
                    mCountySpinner.setAdapter(cAdapter);
                    isDropDownInitialized.set(true);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadCounties:onCancelled", databaseError.toException());
                Toast.makeText(MainActivity.this, "Failed to load all counties.",
                        Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mCountyListener != null) {
            negaCountyDatabaseReference.removeEventListener(mCountyListener);
        }
    }
}
