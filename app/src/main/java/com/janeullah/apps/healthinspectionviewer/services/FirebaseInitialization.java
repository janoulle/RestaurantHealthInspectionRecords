package com.janeullah.apps.healthinspectionviewer.services;

import android.util.Log;

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
        if (databaseReference != null){
            setupListener(databaseReference);
            negaReference = databaseReference;
        }
    }

    private void setupListener(DatabaseReference reference){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private DatabaseReference setup(){
        try{
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            return database.getReference("nega");
        }catch(Exception e){
            Log.e(TAG,"Failed to setup Firebase initialization",e);
        }
        return null;
    }

    public DatabaseReference getNegaDatabaseReference(){
        return negaReference;
    }
}
