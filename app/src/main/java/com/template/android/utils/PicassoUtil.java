package com.template.android.utils;

import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.template.android.R;
import com.template.android.utils.upload.image.bean.ImageBean;

public class PicassoUtil {
    private static String TAG = "PicassoTag";

    public static RequestCreator loadRaw(@DrawableRes int resourceId) {
        return Picasso.get().load(resourceId);
    }

    public static RequestCreator loadRaw(String path) {

        if (TextUtils.isEmpty(path)) {
            return Picasso.get().load(R.drawable.empty_logo);
        }

        Uri uri = Uri.parse(path);
        //本地资源 通过res:///resId 引用
        if ("res".equals(uri.getScheme()) && !TextUtils.isEmpty(uri.getLastPathSegment())) {
            try {
                int resId = Integer.parseInt(uri.getLastPathSegment());
                return Picasso.get().load(resId);
            } catch (NumberFormatException e) {
            }
        }

        return Picasso.get().load(path).tag(TAG);
    }

    public static RequestCreator load(ImageBean imageBean) {
        return load(imageBean, false);
    }

    public static RequestCreator load(ImageBean imageBean, boolean rectangular) {
        String path = null;
        if (imageBean != null) {
            path = TextUtils.isEmpty(imageBean.getImgPath()) ? imageBean.getCompatibleImageUrl() : imageBean.getImgPath();
        }
        return load(path, rectangular);
    }


    public static RequestCreator load(String path) {
        return load(path, false);
    }


    public static RequestCreator load(String path, boolean rectangular) {
        int resId = rectangular ? R.drawable.ll_divider_gray_1dp : R.drawable.ll_divider_gray_1dp;
        return load(path, resId, resId);
    }

    public static RequestCreator loadAvatar(String path) {
        return load(path, R.drawable.ll_divider_gray_1dp, R.drawable.ll_divider_gray_1dp).centerCrop().fit();
    }

    public static RequestCreator load(String path, @DrawableRes int placeholderResId, @DrawableRes int errorResId) {
        return loadRaw(TextUtils.isEmpty(path) ? "res:///" + placeholderResId : path)
                .placeholder(placeholderResId)
                .error(errorResId);
    }

    public static RequestCreator load(Object path) {
        return loadIfDrawableRes(path, load(String.valueOf(path)));
    }

    public static RequestCreator load(Object path, boolean rectangular) {
        return loadIfDrawableRes(path, load(String.valueOf(path), rectangular));
    }

    public static RequestCreator loadAvatar(Object path) {
        return loadIfDrawableRes(path, loadAvatar(String.valueOf(path)));
    }

    private static RequestCreator loadIfDrawableRes(Object imageUrl, RequestCreator requestCreator) {
        if (imageUrl instanceof Integer) {
            return loadRaw(Integer.parseInt(String.valueOf(imageUrl)));
        }

        return requestCreator;
    }

    public static void pauseTag() {
        pauseTag(TAG);
    }

    public static void pauseTag(@NonNull Object tag) {
        Picasso.get().pauseTag(tag);
    }

    public static void resumeTag() {
        resumeTag(TAG);
    }

    public static void resumeTag(@NonNull Object tag) {
        Picasso.get().resumeTag(tag);
    }

}
