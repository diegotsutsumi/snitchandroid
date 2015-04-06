package com.alieeen.snitch.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Aline Borges on 2/10/2015.
 */
public class SharedPrefs {

    //declaracao da key que vai identificar a área das Preferences
    private static final String LOGGED = "logged_in";

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


}