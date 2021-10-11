package com.tes.frezzmart.service.upload;

import android.graphics.Bitmap;

import com.cloudinary.android.preprocess.BitmapEncoder;
import com.cloudinary.android.preprocess.ImagePreprocessChain;
import com.cloudinary.android.preprocess.Limit;
import com.cloudinary.android.preprocess.PreprocessChain;
import com.cloudinary.android.preprocess.ResourceDecoder;
import com.cloudinary.android.preprocess.ResourceEncoder;

/**
 * Created by 5Mall<zhangwei> on 2019-04-25
 * Email:zhangwei@qingsongchou.com
 * 描述：
 */
public class MyImagePreprocessChain extends PreprocessChain<Bitmap> {
    /**
     * Convenience method for building an efficient dimension limiting chain using {@link MyBitmapDecoder} and {@link Limit}.
     * Use this in {@link com.cloudinary.android.UploadRequest#preprocess(PreprocessChain)}.
     * The scaling retains the original aspect ratio while guaranteeing the height and width are within the requested
     * maximum bounds. Note: If the image is already smaller it will be returned unchanged.
     *
     * @param maxWidth  The maximum width allowed. If the width of the image is greater, the image will be resized accordingly.
     * @param maxHeight The maximum height allowed. If the height of the image is greater, the image will be resized accordingly.
     * @return The prepared chain to pass on to {@link com.cloudinary.android.UploadRequest#preprocess(PreprocessChain)}
     */
    public static ImagePreprocessChain limitDimensionsChain(int maxWidth, int maxHeight, BitmapRotator bitmapRotator) {
        return (ImagePreprocessChain) new ImagePreprocessChain()
                .loadWith(new MyBitmapDecoder(maxWidth, maxHeight, bitmapRotator));
    }

    @Override
    protected ResourceEncoder<Bitmap> getDefaultEncoder() {
        return new BitmapEncoder();
    }

    @Override
    protected ResourceDecoder<Bitmap> getDefaultDecoder() {
        return new MyBitmapDecoder();
    }
}