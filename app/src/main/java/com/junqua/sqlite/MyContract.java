package com.junqua.sqlite;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class MyContract {

    private MyContract(){}

    public static final String CONTENT_AUTHORITY_CAR = "com.junqua.sqlite.cars";
    public static final Uri BASE_CONTENT_URI_CAR = Uri.parse("content://" + CONTENT_AUTHORITY_CAR);
    public static final String PATH_CARS = "cars";

    public static class CarsVariables implements BaseColumns {

        public static final Uri CARS_URI = Uri.withAppendedPath(BASE_CONTENT_URI_CAR, PATH_CARS);

        public static final String CARS = "Cars";
        public static final String CAR_ID = "_id";
        public static final String CAR_NAME = "_name";

        public static final String CONTENT_CARS_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY_CAR + "/" + PATH_CARS;

        public static final String CONTENT_CAR_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY_CAR + "/" + PATH_CARS;
    }
}
