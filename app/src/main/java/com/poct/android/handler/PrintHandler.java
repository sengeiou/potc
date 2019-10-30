package com.poct.android.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.poct.R;
import com.poct.android.manager.PrintManager;
import com.poct.android.utils.AppActivityManager;
import com.poct.android.utils.ViewUtils;
import com.poct.android.view.PoctApplication;

public class PrintHandler extends Handler {

    public PrintHandler() {
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
            case PrintManager.SEND_DATA_PRINT:
                ViewUtils.showMessagetop(AppActivityManager.getAppActivityManager().getCurrentActivity(),"正在打印第份检测报告,剩余"+String.valueOf(PrintManager.getInstance().threads.size()-1));
                break;
            case PrintManager.SEND_SUCCESS:
                PrintManager.getInstance().threads.remove(0);
                PrintManager.getInstance().startTask = null;
                PrintManager.getInstance().startPrintNewThread();
                break;
            case PrintManager.SEND_FAIL:
                PrintManager.getInstance().startTask = null;
                ViewUtils.showMessage(AppActivityManager.getAppActivityManager().getCurrentActivity(),PoctApplication.mApp.getString(R.string.print_message_send_fail));
                PrintManager.getInstance().restart();
                break;
        }

    }
}
