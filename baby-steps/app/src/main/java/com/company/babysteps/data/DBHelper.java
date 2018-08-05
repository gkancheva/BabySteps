package com.company.babysteps.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "baby_steps_db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TABLE_FEEDINGS_STATEMENT =
                "CREATE TABLE " + FeedingDBEntry.TABLE_NAME + " (" +
                        FeedingDBEntry._ID + " INTEGER PRIMARY KEY, " +
                        FeedingDBEntry.COLUMN_FEEDING_TYPE + " TEXT NOT NULL, " +
                        FeedingDBEntry.COLUMN_DETAILS + " TEXT, " +
                        FeedingDBEntry.COLUMN_START_DATE_TIME + " TIMESTAMP NOT NULL, " +
                        FeedingDBEntry.COLUMN_END_DATE_TIME + " TIMESTAMP, " +
                        FeedingDBEntry.COLUMN_QUANTITY + " FLOAT, " +
                        FeedingDBEntry.COLUMN_MILK_TYPE + " TEXT);";

        final String SQL_CREATE_TABLE_GROWTH_STATEMENT =
                "CREATE TABLE " + GrowthDBEntry.TABLE_NAME + " (" +
                        GrowthDBEntry._ID + " INTEGER PRIMARY KEY, " +
                        GrowthDBEntry.COLUMN_DATE + " TIMESTAMP NOT NULL, " +
                        GrowthDBEntry.COLUMN_WEIGHT + " FLOAT, " +
                        GrowthDBEntry.COLUMN_HEIGHT + " FLOAT, " +
                        GrowthDBEntry.COLUMN_HEAD + " FLOAT);";
        final String SQL_CREATE_TABLE_POSTS_STATEMENT =
                "CREATE TABLE " + PostsDBEntry.TABLE_NAME + " (" +
                        PostsDBEntry._ID + " INTEGER PRIMARY KEY, " +
                        PostsDBEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                        PostsDBEntry.COLUMN_BODY + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_TABLE_FEEDINGS_STATEMENT);
        db.execSQL(SQL_CREATE_TABLE_GROWTH_STATEMENT);
        db.execSQL(SQL_CREATE_TABLE_POSTS_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion < newVersion) {
            // alter table accordingly without loosing data
            db.execSQL("ALTER TABLE " + FeedingDBEntry.TABLE_NAME);
            db.execSQL("ALTER TABLE " + GrowthDBEntry.TABLE_NAME);
            db.execSQL("ALTER TABLE " + PostsDBEntry.TABLE_NAME);
        }
    }
}