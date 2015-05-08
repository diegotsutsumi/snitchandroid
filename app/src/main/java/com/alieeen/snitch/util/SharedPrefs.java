package com.alieeen.snitch.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by Aline Borges on 2/10/2015.
 */
public class SharedPrefs {

    //declaracao da key que vai identificar a área das Preferences
    private static final String LOGGED = "logged_in";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String PROPERTY_REG_ID = "registration_id";

    public static void setLoggedIn(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(LOGGED, true);
        editor.commit();
    }

    public static boolean isLoggedIn(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean b = preferences.getBoolean(LOGGED, false);
        return b;
    }

    public static String getRegistrationId(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String registrationId = preferences.getString(PROPERTY_REG_ID, "");
        return registrationId;
    }

    public static void setRegistrationId(Context context, String registrationId) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PROPERTY_REG_ID, registrationId);
        editor.commit();
    }

    public static int getRegisteredAppVersion(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int registeredVersion = preferences.getInt(PROPERTY_APP_VERSION,
                Integer.MIN_VALUE);
        return registeredVersion;
    }

    public static void setRegisteredAppVersion(Context context) {

    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

}
