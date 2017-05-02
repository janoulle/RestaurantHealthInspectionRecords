package com.janeullah.apps.healthinspectionviewer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.janeullah.apps.healthinspectionviewer.R;
import com.janeullah.apps.healthinspectionviewer.constants.IntentNames;
import com.janeullah.apps.healthinspectionviewer.dtos.County;
import com.janeullah.apps.healthinspectionviewer.services.FirebaseInitialization;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;

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

    @BindView(R.id.countySubmitButton)
    public Button mCountyButton;

    @BindView(R.id.app_toolbar)
    public Toolbar mAppToolbar;

    private ChildEventListener mCountyListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)
                .build();
        Fabric.with(fabric);
        ButterKnife.bind(this);

        setSupportActionBar(mAppToolbar);
        negaCountyDatabaseReference = FirebaseInitialization
                .getInstance()
                .getNegaDatabaseReference()
                .child("counties");
    }

    @OnClick(R.id.countySubmitButton)
    protected void attachListenerToCountySubmitButton() {
        final Intent intent = new Intent(MainActivity.this, RestaurantsInCountyActivity.class);
        String countyChosen = mCountySpinner.getSelectedItem().toString();
        intent.putExtra(IntentNames.COUNTY_SELECTED, countyChosen);
        startActivity(intent);
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
    public void onStart() {
        super.onStart();

        cAdapter = new ArrayAdapter<>(this,R.layout.item_spinner,R.id.countyItemSpinnerView);

        ChildEventListener countyListener = getChildEventListener();
        negaCountyDatabaseReference.addChildEventListener(countyListener);
        mCountyListener = countyListener;

        cAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mCountySpinner.setAdapter(cAdapter);
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
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

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
