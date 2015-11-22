package io.bleepr.floor.bleepriofloormanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import io.bleepr.floor.bleepriofloormanagement.ChangeCallbacks;
import io.bleepr.floor.bleepriofloormanagement.R;
import io.bleepr.floor.bleepriofloormanagement.fragment.OccupanciesListFragment;
import io.bleepr.floor.bleepriofloormanagement.fragment.OrderDetailFragment;
import io.bleepr.floor.bleepriofloormanagement.fragment.OrderListFragment;
import io.bleepr.floor.bleepriofloormanagement.service.BleeprBackendQueryService;

public class OccupanciesListActivity extends AppCompatActivity implements OccupanciesListFragment.Callbacks {

    public static final String EXTRA_TABLE_ID = "io.bleepr.bleepriofloormanagement.TABLE_ID";

    public ChangeCallbacks callbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_occupancies_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Occupancies");

        Intent sender = getIntent();
        int tableID = sender.getIntExtra(EXTRA_TABLE_ID, -1);

        OccupanciesListFragment frag = ((OccupanciesListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.occupancies_list));

        callbacks = (ChangeCallbacks)frag;
        callbacks.updateTableID(tableID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.refresh_menu, menu);
        return true;
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

    /**
     * Callback method from {@link OrderListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {
    }
}
