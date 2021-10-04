package com.template.project.view;

import androidx.annotation.NonNull;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.view.View;

import java.lang.ref.WeakReference;

public class MyClickSpan extends URLSpan {

    int colorVal = -1;
    WeakReference<OnClickListener> reference;
    public boolean underline = true;

    public MyClickSpan(String url) {
        super(url);
    }

    public MyClickSpan(String url, int colorRes) {
        this(url, colorRes, null);
    }

    public MyClickSpan(String url, int colorVal, OnClickListener listener) {
        this(url);
        this.colorVal = colorVal;
        if (listener != null) {
            this.reference = new WeakReference<>(listener);
        }
    }

    public MyClickSpan(OnClickListener listener) {
        this("", -1, listener);
    }

    public MyClickSpan(int colorRes, OnClickListener listener) {
        this("", colorRes, listener);
    }


    @Override
    public void updateDrawState(@NonNull TextPaint ds) {
        super.updateDrawState(ds);
        if (colorVal != -1)
            ds.setColor(colorVal);
        ds.setUnderlineText(underline);
    }

    @Override
    public void onClick(@NonNull View widget) {
        if (this.reference == null) {
            super.onClick(widget);
            return;
        }

        OnClickListener listener = this.reference.get();
        if (listener == null || !listener.onClick(widget)) { //如果点击事件返回false 表示不拦截 URL 点击事件
            super.onClick(widget);
        }
    }

    public interface OnClickListener {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        boolean onClick(View v);
    }
}
