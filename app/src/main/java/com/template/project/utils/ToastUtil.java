package com.template.project.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.template.project.MyApplication;
import com.template.project.R;


public class ToastUtil {

    private static Toast sToast;


    public static void show(String content) {
        show(MyApplication.getContext(), content);
    }

    public static void show(int resId) {
        show(MyApplication.getContext(), resId);
    }

    public static void showLong(String content) {
        showLong(MyApplication.getContext(), content);
    }

    public static void showLong(int resId) {
        showLong(MyApplication.getContext(), resId);
    }

    public static void show(Context context, CharSequence content) {
        showToast(context, content, Toast.LENGTH_SHORT, Gravity.CENTER).show();
    }

    public static void show(Context context, int resId) {
        showToast(context, context.getText(resId), Toast.LENGTH_SHORT, Gravity.CENTER).show();
    }

    public static void showLong(Context context, CharSequence content) {
        showToast(context, content, Toast.LENGTH_LONG, Gravity.CENTER).show();
    }

    public static void showLong(Context context, int resId) {
        showToast(context, context.getText(resId), Toast.LENGTH_LONG, Gravity.CENTER).show();
    }

    public static Toast showToast(Context context, CharSequence content, int duration, int gravity) {
        if (sToast == null) {
//            sToast = Toast.makeText(context.getApplicationContext(), content, Toast.LENGTH_LONG);
            sToast = new Toast(context.getApplicationContext());
            sToast.setView(LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.layout_toast, null));
        }

        sToast.setText(content);

        if (sToast.getDuration() != duration) {
            sToast.setDuration(duration);
        }

        if (sToast.getGravity() != gravity) {
            sToast.setGravity(gravity, 0, gravity == Gravity.CENTER ? 0 : DensityUtil.dp2px(context, 66f));
        }

        return sToast;
    }


    /**
     * 用于测试...不使用
     */
    @Deprecated
    public static void showBar(Context context, String content) {
        ProgressBar progressBar = new ProgressBar(context.getApplicationContext(), null, android.R.attr.progressBarStyleSmall);
        if (sToast == null) {
            sToast = Toast.makeText(context.getApplicationContext(), content, Toast.LENGTH_SHORT);
        } else {
            sToast.setView(progressBar);
        }
        sToast.show();
    }
}
