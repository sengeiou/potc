package com.poct.android.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.poct.R;
import com.poct.android.asks.ReportAsks;
import com.poct.android.entity.NetObject;
import com.poct.android.entity.SeriesReport;
import com.poct.android.manager.EquipMentManager;
import com.poct.android.net.NetUtils;
import com.poct.android.prase.LoginPrase;
import com.poct.android.utils.DBHelper;
import com.poct.android.utils.ViewUtils;
import com.poct.android.view.activity.TestCenterActivity;

public class TestCenterHandler extends Handler {

    public static final int EVENT_DELETE = 30700;
    public static final int EVENT_ADD = 30701;
    public static final int EVENT_DETIAL = 30703;
    public static final int EVENT_CHANGE = 30704;
    public static final int EVENT_MORE = 30705;
    public static final int EVENT_UPDATE_LIST = 30706;
    public static final int EVENT_UPLOAD_REPORT = 30707;
    public static final int EVENT_DELETE_TEMPID = 30708;
    public static final int UPDAT_LIST = 30709;
    TestCenterActivity mActivity;

    public TestCenterHandler(TestCenterActivity mTestCenterActivity) {
        mActivity = mTestCenterActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        TestCenterActivity theActivity = mActivity;
        Intent intent = new Intent();
        switch (msg.what) {
            case EVENT_DELETE:
                if(theActivity.popupWindow1 != null)
                theActivity.popupWindow1.dismiss();
                theActivity.mTestCenterPresenter.doDeleteSelect((SeriesReport) msg.obj);
                break;
            case EVENT_ADD:
                if(theActivity.popupWindow1 != null)
                theActivity.popupWindow1.dismiss();
                theActivity.mTestCenterPresenter.doAddSelect((SeriesReport) msg.obj,true);
                break;
            case EVENT_DETIAL:
                if(theActivity.popupWindow1 != null)
                    theActivity.popupWindow1.dismiss();
                theActivity.mTestCenterPresenter.startPrint((SeriesReport) msg.obj);
                break;
            case EVENT_CHANGE:
                if(theActivity.popupWindow1 != null)
                    theActivity.popupWindow1.dismiss();
                theActivity.mTestCenterPresenter.doAddSelect((SeriesReport) msg.obj,false);
                break;
            case EVENT_MORE:
                if(theActivity.popupWindow1 != null)
                    theActivity.popupWindow1.dismiss();
                theActivity.mTestCenterPresenter.showMore((SeriesReport) msg.obj);
                break;
            case EVENT_UPDATE_LIST:
                intent = (Intent) msg.obj;
                theActivity.mTestCenterPresenter.updataist(intent.getStringExtra(EquipMentManager.REPORT_ID));
//                SeriesReport mSeriesReport = DBHelper.getInstance(theActivity).getSeriesReport(intent.getStringExtra(EquipMentManager.REPORT_ID));
//                theActivity.mTestCenterPresenter.doUpload(mSeriesReport);
//                theActivity.mSeriasReportAdapter.notifyDataSetChanged();
                break;
            case ReportAsks.REPORT_ADD_SUCCESS:
                theActivity.waitDialog.hide();
                NetObject object = (NetObject) msg.obj;
                SeriesReport seriesReport = (SeriesReport) object.item;
                DBHelper.getInstance(theActivity).updataReport(seriesReport);
                theActivity.mSeriasReportAdapter.notifyDataSetChanged();
                break;
            case ReportAsks.REPORT_ADD_FAIL:
                theActivity.waitDialog.hide();
                NetObject object2 = (NetObject) msg.obj;
                SeriesReport seriesReport2 = (SeriesReport) object2.item;
                theActivity.mTestCenterPresenter.doUpload(seriesReport2);
                ViewUtils.showMessage(theActivity, LoginPrase.loginFail((String) msg.obj));
                break;
            case NetUtils.NO_NET_WORK:
                theActivity.waitDialog.hide();
                ViewUtils.showMessage(theActivity, theActivity.getString(R.string.error_net_network));
                break;
            case EVENT_UPLOAD_REPORT:
                break;
            case EVENT_DELETE_TEMPID:
                theActivity.mTempAdapter.notifyDataSetChanged();
                break;
            case UPDAT_LIST:
                theActivity.mSeriasReportAdapter.notifyDataSetChanged();
                break;
        }

    }
}
