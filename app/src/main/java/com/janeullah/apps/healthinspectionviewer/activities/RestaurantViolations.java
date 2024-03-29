package com.janeullah.apps.healthinspectionviewer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.facebook.stetho.Stetho;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.perf.metrics.AddTrace;
import com.janeullah.apps.healthinspectionviewer.R;
import com.janeullah.apps.healthinspectionviewer.adapters.ViolationPagerAdapter;
import com.janeullah.apps.healthinspectionviewer.constants.IntentNames;
import com.janeullah.apps.healthinspectionviewer.dtos.FlattenedRestaurant;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantViolations extends BaseActivity {
    public static final String TAG = "RestaurantViolations";
    public static final int HAS_ZERO_CRITICAL_VIOLATIONS = 0;
    public static final int NON_CRITICAL_VIOLATIONS_TAB = 1;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     * sections. We use a {@link FragmentPagerAdapter} derivative, which will keep every loaded
     * fragment in memory. If this becomes too memory intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private ViolationPagerAdapter mSectionsPagerAdapter;

    /** The {@link ViewPager} that will host the section contents. */
    @BindView(R.id.container)
    public ViewPager mViewPager;

    @BindView(R.id.tabs)
    public TabLayout mTabLayout;

    @BindView(R.id.app_toolbar)
    public Toolbar mToolbar;

    private FlattenedRestaurant mRestaurantSelected;

    @Override
    @AddTrace(name = "onCreateTrace", enabled = true)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_violations);

        initializeFabric(this);
        Stetho.initializeWithDefaults(this);
        ButterKnife.bind(this);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRestaurantSelected =
                Parcels.unwrap(getIntent().getParcelableExtra(IntentNames.RESTAURANT_SELECTED));
        if (mRestaurantSelected == null) {
            Log.e(TAG, "Restaurant not selected before launching RestaurantDataActivity");
            throw new IllegalArgumentException(
                    "Failed to pass a restaurant selection before viewing inspection report activity");
        }

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity and Set up the ViewPager with the sections adapter.
        mSectionsPagerAdapter =
                new ViolationPagerAdapter(
                        mRestaurantSelected, getSupportFragmentManager(), getApplicationContext());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        // default user to tab with non-critical violations
        if (mRestaurantSelected.criticalViolations == HAS_ZERO_CRITICAL_VIOLATIONS) {
            mViewPager.setCurrentItem(NON_CRITICAL_VIOLATIONS_TAB);
        }

        setTitle(getString(R.string.restaurant_violations_title));
        logViewEvent(TAG);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            loadActivity(this, AboutActivity.class);
            return true;
        } else if (id == R.id.action_legal) {
            loadActivity(this, LegalActivity.class);
            return true;
        } else if (id == android.R.id.home) {
            Log.i(TAG, "Up clicked!");
            Intent upIntent = NavUtils.getParentActivityIntent(this);
            upIntent.putExtra(IntentNames.RESTAURANT_SELECTED, Parcels.wrap(mRestaurantSelected));
            navigateUp(this, upIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
