package com.poct.android.entity;

public class TempGet {

    public String mTempid = "";
    public boolean isGet = false;
    public int realcount = -1;
    public TempGet(String Tempid ,boolean isget)
    {
        this.mTempid = Tempid;
        this.isGet = isget;
    }
}
