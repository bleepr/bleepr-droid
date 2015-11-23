package io.bleepr.floor.bleepriofloormanagement.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.android.gms.iid.InstanceIDListenerService;

public class BleeprInstanceIDListenerService extends InstanceIDListenerService {
    private static final String TAG = "BleeprInstanceIDLS";

    public BleeprInstanceIDListenerService() {
    }

    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
