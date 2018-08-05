package com.company.babysteps.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class FeedingDBEntry implements BaseColumns {

    public static final Uri CONTENT_URI =
            DBContract.BASE_CONTENT_URI
            .buildUpon().appendPath(DBContract.PATH_FEEDINGS)
            .build();

    private FeedingDBEntry() {}

    public static final String TABLE_NAME = "feedings";
    public static final String COLUMN_FEEDING_TYPE = "feeding_type";
    public static final String COLUMN_DETAILS = "details";
    public static final String COLUMN_START_DATE_TIME = "start_date_time";
    public static final String COLUMN_END_DATE_TIME = "end_date_time";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_MILK_TYPE = "milk_type";

}