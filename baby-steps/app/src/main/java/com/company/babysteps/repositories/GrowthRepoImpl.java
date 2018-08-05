package com.company.babysteps.repositories;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.company.babysteps.data.DBQueryHandler;
import com.company.babysteps.data.GrowthDBEntry;
import com.company.babysteps.entities.Growth;
import com.company.babysteps.utils.ConstLoaderIds;
import com.company.babysteps.utils.CursorUtil;

import java.util.ArrayList;
import java.util.List;

public class GrowthRepoImpl implements
        GrowthRepo, DBQueryHandler.AsyncQueryListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String ORDER_BY_DATE = "ORDER_BY_DATE";
    private static final String ASC_ORDER = " ASC";
    private Context mContext;
    private final LoaderManager mLoaderManager;
    private GrowthRepoListener mListener;

    public GrowthRepoImpl(Context ctx, LoaderManager loaderManager, GrowthRepoListener listener) {
        this.mContext = ctx;
        this.mLoaderManager = loaderManager;
        this.mListener = listener;
    }

    @Override
    public void saveGrowth(Growth growth) {
        ContentValues cv = new ContentValues();
        cv.put(GrowthDBEntry.COLUMN_DATE, growth.getDate().getTime());
        cv.put(GrowthDBEntry.COLUMN_WEIGHT, growth.getWeight());
        cv.put(GrowthDBEntry.COLUMN_HEIGHT, growth.getHeight());
        cv.put(GrowthDBEntry.COLUMN_HEAD, growth.getHead());
        AsyncQueryHandler handler = new DBQueryHandler(this.mContext.getContentResolver(), this);
        handler.startInsert(0, growth, GrowthDBEntry.CONTENT_URI, cv);
    }

    @Override
    public void updateGrowth(Growth growth) {
        String whereClause = "_ID = " + growth.getId();
        ContentValues cv = new ContentValues();
        cv.put(GrowthDBEntry.COLUMN_DATE, growth.getDate().getTime());
        cv.put(GrowthDBEntry.COLUMN_WEIGHT, growth.getWeight());
        cv.put(GrowthDBEntry.COLUMN_HEIGHT, growth.getHeight());
        cv.put(GrowthDBEntry.COLUMN_HEAD, growth.getHead());
        AsyncQueryHandler handler = new DBQueryHandler(this.mContext.getContentResolver(), this);
        handler.startUpdate(0, growth, GrowthDBEntry.CONTENT_URI, cv, null, new String[]{whereClause});
    }

    @Override
    public void deleteGrowth(Growth growth) {
        AsyncQueryHandler handler = new DBQueryHandler(this.mContext.getContentResolver(), this);
        String whereClause = "_ID = " + growth.getId();
        handler.startDelete(0, growth, GrowthDBEntry.CONTENT_URI,
                null, new String[]{whereClause});
    }

    @Override
    public void getGrowthList() {
        String orderByAsc = GrowthDBEntry.COLUMN_DATE + ASC_ORDER;
        Bundle args = new Bundle();
        args.putString(ORDER_BY_DATE, orderByAsc);
        this.mLoaderManager.initLoader(ConstLoaderIds.LOADER_ID_GET_GROWTH, args, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String orderSelection = args.getString(ORDER_BY_DATE);
        return new CursorLoader(mContext, GrowthDBEntry.CONTENT_URI,
                null, null, null, orderSelection);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        List<Growth> growths = new ArrayList<>();
        if(data != null) {
            while(data.moveToNext()) {
                growths.add(CursorUtil.getOneRowGrothFromCursor(data));
            }
        }
        mListener.onGetGrowthsSuccess(growths);
        mLoaderManager.destroyLoader(loader.getId());
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    public void onQueryCompleted(Object object) {
        if(object != null) {
            this.mListener.onSingleCompleted();
        }
    }

    @Override
    public void onDeleteCompleted(Object object) {
        if(object != null) {
            this.mListener.onSingleCompleted();
        }
    }
}
