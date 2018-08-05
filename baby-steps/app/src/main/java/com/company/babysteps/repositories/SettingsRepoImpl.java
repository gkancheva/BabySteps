package com.company.babysteps.repositories;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.company.babysteps.R;
import com.company.babysteps.entities.Baby;

import java.util.Date;

public class SettingsRepoImpl implements SettingsRepo {

    private Context mContext;
    private SharedPreferences mSharedPref;

    public SettingsRepoImpl(Context ctx) {
        this.mContext = ctx;
        this.mSharedPref = PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    @Override
    public void saveBabyInfoLocally(Baby baby) {
        SharedPreferences.Editor editor = this.mSharedPref.edit();
        editor.putString(mContext.getString(R.string.pref_baby_name), baby.getName());
        editor.putLong(mContext.getString(R.string.pref_date_of_birth), baby.getDateOfBirth().getTime());
        editor.apply();
    }

    @Override
    public Baby getBabyInfo() {
        String name = this.mSharedPref.getString(mContext.getString(R.string.pref_baby_name), null);
        long dateOfBirth = this.mSharedPref.getLong(mContext.getString(R.string.pref_date_of_birth), 0);
        if(name != null && dateOfBirth != 0) {
            return new Baby(name, new Date(dateOfBirth));
        }
        return null;
    }

    @Override
    public void saveNotificationPref(String time, boolean receiveNotifications) {
        SharedPreferences.Editor editor = this.mSharedPref.edit();
        editor.putString(mContext.getString(R.string.pref_time_of_notification), time);
        editor.putBoolean(mContext.getString(R.string.pref_receive_notifications), receiveNotifications);
        editor.apply();
    }

    @Override
    public boolean getNotificationPref() {
        return this.mSharedPref.getBoolean(mContext.getString(R.string.pref_receive_notifications), true);
    }

    @Override
    public String getNotificationPrefTime() {
        String defaultTime = mContext.getString(R.string.default_notification_time);
        return this.mSharedPref.getString(mContext.getString(R.string.pref_time_of_notification), defaultTime);
    }


    @Override
    public void saveMetricUnitsPreference(boolean isMetricUnitsPref) {
        SharedPreferences.Editor editor = this.mSharedPref.edit();
        editor.putBoolean(mContext.getString(R.string.pref_metric_units), isMetricUnitsPref);
        editor.apply();
    }

    @Override
    public boolean isMetricUnitsPreference() {
        return this.mSharedPref.getBoolean(mContext.getString(R.string.pref_metric_units), true);
    }


}