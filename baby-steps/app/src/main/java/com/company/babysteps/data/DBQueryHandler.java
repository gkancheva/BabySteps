package com.company.babysteps.data;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

public class DBQueryHandler extends AsyncQueryHandler {

    private final AsyncQueryListener mListener;

    public interface AsyncQueryListener {
        void onQueryCompleted(Object object);
        void onDeleteCompleted(Object object);
    }

    public DBQueryHandler(ContentResolver cr, AsyncQueryListener listener) {
        super(cr);
        this.mListener = listener;
    }

    @Override
    protected void onInsertComplete(int token, Object object, Uri uri) {
        this.mListener.onQueryCompleted(object);
    }

    @Override
    protected void onDeleteComplete(int token, Object object, int result) {
        this.mListener.onDeleteCompleted(object);
    }

    @Override
    protected void onQueryComplete(int token, Object object, Cursor cursor) {
        this.mListener.onQueryCompleted(cursor);
    }

    @Override
    protected void onUpdateComplete(int token, Object object, int result) {
        this.mListener.onQueryCompleted(object);
    }
}