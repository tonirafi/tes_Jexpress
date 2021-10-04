package com.template.project.utils;

import android.util.Log;

import com.template.project.BuildConfig;



public class LogUtil {

    private static final String TAG = "Gudangview";

    private static final boolean DEBUG = BuildConfig.DEBUG;


    public static void logW(String msg) {
        logW(TAG, msg);
    }


    public static void logW(String tag, String msg) {
        if (DEBUG) {
            Log.w(tag, msg);
        }
    }

    public static void logE(String msg) {
        logE(TAG, msg);
    }


    public static void logE(String tag, String msg) {
        if (DEBUG) {
            Log.e(tag, msg);
        }
    }

    public static void logD(String msg) {
        logD(TAG, msg);
    }

    public static void logD(String tag, String msg) {
        if (DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void logV(String msg) {
        logV(TAG, msg);
    }

    public static void logV(String tag, String msg) {
        if (DEBUG) {
            Log.v(tag, msg);
        }
    }


    public static void logI(String msg) {
        logI(TAG, msg);
    }


    public static void logI(String tag, String msg) {
        if (DEBUG) {
            Log.i(tag, msg);
        }
    }
}
