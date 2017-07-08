package com.janeullah.apps.healthinspectionviewer.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.janeullah.apps.healthinspectionviewer.R;

/**
 * https://stackoverflow.com/questions/6745797/how-to-set-entire-application-in-portrait-mode-only/9784269#9784269
 * @author Jane Ullah
 * @date 4/27/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {

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
}

