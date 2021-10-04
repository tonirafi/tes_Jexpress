package com.template.project.view

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.view.View
import android.view.ViewGroup
import com.template.project.R

class LayoutSwitchAnimation(val frontView: View, val backFrontView: View) {

    private val layoutSwitchAnimGroup by lazy {
        val rightOutSet = AnimatorInflater.loadAnimator(frontView.context, R.animator.card_anim_out) as AnimatorSet
        val leftInSet = AnimatorInflater.loadAnimator(frontView.context, R.animator.card_anim_in) as AnimatorSet

        // 改变视角距离, 贴近屏幕
        val distance = 16000
        val scale = frontView.context.resources.displayMetrics.density * distance
        frontView.cameraDistance = scale
        backFrontView.cameraDistance = scale

        rightOutSet to leftInSet
    }

    private var layoutSwitchAnimSet: AnimatorSet? = null

    fun animLayoutSwitch(pwdLogin: Boolean = true, delay: Long = 0) {

        layoutSwitchAnimSet?.let {
            if (it.isStarted)
                return
            it.cancel()
        }

        layoutSwitchAnimSet = AnimatorSet()
        layoutSwitchAnimSet?.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                super.onAnimationStart(animation)
                setViewClickable(false)

                frontView.visibility = View.VISIBLE
                backFrontView.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                setViewClickable(true)

                if (pwdLogin) {
                    frontView.visibility = View.INVISIBLE
                    backFrontView.visibility = View.VISIBLE
                } else {
                    backFrontView.visibility = View.INVISIBLE
                    frontView.visibility = View.VISIBLE
                }
            }
        })

        // 正面朝上
        if (pwdLogin) {
            layoutSwitchAnimGroup.first.setTarget(frontView)
            layoutSwitchAnimGroup.second.setTarget(backFrontView)
//            layoutSwitchAnimGroup.first.start()
//            layoutSwitchAnimGroup.second.start()
        } else { // 背面朝上
            layoutSwitchAnimGroup.first.setTarget(backFrontView)
            layoutSwitchAnimGroup.second.setTarget(frontView)
//            layoutSwitchAnimGroup.first.start()
//            layoutSwitchAnimGroup.second.start()
        }

        layoutSwitchAnimSet?.playTogether(layoutSwitchAnimGroup.first, layoutSwitchAnimGroup.second)
        layoutSwitchAnimSet?.startDelay = delay
        layoutSwitchAnimSet?.start()
    }

    fun setViewClickable(boolean: Boolean) {
        (frontView to backFrontView).toList().forEach {
            it.isClickable = boolean
            if (it is ViewGroup) {
                for (i in 0 until it.childCount) {
                    it.getChildAt(i).isClickable = boolean
                }
            }
        }
    }

    fun cancel() {
        layoutSwitchAnimSet?.let {
            it.removeAllListeners()
            it.cancel()
        }
    }
}