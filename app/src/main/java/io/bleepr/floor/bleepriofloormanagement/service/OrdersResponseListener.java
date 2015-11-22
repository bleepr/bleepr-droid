package io.bleepr.floor.bleepriofloormanagement.service;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import io.bleepr.floor.bleepriofloormanagement.provider.BleeprConstants;

/**
 * Created by Matthew on 22/11/2015.
 */
public class OrdersResponseListener implements Response.Listener<JSONArray> {
    private Context context;

    public OrdersResponseListener(Context context) {
        this.context = context;
    }

    @Override
    public void onResponse(JSONArray response) {
        for(int i = 0; i < response.length(); i++){
            JSONObject order = response.optJSONObject(i);

            if(order != null){
                int remoteID = order.optInt("id", -1);

                if(remoteID != -1){
                    String cardID = order.optString("card_id");
                    String status = order.optString("status");
                    int tableID = order.optInt("table_id");
                    String placed = order.optString("created_at");

                    ContentValues values = new ContentValues();
                    values.put(BleeprConstants.ORDERS_REMOTE_ID, remoteID);
                    values.put(BleeprConstants.ORDERS_TABLE_ID, tableID);
                    values.put(BleeprConstants.ORDERS_STATUS, status);
                    values.put(BleeprConstants.ORDERS_CARD_ID, cardID);
                    values.put(BleeprConstants.ORDERS_PLACED_AT, placed);

                    Cursor cursor = context.getContentResolver().query(ContentUris.withAppendedId(BleeprConstants.ORDERS_CONTENT_URI, remoteID),
                            null, null, null, null);

                    if(cursor.moveToFirst()) {
                        context.getContentResolver().update(ContentUris.withAppendedId(BleeprConstants.ORDERS_CONTENT_URI, remoteID), values, null, null);
                    } else {
                        context.getContentResolver().insert(BleeprConstants.ORDERS_CONTENT_URI, values);
                    }

                    cursor.close();
                }
            }
        }
    }
}
