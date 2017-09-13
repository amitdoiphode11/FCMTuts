package com.eaglesofttech.fcmtuts.services;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;

import com.eaglesofttech.fcmtuts.utils.CommanUtils;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import timber.log.Timber;

/**
 * Created by android on 13/9/17.
 */

public class MyFirebaseInstanceId extends FirebaseInstanceIdService {


    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        sendRegistrationToServer(FirebaseInstanceId.getInstance().getToken());
        storeRegIdInPref(FirebaseInstanceId.getInstance().getToken());

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(CommanUtils.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", FirebaseInstanceId.getInstance().getToken());
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Timber.e(token);
    }

    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(CommanUtils.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.commit();
    }

}
