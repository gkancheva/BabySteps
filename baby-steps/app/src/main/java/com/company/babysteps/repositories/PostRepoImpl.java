package com.company.babysteps.repositories;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.company.babysteps.R;
import com.company.babysteps.data.DBQueryHandler;
import com.company.babysteps.data.PostsDBEntry;
import com.company.babysteps.entities.Week;
import com.company.babysteps.services.PostRepoListener;
import com.company.babysteps.services.PostLoaderTask;
import com.company.babysteps.utils.ConstLoaderIds;
import com.company.babysteps.utils.CursorUtil;
import com.company.babysteps.utils.NetworkUtils;
import com.company.babysteps.utils.UrlConst;
import com.company.babysteps.widget.WidgetUpdateService;

public class PostRepoImpl implements PostRepo,
        LoaderManager.LoaderCallbacks<Week>,
        DBQueryHandler.AsyncQueryListener {

    private static final String PATH = "PATH";
    private final Context mContext;
    private final LoaderManager mLoaderManager;
    private PostRepoListener mPostListener;
    private SharedPreferences mSharedPref;

    public PostRepoImpl(Context ctx, LoaderManager loaderManager, PostRepoListener postListener) {
        this.mContext = ctx;
        this.mLoaderManager = loaderManager;
        this.mPostListener = postListener;
        this.mSharedPref = PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    @Override
    public void getPostsForWeekFromTheWeb(int[] age) {
        UrlConst urlConst = UrlConst.getInstance();
        String path = urlConst.getUrl(age[0], age[1]);
        Bundle args = new Bundle();
        args.putString(PATH, path);
        if(NetworkUtils.hasNetworkConnectivity(this.mContext)) {
            this.mLoaderManager.initLoader(ConstLoaderIds.LOADER_ID_GET_POSTS_FROM_WEB, args, this);
        } else {
            this.mPostListener.onPostFailure(mContext.getString(R.string.msg_no_internet));
        }
    }

    @Override
    public void getWeekWithPostsFromDB() {
        this.mLoaderManager.initLoader(ConstLoaderIds.LOADER_ID_GET_POSTS_FROM_DB, null,
                getPostsCursorLoader);
    }

    @Override
    public void saveWeekLocally(Week week) {
        //delete current posts from db
        //after deletion completed -> bulkInsert -> onDeleteCompleted()
        AsyncQueryHandler handler = new DBQueryHandler(this.mContext.getContentResolver(), this);
        handler.startDelete(0, week, PostsDBEntry.CONTENT_URI, null, null);

        //update week info in shared preferences
        SharedPreferences.Editor editor = this.mSharedPref.edit();
        editor.putString(mContext.getString(R.string.pref_week_title), week.getTitle());
        editor.putString(mContext.getString(R.string.pref_week_image_url), week.getImageUrl());
        editor.apply();
    }

    private void savePosts(Week week) {
        //insert new posts into db
        ContentValues[] values = new ContentValues[week.getPosts().size()];
        for (int i = 0; i < values.length; i++) {
            ContentValues cv = new ContentValues();
            cv.put(PostsDBEntry.COLUMN_TITLE, week.getPosts().get(i).getTitle());
            cv.put(PostsDBEntry.COLUMN_BODY, week.getPosts().get(i).getBody());
            values[i] = cv;
        }
        try {
            int insertedRows = mContext.getContentResolver().bulkInsert(PostsDBEntry.CONTENT_URI, values);
            if(insertedRows == week.getPosts().size()) {
                WidgetUpdateService.startUpdateWidgetService(mContext, week);
            }
        } catch (SQLException e) {
            this.mPostListener.onPostFailure(mContext.getString(R.string.msg_error_saving_posts));
        }
    }

    @NonNull
    @Override
    public Loader<Week> onCreateLoader(int id, @Nullable Bundle args) {
        String path = args.getString(PATH);
        return new PostLoaderTask(this.mContext, path);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Week> loader, Week week) {
        //only savePosts locally the week if a baby age is set.
        boolean babyAgeSet = this.mSharedPref.getLong(mContext.getString(R.string.pref_date_of_birth), -1) != -1;
        String savedWeekTitle = this.mSharedPref.getString(mContext.getString(R.string.pref_week_title), null);
        boolean isNewWeek = savedWeekTitle != null ? !week.getTitle().equals(savedWeekTitle) : false;
        if(babyAgeSet || isNewWeek) {
            this.saveWeekLocally(week);
        }
        this.mPostListener.onPostSuccess(week);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Week> loader) {

    }


    private LoaderManager.LoaderCallbacks<Cursor> getPostsCursorLoader =
            new LoaderManager.LoaderCallbacks<Cursor>() {
        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
            return new CursorLoader(mContext, PostsDBEntry.CONTENT_URI,
                    null, null, null, null);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
            String weekTitle = mSharedPref.getString(mContext.getString(R.string.pref_week_title), null);
            Week week = null;
            if(weekTitle != null) {
                week = new Week();
                week.setTitle(weekTitle);
                week.setImageUrl(mSharedPref.getString(mContext.getString(R.string.pref_week_image_url), null));
                week.setPosts(CursorUtil.getPostsFromCursor(cursor));
            }
            if(mPostListener != null) {
                mPostListener.onPostSuccess(week);
            }
            mLoaderManager.destroyLoader(loader.getId());
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        }
    };

    @Override
    public void onQueryCompleted(Object object) {

    }

    @Override
    public void onDeleteCompleted(Object object) {
        if(object instanceof Week) {
            savePosts((Week)object);
        }
    }
}