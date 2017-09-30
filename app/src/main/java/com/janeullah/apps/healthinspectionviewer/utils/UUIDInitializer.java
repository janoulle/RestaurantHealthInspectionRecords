package com.janeullah.apps.healthinspectionviewer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.janeullah.apps.healthinspectionviewer.activities.BaseActivity;
import com.janeullah.apps.healthinspectionviewer.constants.AppConstants;

import java.util.UUID;

/**
 * @author Jane Ullah
 * @date 9/16/2017.
 */

public class UUIDInitializer {
    private static final String TAG = "UUIDInitializer";
    private String uuidStashed = UUID.randomUUID().toString();;
    private static UUIDInitializer instance = null;

    public static UUIDInitializer getInstance(BaseActivity activity) {
        if (instance == null) {
            instance = new UUIDInitializer();
            SharedPreferences preferences = activity.getSharedPreferences(AppConstants.UUID_KEY, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(AppConstants.UUID_IDENTIFIER, instance.getUUID());
            editor.apply();
            Log.i(TAG,"Singleton UUIDInitializer has been initialized");
        }
        return instance;
    }

    private UUIDInitializer(){}

    public String getUUID(){
        return uuidStashed;
    }
}
