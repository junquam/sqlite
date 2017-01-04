package com.junqua.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper  extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MyDB.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";

    private static final String SQL_CREATE_CARS =
            "create table " + MyContract.CarsVariables.CARS
                    + "(" + MyContract.CarsVariables.CAR_ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT, "
                    + MyContract.CarsVariables.CAR_NAME + TEXT_TYPE + " not null"
                    +");";

    private static final String SQL_DELETE_CARS =
            "DROP TABLE IF EXISTS " + MyContract.CarsVariables.CARS;

    public MyDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_CARS);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(SQL_DELETE_CARS);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }
}
