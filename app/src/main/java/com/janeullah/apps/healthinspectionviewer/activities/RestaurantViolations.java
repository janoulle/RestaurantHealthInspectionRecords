package com.janeullah.apps.healthinspectionviewer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.janeullah.apps.healthinspectionviewer.R;
import com.janeullah.apps.healthinspectionviewer.adapters.ViolationPagerAdapter;
import com.janeullah.apps.healthinspectionviewer.constants.IntentNames;
import com.janeullah.apps.healthinspectionviewer.dtos.FlattenedRestaurant;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RestaurantViolations extends BaseActivity {
    public static final String TAG = "RestaurantViolations";
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private ViolationPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    @BindView(R.id.container)
    public ViewPager mViewPager;

    @BindView(R.id.tabs)
    public TabLayout mTabLayout;

    @BindView(R.id.toolbar)
    public Toolbar mToolbar;

    private FlattenedRestaurant mRestaurantSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_violations);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRestaurantSelected = Parcels.unwrap(getIntent().getParcelableExtra(IntentNames.RESTAURANT_SELECTED));
        if (mRestaurantSelected == null) {
            Log.e(TAG,"Restaurant not selected before launching RestaurantDataActivity");
            throw new IllegalArgumentException("Failed to pass a restaurant selection before viewing inspection report activity");
        }

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity and Set up the ViewPager with the sections adapter.
        mSectionsPagerAdapter = new ViolationPagerAdapter(getSupportFragmentManager(),getApplicationContext());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        //default user to tab with non-critical violations
        if (mRestaurantSelected.criticalViolations == 0){
            mViewPager.setCurrentItem(1);
        }

        setTitle(getString(R.string.restaurant_violations_title));
    }

    @OnClick(R.id.fab)
    public void launchSnackbarNotification(View view){
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_restaurant_violations, menu);
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
        }else if (id == android.R.id.home) {
            Log.i(TAG, "Up clicked!");
            Intent upIntent = NavUtils.getParentActivityIntent(this);
            upIntent.putExtra(IntentNames.RESTAURANT_SELECTED,Parcels.wrap(mRestaurantSelected));
            navigateUp(this,upIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
