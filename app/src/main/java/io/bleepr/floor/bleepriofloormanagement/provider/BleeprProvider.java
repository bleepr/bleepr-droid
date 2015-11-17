package io.bleepr.floor.bleepriofloormanagement.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by Matthew on 16/11/2015.
 */
public class BleeprProvider extends ContentProvider {
    public final static int TABLES = 1;
    private final static int TABLE_ID = 2;

    public final static int ORDERS = 3;
    private final static int ORDER_ID = 4;
    private final static int ORDER_BY_TABLE_ID = 5;

    public final static int OCCUPANCIES = 6;
    private final static int OCCUPANCY_ID = 7;
    private final static int OCCUPANCY_BY_TABLE_ID = 8;

    private final static String TABLES_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.io.bleepr.table",
            TABLES_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.io.bleepr.table";
    private final static String ORDERS_CONTENT_TYPE = "vnd.android.cursor,dir/vnd.io.bleepr.order",
            ORDERS_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.io.bleepr.order";
    private final static String OCCUPANCIES_CONTENT_TYPE = "vnd.android.cursor,dir/vnd.io.bleepr.occupancy",
            OCCUPANCIES_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.io.bleepr.occupancy";

    private UriMatcher matcher;
    private BleeprData data;

    @Override
    public boolean onCreate() {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(BleeprConstants.AUTHORITY, "tables", TABLES);
        matcher.addURI(BleeprConstants.AUTHORITY, "tables/#", TABLE_ID);
        matcher.addURI(BleeprConstants.AUTHORITY, "orders", ORDERS);
        matcher.addURI(BleeprConstants.AUTHORITY, "orders/#", ORDER_ID);
        matcher.addURI(BleeprConstants.AUTHORITY, "orders/by-table/#", ORDER_BY_TABLE_ID);
        matcher.addURI(BleeprConstants.AUTHORITY, "occupancies", OCCUPANCIES);
        matcher.addURI(BleeprConstants.AUTHORITY, "occupancies/#", OCCUPANCY_ID);
        matcher.addURI(BleeprConstants.AUTHORITY, "occupancies/by-table/#", OCCUPANCY_BY_TABLE_ID);
        data = new BleeprData(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        long id;
        String name;
        switch(matcher.match(uri)){
            case TABLE_ID:
                id = Long.parseLong(uri.getPathSegments().get(1));
                selection = appendSelectionOnId(selection, id, BleeprConstants.TABLES_REMOTE_ID);
            case TABLES:
                name = BleeprConstants.TABLES_TABLE;
                break;
            case ORDER_ID:
                id = Long.parseLong(uri.getPathSegments().get(1));
                selection = appendSelectionOnId(selection, id, BleeprConstants.ORDERS_REMOTE_ID);
            case ORDERS:
                name = BleeprConstants.ORDERS_TABLE;
                break;
            case OCCUPANCY_ID:
                id = Long.parseLong(uri.getPathSegments().get(1));
                selection = appendSelectionOnId(selection, id, BleeprConstants.OCCUPANCIES_REMOTE_ID);
            case OCCUPANCIES:
                name = BleeprConstants.OCCUPANCIES_TABLE;
                break;
            case ORDER_BY_TABLE_ID:
                id = Long.parseLong(uri.getPathSegments().get(2));
                selection = appendSelectionOnId(selection, id, BleeprConstants.ORDERS_TABLE_ID);
                name = BleeprConstants.ORDERS_TABLE;
                break;
            case OCCUPANCY_BY_TABLE_ID:
                id = Long.parseLong(uri.getPathSegments().get(2));
                selection = appendSelectionOnId(selection, id, BleeprConstants.OCCUPANCIES_TABLE_ID);
                name = BleeprConstants.OCCUPANCIES_TABLE;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = data.getReadableDatabase();
        Cursor cursor = db.query(name, projection, selection, selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (matcher.match(uri)) {
            case TABLES:
                return TABLES_CONTENT_TYPE;
            case TABLE_ID:
                return TABLES_CONTENT_ITEM_TYPE;
            case ORDERS:
                return ORDERS_CONTENT_TYPE;
            case ORDER_BY_TABLE_ID:
                return ORDERS_CONTENT_TYPE;
            case ORDER_ID:
                return ORDERS_CONTENT_ITEM_TYPE;
            case OCCUPANCIES:
                return OCCUPANCIES_CONTENT_TYPE;
            case OCCUPANCY_BY_TABLE_ID:
                return OCCUPANCIES_CONTENT_TYPE;
            case OCCUPANCY_ID:
                return OCCUPANCIES_CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = data.getWritableDatabase();

        // Validate the requested uri
        String name;
        Uri content;
        switch(matcher.match(uri)){
            case TABLES:
                name = BleeprConstants.TABLES_TABLE;
                content = BleeprConstants.TABLES_CONTENT_URI;
                break;
            case ORDERS:
                name = BleeprConstants.ORDERS_TABLE;
                content = BleeprConstants.ORDERS_CONTENT_URI;
                break;
            case OCCUPANCIES:
                name = BleeprConstants.OCCUPANCIES_TABLE;
                content = BleeprConstants.OCCUPANCIES_CONTENT_URI;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        // Insert into database
        long id = db.insertOrThrow(name, null, values);

        // Notify any watchers of the change
        Uri newUri = ContentUris.withAppendedId(content, id);


        getContext().getContentResolver().notifyChange(newUri, null);
        return newUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = data.getWritableDatabase();

        // Validate the requested uri
        long id;
        String name;
        switch(matcher.match(uri)){
            case TABLE_ID:
                id = Long.parseLong(uri.getPathSegments().get(1));
                selection = appendSelectionOnId(selection, id, BleeprConstants.TABLES_REMOTE_ID);
            case TABLES:
                name = BleeprConstants.TABLES_TABLE;
                break;
            case ORDER_ID:
                id = Long.parseLong(uri.getPathSegments().get(1));
                selection = appendSelectionOnId(selection, id, BleeprConstants.ORDERS_REMOTE_ID);
            case ORDERS:
                name = BleeprConstants.ORDERS_TABLE;
                break;
            case OCCUPANCY_ID:
                id = Long.parseLong(uri.getPathSegments().get(1));
                selection = appendSelectionOnId(selection, id, BleeprConstants.OCCUPANCIES_REMOTE_ID);
            case OCCUPANCIES:
                name = BleeprConstants.OCCUPANCIES_TABLE;
                break;
            case ORDER_BY_TABLE_ID:
                id = Long.parseLong(uri.getPathSegments().get(2));
                selection = appendSelectionOnId(selection, id, BleeprConstants.ORDERS_TABLE_ID);
                name = BleeprConstants.ORDERS_TABLE;
                break;
            case OCCUPANCY_BY_TABLE_ID:
                id = Long.parseLong(uri.getPathSegments().get(2));
                selection = appendSelectionOnId(selection, id, BleeprConstants.OCCUPANCIES_TABLE_ID);
                name = BleeprConstants.OCCUPANCIES_TABLE;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        int affected = db.delete(name, selection, selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);
        return affected;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = data.getWritableDatabase();

        // Validate the requested uri
        long id;
        String name;
        switch(matcher.match(uri)){
            case TABLE_ID:
                id = Long.parseLong(uri.getPathSegments().get(1));
                selection = appendSelectionOnId(selection, id, BleeprConstants.TABLES_REMOTE_ID);
            case TABLES:
                name = BleeprConstants.TABLES_TABLE;
                break;
            case ORDER_ID:
                id = Long.parseLong(uri.getPathSegments().get(1));
                selection = appendSelectionOnId(selection, id, BleeprConstants.ORDERS_REMOTE_ID);
            case ORDERS:
                name = BleeprConstants.ORDERS_TABLE;
                break;
            case OCCUPANCY_ID:
                id = Long.parseLong(uri.getPathSegments().get(1));
                selection = appendSelectionOnId(selection, id, BleeprConstants.OCCUPANCIES_REMOTE_ID);
            case OCCUPANCIES:
                name = BleeprConstants.OCCUPANCIES_TABLE;
                break;
            case ORDER_BY_TABLE_ID:
                id = Long.parseLong(uri.getPathSegments().get(2));
                selection = appendSelectionOnId(selection, id, BleeprConstants.ORDERS_TABLE_ID);
                name = BleeprConstants.ORDERS_TABLE;
                break;
            case OCCUPANCY_BY_TABLE_ID:
                id = Long.parseLong(uri.getPathSegments().get(2));
                selection = appendSelectionOnId(selection, id, BleeprConstants.OCCUPANCIES_TABLE_ID);
                name = BleeprConstants.OCCUPANCIES_TABLE;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        int affected = db.update(name, values, selection, selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);
        return affected;
    }

    private String appendSelectionOnId(String selection, long id, String column){
        return column + "=" + id + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
    }
}
