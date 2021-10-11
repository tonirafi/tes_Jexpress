package com.tes.frezzmart.utils.upload;

/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import androidx.annotation.DrawableRes;



/**
 * Class containing some static utility methods.
 */
@SuppressLint("ObsoleteSdkInt")
public class Utils {
    private Utils() {
    }

    private static ImageMemoryCache imageMemoryCache = new ImageMemoryCache();

    public static void clearImageMemoryCache() {
        imageMemoryCache.clearCache();
    }

    static BitmapDrawable getBitmapFromCache(String key) {
        return imageMemoryCache.getBitmapFromCache(key);
    }

    static void addBitmapToCache(String key, BitmapDrawable value) {
        imageMemoryCache.addBitmapToCache(key, value);
    }

    static void addBitmapToReusableList(Bitmap bitmap) {
        imageMemoryCache.addBitmapToReusableList(bitmap);
    }

    static boolean addInBitmapOptions(BitmapFactory.Options options) {

        if (!hasHoneycomb()) return false;

        // inBitmap only works with mutable bitmaps, so force the decoder to
        // return mutable bitmaps.
        options.inMutable = true;

        // Try to find a bitmap to use for inBitmap.
        Bitmap inBitmap = imageMemoryCache.getBitmapFromReusableSet(options);

        if (inBitmap != null) {
            // If a suitable bitmap has been found, set it as the value of
            // inBitmap.
            options.inBitmap = inBitmap;
        }

        return inBitmap != null;
    }

    // Google reference method, see https://developer.android.com/topic/performance/graphics/load-bitmap.html
    static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        if (reqWidth == 0 && reqHeight == 0) {
            return 1;
        }
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            //长或者宽 任一满足条件 即进行采样处理
            while ((halfHeight / inSampleSize) >= reqHeight
                    || (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    public static synchronized Bitmap decodeResource(Context context, @DrawableRes int resId, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeResource(context.getResources(), resId, options);
        options.inSampleSize = calculateInSampleSize(options, width, height);
        options.inJustDecodeBounds = false;

        //原始options 保持着原始图片的长宽数据 用这个options从可复用的bitmap列表中找寻可用的bitmap赋值给Inbitmap 供新bitmap复用其内存空间
        addInBitmapOptions(options);

        //如果资源所在drawable文件夹inDensity与所要加载的目标设备inTargetDensity 不一致 则图片会按照ratio=inTargetDensity/inDensity
        // 缩放处理 所生成的bitmap尺寸(a,b) 会变为(a*ratio,b*ratio)
        if (options.inTargetDensity != options.inDensity) {
            options.inTargetDensity = options.inDensity;
        }

        //decode之后 options中的长宽值会变为按照ratio缩放处理之后的长宽  因为options发生了变化
        //导致同一个资源在此加载的时候 无法满足inbitmap要求 而资源内存地址不能得以复用 所以需要在decode之前让ratio为1
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);
        addBitmapToReusableList(bitmap);
        return bitmap;
    }

    public static boolean hasFroyo() {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed behavior.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }
}
