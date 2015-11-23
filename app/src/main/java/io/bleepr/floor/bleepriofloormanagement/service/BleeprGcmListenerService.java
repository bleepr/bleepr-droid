package io.bleepr.floor.bleepriofloormanagement.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.google.android.gms.gcm.GcmListenerService;

public class BleeprGcmListenerService extends GcmListenerService {

    private static final String TAG = "BleeprGcmListenerService";

    public BleeprGcmListenerService() {
    }

    @Override
    public void onMessageReceived(String from, Bundle data)
    {}
}
