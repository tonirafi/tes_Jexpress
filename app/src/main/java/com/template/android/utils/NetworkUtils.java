package com.template.android.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.template.android.MyApplication;


public class NetworkUtils {


    public static boolean isConnected(Context context) {
        NetworkInfo info = getActiveNetworkInfo(context.getApplicationContext());
        return info != null && info.isConnected();
    }

    public static boolean isConnected() {
        NetworkInfo info = getActiveNetworkInfo(MyApplication.getContext());
        return info != null && info.isConnected();
    }


    private static NetworkInfo getActiveNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
//        return cm == null ? null : cm.getActiveNetworkInfo();
        return null;
    }


}
