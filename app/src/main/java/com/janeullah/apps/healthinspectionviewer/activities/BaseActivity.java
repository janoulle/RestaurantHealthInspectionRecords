package com.janeullah.apps.healthinspectionviewer.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

/**
 * @author Jane Ullah
 * @date 4/27/2017.
 */

public class BaseActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;

    public void showProgressDialog(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage(message);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public boolean isShowing() {
        return mProgressDialog != null && mProgressDialog.isShowing();
    }
}

