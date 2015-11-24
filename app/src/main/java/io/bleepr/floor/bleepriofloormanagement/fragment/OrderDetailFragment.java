package io.bleepr.floor.bleepriofloormanagement.fragment;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.Period;

import io.bleepr.floor.bleepriofloormanagement.R;
import io.bleepr.floor.bleepriofloormanagement.activity.OrderDetailActivity;
import io.bleepr.floor.bleepriofloormanagement.activity.OrderListActivity;
import io.bleepr.floor.bleepriofloormanagement.activity.TableDetailActivity;
import io.bleepr.floor.bleepriofloormanagement.dummy.DummyContent;
import io.bleepr.floor.bleepriofloormanagement.provider.BleeprConstants;

/**
 * A fragment representing a single Order detail screen.
 * This fragment is either contained in a {@link OrderListActivity}
 * in two-pane mode (on tablets) or a {@link OrderDetailActivity}
 * on handsets.
 */
public class OrderDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    private int tableID;
    private DateTime placed;
    private String tableName;
    private String orderStatus;
    private int id;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OrderDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        reloadData(false);

    }

    public void reloadData(boolean redraw){
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            id = getArguments().getInt(ARG_ITEM_ID);
            Cursor cur = getContext().getApplicationContext().getContentResolver().query(ContentUris.withAppendedId(BleeprConstants.ORDERS_CONTENT_URI, id), null, null, null, null);
            cur.moveToFirst();
            placed = new DateTime(cur.getString(cur.getColumnIndex(BleeprConstants.ORDERS_PLACED_AT)));
            tableID = cur.getInt(cur.getColumnIndex(BleeprConstants.ORDERS_TABLE_ID));
            orderStatus = cur.getString(cur.getColumnIndex(BleeprConstants.ORDERS_STATUS));

            switch (orderStatus) {
                case "open":
                    orderStatus = "Open";
                    break;
                case "progress":
                    orderStatus = "In Progress";
                    break;
                case "complete":
                    orderStatus = "Ready";
                    break;
                case "served":
                    orderStatus = "Served";
                    break;
            }

            cur.close();
            cur = getContext().getApplicationContext().getContentResolver().query(ContentUris.withAppendedId(BleeprConstants.TABLES_CONTENT_URI, tableID), null, null, null, null);
            cur.moveToFirst();
            tableName = cur.getString(cur.getColumnIndex(BleeprConstants.TABLES_NAME));
            cur.close();

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(String.format("Order %d", id));
            }

            if(redraw){
                View rootView = getView();
                TextView tvFor = (TextView)rootView.findViewById(R.id.order_table);
                tvFor.setText(String.format("Table %s", tableName));

                TextView tvPlaced = (TextView)rootView.findViewById(R.id.order_time);
                DateTime now = DateTime.now();
                Period tillNow = new Period(placed, now);

                String str;
                if(tillNow.getDays() > 0){
                    str = String.format("%d hours ago", tillNow.getDays());
                } else if (tillNow.getHours() > 0) {
                    str = String.format("%d hours ago", tillNow.getHours());
                } else {
                    str = String.format("%d minutes ago", tillNow.getMinutes());
                }
                tvPlaced.setText(str);


                TextView tvStatus = (TextView)rootView.findViewById(R.id.statusLabel);
                tvStatus.setText(orderStatus);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_order_detail, container, false);

        TextView tvFor = (TextView)rootView.findViewById(R.id.order_table);
        tvFor.setText(String.format("Table %s", tableName));

        TextView tvPlaced = (TextView)rootView.findViewById(R.id.order_time);
        DateTime now = DateTime.now();
        Period tillNow = new Period(placed, now);

        String str;
        if(tillNow.getDays() > 0){
            str = String.format("%d hours ago", tillNow.getDays());
        } else if (tillNow.getHours() > 0) {
            str = String.format("%d hours ago", tillNow.getHours());
        } else {
            str = String.format("%d minutes ago", tillNow.getMinutes());
        }
        tvPlaced.setText(str);


        Button tableView = (Button)rootView.findViewById(R.id.table_view);
        tableView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tableDetail = new Intent(getActivity(), TableDetailActivity.class);
                tableDetail.putExtra(TableDetailFragment.ARG_ITEM_ID, tableID);
                startActivity(tableDetail);
            }
        });

        TextView tvStatus = (TextView)rootView.findViewById(R.id.statusLabel);
        tvStatus.setText(orderStatus);

        return rootView;
    }

    public void viewAssociatedTable(View view){
            Toast.makeText(getActivity().getApplicationContext(), "View associated table", Toast.LENGTH_LONG).show();
    }
}
