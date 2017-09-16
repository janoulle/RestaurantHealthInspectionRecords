package com.janeullah.apps.healthinspectionviewer.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


/**
 * @author Jane Ullah
 * @date 7/8/2017.
 */

public class AppFirebaseTokenRetrievalService extends FirebaseInstanceIdService {
    public static final String TAG = "AppFirebaseTokenSvc";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(refreshedToken);
    }
}
