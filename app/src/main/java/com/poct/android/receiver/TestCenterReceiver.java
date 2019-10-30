package com.poct.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.poct.android.handler.TestCenterHandler;
import com.poct.android.view.activity.TestCenterActivity;

public class TestCenterReceiver extends BroadcastReceiver {
    public Handler handler;
    public TestCenterReceiver(Handler handler)
    {
        this.handler = handler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(TestCenterActivity.ACTION_UPDETE_REPORT_LIST.equals(intent.getAction())){
            if(handler != null) {
                Message message = new Message();
                message.obj = intent;
                message.what = TestCenterHandler.EVENT_UPDATE_LIST;
                handler.sendMessage(message);
            }
        }
        if(TestCenterActivity.ACTION_UPLOAD_REPORT.equals(intent.getAction())){
            if(handler != null) {
                Message message = new Message();
                message.obj = intent;
                message.what = TestCenterHandler.EVENT_UPLOAD_REPORT;
                handler.sendMessage(message);
            }
        }
        if(TestCenterActivity.ACTION_UPDETE_LIST.equals(intent.getAction()))
        {
            if(handler != null) {
                Message message = new Message();
                message.what = TestCenterHandler.UPDAT_LIST;
                handler.sendMessage(message);
            }
        }
    }

}
