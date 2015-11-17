package io.bleepr.floor.bleepriofloormanagement.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import io.bleepr.floor.bleepriofloormanagement.network.RequestQueueBox;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class BleeprBackendQueryService extends IntentService {
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_REFRESH = "io.bleepr.floor.bleepriofloormanagement.service.action.REFRESH";

    private static final String EXTRA_CALLBACK = "io.bleepr.floor.bleepriofloormanagement.service.extra.CALLBACK";

    private static final String TABLES_API_URL = "";
    private static final String ORDERS_API_URL = "";
    private static final String OCCUPANCIES_API_URL = "";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startRefresh(Context context, ResultReceiver callback) {
        Intent intent = new Intent(context, BleeprBackendQueryService.class);
        intent.setAction(ACTION_REFRESH);
        intent.putExtra(EXTRA_CALLBACK, callback);
        context.startService(intent);
    }

    public BleeprBackendQueryService() {
        super("BleeprBackendQueryService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_REFRESH.equals(action)) {
                final ResultReceiver callback = intent.getParcelableExtra(EXTRA_CALLBACK);
                handleActionRefresh(callback);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionRefresh(ResultReceiver callback) {
        JsonObjectRequest tables = new JsonObjectRequest(Request.Method.GET, TABLES_API_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int remoteID = 0;
                        JsonObjectRequest occupancies = new JsonObjectRequest(Request.Method.GET, String.format(OCCUPANCIES_API_URL, remoteID), null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }
                        );

                        JsonObjectRequest orders = new JsonObjectRequest(Request.Method.GET, String.format(ORDERS_API_URL, remoteID), null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }
                        );
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        RequestQueueBox.getInstance(getApplicationContext()).addToRequestQueue(tables);
    }
}
