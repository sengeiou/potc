package com.poct.android.entity;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.poct.android.utils.AppUtils;

public class ScreenDefine {

    public int verticalMinDistance = 40;
    public int mPullScollY = 0;
    public int mGridMaxWidth = 320;
    public int mFunctionWidth = 0;
    public int mFunctionTextSpacing = 0;
    public int mFunctionhImageSpacing = 0;
    public int ScreenWidth;
    public int ScreenHeight;
    public float density;

    public ScreenDefine(Context mContext)
    {
        DisplayMetrics metric = AppUtils.getWindowInfo(mContext);
        if(metric.widthPixels > 1080)
        {
            verticalMinDistance = 40*metric.widthPixels/1080;

        }
        if(metric.heightPixels > 1920)
        {
            mPullScollY = 50*1920/metric.heightPixels;
        }
        Point point = new Point();
        ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRealSize(point);
        mGridMaxWidth = metric.widthPixels/3;
        density = metric.density;
        mFunctionWidth = metric.widthPixels/4;
        mFunctionhImageSpacing = mFunctionWidth/2;
        mFunctionTextSpacing = mFunctionWidth/5;
        ScreenWidth = point.x;
        ScreenHeight = point.y;
    }

}
