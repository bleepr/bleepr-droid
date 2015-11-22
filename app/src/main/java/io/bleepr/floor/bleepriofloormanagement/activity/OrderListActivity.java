package io.bleepr.floor.bleepriofloormanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import io.bleepr.floor.bleepriofloormanagement.ChangeCallbacks;
import io.bleepr.floor.bleepriofloormanagement.fragment.OrderDetailFragment;
import io.bleepr.floor.bleepriofloormanagement.fragment.OrderListFragment;
import io.bleepr.floor.bleepriofloormanagement.R;
import io.bleepr.floor.bleepriofloormanagement.service.BleeprBackendQueryService;


/**
 * An activity representing a list of Orders. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link OrderDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link OrderListFragment} and the item details
 * (if present) is a {@link OrderDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link OrderListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class OrderListActivity extends AppCompatActivity
        implements OrderListFragment.Callbacks {

    public static final String EXTRA_TABLE_ID = "io.bleepr.bleepriofloormanagement.TABLE_ID";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private int tableID = -1;

    private ChangeCallbacks callbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_app_bar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        Intent sender = getIntent();
        tableID = sender.getIntExtra(EXTRA_TABLE_ID, -1);

        OrderListFragment frag = ((OrderListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.order_list));

        callbacks = (ChangeCallbacks)frag;
        callbacks.updateTableID(tableID);

        if (findViewById(R.id.order_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            frag.setActivateOnItemClick(true);
        }

        // TODO: If exposing deep links into your app, handle intents here.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.refresh_menu, menu);
        return true;
    }

    /**
     * Callback method from {@link OrderListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(int id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putInt(OrderDetailFragment.ARG_ITEM_ID, id);
            OrderDetailFragment fragment = new OrderDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.order_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, OrderDetailActivity.class);
            detailIntent.putExtra(OrderDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }

    @Override
    public int getTableID() {
        return tableID;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()) {
            case R.id.action_refresh:
                // Kick off refresh
                BleeprBackendQueryService.startRefresh(getApplicationContext(), null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
