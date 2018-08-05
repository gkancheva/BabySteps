package com.company.babysteps.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.company.babysteps.MainActivity;
import com.company.babysteps.R;

public class NotificationUtils {

    private static final String NOTIFICATION_CHANNEL_ID =  "NOTIFICATION_CHANNEL_ID";
    private static final String NOTIFICATION_CHANNEL_NAME = "BABY_STEPS_NOTIF_NAME";
    private static final int NOTIFICATION_ID_WEEK_MONTH_ANNIV = 100;
    private static final int NOTIFICATION_ID_FEEDING_IN_PROGRESS = 101;
    private static final int REQUEST_CODE_PENDING_INTENT = 200;

    public static void notifyWeekOrMonthAnniversary(Context ctx, String title, String body) {
        NotificationManager nm = (NotificationManager)
                ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);
            nm.createNotificationChannel(mChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(ctx, R.color.colorPrimary))
                .setSmallIcon(R.mipmap.ic_baby_feet)
                .setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.ic_baby_feet_foreground))
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(contentIntent(ctx))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        nm.notify(NOTIFICATION_ID_WEEK_MONTH_ANNIV, builder.build());
    }

    public static void notifyFeedingActivityInProgress(Context context) {
        NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel mChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);
            nm.createNotificationChannel(mChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_baby_feet_foreground)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_baby_feet_foreground))
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(context.getString(R.string.feeding_in_progress))
                .setContentIntent(contentFeedingIntent(context))
                .setOngoing(true)
                .setAutoCancel(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        nm.notify(NOTIFICATION_ID_FEEDING_IN_PROGRESS, builder.build());
    }

    public static void cancelFeedingNotification(Context ctx) {
        NotificationManager nm = (NotificationManager) ctx
                .getSystemService(Context.NOTIFICATION_SERVICE);

        nm.cancel(NOTIFICATION_ID_FEEDING_IN_PROGRESS);
    }

    private static PendingIntent contentIntent(Context ctx) {
        Intent startActivity = new Intent(ctx, MainActivity.class);
        return PendingIntent.getActivity(
                ctx, REQUEST_CODE_PENDING_INTENT, startActivity,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static PendingIntent contentFeedingIntent(Context ctx) {
        Intent startActivity = new Intent(ctx, MainActivity.class);
        startActivity.putExtra(ctx.getString(R.string.feeding_in_progress_key), true);
        return PendingIntent.getActivity(
                ctx, REQUEST_CODE_PENDING_INTENT, startActivity,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

}