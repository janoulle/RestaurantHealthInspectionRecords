package com.janeullah.apps.healthinspectionviewer.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.janeullah.apps.healthinspectionviewer.R;
import com.janeullah.apps.healthinspectionviewer.utils.EventLoggingUtils;
import com.janeullah.apps.healthinspectionviewer.utils.MessageDelayer;
import com.janeullah.apps.healthinspectionviewer.utils.SimpleIdlingResource;

/**
 * https://stackoverflow.com/questions/6745797/how-to-set-entire-application-in-portrait-mode-only/9784269#9784269
 * @author Jane Ullah
 * @date 4/27/2017.
 */

public abstract class BaseActivity extends AppCompatActivity implements MessageDelayer.DelayerCallback {
    protected FirebaseAnalytics mFirebaseAnalytics;
    private ProgressBar progressBar;
    // The Idling Resource which will be null in production.
    @Nullable
    protected SimpleIdlingResource mIdlingResource;

    public void showProgressDialog(String message) {
        if (progressBar == null) {
            progressBar = (ProgressBar) findViewById(R.id.loadingModalForIndeterminateProgress);
        }
        progressBar.setVisibility(ProgressBar.VISIBLE);
    }

    public void hideProgressDialog() {
        if (isProgressBarVisible()) {
            progressBar.setVisibility(ProgressBar.GONE);
        }
    }

    private boolean isProgressBarVisible() {
        return progressBar != null && progressBar.getVisibility() == ProgressBar.VISIBLE;
    }

    protected void navigateUp(Activity activity, Intent upIntent) {
        if (NavUtils.shouldUpRecreateTask(activity, upIntent)) {
            // This activity is NOT part of this app's task, so create a new task
            // when navigating up, with a synthesized back stack.
            TaskStackBuilder.create(activity)
                    // Add all of this activity's parents to the back stack
                    .addNextIntentWithParentStack(upIntent)
                    // Navigate up to the closest parent
                    .startActivities();
        } else {
            // This activity is part of this app's task, so simply
            // navigate up to the logical parent activity.
            NavUtils.navigateUpTo(activity, upIntent);
        }
    }

    public void showToast(String message, int duration) {
        Toast.makeText(this, message, duration).show();
    }

    protected <T> void loadActivity(Activity sourceActivity, Class<T> pageToLoad){
        Intent intent = new Intent(sourceActivity, pageToLoad);
        this.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }


    @Override
    public void onDone(String text) {
        // The delayer notifies the activity via a callback.
        //please hold, caller
    }

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    protected void logViewEvent(String tag) {
        EventLoggingUtils.logViewEvent(tag,mFirebaseAnalytics,this);
    }

    protected void logSelectionEvent(String key, String value, String tag, FirebaseAnalytics firebaseAnalytics) {
        EventLoggingUtils.logSelectionEvent(key,value,tag,firebaseAnalytics,this);
    }
}

