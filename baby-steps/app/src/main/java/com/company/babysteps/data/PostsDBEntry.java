package com.company.babysteps.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class PostsDBEntry implements BaseColumns {
    public static final Uri CONTENT_URI =
            DBContract.BASE_CONTENT_URI
                    .buildUpon().appendPath(DBContract.PATH_POSTS)
                    .build();

    private PostsDBEntry() {}

    public static final String TABLE_NAME = "posts";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_BODY = "body";
}