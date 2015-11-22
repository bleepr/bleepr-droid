package io.bleepr.floor.bleepriofloormanagement.service;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import io.bleepr.floor.bleepriofloormanagement.network.RequestQueueBox;
import io.bleepr.floor.bleepriofloormanagement.provider.BleeprConstants;

/**
 * Created by Matthew on 22/11/2015.
 */
public class TableResponseListener implements Response.Listener<JSONArray> {
    private Context context;

    public TableResponseListener(Context context) {
        this.context = context;
    }

    @Override
    public void onResponse(JSONArray response) {
        for (int i = 0; i < response.length(); i++) {
            JSONObject table = response.optJSONObject(i);
            if (table != null) {
                int remoteID = table.optInt("id", -1);
                if (remoteID != -1) {
                    JsonArrayRequest occupancies = new JsonArrayRequest(Request.Method.GET, String.format(BleeprBackendQueryService.OCCUPANCIES_API_URL, remoteID), null,
                            new OccupanciesResponseListener(context),
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }
                    );
                    RequestQueueBox.getInstance(context).addToRequestQueue(occupancies);

                    JsonArrayRequest bookings = new JsonArrayRequest(Request.Method.GET, String.format(BleeprBackendQueryService.OCCUPANCIES_FUTURE_API_URL, remoteID), null,
                            new OccupanciesResponseListener(context),
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }
                    );
                    RequestQueueBox.getInstance(context).addToRequestQueue(bookings);

                    JsonArrayRequest orders = new JsonArrayRequest(Request.Method.GET, String.format(BleeprBackendQueryService.ORDERS_API_URL, remoteID), null,
                            new OrdersResponseListener(context),
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }
                    );
                    RequestQueueBox.getInstance(context).addToRequestQueue(orders);

                    String name = table.optString("name");
                    int xpos = table.optInt("position_x");
                    int ypos = table.optInt("position_y");
                    int capacity = table.optInt("capacity");

                    ContentValues values = new ContentValues();
                    values.put(BleeprConstants.TABLES_REMOTE_ID, remoteID);
                    values.put(BleeprConstants.TABLES_XPOS, xpos);
                    values.put(BleeprConstants.TABLES_YPOS, ypos);
                    values.put(BleeprConstants.TABLES_NAME, name);
                    values.put(BleeprConstants.TABLES_CAPACITY, capacity);

                    Cursor cursor = context.getContentResolver().query(ContentUris.withAppendedId(BleeprConstants.TABLES_CONTENT_URI, remoteID),
                            null, null, null, null);

                    if(cursor.moveToFirst()) {
                        context.getContentResolver().update(ContentUris.withAppendedId(BleeprConstants.TABLES_CONTENT_URI, remoteID), values, null, null);
                    } else {
                        context.getContentResolver().insert(BleeprConstants.TABLES_CONTENT_URI, values);
                    }

                    cursor.close();
                }
            }
        }
    }
}
