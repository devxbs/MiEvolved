package com.dimz.os;

import android.content.Context;
import android.content.SharedPreferences;

//Dimz OS//

public class SharedPrefs {

    final static String KEY_LOGIN_NAME = "DimzCode";
    public static final String PREF_NAME = "name";
    public static final String PREF_EMAIL = "email";
    public static final String PREF_AGE = "age";
    public static final String PREF_MOBILE = "mobile";
    public static final String PREF_URL = "url";

    public static String readSharedSetting(Context ctx, String settingName, String defaultValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(KEY_LOGIN_NAME, Context.MODE_PRIVATE);
        return sharedPref.getString(settingName, defaultValue);
    }

    public static void saveSharedSetting(Context ctx, String settingName, String settingValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(KEY_LOGIN_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(settingName, settingValue);
        editor.apply();
    }

    // untuk 2 fungsi dibawah, mending pake boolean aja. jangan string.
    public static boolean readSharedSetting(Context ctx, String settingName) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(KEY_LOGIN_NAME, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(settingName, false);
    }


    public static void saveSharedSetting(Context ctx, String settingName, boolean settingValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(KEY_LOGIN_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(settingName, settingValue);
        editor.apply();
    }
}