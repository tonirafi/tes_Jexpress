package com.template.android.utils.upload;

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

            //???????????? ?????????????????? ?????????????????????
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

        //??????options ???????????????????????????????????? ?????????options???????????????bitmap????????????????????????bitmap?????????Inbitmap ??????bitmap?????????????????????
        addInBitmapOptions(options);

        //??????????????????drawable?????????inDensity??????????????????????????????inTargetDensity ????????? ??????????????????ratio=inTargetDensity/inDensity
        // ???????????? ????????????bitmap??????(a,b) ?????????(a*ratio,b*ratio)
        if (options.inTargetDensity != options.inDensity) {
            options.inTargetDensity = options.inDensity;
        }

        //decode?????? options??????????????????????????????ratio???????????????????????????  ??????options???????????????
        //?????????????????????????????????????????? ????????????inbitmap?????? ??????????????????????????????????????? ???????????????decode?????????ratio???1
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
