package io.bleepr.floor.bleepriofloormanagement.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Matthew on 16/11/2015.
 */
public class BleeprData extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "bleepr.db";
    private static final int DATABASE_VERSION = 3;

    BleeprData(Context ctx){
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + BleeprConstants.TABLES_TABLE + " ("
                    + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + BleeprConstants.TABLES_NAME + " TEXT, "
                    + BleeprConstants.TABLES_XPOS + " REAL, "
                    + BleeprConstants.TABLES_YPOS + " REAL, "
                    + BleeprConstants.TABLES_MAP_ID + " INTEGER, "
                    + BleeprConstants.TABLES_REMOTE_ID + " INTEGER);");

        db.execSQL("CREATE TABLE " + BleeprConstants.ORDERS_TABLE + " ("
                    + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + BleeprConstants.ORDERS_STATUS + " TEXT, "
                    + BleeprConstants.ORDERS_CARD_ID + " INTEGER, "
                    + BleeprConstants.ORDERS_CUSTOMER_ID + " INTEGER, "
                    + BleeprConstants.ORDERS_TABLE_ID + " INTEGER, "
                    + BleeprConstants.ORDERS_REMOTE_ID + " INTEGER);");

        db.execSQL("CREATE TABLE " + BleeprConstants.OCCUPANCIES_TABLE + " ("
                    + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + BleeprConstants.OCCUPANCIES_CUSTOMER_FIRST_NAME + " TEXT, "
                    + BleeprConstants.OCCUPANCIES_CUSTOMER_LAST_NAME + " TEXT, "
                    + BleeprConstants.OCCUPANCIES_PREBOOKED + " BOOLEAN, "
                    + BleeprConstants.OCCUPANCIES_OCCUPIED + " BOOLEAN, "
                    + BleeprConstants.OCCUPANCIES_START + " TEXT, "
                    + BleeprConstants.OCCUPANCIES_END + " TEXT, "
                    + BleeprConstants.OCCUPANCIES_TABLE_ID + " INTEGER, "
                    + BleeprConstants.OCCUPANCIES_REMOTE_ID + " INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BleeprConstants.OCCUPANCIES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + BleeprConstants.ORDERS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + BleeprConstants.TABLES_TABLE);
        onCreate(db);
    }
}
