package io.bleepr.floor.bleepriofloormanagement.service;

import android.app.IntentService;
import android.content.ContentUris;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.bleepr.floor.bleepriofloormanagement.network.RequestQueueBox;
import io.bleepr.floor.bleepriofloormanagement.provider.BleeprConstants;

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

    private static final String ACTION_ORDER_UPDATE  = "io.bleepr.floor.bleepriofloormanagement.service.action.ORDERUPDATE";
    private static final String ACTION_ORDER_DELETE = "io.bleepr.floor.bleepriofloormanagement.service.action.ORDERDELETE";
    private static final String EXTRA_ORDER_ID = "io.bleepr.floor.bleepriofloormanagement.service.extra.ORDERID";

    public static final String TABLES_API_URL = "http://burger.bleepr.io/tables.json";
    public static final String ORDERS_API_URL = "http://burger.bleepr.io/tables/%d/orders.json";
    public static final String OCCUPANCIES_API_URL = "http://burger.bleepr.io/tables/%d/occupancies/current.json";
    public static final String OCCUPANCIES_FUTURE_API_URL = "http://burger.bleepr.io/tables/%d/occupancies/bookings.json";
    public static final String CUSTOMER_API_URL = "http://burger.bleepr.io/customers/%d.json";

    public static final String ORDER_UPDATE_API_URL = "http://burger.bleepr.io/orders/%d.json";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startRefresh(Context context, ResultReceiver callback) {
        Intent intent = new Intent(context, BleeprBackendQueryService.class);
        intent.setAction(ACTION_REFRESH);
        intent.putExtra(EXTRA_CALLBACK, callback);
        context.startService(intent);
    }

    public static void deleteOrderRemote(Context context, int orderId, ResultReceiver callback) {
        Intent intent = new Intent(context, BleeprBackendQueryService.class);
        intent.setAction(ACTION_ORDER_DELETE);
        intent.putExtra(EXTRA_ORDER_ID, orderId);
        intent.putExtra(EXTRA_CALLBACK, callback);
        context.startService(intent);
    }

    public static void updateOrderRemote(Context context, int orderId, ResultReceiver callback) {
        Intent intent = new Intent(context, BleeprBackendQueryService.class);
        intent.setAction(ACTION_ORDER_UPDATE);
        intent.putExtra(EXTRA_ORDER_ID, orderId);
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
            } else if(ACTION_ORDER_DELETE.equals(action)){
                final ResultReceiver callback = intent.getParcelableExtra(EXTRA_CALLBACK);
                final int orderId = intent.getIntExtra(EXTRA_ORDER_ID, -1);
                handleActionOrderDelete(orderId, callback);
            } else if(ACTION_ORDER_UPDATE.equals(action)){
                final ResultReceiver callback = intent.getParcelableExtra(EXTRA_CALLBACK);
                final int orderId = intent.getIntExtra(EXTRA_ORDER_ID, -1);
                handleActionOrderUpdate(orderId, callback);
            }
        }
    }

    private void handleActionOrderUpdate(int orderId, final ResultReceiver callback) {
        Cursor cur = getApplicationContext().getContentResolver().query(ContentUris.withAppendedId(BleeprConstants.ORDERS_CONTENT_URI, orderId), null, null, null, null);
        cur.moveToFirst();
        String orderStatus = cur.getString(cur.getColumnIndex(BleeprConstants.ORDERS_STATUS));

        JSONObject packed = null;
        try {
            packed = new JSONObject().put("status", orderStatus);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest update = new JsonObjectRequest(Request.Method.PUT, String.format(ORDER_UPDATE_API_URL, orderId), packed,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        //callback.send(1, null);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Should handle this better but API is dodgy right now
                        // TODO: Keep an eye on this
                        //callback.send(1, null);
                        error.printStackTrace();
                    }
                }
        );

        RequestQueueBox.getInstance(getApplicationContext()).addToRequestQueue(update);
    }

    private void handleActionOrderDelete(int orderId, ResultReceiver callback) {

    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionRefresh(ResultReceiver callback) {
        JsonArrayRequest tables = new JsonArrayRequest(Request.Method.GET, TABLES_API_URL, null,
                new TableResponseListener(getApplicationContext()),
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        RequestQueueBox.getInstance(getApplicationContext()).addToRequestQueue(tables);
    }
}
