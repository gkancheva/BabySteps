package com.company.babysteps.repositories;

import com.company.babysteps.entities.Baby;

public interface SettingsRepo {
    void saveBabyInfoLocally(Baby baby);
    Baby getBabyInfo();
    void saveNotificationPref(String time, boolean receiveNotifications);
    boolean getNotificationPref();
    String getNotificationPrefTime();
    boolean isMetricUnitsPreference();
    void saveMetricUnitsPreference(boolean isMetricUnitsPref);
}