package com.tes.frezzmart.utils;

import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.NonNull;

import android.util.DisplayMetrics;


public class DensityUtil {

    private DensityUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static int dp2px(@NonNull Context context, float dpVal) {
        float scale = getDisplayMetrics(context).density;
        return (int) (dpVal * scale + 0.5F);
    }

    public static float dp2Px(@NonNull Context context, float dpVal) {
        float scale = getDisplayMetrics(context).density;
        return dpVal * scale;
    }

    public static float px2dp(@NonNull Context context, float pxVal) {
        float scale = getDisplayMetrics(context).density;
        return pxVal / scale;
    }

    public static int sp2px(@NonNull Context context, float spVal) {
        float fontScale = getDisplayMetrics(context).scaledDensity;
        return (int) (spVal * fontScale + 0.5F);
    }

    public static float px2sp(@NonNull Context context, float pxVal) {
        return pxVal / getDisplayMetrics(context).scaledDensity;
    }

    public static int dp2px(float dpVal) {
        float scale = getDisplayMetrics(null).density;
        return (int) (dpVal * scale + 0.5F);
    }

    public static float dp2Px(float dpVal) {
        float scale = getDisplayMetrics(null).density;
        return dpVal * scale;
    }

    public static float px2dp(float pxVal) {
        float scale = getDisplayMetrics(null).density;
        return pxVal / scale;
    }

    public static float px2sp(float pxVal) {
        return pxVal / getDisplayMetrics(null).scaledDensity;
    }

    public static int sp2px(float spVal) {
        float fontScale = getDisplayMetrics(null).scaledDensity;
        return (int) (spVal * fontScale + 0.5F);
    }

    public static float getDpOfSWR(float ratio) {
        return getDisplayMetrics(null).widthPixels * ratio;
    }

    private static DisplayMetrics getDisplayMetrics(Context context) {
        return context == null ? Resources.getSystem().getDisplayMetrics() : context.getResources().getDisplayMetrics();
    }

}
