package com.junqua.sqlite;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class CarsProvider extends ContentProvider {

    public static final String LOG_TAG = "MY TAG";

    private MyDBHelper mDbHelper;

    private static final int CARS = 100;
    private static final int CAR_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(MyContract.CONTENT_AUTHORITY_CAR, MyContract.PATH_CARS, CARS);
        sUriMatcher.addURI(MyContract.CONTENT_AUTHORITY_CAR, MyContract.PATH_CARS + "/#", CAR_ID);
    }

    @Override
    public boolean onCreate(){
        mDbHelper = new MyDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor c;
        int match = sUriMatcher.match(uri);

        switch (match){
            case CARS :
                c = db.query(MyContract.CarsVariables.CARS, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CAR_ID :
                selection = MyContract.CarsVariables.CAR_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                c = db.query(MyContract.CarsVariables.CARS, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default :
                throw new IllegalArgumentException("Cannot query unknown URI : " + uri);
        }

        return c;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues){
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CARS:
                return insertCar(uri, contentValues);
            default :
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertCar(Uri uri, ContentValues contentValues){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long newId = db.insert(MyContract.CarsVariables.CARS, null, contentValues);

        if (newId == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        return ContentUris.withAppendedId(uri, newId);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CARS:
                return insertCar(contentValues, selection, selectionArgs);
            case CAR_ID:

                selection = MyContract.CarsVariables.CAR_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return insertCar(contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int insertCar(ContentValues contentValues, String selection, String[] selectionArgs){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int nbAffected = db.update(MyContract.CarsVariables.CARS, contentValues, selection, selectionArgs);
        return nbAffected;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CARS:
                return deleteCar(selection, selectionArgs);
            case CAR_ID:
                selection = MyContract.CarsVariables.CAR_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return deleteCar(selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Delete is not supported for " + uri);
        }
    }

    public int deleteCar(String selection, String[] selectionArgs){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int nbAffected = db.delete(MyContract.CarsVariables.CARS, selection, selectionArgs);
        return nbAffected;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CARS:
                return MyContract.CarsVariables.CONTENT_CARS_TYPE;
            case CAR_ID:
                return MyContract.CarsVariables.CONTENT_CAR_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
