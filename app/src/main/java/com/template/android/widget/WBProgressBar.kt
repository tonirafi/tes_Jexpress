package com.template.android.widget

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ProgressBar
import android.widget.Toast
import com.template.android.utils.NetworkUtils

class WBProgressBar : ProgressBar {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    private val MIN_PROGRESS = 5

    private val defaultInterpolator by lazy {
        AccelerateDecelerateInterpolator()
    }

    private var endProcessAnim: ObjectAnimator? = null

    private fun prepareEndProcessAnim(): ObjectAnimator? {
        Log.d("WEBVIEW", ">>>> prepareEndProcessAnim>>>>")
        cancelEndProcessAnim()
        return ObjectAnimator.ofFloat(this@WBProgressBar, "alpha", 1f, 0.3f, 0f).apply {
            duration = 500
            interpolator = defaultInterpolator
            startDelay = 100
        }
    }

    private var autoProgressAnimSet: AnimatorSet? = null

    private val autoProgressRunnable = Runnable {

        Log.d("WEBVIEW", ">>>> executing autoProgressRunnable >>>>")

        cancelAutoProgressAnim()

        val autoProgressAnim = ValueAnimator.ofInt(progress, max).apply {
            addUpdateListener {
                val progress = it.animatedValue.toString().toInt()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setProgress(progress, true)
                } else {
                    setProgress(progress)
                }
            }

            interpolator = defaultInterpolator
            duration = 30_1000
        }

        autoProgressAnimSet = AnimatorSet().also {
            it.play(autoProgressAnim)?.before(prepareEndProcessAnim())
            it.start()
        }
    }

    private fun cancelAutoProgressAnim() {
        Log.d("WEBVIEW", ">>>>cancelAutoProgressAnim >>>>")
        removeCallbacks(autoProgressRunnable)
        autoProgressAnimSet?.takeIf { it.isStarted }?.also {
            it.cancel()
            Log.d("WEBVIEW", ">>>>cancelAutoProgressAnim success>>>>")
        }
        autoProgressAnimSet = null
    }

    private fun cancelEndProcessAnim() {
        Log.d("WEBVIEW", ">>>>cancelEndProcessAnim >>>>")
        endProcessAnim?.takeIf { it.isStarted }?.also {
            it.cancel()
            Log.d("WEBVIEW", ">>>>cancelEndProcessAnim success>>>>")
        }
        endProcessAnim = null
        resetAlpha()
    }

    fun cancelAllAnim() {
        cancelEndProcessAnim()
        cancelAutoProgressAnim()
    }

    fun onWebViewPageProgressChanged(newProgress: Int) {
        if (NetworkUtils.isConnected()) {
            updateProgress(newProgress)
        } else {
            Toast.makeText(context, "Oops! Can not access the internet.", Toast.LENGTH_SHORT).show()
            if (progress == MIN_PROGRESS) {
                updateProgress(0)
            } else {
                cancelAllAnim()
            }
        }
    }

    @Synchronized
    private fun updateProgress(progress: Int) {
        Log.d("WEBVIEW", ">>>>updateProgress progress=$progress")
        Log.d("WEBVIEW", ">>>>updateProgress reloading=$reloading")
        Log.d("WEBVIEW", ">>>>updateProgress goBack=$goBack")
        if (reloading || goBack) {
            if (progress == 100 && goBack) {
                Log.d("WEBVIEW", ">>>>goBack And progress=$progress")
                goBack = false
            }
            return
        }

        val progressVal = if (progress == 0 || progress > MIN_PROGRESS) progress else MIN_PROGRESS
        Log.d("WEBVIEW", ">>>>updateProgress progressVal=$progressVal")
        Log.d("WEBVIEW", ">>>>updateProgress getProgress()=${getProgress()}")
        if (progressVal == getProgress()) {
            return
        }

        cancelEndProcessAnim()

        autoProgressAnimSet?.let {
            if (it.isStarted) {
                if (progressVal < 100) {
                    return
                }
                cancelAutoProgressAnim()
            } else {
                if (progressVal >= 60 && getProgress() == 100) {
                    return
                }
            }
        }

        Log.d("WEBVIEW", ">>>>setProgress=$progressVal>>>>")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            setProgress(progressVal, progressVal > getProgress())
        } else {
            setProgress(progressVal)
        }

        if (progressVal == 100) {
            Log.d("WEBVIEW", ">>>>start EndProcessAnim>>>>")
            endProcessAnim = prepareEndProcessAnim()?.also { it.start() }
        } else {
            //当进度条大于等于70%且 开始监测进度条是否卡住
            if (getProgress() >= 60) {
                Log.d("WEBVIEW", ">>>>start autoProgressRunnable>>>>")
                //去除前一个待执行动画
                removeCallbacks(autoProgressRunnable)
                cancelAutoProgressAnim()
                //卡住超过5秒 则启动自动进度动画
                postDelayed(autoProgressRunnable, 5_000)
            }
        }
    }

    private fun resetAlpha() {
        Log.d("WEBVIEW", ">>>resetAlpha alpha=$alpha>>>")
        if (alpha < 1f && progress < 100) {
            alpha = 1f
            Log.d("WEBVIEW", ">>>resetAlpha to 1f>>>")
        }
    }

    private var reloading = false

    fun onWebViewReload() {
        progress = 0
        reloading = true
        Log.d("WEBVIEW", ">>>> reload >>>>")
    }

    fun onWebViewPageStarted() {
        cancelAllAnim()
        reloading = false
        if (progress < MIN_PROGRESS || progress == 100) {
            updateProgress(MIN_PROGRESS)
        }
    }

    fun onWebViewPageFinished() {
        autoProgressAnimSet?.let {
            if (it.isStarted && progress < 95) {
                updateProgress(100)
            }
        }
    }

    /**
     * 控制网页返回后 是否加载进度条
     * TODO 暂时禁用该功能 待测试
     */
    private var goBack = false

    fun onWebViewPageGoBack() {
//        goBack = true
    }

    fun onLoadNewPage() {
//        goBack = false
    }

}