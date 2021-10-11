package com.tes.frezzmart.service.upload;


import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build.VERSION_CODES;

import androidx.annotation.NonNull;
import androidx.collection.LruCache;

import android.util.Log;


import com.tes.frezzmart.BuildConfig;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class ImageMemoryCache {
    private static final String TAG = "ImageMemoryCache";

    // Default memory cache size in kilobytes
    private static final int DEFAULT_MEM_CACHE_SIZE = 5 * 1024 * 1024; // 5MB

    private LruCache<String, BitmapDrawable> mMemoryCache;
    private Set<SoftReference<Bitmap>> mReusableBitmaps;

    public ImageMemoryCache() {
        this(DEFAULT_MEM_CACHE_SIZE);
    }

    public ImageMemoryCache(int memCacheSize) {

        // If we're running on Honeycomb or newer, create a set of reusable bitmaps that can be
        // populated into the inBitmap field of BitmapFactory.Options. Note that the set is
        // of SoftReferences which will actually not be very effective due to the garbage
        // collector being aggressive clearing Soft/WeakReferences. A better approach
        // would be to use a strongly references bitmaps, however this would require some
        // balancing of memory usage between this set and the bitmap LruCache. It would also
        // require knowledge of the expected size of the bitmaps. From Honeycomb to JellyBean
        // the size would need to be precise, from KitKat onward the size would just need to
        // be the upper bound (due to changes in how inBitmap can re-use bitmaps).
        if (Utils.hasHoneycomb()) {
            mReusableBitmaps =
                    Collections.synchronizedSet(new HashSet<>());
        }

        mMemoryCache = new LruCache<String, BitmapDrawable>(memCacheSize) {

            /**
             * Notify the removed entry that is no longer being cached
             */
            @Override
            protected void entryRemoved(boolean evicted, @NonNull String key,
                                        @NonNull BitmapDrawable oldValue, BitmapDrawable newValue) {
                if (oldValue instanceof RecyclingBitmapDrawable) {
                    // The removed entry is a recycling drawable, so notify it
                    // that it has been removed from the memory cache
                    ((RecyclingBitmapDrawable) oldValue).setIsCached(false);
                } else {
                    // The removed entry is a standard BitmapDrawable
                    if (Utils.hasHoneycomb()) {
                        // We're running on Honeycomb or later, so add the bitmap
                        // to a SoftReference set for possible use with inBitmap later
//                        mReusableBitmaps.add(new SoftReference<>(oldValue.getBitmap()));
                        addBitmapToReusableList(oldValue.getBitmap());
                    }
                }
            }

            /**
             * Measure item size in kilobytes rather than units which is more practical
             * for a bitmap cache
             */
            @Override
            protected int sizeOf(@NonNull String key, @NonNull BitmapDrawable value) {
                final int bitmapSize = getBitmapSize(value);
                return bitmapSize == 0 ? 1 : bitmapSize;
            }
        };

    }

    /**
     * 将位图可复用软引用列表 以用于decode opt inBitmap属性 复用内存空间
     *
     * @param bitmap
     */
    public void addBitmapToReusableList(Bitmap bitmap) {
        if (mReusableBitmaps != null && bitmap != null && !bitmap.isRecycled()) {
            mReusableBitmaps.add(new SoftReference<>(bitmap));
        }
    }

    /**
     * Adds a bitmap to both memory and disk cache.
     *
     * @param key   Unique identifier for the bitmap to store
     * @param value The bitmap drawable to store
     */
    public void addBitmapToCache(String key, BitmapDrawable value) {
        //BEGIN_INCLUDE(add_bitmap_to_cache)
        if (key == null || value == null || value.getBitmap() == null) {
            return;
        }

        if (value.getBitmap().isRecycled()) {
            mMemoryCache.remove(key);
            return;
        }

        // Add to memory cache
        if (mMemoryCache != null) {
            if (value instanceof RecyclingBitmapDrawable) {
                // The removed entry is a recycling drawable, so notify it
                // that it has been added into the memory cache
                ((RecyclingBitmapDrawable) value).setIsCached(true);
            }

            /**
             * 规避异常：java.lang.IllegalStateException:xxx.sizeOf() is reporting inconsistent results 异常
             */
            int size = getBitmapSize(value);
            if (size > 0 && (size + mMemoryCache.size()) < mMemoryCache.maxSize()) {
                mMemoryCache.put(key, value);
            }
        }
        //END_INCLUDE(add_bitmap_to_cache)
    }

    /**
     * Get from memory cache.
     *
     * @param key Unique identifier for which item to get
     * @return The bitmap drawable if found in cache, null otherwise
     */
    public BitmapDrawable getBitmapFromCache(String key) {
        //BEGIN_INCLUDE(get_bitmap_from_mem_cache)
        BitmapDrawable memValue = null;

        if (mMemoryCache != null) {
            memValue = mMemoryCache.get(key);
        }

        if (BuildConfig.DEBUG && memValue != null) {
            Log.d(TAG, "Memory cache hit");
        }

        if (memValue != null) {
            if (memValue.getBitmap() == null || memValue.getBitmap().isRecycled()) {
                mMemoryCache.remove(key);
                memValue = null;
            }
        }

        return memValue;
        //END_INCLUDE(get_bitmap_from_mem_cache)
    }

    /**
     * Clears both the memory and disk cache associated with this ImageCache object. Note that
     * this includes disk access so this should not be executed on the main/UI thread.
     */
    public void clearCache() {
        if (mMemoryCache != null) {
            mMemoryCache.evictAll();
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Memory cache cleared");
            }
        }
    }

    /**
     * @param options - BitmapFactory.Options with out* options populated
     * @return Bitmap that case be used for inBitmap
     */
    protected Bitmap getBitmapFromReusableSet(BitmapFactory.Options options) {
        //BEGIN_INCLUDE(get_bitmap_from_reusable_set)
        Bitmap bitmap = null;

        if (mReusableBitmaps != null && !mReusableBitmaps.isEmpty()) {
            synchronized (mReusableBitmaps) {
                final Iterator<SoftReference<Bitmap>> iterator = mReusableBitmaps.iterator();
                Bitmap item;

                while (iterator.hasNext()) {
                    item = iterator.next().get();

                    if (null != item && item.isMutable() && !item.isRecycled()) {
                        // Check to see it the item can be used for inBitmap
                        if (canUseForInBitmap(item, options)) {
                            bitmap = item;

                            // Remove from reusable set so it can't be used again
                            iterator.remove();
                            break;
                        }
                    } else {
                        // Remove from the set if the reference has been cleared.
                        iterator.remove();
                    }
                }
            }
        }

        return bitmap;
        //END_INCLUDE(get_bitmap_from_reusable_set)
    }


    /**
     * @param candidate     - Bitmap to check
     * @param targetOptions - Options that have the out* value populated
     * @return true if <code>candidate</code> can be used for inBitmap re-use with
     * <code>targetOptions</code>
     */
    @TargetApi(VERSION_CODES.KITKAT)
    private static boolean canUseForInBitmap(
            Bitmap candidate, BitmapFactory.Options targetOptions) {
        //BEGIN_INCLUDE(can_use_for_inbitmap)
        if (Utils.hasKitKat()) {
            // From Android 4.4 (KitKat) onward we can re-use if the byte size of
            // the new bitmap is smaller than the reusable bitmap candidate
            // allocation byte count.
            int width = targetOptions.outWidth / targetOptions.inSampleSize;
            int height = targetOptions.outHeight / targetOptions.inSampleSize;
            int byteCount = width * height * getBytesPerPixel(candidate.getConfig());
            return byteCount <= candidate.getAllocationByteCount();
        }

        // On earlier versions, the dimensions must match exactly and the inSampleSize must be 1
        return candidate.getWidth() == targetOptions.outWidth
                && candidate.getHeight() == targetOptions.outHeight
                && targetOptions.inSampleSize == 1;
        //END_INCLUDE(can_use_for_inbitmap)
    }

    /**
     * Return the byte usage per pixel of a bitmap based on its configuration.
     *
     * @param config The bitmap configuration.
     * @return The byte usage per pixel.
     */
    private static int getBytesPerPixel(Config config) {
        if (config == Config.ARGB_8888) {
            return 4;
        } else if (config == Config.RGB_565) {
            return 2;
        } else if (config == Config.ARGB_4444) {
            return 2;
        } else if (config == Config.ALPHA_8) {
            return 1;
        }
        return 1;
    }


    private static String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * Get the size in bytes of a bitmap in a BitmapDrawable. Note that from Android 4.4 (KitKat)
     * onward this returns the allocated memory size of the bitmap which can be larger than the
     * actual bitmap data byte count (in the case it was re-used).
     *
     * @param value
     * @return size in bytes
     */
    @TargetApi(VERSION_CODES.KITKAT)
    public static int getBitmapSize(BitmapDrawable value) {
        Bitmap bitmap = value.getBitmap();

        // From KitKat onward use getAllocationByteCount() as allocated bytes can potentially be
        // larger than bitmap byte count.
        if (Utils.hasKitKat()) {
            return bitmap.getAllocationByteCount();
        }

        if (Utils.hasHoneycombMR1()) {
            return bitmap.getByteCount();
        }

        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();
    }


}