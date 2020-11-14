package com.projekt.tdp028.utility;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class LocalStore {
    public static String USER_ID_KEY = "USER_ID";
    public static String LOCALE_KEY = "LOCALE";


    public static String LOGIN_PREFERENCE = "Login";

    public static String DARK_MODE_KEY = "DARK_MODE";
    public static String UI_PREFERENCE = "UI";



    public static void saveUserId(String userId, Context context) {
        SharedPreferences sp= context.getSharedPreferences(LOGIN_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor Ed=sp.edit();
        Ed.putString(USER_ID_KEY, userId);
        Ed.commit();
    }

    public static void saveLocale(String locale, Context context) {
        SharedPreferences sp= context.getSharedPreferences(UI_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor Ed=sp.edit();
        Ed.putString(LOCALE_KEY, locale);
        Ed.commit();
    }

    public static void saveDarkMode(boolean isSet, Context context) {
        SharedPreferences sp= context.getSharedPreferences(DARK_MODE_KEY, MODE_PRIVATE);
        SharedPreferences.Editor Ed=sp.edit();
        Ed.putBoolean(DARK_MODE_KEY, isSet);
        Ed.commit();
    }

    public static String getSavedUserId(Context context) {
        return context.getSharedPreferences(LOGIN_PREFERENCE, MODE_PRIVATE).getString(USER_ID_KEY, null);
    }

    public static String getSavedLocale(Context context) {
        return context.getSharedPreferences(UI_PREFERENCE, MODE_PRIVATE).getString(LOCALE_KEY, "en-US");
    }

    public static boolean getDarkMode(Context context) {
        return context.getSharedPreferences(UI_PREFERENCE, MODE_PRIVATE).getBoolean(DARK_MODE_KEY, false);
    }
}
