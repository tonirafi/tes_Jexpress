package com.template.project.service.upload;

import com.template.project.service.upload.image.ImageUploadingListener;
import com.template.project.service.upload.image.IUpdateView;
import com.template.project.service.upload.image.bean.UploadBean;
import com.template.project.utils.LogUtil;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

public class ImageUploadCallback<T extends IUpdateView> implements ImageUploadingListener, IUpdateView {

    WeakReference<T> viewRel;

    public ImageUploadCallback(T view) {
        this.viewRel = new WeakReference<>(view);
    }

    @Override
    public void onSuccess(@NotNull UploadBean uploadBean) {
        LogUtil.logD("IMAGE_UPLOAD", String.format("success:url=%s", uploadBean.getOriginalImageUrl()));
        LogUtil.logD("IMAGE_UPLOAD", String.format("success:thumbUrl=%s", uploadBean.getEagerThumbUrl()));
        LogUtil.logD("IMAGE_UPLOAD", String.format("success:watermarkUrl=%s", uploadBean.getEagerImageUrl()));
        update(uploadBean);
    }

    @Override
    public void onFail(@NotNull UploadBean uploadBean) {
        LogUtil.logD("IMAGE_UPLOAD", String.format("onFail:error=%s", uploadBean.getErrorInfo()));
        update(uploadBean);
    }

    @Override
    public void onProgress(@NotNull UploadBean uploadBean) {
        LogUtil.logD("IMAGE_UPLOAD", String.format("onProgress:%s", uploadBean.getProgress()));
        update(uploadBean);
    }

    @Override
    public void update(UploadBean uploadBean) {
        IUpdateView view = viewRel.get();
        if (view == null) return;

        LogUtil.logD("IMAGE_UPLOAD", "===update view ===" + view.getClass().getSimpleName());
        view.update(uploadBean);
    }
}
