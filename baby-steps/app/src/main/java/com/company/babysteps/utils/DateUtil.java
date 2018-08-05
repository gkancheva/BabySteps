package com.company.babysteps.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

    private DateUtil() { }

    public static int[] getTiming(String timing) {
        int hour = Integer.parseInt(timing.substring(0, timing.indexOf(":")));
        int minutes = Integer.parseInt(timing.substring(timing.indexOf(":") + 1));
        return new int[]{hour, minutes};
    }

    /** Returns number of months and weeks between given date and today`s day. **/
    public static int[] getPeriodToToday(Date date) {
        Calendar oldDate = convertToCalendar(date);
        Calendar currentDate = convertToCalendar(new Date());
        int monthsBetween = getMonthBetween(oldDate, currentDate);
        if(monthsBetween >= 2) {
            oldDate.add(Calendar.MONTH, monthsBetween);
        } else {
            monthsBetween = 0;
        }
        int days = getDaysBetween(currentDate.getTime(), oldDate.getTime());
        int numberOfWeeks = days / 7;
        return new int[]{monthsBetween, numberOfWeeks};
    }

    public static boolean weekOrMonthAnniversary(Date date) {
        Calendar oldDate = convertToCalendar(date);
        Calendar currentDate = convertToCalendar(new Date());
        int monthsBetween = getMonthBetween(oldDate, currentDate);
        oldDate.add(Calendar.MONTH, monthsBetween);
        int days = getDaysBetween(currentDate.getTime(), oldDate.getTime());
        return days % 7 == 0;
    }

    public static int getMonths(Date dateOfBirth, Date date) {
        Calendar dob = convertToCalendar(dateOfBirth);
        Calendar d = convertToCalendar(date);
        return getMonthBetween(dob, d);
    }

    private static int getMonthBetween(Calendar oldDate, Calendar currentDate) {
        int monthsBetween = currentDate.get(Calendar.MONTH) - oldDate.get(Calendar.MONTH);
        if(currentDate.get(Calendar.YEAR) > oldDate.get(Calendar.YEAR)){
            monthsBetween += 12;
        }
        if(currentDate.get(Calendar.DAY_OF_MONTH) < oldDate.get(Calendar.DAY_OF_MONTH)) {
            monthsBetween--;
        }
        return monthsBetween;
    }

    private static Calendar convertToCalendar(Date date) {
        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        c.setTime(date);
        return c;
    }

    private static int getDaysBetween(Date now, Date before) {
        return (int)((now.getTime() - before.getTime()) / (1000 * 60 * 60 * 24));
    }
}