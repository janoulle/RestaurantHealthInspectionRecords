package com.janeullah.apps.healthinspectionviewer.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.janeullah.apps.healthinspectionviewer.R;
import com.janeullah.apps.healthinspectionviewer.analytics.ActionParameters;
import com.janeullah.apps.healthinspectionviewer.services.UUIDInitializer;

import java.util.Calendar;

/**
 * https://stackoverflow.com/questions/6745797/how-to-set-entire-application-in-portrait-mode-only/9784269#9784269
 * @author Jane Ullah
 * @date 4/27/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected FirebaseAnalytics mFirebaseAnalytics;
    private ProgressBar progressBar;

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

    protected void logViewEventsWithFabric(String tag, String contentType){
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName(tag)
                .putContentType(contentType)
                .putCustomAttribute(ActionParameters.UUID.text(),UUIDInitializer.getInstance(this).getUUID())
                .putCustomAttribute(ActionParameters.START_DATE.text(), Calendar.getInstance().getTimeInMillis()));
    }

    private void logSelectionEventWithFabric(String key, String value, String tag){
        Answers.getInstance().logCustom(new CustomEvent("Selection Made")
                .putCustomAttribute(ActionParameters.LOCATION.text(),tag)
                .putCustomAttribute(ActionParameters.ITEM_NAME.text(),key)
                .putCustomAttribute(ActionParameters.ITEM_VALUE.text(),value)
                .putCustomAttribute(ActionParameters.UUID.text(),UUIDInitializer.getInstance(this).getUUID())
                .putCustomAttribute(ActionParameters.START_DATE.text(), Calendar.getInstance().getTimeInMillis()));
    }

    protected void logViewEvent(String tag) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.LOCATION, tag);
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, UUIDInitializer.getInstance(this).getUUID());
        bundle.putLong(FirebaseAnalytics.Param.START_DATE, Calendar.getInstance().getTimeInMillis());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
        logViewEventsWithFabric(tag,"Activity");
    }

    protected void logSelectionEvent(String key, String value, String tag) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.LOCATION, tag);
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, UUIDInitializer.getInstance(this).getUUID());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, key);
        bundle.putString(FirebaseAnalytics.Param.VALUE,value);
        bundle.putLong(FirebaseAnalytics.Param.START_DATE, Calendar.getInstance().getTimeInMillis());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        logSelectionEventWithFabric(key,value,tag);
    }


}

