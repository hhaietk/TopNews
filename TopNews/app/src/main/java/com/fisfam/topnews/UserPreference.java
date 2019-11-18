package com.fisfam.topnews;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreference {

    private static final String DEFAULT_RINGTONE_URL = "content://settings/system/notification_sound";
    private SharedPreferences mSharedPref;

    public UserPreference(final Context context) {
        mSharedPref = context.getSharedPreferences("MAIN_PREF", Context.MODE_PRIVATE);
    }

    public boolean getVibration() {
        return mSharedPref.getBoolean("SETTINGS_VIBRATION", true);
    }

    public void setImageCache(boolean value) {
        mSharedPref.edit().putBoolean("SETTINGS_IMG_CACHE", value).apply();
    }

    public boolean getImageCache() {
        return mSharedPref.getBoolean("SETTINGS_IMG_CACHE", true);
    }

    public void setSelectedTheme(int index) {
        mSharedPref.edit().putInt("SETTINGS_THEME", index).apply();
    }

    public int getSelectedTheme() {
        return mSharedPref.getInt("SETTINGS_THEME", 0);
    }

    public String getRingtone() {
        return mSharedPref.getString("SETTINGS_RINGTONE", DEFAULT_RINGTONE_URL);
    }

    public void setPushNotification(boolean value) {
        mSharedPref.edit().putBoolean("SETTINGS_PUSH_NOTIF", value).apply();
    }

    public boolean getPushNotification() {
        return mSharedPref.getBoolean("SETTINGS_PUSH_NOTIF", true);
    }

    public void setVibration(boolean value) {
        mSharedPref.edit().putBoolean("SETTINGS_VIBRATION", value).apply();
    }
}
