package com.tes.frezzmart.recevers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

class BaseActBroadcastReceiver(private var listener: EventListener?) : BroadcastReceiver() {
    val filter: IntentFilter

    override fun onReceive(context: Context, intent: Intent) {
        if (listener == null) return
        if (LOCALE_CHANGED_ACTION == intent.action) {
            listener!!.localeChanged()
        } else if (FINISH_ACT_ACTION == intent.action) {
            listener!!.finishActivity()
        }
    }

    fun removeEventListener() {
        listener = null
    }

    interface EventListener {
        fun localeChanged()
        fun finishActivity()
    }

    companion object {
        const val LOCALE_CHANGED_ACTION = Intent.ACTION_LOCALE_CHANGED
        const val FINISH_ACT_ACTION = "finish_cur_activity"
    }

    init {
        filter = IntentFilter()
        //        intentFilter.addAction(Intent.ACTION_LOCALE_CHANGED);
        filter.addAction(LOCALE_CHANGED_ACTION)
        filter.addAction(FINISH_ACT_ACTION)
    }
}