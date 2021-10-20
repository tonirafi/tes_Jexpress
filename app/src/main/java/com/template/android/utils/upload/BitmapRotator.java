package com.template.android.utils.upload;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import androidx.exifinterface.media.ExifInterface;

import com.cloudinary.android.preprocess.Preprocess;
import com.template.android.utils.LogUtil;

import java.io.IOException;


public class BitmapRotator implements Preprocess<Bitmap> {

    private final int degree;

    public BitmapRotator(String imgPath) {
        this.degree = getRotateDegree(imgPath);
    }


    public Bitmap execute(Context context, Bitmap resource, boolean recycle) {
        if (degree == 0)
            return resource;

        Bitmap rotatedBitmap = null;
        try {
            Matrix matrix = new Matrix();
            matrix.postRotate(degree);
            rotatedBitmap = Bitmap.createBitmap(resource, 0, 0, resource.getWidth(), resource.getHeight(), matrix, false);
            if (resource != rotatedBitmap && recycle) {
                resource.recycle(); // 释放原来的bitmap
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            LogUtil.logE("ImageUploader", "execute rotate error:" + e.getLocalizedMessage());
        }

        return rotatedBitmap == null ? resource : rotatedBitmap;
    }

    @Override
    public Bitmap execute(Context context, Bitmap resource) {
        return execute(context, resource, true);
    }

    /**
     * 获取图片的旋转角度。
     * 只能通过原始文件获取，如果已经进行过bitmap操作无法获取。
     */
    private int getRotateDegree(String imgPath) {
        int result = 0;

        try {
            ExifInterface exif = new ExifInterface(imgPath);
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    result = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    result = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    result = 270;
                    break;
            }
        } catch (IOException e) {
            LogUtil.logE("ImageUploader", "do rotate error:" + e.getLocalizedMessage());
        }

        LogUtil.logD("ImageUploader", imgPath + " has rotate by $result degrees");
        return result;
    }
}
