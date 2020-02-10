package com.tenovaters.android.writer.Notification;

import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseInstanceIdService extends FirebaseMessagingService {
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d("MyRefreshedToken", FirebaseInstanceId.getInstance().getToken());
    }
}
