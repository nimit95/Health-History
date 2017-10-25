package com.hackdtu.healthhistory.fcm;


import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.hackdtu.healthhistory.FirebaseReference;
import com.hackdtu.healthhistory.utils.SuperPrefs;

/**
 * Created by nimit on 25/10/17.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {


    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);


    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        SuperPrefs superPrefs = new SuperPrefs(this);
        if (superPrefs.stringExists("user-id")) {
            FirebaseReference.userReference.child(superPrefs.getString("user-id"))
                    .child("firebaseInsstanceId").setValue(token);
        }
    }
}
