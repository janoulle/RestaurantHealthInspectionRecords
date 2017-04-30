package com.janeullah.apps.healthinspectionviewer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.crash.FirebaseCrash;
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
 * https://www.bignerdranch.com/blog/a-view-divided-adding-dividers-to-your-recyclerview-with-itemdecoration/
 */
public class HomeActivity extends BaseActivity {
    private static final String TAG = "HomeActivity";
    private DatabaseReference negaCountyDatabaseReference;
    private ArrayAdapter<CharSequence> cAdapter;

    @BindView(R.id.counties_spinner)
    public Spinner mCountySpinner;

    @BindView(R.id.countySubmitButton)
    public Button mCountyButton;

    private ChildEventListener mCountyListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)
                .build();
        Fabric.with(fabric);
        setContentView(R.layout.activity_main_spinner);
        ButterKnife.bind(this);

        negaCountyDatabaseReference = FirebaseInitialization
                .getInstance()
                .getNegaDatabaseReference()
                .child("counties");
    }

    @OnClick(R.id.countySubmitButton)
    protected void attachListenerToCountySubmitButton() {
        final Intent intent = new Intent(HomeActivity.this, RestaurantsInCountyActivity.class);
        String countyChosen = mCountySpinner.getSelectedItem().toString();
        intent.putExtra(IntentNames.COUNTY_SELECTED, countyChosen);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        cAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, android.R.id.text1);

        ChildEventListener countyListener = getChildEventListener();
        negaCountyDatabaseReference.addChildEventListener(countyListener);
        mCountyListener = countyListener;

        cAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
                Toast.makeText(HomeActivity.this, "Failed to load all counties.",
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
