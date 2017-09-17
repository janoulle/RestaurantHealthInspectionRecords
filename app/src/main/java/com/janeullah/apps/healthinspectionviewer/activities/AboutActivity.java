package com.janeullah.apps.healthinspectionviewer.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.janeullah.apps.healthinspectionviewer.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutActivity extends BaseActivity {
    private static final String TAG = "AboutActivity";

    @BindView(R.id.app_toolbar)
    public Toolbar mAppToolbar;

    @BindView(R.id.aboutTextView)
    public TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        setSupportActionBar(mAppToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        logViewEvent(TAG);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
