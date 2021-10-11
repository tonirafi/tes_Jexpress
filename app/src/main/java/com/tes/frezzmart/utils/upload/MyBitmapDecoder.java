package com.tes.frezzmart.utils.upload;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import com.cloudinary.android.payload.Payload;
import com.cloudinary.android.payload.PayloadNotFoundException;
import com.cloudinary.android.preprocess.Limit;
import com.cloudinary.android.preprocess.PayloadDecodeException;
import com.cloudinary.android.preprocess.ResourceDecoder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Decodes a bitmap from a payload. If given width and height the decoding process will take them
 * into account to decode the bitmap efficiently but will not necessarily resize the bitmap.
 */
public class MyBitmapDecoder implements ResourceDecoder<Bitmap> {

    private final int width;
    private final int height;
    private final BitmapRotator bitmapRotator;

    /**
     * Create a new decoder.
     */
    public MyBitmapDecoder() {
        this(Integer.MAX_VALUE, Integer.MAX_VALUE, null);
    }

    /**
     * Create a new decoder taking the required width and height into account to decode the bitmap efficiently.
     *
     * @param width  Required width
     * @param height Required height
     */
    public MyBitmapDecoder(int width, int height, BitmapRotator bitmapRotator) {
        this.width = width;
        this.height = height;
        this.bitmapRotator = bitmapRotator;
    }

    /**
     * Decodes a bitmap from the given payload. If the bitmap is at least two times larger than the required
     * dimensions it will decode a version scaled down by a factor. Note: The dimensions of the decoded bitmap
     * will not necessarily be equal to {@link MyBitmapDecoder#width} and {@link MyBitmapDecoder#height}. For
     * exact resizing combine this decoder with {@link Limit} processing step, or use
     * {@link MyImagePreprocessChain#limitDimensionsChain(int, int, BitmapRotator)}.
     *
     * @param context Android context.
     * @param payload Payload to extract the bitmap from
     * @return The decoded bitmap
     * @throws PayloadNotFoundException if the payload is not found
     * @throws PayloadDecodeException   if the payload exists but cannot be decoded
     */
    @Override
    public Bitmap decode(Context context, Payload payload) throws PayloadNotFoundException, PayloadDecodeException {
        synchronized (MyBitmapDecoder.class) {
            return bitmapFromPayload(context, payload, width, height);
        }
    }

    private Bitmap bitmapFromPayload(Context context, Payload payload, int width, int height) throws PayloadNotFoundException, PayloadDecodeException {
        Object resource = payload.prepare(context);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = null;
        if (resource instanceof File) {
            String filePath = ((File) resource).getPath();
            BitmapDrawable bitmapCache = Utils.getBitmapFromCache(filePath);
            if (bitmapCache == null) {
                BitmapFactory.decodeFile(filePath, options);
                options.inSampleSize = Utils.calculateInSampleSize(options, width, height);
                options.inJustDecodeBounds = false;

                Utils.addInBitmapOptions(options);

                bitmap = BitmapFactory.decodeFile(filePath, options);
                bitmap = preprocessAndCache(context, filePath, bitmap, false);
            } else {
                bitmap = bitmapCache.getBitmap();
            }

        } else if (resource instanceof InputStream) {
            InputStream is = null;
            try {
                String key = resource.toString();
                BitmapDrawable bitmapCache = Utils.getBitmapFromCache(key);
                if (bitmapCache == null) {
                    BitmapFactory.decodeStream((InputStream) resource, null, options);
                    options.inSampleSize = Utils.calculateInSampleSize(options, width, height);
                    options.inJustDecodeBounds = false;
                    is = (InputStream) payload.prepare(context);

                    Utils.addInBitmapOptions(options);

                    bitmap = BitmapFactory.decodeStream(is, null, options);
                    bitmap = preprocessAndCache(context, key, bitmap, true);
                } else {
                    bitmap = bitmapCache.getBitmap();
                }
            } finally {
                try {
                    ((InputStream) resource).close();
                } catch (IOException ignored) {
                }
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException ignored) {
                }
            }
        } else if (resource instanceof byte[]) {
            byte[] data = (byte[]) resource;
            String key = resource.toString();
            BitmapDrawable bitmapCache = Utils.getBitmapFromCache(key);
            if (bitmapCache == null) {
                BitmapFactory.decodeByteArray(data, 0, data.length, options);
                options.inSampleSize = Utils.calculateInSampleSize(options, width, height);
                options.inJustDecodeBounds = false;

                Utils.addInBitmapOptions(options);

                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
                bitmap = preprocessAndCache(context, key, bitmap, true);
            } else {
                bitmap = bitmapCache.getBitmap();
            }
        }

        if (bitmap == null) {
            throw new PayloadDecodeException();
        }

        return bitmap;
    }

    private Bitmap preprocessAndCache(Context context, String key, Bitmap bitmap, boolean recycle) {
        if (bitmap != null) {
            //现原图在处理前加入可复用软引用列表 以用于后续decode操作 opt设置inBitmap属性 让后续bitmap可复用此bitmap内存空间
            if (!recycle) {
                Utils.addBitmapToReusableList(bitmap);
            }

            Bitmap limitedBitmap = adjustToLimit(bitmap, width, height, recycle);

            if (bitmapRotator != null) {
                bitmap = bitmapRotator.execute(context, limitedBitmap, recycle || limitedBitmap != bitmap);
            }

            Utils.addBitmapToCache(key,
                    recycle ? new RecyclingBitmapDrawable(context.getResources(), bitmap)
                            : new BitmapDrawable(context.getResources(), bitmap));
        }

        return bitmap;
    }

    private Bitmap adjustToLimit(Bitmap resource, int width, int height, boolean recycle) {
        if (resource.getWidth() > width || resource.getHeight() > height) {
            double widthRatio = (double) width / resource.getWidth();
            double heightRatio = (double) height / resource.getHeight();
            Bitmap limitedBitmap;
            if (heightRatio > widthRatio) {
                limitedBitmap = Bitmap.createScaledBitmap(resource, width, (int) Math.round(widthRatio * resource.getHeight()), true);
            } else {
                limitedBitmap = Bitmap.createScaledBitmap(resource, (int) Math.round(heightRatio * resource.getWidth()), height, true);
            }

            if (resource != limitedBitmap && recycle) {
                resource.recycle();
            }

            return limitedBitmap;
        }

        return resource;
    }

}
