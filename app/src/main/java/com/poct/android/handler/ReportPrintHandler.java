package com.poct.android.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.poct.R;
import com.poct.android.manager.PrintManager;
import com.poct.android.utils.ViewUtils;
import com.poct.android.view.activity.ReportPrintActivity;

public class ReportPrintHandler extends Handler {

    public static final int CREAT_PRINT_VIEW = 30700;
    public static final int CREAT_PRINT_VIEW_START = 30701;

    ReportPrintActivity mActivity;

    public ReportPrintHandler(ReportPrintActivity mReportPrintActivity) {
        mActivity = mReportPrintActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        ReportPrintActivity theActivity = mActivity;
        Intent intent = new Intent();
        switch (msg.what) {
            case PrintManager.SEND_PRINT_INIT:
                ViewUtils.showMessage(theActivity,theActivity.getString(R.string.print_message_init));
                break;
            case PrintManager.NONE_PRINT:
                theActivity.waitDialog.hide();
                ViewUtils.showMessage(theActivity,theActivity.getString(R.string.error_no_print));
                break;
            case CREAT_PRINT_VIEW:
                theActivity.mReportPrintPresenter.startPrint();
                break;
            case CREAT_PRINT_VIEW_START:
                theActivity.waitDialog.show();
                theActivity.mReportPrintPresenter.creatView();
                break;
        }

    }
}
