package com.janeullah.apps.healthinspectionviewer.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.facebook.stetho.Stetho;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.perf.metrics.AddTrace;
import com.janeullah.apps.healthinspectionviewer.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

public class LegalActivity extends BaseActivity {
    private static final String TAG = "DisclaimerActivity";

    @BindView(R.id.app_toolbar)
    public Toolbar mAppToolbar;

    @BindView(R.id.legalTextView)
    public TextView mTextView;

    @Override
    @AddTrace(name = "onCreateTrace", enabled = true)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legal);

        final Fabric fabric =
                new Fabric.Builder(this)
                        .kits(new Crashlytics(), new Answers())
                        .debuggable(true)
                        .build();
        Fabric.with(fabric);
        Stetho.initializeWithDefaults(this);
        ButterKnife.bind(this);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        setSupportActionBar(mAppToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTextView.setMovementMethod(LinkMovementMethod.getInstance());
        logViewEvent(TAG);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
