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

import io.bleepr.floor.bleepriofloormanagement.R;
import io.bleepr.floor.bleepriofloormanagement.activity.OccupanciesListActivity;
import io.bleepr.floor.bleepriofloormanagement.activity.OrderListActivity;
import io.bleepr.floor.bleepriofloormanagement.activity.TableDetailActivity;
import io.bleepr.floor.bleepriofloormanagement.activity.TableListActivity;
import io.bleepr.floor.bleepriofloormanagement.dummy.DummyContent;
import io.bleepr.floor.bleepriofloormanagement.provider.BleeprConstants;

/**
 * A fragment representing a single Table detail screen.
 * This fragment is either contained in a {@link TableListActivity}
 * in two-pane mode (on tablets) or a {@link TableDetailActivity}
 * on handsets.
 */
public class TableDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TableDetailFragment() {
    }

    private int capacity;
    private int tableID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            tableID = getArguments().getInt(ARG_ITEM_ID);

            Cursor cur = getContext().getApplicationContext().getContentResolver().query(ContentUris.withAppendedId(BleeprConstants.TABLES_CONTENT_URI, tableID), null, null, null, null);
            cur.moveToFirst();
            String name = cur.getString(cur.getColumnIndex(BleeprConstants.TABLES_NAME));
            capacity = cur.getInt(cur.getColumnIndex(BleeprConstants.TABLES_CAPACITY));


            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(name);
            }

            cur.close();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_table_detail, container, false);

        TextView tvCapacity = (TextView)rootView.findViewById(R.id.capacityCount);
        tvCapacity.setText(Integer.toString(capacity));

        Button btViewOcc = (Button)rootView.findViewById(R.id.button_occupancies);
        btViewOcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent occIntent = new Intent(getActivity(), OccupanciesListActivity.class);
                occIntent.putExtra(OccupanciesListActivity.EXTRA_TABLE_ID, tableID);
                startActivity(occIntent);
            }
        });

        Button btViewOrd = (Button)rootView.findViewById(R.id.button_orders);
        btViewOrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ordIntent = new Intent(getActivity(), OrderListActivity.class);
                ordIntent.putExtra(OrderListActivity.EXTRA_TABLE_ID, tableID);
                startActivity(ordIntent);
            }
        });

        return rootView;
    }

    public void viewOccupancies(View view){
        Toast.makeText(getActivity().getApplicationContext(), "View occupancies", Toast.LENGTH_LONG).show();
    }
}
