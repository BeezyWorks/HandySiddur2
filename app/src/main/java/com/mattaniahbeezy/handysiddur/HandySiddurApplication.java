package com.mattaniahbeezy.handysiddur;

import android.app.Application;

import firebaseconnector.database.BaseFirebaseConnector;

/**
 * Created by Beezy Works Studios on 6/9/2017.
 */

public class HandySiddurApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BaseFirebaseConnector.initializeFirebase();
    }
}
