package com.poct.android.entity;

import android.view.View;

public class MenuItem {

    public String btnName;
    public Object item;
    public View.OnClickListener mListener;

    public MenuItem()
    {

    }

    public MenuItem(String btnName, View.OnClickListener mListener)
    {
        this.btnName = btnName;
        this.mListener = mListener;
    }
}
