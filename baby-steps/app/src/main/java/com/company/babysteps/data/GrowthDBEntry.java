package com.company.babysteps.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class GrowthDBEntry implements BaseColumns {

    public static final Uri CONTENT_URI =
            DBContract.BASE_CONTENT_URI
            .buildUpon().appendPath(DBContract.PATH_GROWTH)
            .build();

    private GrowthDBEntry() {}

    public static final String TABLE_NAME = "growth";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_HEIGHT = "height";
    public static final String COLUMN_HEAD = "head";

}