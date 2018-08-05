package com.company.babysteps.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.company.babysteps.MainActivity;
import com.company.babysteps.R;
import com.company.babysteps.data.PostsDBEntry;
import com.company.babysteps.entities.Week;
import com.company.babysteps.utils.CursorUtil;

public class WidgetProvider extends AppWidgetProvider {

    public static final String BUNDLE_ARGS_KEY = "BUNDLE_WEEK";
    public static final String WEEK_KEY = "WEEK_KEY";

    private static Week sWeek;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Cursor cursor = context.getContentResolver()
                .query(PostsDBEntry.CONTENT_URI, null, null, null, null);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String weekTitle = pref.getString(context.getString(R.string.pref_week_title), null);
        String urlImageWeek = pref.getString(context.getString(R.string.pref_week_image_url), null);
        if(weekTitle != null) {
            sWeek = new Week();
            sWeek.setTitle(weekTitle);
            sWeek.setImageUrl(urlImageWeek);
            if(cursor != null) {
                sWeek.setPosts(CursorUtil.getPostsFromCursor(cursor));
            }
        }
        if(cursor != null) {
            cursor.close();
        }
        updateWidgets(context, appWidgetManager, appWidgetIds, sWeek);
    }

    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {

    }

    public static Week getWeek() {
        return sWeek;
    }

    public static void updateWidgets(Context ctx, AppWidgetManager awm, int[] widgetIds, Week week) {
        for (int widgetId : widgetIds) {
            sWeek = week;
            RemoteViews rv = new RemoteViews(ctx.getPackageName(), R.layout.app_widget);
            Intent listService = new Intent(ctx, WidgetListService.class);
            listService.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            listService.setData(Uri.parse(listService.toUri(Intent.URI_INTENT_SCHEME)));
            String widgetTitle = week == null ? ctx.getString(R.string.default_value_week_title) : week.getTitle();
            rv.setTextViewText(R.id.appwidget_text, widgetTitle);

            if(week != null) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(WEEK_KEY, week);
                listService.putExtra(BUNDLE_ARGS_KEY, bundle);
            }

            rv.setRemoteAdapter(widgetId, R.id.lv_posts, listService);
            rv.setEmptyView(R.id.lv_posts, R.id.empty_view);
            Intent clickIntent = new Intent(ctx, MainActivity.class);
            PendingIntent pi = PendingIntent.getActivity(ctx, widgetId,
                    clickIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            rv.setPendingIntentTemplate(R.id.lv_posts, pi);
            awm.updateAppWidget(widgetId, rv);
            awm.notifyAppWidgetViewDataChanged(widgetId, R.id.lv_posts);
        }
    }

}

