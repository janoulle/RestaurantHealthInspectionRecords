package com.janeullah.apps.healthinspectionviewer.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.janeullah.apps.healthinspectionviewer.R;
import com.janeullah.apps.healthinspectionviewer.constants.IntentNames;
import com.janeullah.apps.healthinspectionviewer.databinding.ActivityInspectionreportbyrestaurantBinding;
import com.janeullah.apps.healthinspectionviewer.dtos.FlattenedRestaurant;
import com.janeullah.apps.healthinspectionviewer.models.InspectionReport;

import org.parceler.Parcels;

import butterknife.OnClick;

/**
 * https://developer.android.com/topic/libraries/data-binding/index.html
 * https://guides.codepath.com/android/Applying-Data-Binding-for-Views
 * https://github.com/johncarl81/parceler
 * https://github.com/codepath/android_guides/wiki/Using-Parcelable
 *
 * @author Jane Ullah
 * @date 4/23/2017.
 */

public class InspectionReportByRestaurantActivity extends BaseActivity {
    private static final String TAG = "InspectionReport";
    private String mRestaurantSelected;
    private FlattenedRestaurant mRestaurant;
    private ActivityInspectionreportbyrestaurantBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_inspectionreportbyrestaurant);
        mRestaurantSelected = getIntent().getStringExtra(IntentNames.RESTAURANT_KEY_SELECTED);
        if (mRestaurantSelected == null) {
            throw new IllegalArgumentException("Failed to pass a restaurant selection when viewing inspection report activity");
        }
        Log.i(TAG, mRestaurantSelected + " was selected");
        mRestaurant = Parcels.unwrap(getIntent().getParcelableExtra(IntentNames.RESTAURANT_SELECTED));
        Log.i(TAG, "Restaurant data: " + mRestaurant.toMap());
        binding.setInspectionreport(generateInspectionReport(mRestaurant));
    }

    //@OnClick(R.id.countySubmitButton)
    protected void attachListenerToCountySubmitButton() {
        final Intent intent = new Intent(InspectionReportByRestaurantActivity.this, MapsActivity.class);
        startActivity(intent);
    }

    private InspectionReport generateInspectionReport(FlattenedRestaurant restaurant) {
        InspectionReport report = new InspectionReport();
        report.setRestaurant(restaurant);
        report.setScore(restaurant.score);
        report.setCriticalViolationCount(restaurant.criticalViolations);
        report.setNonCriticalViolationCount(restaurant.nonCriticalViolations);
        report.setMostRecentInspectionDate(restaurant.dateReported);
        return report;
    }
}
