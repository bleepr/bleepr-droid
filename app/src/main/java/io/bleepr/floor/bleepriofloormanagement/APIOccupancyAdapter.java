package io.bleepr.floor.bleepriofloormanagement;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Matthew on 16/11/2015.
 */
public class APIOccupancyAdapter extends CursorAdapter {
    public APIOccupancyAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.layout_occupancy, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvName = (TextView) view.findViewById(R.id.bookingName);
        TextView tvStatus = (TextView) view.findViewById(R.id.bookingStatus);
        TextView tvStart = (TextView) view.findViewById(R.id.bookingStart);
        TextView tvEnd = (TextView) view.findViewById(R.id.bookingEnd);

        // TODO: Populate name, booking status, format dates
    }
}
