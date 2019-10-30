package com.poct.android.entity;

import android.net.wifi.WifiConfiguration;

public class WifiBean implements Comparable<WifiBean> {
    public String wifiName = "";
    public String address = "";
    public boolean saved = false;
    public int type = 0;
    public boolean is5G = false;
    public String level = "";
    public String state = "";  //已连接  正在连接  未连接 三种状态
    public String capabilities = "";//加密方式
    public WifiConfiguration mWifiConfiguration = null;

    @Override
    public int compareTo(WifiBean o) {
        int level1 = Integer.parseInt(level);
        int level2 = Integer.parseInt(o.level);
        return level2 - level1;
    }
}
