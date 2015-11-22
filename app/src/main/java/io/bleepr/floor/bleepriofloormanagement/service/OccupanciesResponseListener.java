package io.bleepr.floor.bleepriofloormanagement.service;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import io.bleepr.floor.bleepriofloormanagement.network.RequestQueueBox;
import io.bleepr.floor.bleepriofloormanagement.provider.BleeprConstants;

/**
 * Created by Matthew on 22/11/2015.
 */
public class OccupanciesResponseListener implements Response.Listener<JSONArray> {
    private Context context;

    public OccupanciesResponseListener(Context context) {
        this.context = context;
    }

    @Override
    public void onResponse(JSONArray response) {
        for(int i = 0; i < response.length(); i++){
            JSONObject occupancy = response.optJSONObject(i);

            if(occupancy != null){
                int remoteID = occupancy.optInt("id", -1);

                if(remoteID != -1){
                    int customerID = occupancy.optInt("customer_id", -1);
                    boolean prebooked = occupancy.optBoolean("prebooked");
                    String start = occupancy.optString("start");
                    String end = occupancy.optString("end");
                    boolean occupied = occupancy.optBoolean("occupied");
                    int tableID = occupancy.optInt("table_id");

                    if(customerID != -1){
                        JsonObjectRequest customer =  new JsonObjectRequest(Request.Method.GET, String.format(BleeprBackendQueryService.CUSTOMER_API_URL, customerID), null,
                                new CustomerResponseListener(context, prebooked, tableID, remoteID, occupied, start, end),
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                });
                        RequestQueueBox.getInstance(context).addToRequestQueue(customer);
                    } else {
                        ContentValues values = new ContentValues();
                        values.put(BleeprConstants.OCCUPANCIES_PREBOOKED, prebooked);
                        values.put(BleeprConstants.OCCUPANCIES_START, start);
                        values.put(BleeprConstants.OCCUPANCIES_END, end);
                        values.put(BleeprConstants.OCCUPANCIES_TABLE_ID, tableID);
                        values.put(BleeprConstants.OCCUPANCIES_REMOTE_ID, remoteID);
                        values.put(BleeprConstants.OCCUPANCIES_OCCUPIED, occupied);

                        Cursor cursor = context.getContentResolver().query(ContentUris.withAppendedId(BleeprConstants.OCCUPANCIES_CONTENT_URI, remoteID),
                                null, null, null, null);

                        if(cursor.moveToFirst()) {
                            context.getContentResolver().update(ContentUris.withAppendedId(BleeprConstants.OCCUPANCIES_CONTENT_URI, remoteID), values, null, null);
                        } else {
                            context.getContentResolver().insert(BleeprConstants.OCCUPANCIES_CONTENT_URI, values);
                        }

                        cursor.close();
                    }
                }
            }
        }
    }
}
