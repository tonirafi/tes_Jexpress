package com.template.project.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class SharedPreferencesTool {

    private SharedPreferences sp;
    private Editor mEditor;
    private Gson mGson;

    private Gson getGson() {
        if (mGson == null)
            mGson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
        return mGson;
    }

    private static SharedPreferencesTool instance;

    public static SharedPreferencesTool getInstance(Context context) {
        if (instance == null)
            instance = new SharedPreferencesTool(context.getApplicationContext());
        return instance;
    }

    private SharedPreferencesTool(Context context) {
        sp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public Editor getEditor() {
        if (mEditor == null)
            mEditor = sp.edit();
        return mEditor;
    }

    public boolean getBoolean(String key, boolean value) {
        return sp.getBoolean(key, value);
    }

    public int getInt(String key, int value) {
        return sp.getInt(key, value);
    }

    public long getLong(String key, long value) {
        return sp.getLong(key, value);
    }

    public String getString(String key, String value) {
        return sp.getString(key, value);
    }

    public float getFloat(String key, float value) {
        return sp.getFloat(key, value);
    }

    public void putBoolean(String key, boolean value) {
        Editor editor = getEditor();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public void putInt(String key, int value) {
        Editor editor = getEditor();
        editor.putInt(key, value);
        editor.apply();
    }

    public void putLong(String key, long value) {
        Editor editor = getEditor();
        editor.putLong(key, value);
        editor.apply();
    }

    public void putString(String key, String value) {
        Editor editor = getEditor();
        editor.putString(key, value);
        editor.apply();
    }

    public void putFloat(String key, float value) {
        Editor editor = getEditor();
        editor.putFloat(key, value);
        editor.apply();
    }

    public void putObject(String key, @NonNull Object object) {
        String json = getGson().toJson(object);
        putString(key, json);
    }

    public void putObject(@NonNull Object object) {
        String key = getKeyFromClass(object.getClass());
        putObject(key, object);
    }

    public <T> T getObject(String key, Class<T> clazz, T defaultObject) {
        String json = getString(key, null);
        if (json == null || json.isEmpty()) return defaultObject;

        T object = getGson().fromJson(json, TypeToken.get(clazz).getType());
//        T object = getGson().fromJson(json, clazz);
        return object == null ? defaultObject : object;
    }

    public <T> T getObject(Class<T> clazz, T defaultObject) {
        return getObject(getKeyFromClass(clazz), clazz, defaultObject);
    }

    private String getKeyFromClass(Class<?> aClass) {
        return aClass.getName().toUpperCase();
    }

    public void remove(String key) {
        Editor editor = getEditor();
        editor.remove(key);
        editor.apply();
    }

    public void remove(Object object) {
        String key = getKeyFromClass(object.getClass());
        remove(key);
    }

    public void remove(Class<?> aClass) {
        remove(aClass.getName().toUpperCase());
    }

}