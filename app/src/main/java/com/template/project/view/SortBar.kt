//package com.s1rem.gudangview.view
//
//import android.content.Context
//import android.util.AttributeSet
//import android.widget.LinearLayout
//import com.s1rem.gudangview.R
//
///**
// *   Created by 5Mall<zhangwei> on 2020/8/11
// *   Email:zhangwei@qingsongchou.com
// *   描述：
// */
//class SortBar : LinearLayout {
//    constructor(context: Context) : this(context, null)
//    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
//        inflate(context, R.layout.item_card_sort_bar, this)
//
//        itemComposite.setStatusListener { _: Int, _: Boolean -> notifyNewStatus(0) }
//
//        itemTrending.setStatusListener { _: Int, up: Boolean -> notifyNewStatus(if (up) 11 else 10) }
//
//        itemNew.setStatusListener { _: Int, up: Boolean -> notifyNewStatus(if (up) 21 else 20) }
//        //设置初始状态
//        updateStatus(0)
//    }
//
//    var onSortStatusChangedListener: OnSortStatusChangedListener? = null
//
//    private fun notifyNewStatus(status: Int) {
//        onSortStatusChangedListener?.onSortStatusChanged(status)
//        updateStatus(status)
//    }
//
//    fun updateStatus(status: Int) = when (status) {
//        11 -> {
//            itemTrending.checkUp()
//            itemComposite.unChecked()
//            itemNew.unChecked()
//        }
//        10 -> {
//            itemTrending.checkDown()
//            itemComposite.unChecked()
//            itemNew.unChecked()
//        }
//        21 -> {
//            itemNew.checkUp()
//            itemComposite.unChecked()
//            itemTrending.unChecked()
//        }
//        20 -> {
//            itemNew.checkDown()
//            itemComposite.unChecked()
//            itemTrending.unChecked()
//        }
//        else -> {
//            itemComposite.checkDown()
//            itemTrending.unChecked()
//            itemNew.unChecked()
//        }
//    }
//
//}