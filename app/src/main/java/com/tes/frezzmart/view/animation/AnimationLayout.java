package com.tes.frezzmart.view.animation;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;

import com.tes.frezzmart.R;
import com.tes.frezzmart.ui.base.IAnimationLayout;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

/**
 * AnimationFragment
 * Created by smileCloud on 16/8/8.
 */
public class AnimationLayout extends IAnimationLayout {

    @BindView(R.id.image_animation)
    ImageView mAnimationView;
    @BindView(R.id.an_txt)
    TextView mAnTxt;

    @BindView(R.id.btn_reload)
    Button btnReload;

    @BindView(R.id.btn_back)
    Button btnBack;

    @BindView(R.id.guideline)
    Guideline guideline;

    @OnClick(R.id.btn_back)
    void back() {
        if (mListener != null)
            mListener.goBack();
    }

    @OnClick(R.id.btn_reload)
    void reload() {
        if (isRunning || mListener == null) {
            return;
        }
        if (exitWhenReload) {
            mListener.netErrorReload();
            ((ViewGroup) getParent()).removeView(this);
        } else {
            startAnimation();
            mListener.netErrorReload();
        }

    }

//    @OnClick(R.id.an_txt)
//    void switchV() {
//        netError(5000);
//        updateActionButton(false);
//    }

    public final String RUN_TEXT;
    public final String RUN_TEXT_500;
    public final String RUN_TEXT_200;
    public final String RUN_TEXT_OTHER;
    private AnimationListener mListener;
    private boolean isRunning;
    private boolean showGoBackBtn = false;
    private boolean showReloadBtn = false;
    private boolean exitWhenReload = false;
    private String[] autos = {"      ", " .    ", " . .  ", " . . ."};
    @NonNull
    private CompositeDisposable mCompositeDisposable;
    private AnimationDrawable mDrawable;

    public void setAnimationListener(@NotNull AnimationListener mListener) {
        this.mListener = mListener;
    }

    public AnimationLayout(Context context) {
        super(context);
        RUN_TEXT = context.getString(R.string.run_text);
        RUN_TEXT_500 = context.getString(R.string.run_text_500);
        RUN_TEXT_200 = context.getString(R.string.run_text_200);
        RUN_TEXT_OTHER = context.getString(R.string.run_text_other);
//        setGravity(Gravity.CENTER);
//        setOrientation(VERTICAL);
        setBackgroundColor(getResources().getColor(android.R.color.white));
//        setPadding(getPaddingLeft(), getResources().getDimensionPixelSize(R.dimen.status_bar_height), getPaddingRight(), getPaddingBottom());
        inflate(context, R.layout.layout_animation, this);
        ButterKnife.bind(this);
        mAnTxt.setText(String.format("%s%s", RUN_TEXT, autos[0]));
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    protected void onDetachedFromWindow() {
        stopAnimation();
        super.onDetachedFromWindow();
    }


    @Override
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * 动画开始
     */
    @Override
    public void startAnimation() {
        stopAnimation();
        if (!(mAnimationView.getBackground() instanceof AnimationDrawable)) {
            mAnTxt.setText(RUN_TEXT);
            mAnimationView.setBackgroundResource(R.drawable.loading_animation);
            hideActionButton();
        }

        if (getVisibility() != VISIBLE) {
            setVisibility(VISIBLE);
        }
        isRunning = true;
        mDrawable = (AnimationDrawable) mAnimationView.getBackground();
        mDrawable.start();
        mCompositeDisposable.add(
                Observable.interval(0, 1, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(aLong -> mAnTxt.setText(String.format("%s%s", RUN_TEXT, autos[(int) (aLong % 4)])),
                                throwable -> {
                                    Log.e("AnimationLayout", String.format("[autoTxt]Error:%s", throwable.getMessage()));
                                })
        );
    }

    /**
     * 停止动画
     */
    @Override
    public void stopAnimation() {
        isRunning = false;
        if (mDrawable != null && mDrawable.isRunning()) {
            mDrawable.stop();
        }
        mCompositeDisposable.clear();
    }

    /**
     * 动画的错误码
     */
    @Override
    public void netError(int code) {
        if (mAnimationView == null) {
            return;
        }
        stopAnimation();
        switch (code) {
            case 500:  //网络错误<设备没有连接因特网>
                mAnTxt.setText(RUN_TEXT_500);
//                mAnimationView.setBackgroundResource(R.drawable.net_error_500);
                updateActionButton();
                break;
            case 200: //服务器端错误
            case 201: //服务器端响应超时错误
                mAnTxt.setText(RUN_TEXT_200);
//                mAnimationView.setBackgroundResource(R.drawable.loading_failed);
                updateActionButton();
                break;
            case 401: //接口请求验证错误 提示用户重新登陆
                if (mListener != null) {
                    mListener.authError();
                }
            default: //其它都归纳为 请求错误
                mAnTxt.setText(RUN_TEXT_OTHER);
//                mAnimationView.setBackgroundResource(R.drawable.loading_failed);
                updateActionButton();
                break;
        }
    }

    public void setGuidelineRatio(float ratio) {
        guideline.setGuidelinePercent(ratio);
    }

    public float getGuidelineRatio() {
        return ((ConstraintLayout.LayoutParams) guideline.getLayoutParams()).guidePercent;
    }

    public void setActionButtonStatus(boolean showGoBackBtn, boolean showReloadBtn) {
        this.showGoBackBtn = showGoBackBtn;
        this.showReloadBtn = showReloadBtn;
    }

    public void updateActionButton() {
        btnBack.setVisibility(showGoBackBtn ? VISIBLE : GONE);
        btnReload.setVisibility(showReloadBtn ? VISIBLE : GONE);
    }

    public void hideActionButton() {
        btnBack.setVisibility(GONE);
        btnReload.setVisibility(GONE);
    }

    public void setExitWhenReload(boolean exitWhenReload) {
        this.exitWhenReload = exitWhenReload;
    }

    public static class Builder implements IAnimationLayout.Builder {
        private boolean showGoBackBtn = false;
        private boolean showReloadBtn = true;
        private boolean exitWhenReload = false;
        private float guidelineRatio = 0.38f;  //0.55f

        public Builder showGoBackBtn() {
            this.showGoBackBtn = true;
            return this;
        }

        public Builder hideReloadBtn() {
            this.showReloadBtn = false;
            return this;
        }

        public Builder exitWhenReload() {
            this.exitWhenReload = true;
            return this;
        }

        public Builder guidelineRatio(float ratio) {
            this.guidelineRatio = ratio;
            return this;
        }

        @NotNull
        @Override
        public IAnimationLayout build(@NotNull Context context) {
            AnimationLayout mAnimationLayout = new AnimationLayout(context);
            mAnimationLayout.setGuidelineRatio(guidelineRatio);
            mAnimationLayout.setExitWhenReload(exitWhenReload);
            mAnimationLayout.setActionButtonStatus(showGoBackBtn, showReloadBtn);
            return mAnimationLayout;
        }
    }
}
