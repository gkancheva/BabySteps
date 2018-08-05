package com.company.babysteps.data;

import android.net.Uri;

public class DBContract {
    public static final String AUTHORITY = "com.company.babysteps";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FEEDINGS = "feedings";
    public static final String PATH_GROWTH = "growth";
    public static final String PATH_POSTS = "posts";
}