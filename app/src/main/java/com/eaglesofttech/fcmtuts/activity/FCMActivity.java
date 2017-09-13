package com.eaglesofttech.fcmtuts.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.eaglesofttech.fcmtuts.R;
import com.eaglesofttech.fcmtuts.utils.CommanUtils;
import com.eaglesofttech.fcmtuts.utils.NotificationUtils;
import com.google.firebase.messaging.FirebaseMessaging;

import timber.log.Timber;

public class FCMActivity extends AppCompatActivity {

    BroadcastReceiver broadcastReceiver;
    private android.widget.TextView tvmessage;
    private android.widget.TextView tvregiid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fcm);
        this.tvregiid = (TextView) findViewById(R.id.tv_regi_id);
        this.tvmessage = (TextView) findViewById(R.id.tv_message);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(CommanUtils.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(CommanUtils.TOPIC_GLOBAL);

                    displayFirebaseRegId();
                } else if (intent.getAction().equals(CommanUtils.PUSH_NOTIFICATION)) {
                    String message = intent.getStringExtra("message");
                    tvmessage.setText(message);
                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(CommanUtils.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(CommanUtils.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(CommanUtils.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Timber.e("Firebase reg id: " + regId);
        Log.e("Firebase reg id: ", regId);
        /*if (!TextUtils.isEmpty(regId))
            tvregiid.setText("Firebase Reg Id: " + regId);
        else
            tvregiid.setText("Firebase Reg Id is not received yet!");*/

        if (TextUtils.isEmpty(regId))
            tvregiid.setText("Firebase Reg Id is not received yet!");
    }


}
