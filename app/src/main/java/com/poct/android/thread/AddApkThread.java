package com.poct.android.thread;


import android.content.Context;
import android.os.Handler;

import com.poct.android.handler.LoginHandler;
import com.poct.android.manager.UpDataManager;
import com.poct.android.view.PoctApplication;

public class AddApkThread extends Thread {

    public String path;
    public Handler mHandler;
    public Context context;
    public AddApkThread(Context context,String path, Handler mHandler) {
        this.path = path;
        this.mHandler = mHandler;
        this.context = context;
    }

    @Override
    public void run() {
        super.run();
        UpDataManager.isInstall = true;
        mHandler.sendEmptyMessage(LoginHandler.EVENT_SYSTEM_UPDATA_INSTALL);
        PoctApplication.mApp.installSlient(context,path);
        mHandler.sendEmptyMessage(LoginHandler.EVENT_SYSTEM_UPDATA_INSTALL_END);
        UpDataManager.isInstall = false;
    }
}
