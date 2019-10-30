package com.poct.android.cache;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.StatFs;

import com.poct.android.utils.AppUtils;
import com.poct.android.view.PoctApplication;

import java.io.File;

public class CacheManager {

    public static BitmapLruCache mMemoryCache;
    private static CacheManager cacheMananger;
    public static final String APP_PATH = Environment
            .getExternalStorageDirectory().getPath() + "/doctortest";
    public static boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }


    public CacheManager(Context mContext) {
        int maxsize = ((ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE)).getLargeMemoryClass();
        int memClass = ((ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE)).getLargeMemoryClass() * 2/ 3;
        mMemoryCache = new BitmapLruCache(memClass * 1024 * 1024);
    }
    public static synchronized CacheManager getInstance(Context mContext) {
        if (cacheMananger == null) {
            cacheMananger = new CacheManager(mContext);
        }

        return cacheMananger;
    }

    public Bitmap getBitMapLoacl(File mFile) {
        Bitmap image = mMemoryCache.get(mFile.getName() + String.valueOf(mFile.length()));
        if (image == null) {
            File mfile = new File(mFile.getPath());
            image = AppUtils.decodeBitmap2(mFile.getPath(), PoctApplication.mScreenDefine.ScreenWidth, PoctApplication.mScreenDefine.ScreenHeight);
            if (image != null) {
                mMemoryCache.put(mFile.getName() + String.valueOf(mFile.length()), image);
            }
            return image;

        } else {
            return image;
        }
    }

    public static boolean checkDownload(long size) {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
                .getPath());
        double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat
                .getBlockSize()) / 1024 / 1024;
        double addMB = size / 1024 / 1024;
        if (addMB > sdFreeMB) {
            return false;
        } else {
            return true;
        }
    }
}
