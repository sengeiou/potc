package com.poct.android.entity;

public class Setting {

    public String mName;
    public int mImageId;
    public int mContentId;
    public boolean isSwitch = false;

    public Setting(String mName,int mImageId,int mContentId,Boolean isSwitch) {
        this.mName = mName;
        this.mImageId = mImageId;
        this.mContentId = mContentId;
        this.isSwitch = isSwitch;
    }
}
