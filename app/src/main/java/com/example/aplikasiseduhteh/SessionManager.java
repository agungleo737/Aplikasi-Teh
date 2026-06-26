package com.example.aplikasiseduhteh;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME  = "SeduhTehSession";
    private static final String KEY_TOKEN  = "token";
    private static final String KEY_NAME   = "name";
    private static final String KEY_EMAIL  = "email";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_ROLE   = "role";

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
    public void savesession(String token, String name, String email, int userId, String role) {
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_ROLE, role != null ? role : "user");
        editor.apply();
    }

    public String gettoken()  { return "Bearer " + prefs.getString(KEY_TOKEN, ""); }
    public String getname()   { return prefs.getString(KEY_NAME, ""); }
    public String getemail()  { return prefs.getString(KEY_EMAIL, ""); }
    public int    getuserid() { return prefs.getInt(KEY_USER_ID, -1); }
    public String getrole()   { return prefs.getString(KEY_ROLE, "user"); }
    public boolean isAdmin()  { return "admin".equals(getrole()); }

    public void setname(String name) {
        editor.putString(KEY_NAME, name);
        editor.apply();
    }

    public boolean isloggedin() {
        return !prefs.getString(KEY_TOKEN, "").isEmpty();
    }

    public void clearsession() {
        editor.clear();
        editor.apply();
    }
}