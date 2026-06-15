package com.example.aplikasiseduhteh;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "SeduhTehSession";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_NAME  = "name";
    private static final String KEY_EMAIL = "email";

    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs  = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void savesession(String token, String name, String email) {
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    public String gettoken() {
        return "Bearer " + prefs.getString(KEY_TOKEN, "");
    }

    public String getname()  { return prefs.getString(KEY_NAME, ""); }
    public String getemail() { return prefs.getString(KEY_EMAIL, ""); }

    public boolean isloggedin() {
        return !prefs.getString(KEY_TOKEN, "").isEmpty();
    }

    public void clearsession() {
        editor.clear();
        editor.apply();
    }
}