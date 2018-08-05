package com.company.babysteps.repositories;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.company.babysteps.data.FeedingDBEntry;
import com.company.babysteps.data.DBQueryHandler;
import com.company.babysteps.entities.Feeding;
import com.company.babysteps.utils.ConstLoaderIds;
import com.company.babysteps.utils.CursorUtil;

import java.util.ArrayList;
import java.util.List;

public class FeedingRepoImpl implements
        FeedingRepo,
        DBQueryHandler.AsyncQueryListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String ORDER_BY_DATE = "ORDER_BY_DATE";
    private static final String DESC_ORDER = " DESC";
    private Context mContext;
    private FeedingRepoListener mListener;
    private final LoaderManager mLoaderManager;

    public FeedingRepoImpl(Context ctx, FeedingRepoListener listener, LoaderManager loaderManager) {
        this.mContext = ctx;
        this.mListener = listener;
        this.mLoaderManager = loaderManager;
    }

    @Override
    public void getFeedings() {
        String orderByDateDesc = FeedingDBEntry.COLUMN_START_DATE_TIME  + DESC_ORDER;
        Bundle args = new Bundle();
        args.putString(ORDER_BY_DATE, orderByDateDesc);
        this.mLoaderManager.initLoader(ConstLoaderIds.LOADER_ID_GET_FEEDINGS, args, this);
    }

    @Override
    public void saveFeeding(Feeding f) {
        ContentValues cv = new ContentValues();
        cv.put(FeedingDBEntry.COLUMN_DETAILS, f.getDetails());
        cv.put(FeedingDBEntry.COLUMN_START_DATE_TIME, f.getStartDateTime().getTime());
        cv.put(FeedingDBEntry.COLUMN_END_DATE_TIME, f.getEndDateTime() != null ? f.getEndDateTime().getTime() : null);
        cv.put(FeedingDBEntry.COLUMN_FEEDING_TYPE, f.getType().name());
        cv.put(FeedingDBEntry.COLUMN_QUANTITY, f.getQuantity());
        cv.put(FeedingDBEntry.COLUMN_MILK_TYPE, f.getMilkType() != null ? f.getMilkType().name() : null);
        AsyncQueryHandler handler = new DBQueryHandler(this.mContext.getContentResolver(), this);
        try {
            handler.startInsert(0, f, FeedingDBEntry.CONTENT_URI, cv);
        } catch (SQLException e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage());
        }
    }

    @Override
    public void deleteFeeding(Feeding feeding) {
        AsyncQueryHandler handler = new DBQueryHandler(this.mContext.getContentResolver(), this);
        String whereClause = "_ID = " + feeding.getId();
        handler.startDelete(0, feeding, FeedingDBEntry.CONTENT_URI,
                null, new String[]{whereClause});
    }

    @Override
    public void updateFeeding(Feeding f) {
        String whereClause = "_ID = " + f.getId();
        ContentValues cv = new ContentValues();
        cv.put(FeedingDBEntry.COLUMN_DETAILS, f.getDetails());
        cv.put(FeedingDBEntry.COLUMN_START_DATE_TIME, f.getStartDateTime().getTime());
        cv.put(FeedingDBEntry.COLUMN_END_DATE_TIME, f.getEndDateTime() != null ? f.getEndDateTime().getTime() : null);
        cv.put(FeedingDBEntry.COLUMN_FEEDING_TYPE, f.getType().name());
        cv.put(FeedingDBEntry.COLUMN_QUANTITY, f.getQuantity());
        cv.put(FeedingDBEntry.COLUMN_MILK_TYPE, f.getMilkType() != null ? f.getMilkType().name() : null);
        AsyncQueryHandler handler = new DBQueryHandler(this.mContext.getContentResolver(), this);
        handler.startUpdate(0, f, FeedingDBEntry.CONTENT_URI, cv, null, new String[]{whereClause});
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

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String orderSelection = args.getString(ORDER_BY_DATE);
        return new CursorLoader(mContext, FeedingDBEntry.CONTENT_URI,
                null, null, null, orderSelection);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        List<Feeding> feedings = new ArrayList<>();
        if(data != null) {
            while(data.moveToNext()) {
                feedings.add(CursorUtil.getOneRowFeedingFromCursor(data));
            }
        }
        mListener.onGetFeedingsSuccess(feedings);
        mLoaderManager.destroyLoader(loader.getId());
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}

