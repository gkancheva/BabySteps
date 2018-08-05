package com.company.babysteps.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.company.babysteps.entities.Week;

public class WidgetUpdateService extends IntentService {
    public static final String ACTION_UPDATE_LIST = "com.company.babysteps.widget.action.UPDATE_LIST";

    public WidgetUpdateService() {
        super("WidgetUpdateService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null) {
            String action = intent.getAction();
            if(action.equals(ACTION_UPDATE_LIST)){
                Bundle bundle = intent.getBundleExtra(WidgetProvider.BUNDLE_ARGS_KEY);
                if(bundle != null) {
                    Week week = bundle.getParcelable(WidgetProvider.WEEK_KEY);
                    handleUpdateWidget(week);
                }
            }
        }
    }

    private void handleUpdateWidget(Week week) {
        AppWidgetManager awm = AppWidgetManager.getInstance(this);
        int[] widgetIds = awm.getAppWidgetIds(new ComponentName(this, WidgetProvider.class));
        WidgetProvider.updateWidgets(this, awm, widgetIds, week);
    }

    public static void startUpdateWidgetService(Context ctx, Week week) {
        Intent intent = new Intent(ctx, WidgetUpdateService.class);
        intent.setAction(ACTION_UPDATE_LIST);
        Bundle bundle = new Bundle();
        bundle.putParcelable(WidgetProvider.WEEK_KEY, week);
        intent.putExtra(WidgetProvider.BUNDLE_ARGS_KEY, bundle);
        ctx.startService(intent);
    }
}