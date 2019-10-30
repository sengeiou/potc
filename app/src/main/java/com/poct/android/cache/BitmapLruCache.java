package com.poct.android.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by xpx on 2017/6/7.
 */

public class BitmapLruCache extends LruCache<String, Bitmap> {

    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */
    public BitmapLruCache(int maxSize) {
        super(maxSize);
    }


    @Override
    protected int sizeOf(String key, Bitmap bitmap) {
        // 重写此方法来衡量每张图片的大小，默认返回图片数量。

        int a = 0;
        if(bitmap != null)
        {
            a = bitmap.getByteCount();
        }
        long b = this.maxSize();
        long c = this.size();
        return a;
    }

    @Override
    // 当缓存被移除时调用，第一个参数是表明缓存移除的原因，true表示被LruCache移除，false表示被主动remove移除，可不重写
    protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap
            newValue) {
        if(oldValue != null)
        {
            if (!oldValue.isRecycled()) {
                oldValue.recycle();
                oldValue = null;
                System.gc();
            }
        }
        super.entryRemoved(evicted, key, oldValue, newValue);

    }


}
