package com.company.babysteps.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class DBContentProvider extends ContentProvider {

    private static final int FEEDINGS = 100;
    private static final int GROWTH = 200;
    private static final int POSTS = 300;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DBHelper sDbHelper;

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(DBContract.AUTHORITY, DBContract.PATH_FEEDINGS, FEEDINGS);
        uriMatcher.addURI(DBContract.AUTHORITY, DBContract.PATH_GROWTH, GROWTH);
        uriMatcher.addURI(DBContract.AUTHORITY, DBContract.PATH_POSTS, POSTS);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        this.sDbHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = sDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor cursor;
        switch (match) {
            case FEEDINGS:
                cursor = db.query(FeedingDBEntry.TABLE_NAME,
                        projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case GROWTH:
                cursor = db.query(GrowthDBEntry.TABLE_NAME,
                        projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case POSTS:
                cursor = db.query(PostsDBEntry.TABLE_NAME,
                        projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case FEEDINGS:
                return DBContract.PATH_FEEDINGS;
            case GROWTH:
                return DBContract.PATH_GROWTH;
            case POSTS:
                return DBContract.PATH_POSTS;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = sDbHelper.getWritableDatabase();
        int numInserted = 0;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case POSTS:
                db.beginTransaction();
                try {
                    for (ContentValues cv : values) {
                        long id = db.insert(PostsDBEntry.TABLE_NAME, null, cv);
                        if(id <= 0) {
                            throw new SQLException("Failed to insert row into: " + uri);
                        }
                    }
                    db.setTransactionSuccessful();
                    getContext().getContentResolver().notifyChange(uri, null);
                    numInserted = values.length;
                } finally {
                    db.endTransaction();
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return numInserted;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = sDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        long id;
        switch (match) {
            case FEEDINGS:
                id = db.insert(FeedingDBEntry.TABLE_NAME, null, values);
                if(id > 0) {
                    returnUri = ContentUris.withAppendedId(FeedingDBEntry.CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to insert row into: " + uri);
                }
                break;
            case GROWTH:
                id = db.insert(GrowthDBEntry.TABLE_NAME, null, values);
                if(id > 0) {
                    returnUri = ContentUris.withAppendedId(GrowthDBEntry.CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to insert row into: " + uri);
                }
                break;
            case POSTS:
                id = db.insert(PostsDBEntry.TABLE_NAME, null, values);
                if(id > 0) {
                    returnUri = ContentUris.withAppendedId(PostsDBEntry.CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to insert row into: " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = sDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int deletedRows;
        switch (match) {
            case FEEDINGS:
                deletedRows = db.delete(FeedingDBEntry.TABLE_NAME, selectionArgs == null ? null : selectionArgs[0], null);
                break;
            case GROWTH:
                deletedRows = db.delete(GrowthDBEntry.TABLE_NAME, selectionArgs == null ? null : selectionArgs[0], null);
                break;
            case POSTS:
                deletedRows = db.delete(PostsDBEntry.TABLE_NAME, selectionArgs == null ? null : selectionArgs[0], null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return deletedRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = sDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int updatedRows;
        switch (match) {
            case FEEDINGS:
                updatedRows = db.update(FeedingDBEntry.TABLE_NAME, values, selectionArgs == null ? null : selectionArgs[0], null);
                break;
            case GROWTH:
                updatedRows = db.update(GrowthDBEntry.TABLE_NAME, values, selectionArgs == null ? null : selectionArgs[0], null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return updatedRows;
    }
}