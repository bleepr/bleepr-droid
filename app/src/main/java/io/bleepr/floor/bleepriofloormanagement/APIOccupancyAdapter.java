package io.bleepr.floor.bleepriofloormanagement;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.GridLayout;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import io.bleepr.floor.bleepriofloormanagement.provider.BleeprConstants;

/**
 * Created by Matthew on 16/11/2015.
 */
public class APIOccupancyAdapter extends CursorAdapter {
    private Context ctx;
    public APIOccupancyAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
        ctx = context;
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
        GridLayout layout = (GridLayout) view.findViewById(R.id.layoutBase);

        String firstName = cursor.getString(cursor.getColumnIndex(BleeprConstants.OCCUPANCIES_CUSTOMER_FIRST_NAME));
        String lastName = cursor.getString(cursor.getColumnIndex(BleeprConstants.OCCUPANCIES_CUSTOMER_LAST_NAME));
        String name = String.format("%s, %s", lastName, firstName);

        tvName.setText(name);

        boolean prebooked = cursor.getInt(cursor.getColumnIndex(BleeprConstants.OCCUPANCIES_PREBOOKED)) == 1;

        if(prebooked){
            layout.setBackgroundColor(Color.parseColor("#3F51B5"));
            tvName.setTextColor(Color.WHITE);
            tvStatus.setTextColor(Color.WHITE);
            tvStart.setTextColor(Color.WHITE);
            tvEnd.setTextColor(Color.WHITE);

            tvStatus.setText("Pre-booked");
        } else {
            tvStatus.setText("Unreserved");
        }

        DateTime start = new DateTime(cursor.getString(cursor.getColumnIndex(BleeprConstants.OCCUPANCIES_START)));
        DateTime end = new DateTime(cursor.getString(cursor.getColumnIndex(BleeprConstants.OCCUPANCIES_END)));

        DateTime now = DateTime.now();
        Period startDiff = new Period(now, start);
        Period endDiff = new Period(now, end);

        DateTimeFormatter sameDayFmt = DateTimeFormat.forPattern("h:ma");
        DateTimeFormatter diffDayFmt = DateTimeFormat.forPattern("d/M/yy h:ma");

        String str;
        if(startDiff.getDays() > 1) {
            str = diffDayFmt.print(start);

        } else {
            str = sameDayFmt.print(start);
        }

        String stStr = str.replace("a.m.", "AM").replace("p.m.","PM");
        tvStart.setText(stStr);

        if(endDiff.getDays() > 1) {
            str = diffDayFmt.print(end);
        } else {
            str = sameDayFmt.print(end);
        }

        stStr = str.replace("a.m.", "AM").replace("p.m.","PM");
        tvEnd.setText(stStr);
    }
}
