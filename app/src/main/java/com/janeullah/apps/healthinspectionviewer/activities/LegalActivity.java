package com.janeullah.apps.healthinspectionviewer.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.janeullah.apps.healthinspectionviewer.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LegalActivity extends BaseActivity {
    private static final String TAG = "DisclaimerActivity";
    @BindView(R.id.app_toolbar)
    public Toolbar mAppToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legal);
        ButterKnife.bind(this);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        setSupportActionBar(mAppToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        logViewEvent(TAG);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        finish();
        return true;
    }

}
