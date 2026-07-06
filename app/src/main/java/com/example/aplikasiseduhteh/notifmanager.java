package com.example.aplikasiseduhteh;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class notifmanager {

    private static final String PREF_NAME = "NotifPrefs";
    private static final String KEY_LIST  = "notif_list";

    public static List<notifmodel> getList(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = sp.getString(KEY_LIST, null);
        if (json == null) return new ArrayList<>();
        Type type = new TypeToken<List<notifmodel>>() {}.getType();
        List<notifmodel> list = new Gson().fromJson(json, type);
        return list != null ? list : new ArrayList<>();
    }

    public static void saveList(Context context, List<notifmodel> list) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = new Gson().toJson(list);
        sp.edit().putString(KEY_LIST, json).apply();
    }

    public static void tambahnotif(Context context, String judul, String pesan) {
        List<notifmodel> list = getList(context);
        list.add(0, new notifmodel(judul, pesan));
        saveList(context, list);
    }

    public static void hapusnotif(Context context, int index) {
        List<notifmodel> list = getList(context);
        if (index >= 0 && index < list.size()) {
            list.remove(index);
            saveList(context, list);
        }
    }

    public static void sematkannotif(Context context, int index) {
        List<notifmodel> list = getList(context);
        if (index >= 0 && index < list.size()) {
            notifmodel pinned = list.remove(index);
            list.add(0, pinned);
            saveList(context, list);
        }
    }
}