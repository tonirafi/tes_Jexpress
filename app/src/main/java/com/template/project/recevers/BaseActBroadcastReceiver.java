package com.template.project.recevers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class BaseActBroadcastReceiver extends BroadcastReceiver {

    public static final String LOCALE_CHANGED_ACTION = Intent.ACTION_LOCALE_CHANGED;
    public static final String FINISH_ACT_ACTION = "finish_cur_activity";

    private IntentFilter intentFilter;
    private EventListener listener;

    public BaseActBroadcastReceiver(EventListener listener) {
        this.listener = listener;

        intentFilter = new IntentFilter();
//        intentFilter.addAction(Intent.ACTION_LOCALE_CHANGED);
        intentFilter.addAction(LOCALE_CHANGED_ACTION);
        intentFilter.addAction(FINISH_ACT_ACTION);
    }

    public IntentFilter getFilter() {
        return intentFilter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (listener==null) return;

        if (LOCALE_CHANGED_ACTION.equals(intent.getAction())) {
            listener.localeChanged();
        } else if (FINISH_ACT_ACTION.equals(intent.getAction())) {
            listener.finishActivity();
        }

    }

    public void removeEventListener() {
        this.listener = null;
    }

    public interface EventListener {

        void localeChanged();

        void finishActivity();
    }

}