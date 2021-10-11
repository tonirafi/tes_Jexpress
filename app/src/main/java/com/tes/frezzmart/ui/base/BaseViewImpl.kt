package com.tes.frezzmart.ui.base

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Looper
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.tes.frezzmart.router.RouterConstants
import com.tes.frezzmart.exception.ApiExceptionHelper
import com.tes.frezzmart.http.bean.Act
import com.tes.frezzmart.router.IntentUtil
import com.tes.frezzmart.utils.ToastUtil
import com.tes.frezzmart.view.CommonLoadingDialog
import com.tes.frezzmart.view.animation.AnimationLayout
import com.tes.frezzmart.view.animation.AnimationListener

class BaseViewImpl(private var root: ViewGroup?, private var animationListener: AnimationListener?) : BaseView, ViewModel() {

    private var loadingDialog: Dialog? = null
    private var animLayout: IAnimationLayout? = null

    fun recycle() {
        root?.removeCallbacks(null)
        root = null

        animLayout?.stopAnimation()
        animLayout = null

        animationListener = null
    }

    override fun getContext(): Context? = root?.context


    override fun login(requestCode: Int) {
        IntentUtil.intentToLogin(getContext(), requestCode)
    }

    override fun bindMobile(userId: String?, openId: String?) {
//        IntentUtil.intentToBindMobile(getContext(), openId, userId)
    }

    override fun setResult(resultCode: Int, data: Intent?) {

    }

    override fun showLoading(cancelableOnBack: Boolean, cancelableOnTouch: Boolean, dialog: Dialog?) {
        loadingDialog = when {
            getContext() == null || loadingDialog?.isShowing == true -> return
            dialog != null -> dialog
            loadingDialog == null -> CommonLoadingDialog(getContext())
            else -> loadingDialog
        }?.apply {
            setCanceledOnTouchOutside(cancelableOnTouch)
            setCancelable(cancelableOnBack)
            show()
        }
    }

    override fun hideLoading() {
        loadingDialog?.takeIf { it.isShowing }?.dismiss()
    }

    override fun isAnimationLayoutShowing(): Boolean {
        root?.let {
            val view = animLayout ?: return false
            return it.indexOfChild(view) != -1
        } ?: return false
    }

    override fun setAnimLayoutContainer(container: ViewGroup) {
        root?.let {
            hideAnimation()
        }
        this.root = container
    }

    override fun showAnimation(guidelineRatio: Float) {
        showAnimation(AnimationLayout.Builder().guidelineRatio(guidelineRatio))
    }

    override fun showAnimation(builder: IAnimationLayout.Builder?) {
        when {
            getContext() == null || isAnimationLayoutShowing() -> null
            builder != null -> builder.build(getContext()!!)
            animLayout != null -> animLayout
            else -> AnimationLayout.Builder().build(getContext()!!)
        }?.run {
            isClickable = true
            setAnimationListener(this@BaseViewImpl)
            root?.addView(this, 0, ViewGroup.LayoutParams(-1, -1))
            bringToFront()
            animLayout = this
            startAnimation()
        }
    }

    override fun hideAnimation() {
        animLayout?.let {
            it.stopAnimation()
            root?.removeView(it)
        }
    }

    override fun hideAnimationOrLoading(throwable: Throwable?, hintViewWhileError: Boolean, builder: IAnimationLayout.Builder?) {
        if (throwable == null) {
            if (isAnimationLayoutShowing()) {
                hideAnimation()
            } else {
                hideLoading()
            }
        } else if (hintViewWhileError) {
            netError(throwable, builder)
        } else {
            showMessage(throwable)
        }
    }

    override fun netError(throwable: Throwable, builder: IAnimationLayout.Builder?) {
        showAnimation(builder)
        animLayout?.netError(ApiExceptionHelper.getNetErrorCode(throwable))
        showMessage(throwable)
    }

    override fun showMessage(stringResId: Int) {
        showMessage(getContext()?.getString(stringResId))
    }

    override fun showMessage(throwable: Throwable) {
        showMessage(ApiExceptionHelper.getDisplayError(throwable))
    }

    override fun showMessage(message: String?) {
        val context = getContext() ?: return
        message?.let {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                ToastUtil.show(context, it)
            } else {
                this.root?.post {
                    ToastUtil.show(context, it)
                }
            }
        }
    }

    override fun onComplete() {
        recycle()
    }

    override fun parseIntent(intent: Intent?): Uri? {
        val extra = intent?.extras ?: return intent?.data

        return extra.keySet()?.asSequence()?.find {
            extra[it]?.toString()?.startsWith(RouterConstants.Common.SCHEME) ?: false
        }?.let { Uri.parse(extra[it]?.toString()) } ?: intent?.data
    }

    override fun popUpAct(act: Act) {
        getContext()?.let {
            if (it is AppCompatActivity) it.supportFragmentManager else null
        }?.let {
            ActDialogFragment().show(it, act)
        }
    }

    override fun netErrorReload() {
        animationListener?.netErrorReload()
    }

    override fun goBack() {
        animationListener?.goBack()
    }

    override fun authError(): Boolean {
        //首先交给View处理 如果View已处理 则此处不作任何处理
        return (animationListener?.authError() == true).takeUnless { it }?.also {
            //如果View未监听或者未处理 则默认走下方处理逻辑
//            Passport.instance.signOut(false)
            login()
            !it
        } ?: true
    }


}