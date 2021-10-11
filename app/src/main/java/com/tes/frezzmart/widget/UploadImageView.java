package com.tes.frezzmart.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.tes.frezzmart.R;
import com.tes.frezzmart.service.upload.ImageUploadCallback;
import com.tes.frezzmart.service.upload.ImageUploadManager;
import com.tes.frezzmart.service.upload.image.IUpdateView;
import com.tes.frezzmart.service.upload.image.ImageUploadStatus;
import com.tes.frezzmart.service.upload.image.bean.ImageBean;
import com.tes.frezzmart.service.upload.image.bean.UploadBean;
import com.tes.frezzmart.utils.PicassoUtil;

import java.io.File;

public class UploadImageView extends RelativeLayout implements View.OnClickListener, IUpdateView {

    public UploadImageView(Context context) {
        this(context, null);
    }

    public UploadImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UploadImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UploadImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }


    ImageView ivUploadUnitSrc;
    ProgressBar progressBar;
    TextView tvUploadUnitBottomText;
    ImageView ivUploadDel;

    ImageBean imageBean;
    private String successText;
    View ll_retry_unit;

    public void setDefaultCover(@DrawableRes int defaultCover) {
        if (ivUploadUnitSrc != null) {
            ivUploadUnitSrc.setImageDrawable(null);
//            ivUploadUnitSrc.setScaleType(ImageView.ScaleType.FIT_XY);
            ivUploadUnitSrc.setBackgroundResource(defaultCover);
            ivUploadDel.setVisibility(GONE);
        }
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.UploadImageView);
        int progressBarStyle = a.getInt(R.styleable.UploadImageView_progressBarViewStyle, 0);
        Drawable d = a.getDrawable(R.styleable.UploadImageView_defaultCover);
        a.recycle();

        inflate(context, R.layout.view_upload_image_view, this);
        ivUploadUnitSrc = findViewById(R.id.iv_upload_unit_src);
        ll_retry_unit = findViewById(R.id.ll_retry_unit);
        progressBar = findViewById(progressBarStyle == 0 ? R.id.progressBar : R.id.progressBarCircle);
        tvUploadUnitBottomText = findViewById(R.id.tv_upload_unit_bottom_text);
        ivUploadDel = findViewById(R.id.iv_upload_unit_del);

        if (d != null) {
//            ivUploadUnitSrc.setImageDrawable(d);
            ivUploadUnitSrc.setBackground(d);
        }

        setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        ivUploadDel.setOnClickListener(this);
        ivUploadUnitSrc.setOnClickListener(this);
        ll_retry_unit.setOnClickListener(this);
    }


    public void setSuccessText(String successText) {
        this.successText = successText;
        updateBottomTextStatus();

    }

    private void updateBottomTextStatus() {
        if (TextUtils.isEmpty(successText) || imageBean == null || imageBean.failed()) {
            tvUploadUnitBottomText.setVisibility(GONE);
        } else {
            tvUploadUnitBottomText.setVisibility(VISIBLE);
            tvUploadUnitBottomText.setText(successText);
        }
    }


    /**
     * 刷新状态
     */
    private void update() {

        updateBottomTextStatus();

        if (imageBean == null) return;

        if (imageBean.needUpload()) {

            if (disPlayLocal(imageBean.getImgPath())) {
                if (imageBean.isNotYetUploadedLocalBean()) {
                    progressBar.setProgress(1);
                    progressBar.setVisibility(VISIBLE);
                    ll_retry_unit.setVisibility(GONE);
                    imageBean.setUploadStatus(ImageUploadStatus.UPLOADING.ordinal());
                    ImageUploadManager.getInstance(getContext()).uploadImage(imageBean, new ImageUploadCallback<>(this));
                    return;
                }
            }

            if (!TextUtils.isEmpty(imageBean.getImageUrl())) {
                imageBean.setUploadStatus(ImageUploadStatus.SUCCESS.ordinal());
            } else {
                imageBean.setUploadStatus(ImageUploadStatus.FAILED.ordinal());
            }

        }

        if (imageBean.getUploadStatus() == ImageUploadStatus.FAILED.ordinal()) {
            ll_retry_unit.setVisibility(VISIBLE);
            progressBar.setVisibility(GONE);
            display(imageBean);
        } else if (imageBean.getUploadStatus() == ImageUploadStatus.SUCCESS.ordinal()) {
            ll_retry_unit.setVisibility(GONE);
            progressBar.setVisibility(GONE);
            //todo 是否需要吐司提示错误信息 ？
            display(imageBean);
            //ivUploadBg背景需要外部处理
        } else if (imageBean.getUploadStatus() == ImageUploadStatus.UPLOADING.ordinal()) {
            ll_retry_unit.setVisibility(GONE);
            progressBar.setVisibility(VISIBLE);
            progressBar.setProgress((int) imageBean.getProgress());
            display(imageBean);
        }

    }

    private void display(ImageBean imageBean) {
        String picturePath = imageBean.getImgPath();
        if (!TextUtils.isEmpty(picturePath)) {
            disPlayLocal(picturePath);
        } else if (!TextUtils.isEmpty(imageBean.getImgThumb())) {
            PicassoUtil.load(imageBean.getCompatibleThumb())
                    .centerCrop().fit()
                    .into(ivUploadUnitSrc);
        } else if (!TextUtils.isEmpty(imageBean.getImageUrl())) {
            PicassoUtil.load(imageBean.getCompatibleImageUrl())
                    .centerCrop().fit()
                    .into(ivUploadUnitSrc);
        }

    }


    private boolean disPlayLocal(String picturePath) {

        if (!TextUtils.isEmpty(picturePath)) {
            if (!picturePath.equals(ivUploadUnitSrc.getTag())) {
                File f = new File(picturePath);
                if (f.exists()) {
                    Uri uri = Uri.fromFile(f);
                    if (uri != null) {
                        PicassoUtil.load(uri.toString()).centerCrop().fit().into(ivUploadUnitSrc);
                        ivUploadUnitSrc.setTag(picturePath);
                        return true;
                    }
                }

                imageBean.setImgPath(null);
            } else
                return true;
        }

        return false;
    }

    public void addImageBean(final ImageBean imageBean) {
        this.imageBean = imageBean;
        ivUploadUnitSrc.post(runnable);
    }

    private Runnable runnable = this::update;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_upload_unit_src:
                if (imageBean != null) {
                    if (imageBean.getUploadStatus() == ImageUploadStatus.FAILED.ordinal()) {
                        ImageUploadManager.getInstance(getContext()).uploadImage(imageBean, new ImageUploadCallback<>(this));
//                    } else if (imageBean.getUploadStatus() == ImageUploadStatus.SUCCESS.ordinal()) {
                    } else {
                        if (imageListener != null) {
                            imageListener.preview(imageBean);
                        }

                    }
                } else {
                    if (imageListener != null) {
                        imageListener.selectPhoto();
                    }
                }
                break;
            case R.id.iv_upload_unit_del:
                if (imageListener != null) {
                    imageListener.remove(imageBean);
                }
                break;
            case R.id.ll_retry_unit:
                imageBean.reset();
                update();
                break;
        }
    }

    ImageListener imageListener;

    public void setImageListener(ImageListener imageListener) {
        this.imageListener = imageListener;
    }



    public interface ImageListener {

        void preview(ImageBean imageBean);

        void remove(ImageBean imageBean);

        void selectPhoto();
    }

    public void onDestroy() {
        if (imageBean != null) {
            ImageUploadManager.getInstance(getContext()).cancelUploadImage(imageBean);
        }

        if (getHandler() != null)
            getHandler().removeCallbacksAndMessages(null);

    }

    @Override
    public void update(UploadBean uploadBean) {
        if (imageBean != null && imageBean.update(uploadBean)) {
            update();
        }
    }

}
