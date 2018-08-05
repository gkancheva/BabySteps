package com.company.babysteps.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.company.babysteps.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class StringFormatter {

    private static final SimpleDateFormat SDF_PARSER = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    private static final SimpleDateFormat SDF_PARSER_DATE_TIME = new SimpleDateFormat("dd-M-yyyy-hh-mm", Locale.getDefault());
    private static final SimpleDateFormat SDF_FORMATTER = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
    private static final SimpleDateFormat SDF_TIME_FORMATTER = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private static final SimpleDateFormat SDF_DATE_TIME_FORMATTER = new SimpleDateFormat("dd MMM, yyyy, HH:mm", Locale.getDefault());
    private static final SimpleDateFormat SDF_DATE_FORMAT_SHORT = new SimpleDateFormat("ddMMMyy", Locale.getDefault());

    private StringFormatter() {}

    public static Date parseDate(int year, int month, int dayOfMonth) throws ParseException {
        return SDF_PARSER.parse(dayOfMonth + "-" + ++month + "-" + year);
    }

    public static Date parseDateTime(int y, int m, int d, int h, int min) throws ParseException {
        return SDF_PARSER_DATE_TIME.parse(d + "-" + ++m + "-" + y + "-" + h + "-" + min);
    }

    public static String formatDate(Date date) {
        return SDF_FORMATTER.format(date);
    }

    public static String formatTime(Date date) {
        return SDF_TIME_FORMATTER.format(date);
    }

    public static String formatDateTime(Date date) {
        return SDF_DATE_TIME_FORMATTER.format(date);
    }

    public static String formatDateShort(Date date) {
        return SDF_DATE_FORMAT_SHORT.format(date);
    }

    public static String formatWeight(double weight, Context ctx) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ctx);
        boolean isEUUnits = sharedPref.getBoolean(ctx.getString(R.string.pref_metric_units), true);
        String result = "";
        if(isEUUnits) {
            result = String.format(Locale.getDefault(), ctx.getString(R.string.format_kg), weight);
        } else {
            result = String.format(Locale.getDefault(), ctx.getString(R.string.format_lb), weight);
        }
        return result;
    }

    public static String formatLength(double length, Context ctx) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ctx);
        boolean isEUUnits = sharedPref.getBoolean(ctx.getString(R.string.pref_metric_units), true);
        String result = "";
        if(isEUUnits) {
            result = String.format(Locale.getDefault(), ctx.getString(R.string.format_cm), length);
        } else {
            result = String.format(Locale.getDefault(), ctx.getString(R.string.format_in), length);
        }
        return result;
    }

    public static String formatQuantity(double quantity, Context ctx) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ctx);
        boolean isEUUnits = sharedPref.getBoolean(ctx.getString(R.string.pref_metric_units), true);
        String result = "";
        if(isEUUnits) {
            result = String.format(Locale.getDefault(), ctx.getString(R.string.format_ml), quantity);
        } else {
            result = String.format(Locale.getDefault(), ctx.getString(R.string.format_oz), quantity);
        }
        return result;
    }

    public static String getFormattedElapsedTime(Date dateTime, Context ctx) {
        Date currentDateTime = new Date();
        long difference = currentDateTime.getTime() - dateTime.getTime();
        int seconds = (int) TimeUnit.MILLISECONDS.toSeconds(difference);
        int days = seconds / (3600 * 24);
        seconds = seconds % (3600 * 24);
        int hours = seconds / 3600;
        seconds = seconds % 3600;
        int minutes = seconds / 60;
        seconds = seconds % 60;

        StringBuilder sb = new StringBuilder();
        if(days != 0) {
            sb.append(days).append(ctx.getString(R.string.days));
        } else if(hours != 0) {
            sb.append(hours).append(ctx.getString(R.string.hours));
        } else if(minutes != 0){
            sb.append(minutes).append(ctx.getString(R.string.minutes));
        } else if(seconds != 0) {
            sb.append(seconds).append(ctx.getString(R.string.seconds));
        } else {
            sb.append(seconds).append(ctx.getString(R.string.seconds));
        }
        return sb.toString();
    }


}