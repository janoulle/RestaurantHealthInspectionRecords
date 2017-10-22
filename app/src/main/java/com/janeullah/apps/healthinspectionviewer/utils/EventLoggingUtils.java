package com.janeullah.apps.healthinspectionviewer.utils;

import android.os.Bundle;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.janeullah.apps.healthinspectionviewer.activities.BaseActivity;
import com.janeullah.apps.healthinspectionviewer.analytics.ActionParameters;

import java.util.Calendar;

/**
 * @author Jane Ullah
 * @date 10/1/2017.
 */

public class EventLoggingUtils {

    private EventLoggingUtils(){}

    public static void logSelectionEvent(String key, String value, String tag, FirebaseAnalytics firebaseAnalytics, BaseActivity activity) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.LOCATION, tag);
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, UUIDInitializer.getInstance(activity).getUUID());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, key);
        bundle.putString(FirebaseAnalytics.Param.VALUE,value);
        bundle.putLong(FirebaseAnalytics.Param.START_DATE, Calendar.getInstance().getTimeInMillis());
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT,bundle);
        logSelectionEventWithFabric(key,value,tag, activity);
    }

    private static void logSelectionEventWithFabric(String key, String value, String tag, BaseActivity activity){
        Answers.getInstance().logCustom(new CustomEvent("Selection Made")
                .putCustomAttribute(ActionParameters.LOCATION.text(),tag)
                .putCustomAttribute(ActionParameters.ITEM_NAME.text(),key)
                .putCustomAttribute(ActionParameters.ITEM_VALUE.text(),value)
                .putCustomAttribute(ActionParameters.UUID.text(),UUIDInitializer.getInstance(activity).getUUID())
                .putCustomAttribute(ActionParameters.START_DATE.text(), Calendar.getInstance().getTimeInMillis()));
    }

    public static void logViewEvent(String tag, FirebaseAnalytics firebaseAnalytics, BaseActivity activity) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.LOCATION, tag);
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, UUIDInitializer.getInstance(activity).getUUID());
        bundle.putLong(FirebaseAnalytics.Param.START_DATE, Calendar.getInstance().getTimeInMillis());
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
        logViewEventsWithFabric(tag,"Activity",activity);
    }

    private static void logViewEventsWithFabric(String tag, String contentType, BaseActivity activity){
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName(tag)
                .putContentType(contentType)
                .putCustomAttribute(ActionParameters.UUID.text(),UUIDInitializer.getInstance(activity).getUUID())
                .putCustomAttribute(ActionParameters.START_DATE.text(), Calendar.getInstance().getTimeInMillis()));
    }
}
