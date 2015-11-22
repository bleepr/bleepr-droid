package io.bleepr.floor.bleepriofloormanagement.service;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.android.volley.Response;

import org.json.JSONObject;

import io.bleepr.floor.bleepriofloormanagement.provider.BleeprConstants;

/**
 * Created by Matthew on 22/11/2015.
 */
public class CustomerResponseListener implements Response.Listener<JSONObject> {
    private boolean prebooked;
    private int tableID;
    private int remoteID;
    private boolean occupied;
    private String start;
    private String end;
    private Context context;

    public CustomerResponseListener(Context ctx, boolean prebooked, int tableID, int remoteID, boolean occupied, String start, String end) {
        this.prebooked = prebooked;
        this.tableID = tableID;
        this.remoteID = remoteID;
        this.occupied = occupied;
        this.start = start;
        this.end = end;
        this.context = ctx;
    }

    @Override
    public void onResponse(JSONObject response) {
        String firstName = response.optString("first_name");
        String lastName = response.optString("last_name");

        ContentValues values = new ContentValues();
        values.put(BleeprConstants.OCCUPANCIES_CUSTOMER_FIRST_NAME, firstName);
        values.put(BleeprConstants.OCCUPANCIES_CUSTOMER_LAST_NAME, lastName);
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
