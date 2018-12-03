package com.mlh.mlh.data;

import android.content.Context;
import android.content.SharedPreferences;

public final class SharedPreferencesManager {

    private static final String APP_PREFS = "prefs";
    private static final String PREFS_TOKEN = "token";

    private SharedPreferences prefs;
    private static SharedPreferencesManager instance;

    private SharedPreferencesManager(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesManager(context);
        }
        return instance;
    }

    public void saveToken(int token) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PREFS_TOKEN, token);
        editor.apply();
    }

    public int getToken() {
        return prefs.getInt(PREFS_TOKEN, 0);
    }
}
