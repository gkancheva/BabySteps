package com.company.babysteps.services;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.company.babysteps.entities.Week;
import com.company.babysteps.utils.HTMLParserUtils;

public class PostLoaderTask extends AsyncTaskLoader<Week> {

    private final String mPath;
    private Week mQueryResult;

    public PostLoaderTask(Context ctx, String path) {
        super(ctx);
        this.mPath = path;
    }

    @Override
    protected void onStartLoading() {
        if(this.mQueryResult != null) {
            deliverResult(this.mQueryResult);
        } else {
            forceLoad();
        }
    }

    @Override
    public Week loadInBackground() {
        return HTMLParserUtils.getWeekInfo(this.mPath);
    }

    @Override
    public void deliverResult(Week week) {
        this.mQueryResult = week;
        super.deliverResult(week);
    }
}