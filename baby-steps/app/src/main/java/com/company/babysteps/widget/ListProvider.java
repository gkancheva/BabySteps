package com.company.babysteps.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.company.babysteps.R;
import com.company.babysteps.entities.Post;
import com.company.babysteps.entities.Week;

import java.util.ArrayList;
import java.util.List;

public class ListProvider implements RemoteViewsService.RemoteViewsFactory {

    private Week mWeek;
    private Context mContext;

    public ListProvider(Context mContext, Intent intent) {
        this.mContext = mContext;
        Bundle bundle = intent.getBundleExtra(WidgetProvider.BUNDLE_ARGS_KEY);
        if(bundle != null) {
            mWeek = bundle.getParcelable(WidgetProvider.WEEK_KEY);
        }
    }


    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        if(WidgetProvider.getWeek() != null) {
            this.mWeek = WidgetProvider.getWeek();
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if(this.mWeek == null) {
            return 0;
        }
        return this.mWeek.getPosts().size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        List<Post> postList = new ArrayList<>();
        if(this.mWeek != null) {
            postList = this.mWeek.getPosts();
        }
        Post currentPost = postList.get(position);
        final RemoteViews remoteView = new RemoteViews(
                mContext.getPackageName(), R.layout.widget_row);
        String title = currentPost.getTitle();
        remoteView.setTextViewText(R.id.tv_post_title, title);
        Intent fillInIntent = new Intent();
        remoteView.setOnClickFillInIntent(R.id.tv_post_title, fillInIntent);
        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

}