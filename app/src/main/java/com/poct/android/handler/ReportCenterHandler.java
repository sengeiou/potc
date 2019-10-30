package com.poct.android.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.poct.R;
import com.poct.android.asks.ReportAsks;
import com.poct.android.net.NetUtils;
import com.poct.android.prase.LoginPrase;
import com.poct.android.prase.ReportPrase;
import com.poct.android.utils.ViewUtils;
import com.poct.android.view.activity.ReportCenterActivity;

public class ReportCenterHandler extends Handler {
    ReportCenterActivity mActivity;

    public ReportCenterHandler(ReportCenterActivity mReportCenterActivity) {
        mActivity = mReportCenterActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        ReportCenterActivity theActivity = mActivity;
        Intent intent = new Intent();
        switch (msg.what) {
            case ReportAsks.REPORT_GET_SUCCESS:
                theActivity.waitDialog.hide();
                theActivity.mPageSeraisReport = ReportPrase.praseReport((String) msg.obj,theActivity.etxtName.getText().toString(),theActivity.etxtSender.getText().toString());
                theActivity.mSeriesReports.clear();
                theActivity.mSeriesReports.addAll(theActivity.mPageSeraisReport.getShow());
                theActivity.mSeriasReportAdapter.notifyDataSetChanged();
                theActivity.mReportCenterPresenter.setEndImf();
                break;
            case ReportAsks.REPORT_GET_FAIL:
                theActivity.waitDialog.hide();
                ViewUtils.showMessage(theActivity, LoginPrase.loginFail((String) msg.obj));
                break;
            case NetUtils.NO_NET_WORK:
                theActivity.waitDialog.hide();
                ViewUtils.showMessage(theActivity, theActivity.getString(R.string.error_net_network));
                break;
        }

    }
}