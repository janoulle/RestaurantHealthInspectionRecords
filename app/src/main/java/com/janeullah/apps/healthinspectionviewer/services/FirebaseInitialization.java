package com.janeullah.apps.healthinspectionviewer.services;

import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * @author Jane Ullah
 * @date 4/22/2017.
 */

public class FirebaseInitialization {
    private static final String TAG = "FirebaseInitialization";
    private static final FirebaseInitialization ourInstance = new FirebaseInitialization();
    private DatabaseReference negaReference;

    public static FirebaseInitialization getInstance() {
        return ourInstance;
    }

    private FirebaseInitialization() {
        DatabaseReference databaseReference = setup();
        if (databaseReference != null) {
            negaReference = databaseReference;
        }
    }

    private DatabaseReference setup() {
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(Boolean.TRUE);
            return database.getReference("nega");
        } catch (Exception e) {
            Log.e(TAG, "Failed to setup Firebase initialization", e);
            FirebaseCrash.report(e);
        }
        return null;
    }

    public DatabaseReference getNegaDatabaseReference() {
        return negaReference;
    }
}
