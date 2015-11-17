package io.bleepr.floor.bleepriofloormanagement.provider;

import android.net.Uri;

/**
 * Created by Matthew on 16/11/2015.
 */
public class BleeprConstants {
    public static final String TABLES_TABLE = "tables";
    public static final String ORDERS_TABLE = "orders";
    public static final String OCCUPANCIES_TABLE = "occupancies";
    public static final String AUTHORITY = "io.bleepr.floor.bleepriofloormanagement.api";

    public static final Uri TABLES_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLES_TABLE);
    public static final Uri ORDERS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + ORDERS_TABLE);
    public static final Uri OCCUPANCIES_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + OCCUPANCIES_TABLE);

    public static final String TABLES_NAME = "name",
            TABLES_XPOS = "xpos",
            TABLES_YPOS = "ypos",
            TABLES_MAP_ID = "map_id",
            TABLES_REMOTE_ID = "remote_id";

    public static final String OCCUPANCIES_PREBOOKED = "prebooked",
            OCCUPANCIES_START = "start",
            OCCUPANCIES_END = "end",
            OCCUPANCIES_OCCUPIED = "occupied",
            OCCUPANCIES_CUSTOMER_FIRST_NAME = "customer_first",
            OCCUPANCIES_CUSTOMER_LAST_NAME = "customer_last",
            OCCUPANCIES_TABLE_ID = "table_id",
            OCCUPANCIES_REMOTE_ID = "remote_id";

    public static final String ORDERS_STATUS = "status",
            ORDERS_CUSTOMER_ID = "customer_id",
            ORDERS_CARD_ID = "card_id",
            ORDERS_TABLE_ID = "table_id",
            ORDERS_REMOTE_ID = "remote_id";
}
