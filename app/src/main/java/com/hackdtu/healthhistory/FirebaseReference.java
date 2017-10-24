package com.hackdtu.healthhistory;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



/**
 * Created by piyush on 4/8/17.
 */

public class FirebaseReference extends Application {
    public static FirebaseDatabase database;
    public static DatabaseReference userReference;
    public static DatabaseReference groupsReference;

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(getApplicationContext());
        database = FirebaseDatabase.getInstance();
        userReference = database.getReference().child("users");
        groupsReference = database.getReference().child("groups");
    }
}
