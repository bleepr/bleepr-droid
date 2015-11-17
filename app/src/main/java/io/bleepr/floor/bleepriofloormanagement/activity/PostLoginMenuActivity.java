package io.bleepr.floor.bleepriofloormanagement.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import io.bleepr.floor.bleepriofloormanagement.R;

public class PostLoginMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_login_menu);
    }

    public void goToOrders(View view) {
        Intent ordersIntent = new Intent(this, OrderListActivity.class);
        startActivity(ordersIntent);
    }

    public void goToTables(View view) {
        Intent tablesIntent = new Intent(this, TableListActivity.class);
        startActivity(tablesIntent);
    }
}
