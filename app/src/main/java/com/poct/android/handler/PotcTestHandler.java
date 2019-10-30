package com.poct.android.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.poct.R;
import com.poct.android.asks.ReportAsks;
import com.poct.android.asks.TestReportAsks;
import com.poct.android.entity.NetObject;
import com.poct.android.entity.Report;
import com.poct.android.entity.SeriesReport;
import com.poct.android.entity.TempGet;
import com.poct.android.manager.EquipMentManager;
import com.poct.android.prase.DataPrase;
import com.poct.android.utils.AppUtils;
import com.poct.android.utils.DBHelper;
import com.poct.android.utils.ViewUtils;
import com.poct.android.view.activity.PotcTestActivity;

public class PotcTestHandler extends Handler {

    public static final int EVENT_POTC_GET = 30506;
    public static final int EVENT_POTC_GET_CHECK = 30507;
    public static final int EVENT_POTC_LIST_UPDATA = 30508;
    public static final int EVENT_GET_POTC_BOND = 305009;
    public static final int EVENT_GET_POTC_BONO = 305010;
    public static final int EVENT_GET_POTC_DATA_ITEM_SUCCESS = 105000;
    public static final int EVENT_GET_POTC_DATA_ITEM_FAIL= 205000;
    public static final int EVENT_GET_POTC_DATA_FINISH = 305001;
    public static final int EVENT_GET_POTC_DATA_NONE = 305002;
    public static final int EVENT_GET_POTC_GETTEMP_UPDATA = 305003;
    PotcTestActivity mActivity;

    public PotcTestHandler(PotcTestActivity mPotcTestActivity) {
        mActivity = mPotcTestActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        PotcTestActivity theActivity = mActivity;
        Intent intent = new Intent();
        switch (msg.what) {
            case EVENT_POTC_GET:
                DBHelper.getInstance(theActivity).upDateDevice(EquipMentManager.getInstance().devicePotc);
                EquipMentManager.getInstance().connectPotc(theActivity,theActivity.connectState);
                break;
            case EVENT_POTC_GET_CHECK:
                if(EquipMentManager.getInstance().devicePotc.device == null)
                {
                    ViewUtils.showMessage(theActivity,theActivity.getString(R.string.qtest_error_device_find));
                    theActivity.connectState.setText(theActivity.getString(R.string.device_state_disconnected));
                }
                break;
            case EVENT_POTC_LIST_UPDATA:
                theActivity.mPotcTestPresenter.doDelete((Report) msg.obj);
                break;
            case TestReportAsks.GET_REPORT_COUNT_SUCCESS:
                int count = DataPrase.praseGetReportCount((String) msg.obj);
//                theActivity.mPotcTestPresenter.getPotcData(count);
//                TestReportAsks.getReport(theActivity,theActivity.mPotcTestPresenter.mPotcTestHandler, AppUtils.getDate(),count,1);
                break;
            case TestReportAsks.GET_REPORT_REPORT_SUCCESS:
                NetObject object = (NetObject) msg.obj;
                TempGet tempGet = (TempGet) object.item;
                DataPrase.praseGetReport(object.result,theActivity.mReports,theActivity.mSeriesReport.mReports);
                tempGet.isGet = true;
                if(tempGet.realcount == -1)
                {
                    tempGet.realcount = 1;
                    tempGet.isGet = true;
                    theActivity.mSeriesReport.tempId = DataPrase.praseTempid( theActivity.mSeriesReport.tempId,theActivity.mTempGet,EquipMentManager.DEVICE_POTC);
                    DBHelper.getInstance(theActivity).updataReport(theActivity.mSeriesReport);
                }
                if(msg.arg1 <= msg.arg2 || msg.arg1 == theActivity.lastcount)
                {
//                    TestReportAsks.getReport(theActivity,theActivity.mPotcTestPresenter.mPotcTestHandler, AppUtils.getDate(),msg.arg2,msg.arg1+1);
                    theActivity.waitDialog.hide();
                    theActivity.deviceReportAdapter.notifyDataSetChanged();
                    theActivity.isGetting = false;
                    theActivity.mPotcTestPresenter.updataButtomImf();
                }
                break;
            case EVENT_GET_POTC_DATA_FINISH:
                theActivity.waitDialog.hide();
                theActivity.deviceReportAdapter.notifyDataSetChanged();
                theActivity.isGetting = false;
                theActivity.mPotcTestPresenter.updataButtomImf();
                break;
            case EVENT_GET_POTC_DATA_NONE:
                theActivity.waitDialog.hide();
                theActivity.deviceReportAdapter.notifyDataSetChanged();
                theActivity.isGetting = false;
                theActivity.mPotcTestPresenter.updataButtomImf();
                ViewUtils.showMessage(theActivity,theActivity.getString(R.string.qtest_potc_temp_no_data));
                break;
            case EVENT_GET_POTC_DATA_ITEM_FAIL:
                TempGet tempGet2 = (TempGet) msg.obj;
                ViewUtils.showMessage(theActivity,theActivity.getString(R.string.qtest_potc_temp_error1)+tempGet2.mTempid+theActivity.getString(R.string.qtest_potc_temp_error2));
                break;
            case EVENT_GET_POTC_DATA_ITEM_SUCCESS:
                NetObject object1 = (NetObject) msg.obj;
                TempGet tempGet1 = (TempGet) object1.item;
                DataPrase.praseGetReport(object1.result,theActivity.mReports,theActivity.mSeriesReport.mReports);
//                tempGet1.isGet = true;
//                if(tempGet1.realcount == -1)
//                {
//                    tempGet1.realcount = 1;
//                    tempGet1.isGet = true;
//                    theActivity.mSeriesReport.tempId = DataPrase.praseTempid( theActivity.mSeriesReport.tempId,tempGet1,EquipMentManager.DEVICE_POTC);
//                    DBHelper.getInstance(theActivity).saveReport(theActivity.mSeriesReport);
//                }
                break;
            case EVENT_GET_POTC_GETTEMP_UPDATA:
                TempGet tempGet5 = (TempGet) msg.obj;
                tempGet5.isGet = true;
                theActivity.mSeriesReport.tempId = DataPrase.praseTempid( theActivity.mSeriesReport.tempId,tempGet5,EquipMentManager.DEVICE_POTC);
                DBHelper.getInstance(theActivity).updataReport(theActivity.mSeriesReport);
                break;
            case TestReportAsks.GET_REPORT_COUNT_FAIL:
                ViewUtils.showMessage(theActivity,"获取数据失败,请重新尝试，如果一直失败,请检查设备配置");
                theActivity.waitDialog.hide();
                theActivity.isGetting = false;
                theActivity.deviceReportAdapter.notifyDataSetChanged();
                theActivity.mPotcTestPresenter.updataButtomImf();
                break;
            case TestReportAsks.GET_REPORT_REPORT_FAIL:
                theActivity.waitDialog.hide();
                theActivity.isGetting = false;
                theActivity.deviceReportAdapter.notifyDataSetChanged();
                theActivity.mPotcTestPresenter.updataButtomImf();
                break;
            case EVENT_GET_POTC_BOND:
                ViewUtils.showMessage(theActivity,theActivity.getString(R.string.bluetooth_device_ready));
                theActivity.connectState.setText(theActivity.getString(R.string.device_state_connected));
                break;
            case EVENT_GET_POTC_BONO:
                ViewUtils.showMessage(theActivity,theActivity.getString(R.string.bluetooth_device_disconnect));
                theActivity.connectState.setText(theActivity.getString(R.string.device_state_disconnected));
                break;
            case ReportAsks.REPORT_ADD_FAIL:
                theActivity.waitDialog.hide();
                NetObject object2 = (NetObject) msg.obj;
                SeriesReport seriesReport2 = (SeriesReport) object2.item;
                seriesReport2.updatacount++;
                if(AppUtils.gotoMain(theActivity) == false)
                {
                    if(seriesReport2.updatacount != 4)
                        ReportAsks.reportAdd(seriesReport2,theActivity.mPotcTestPresenter.mPotcTestHandler,theActivity);
                }

//                ViewUtils.showMessage(theActivity, LoginPrase.loginFail((String) msg.obj));
                break;
        }

    }
}
