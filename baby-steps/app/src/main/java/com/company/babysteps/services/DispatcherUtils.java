package com.company.babysteps.services;

import android.content.Context;
import android.support.annotation.NonNull;

import com.company.babysteps.utils.DateUtil;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobTrigger.ExecutionWindowTrigger;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DispatcherUtils {

    private static final int PERIODIC_JOB_INTERVAL_HOURS = 24;
    private static final int PERIODIC_JOB_INTERVAL_END_MINUTES = 10;
    private static final int WINDOW_START_PERIODIC = (int) TimeUnit.HOURS.toSeconds(PERIODIC_JOB_INTERVAL_HOURS);
    private static final int WINDOW_END_PERIODIC = WINDOW_START_PERIODIC + (int)TimeUnit.MINUTES.toSeconds(PERIODIC_JOB_INTERVAL_END_MINUTES);

    private static final String PERIODIC_JOB_TAG = "FIREBASE_DISPATCHER_SERVICE_PERIODIC";
    private static final String SINGLE_JOB_TAG = "FIREBASE_DISPATCHER_SERVICE_SINGLE";

    private static boolean sInitializedSingle;
    private static boolean sInitializedPeriodic;

    public synchronized static void scheduleSingleJob(@NonNull final Context context, String timing) {
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        if(sInitializedSingle) {
            dispatcher.cancel(SINGLE_JOB_TAG);
        }

        int[] time = DateUtil.getTiming(timing);

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.HOUR_OF_DAY, time[0]);
        c.set(Calendar.MINUTE, time[1]);
        Date currentTime = new Date();
        if(c.getTime().getTime() < currentTime.getTime()) {
            c.add(Calendar.HOUR, 24);
        }
        long diffInMilliseconds = c.getTime().getTime() - currentTime.getTime();
        if(diffInMilliseconds < 0){
            diffInMilliseconds += TimeUnit.HOURS.toMillis(23);
        }
        int windowStart = (int) TimeUnit.MILLISECONDS.toSeconds(diffInMilliseconds);
        int windowEnd = windowStart + (int) (TimeUnit.MINUTES.toSeconds(5));

        ExecutionWindowTrigger executionWindow = Trigger
                .executionWindow(windowStart, windowEnd);

        Job job = dispatcher.newJobBuilder()
                .setService(SingleJobService.class)
                .setTag(SINGLE_JOB_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(false)
                .setTrigger(executionWindow)
                .setReplaceCurrent(true)
                .build();
        dispatcher.schedule(job);
        sInitializedSingle = true;
    }

    public synchronized static void schedulePeriodicJob(@NonNull final Context context) {
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        if(sInitializedPeriodic) {
            dispatcher.cancel(PERIODIC_JOB_TAG);
        }

        ExecutionWindowTrigger executionWindow = Trigger
                .executionWindow(WINDOW_START_PERIODIC, WINDOW_END_PERIODIC);

        Job job = dispatcher.newJobBuilder()
                .setService(PeriodicJobService.class)
                .setTag(PERIODIC_JOB_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(executionWindow)
                .setReplaceCurrent(true)
                .build();
        dispatcher.schedule(job);
        sInitializedPeriodic = true;
    }

    public static void cancelNotifications(@NonNull final Context context) {
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        dispatcher.cancelAll();
    }
}