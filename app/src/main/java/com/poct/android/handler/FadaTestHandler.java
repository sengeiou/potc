package com.poct.android.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.poct.R;
import com.poct.android.asks.ReportAsks;
import com.poct.android.entity.NetObject;
import com.poct.android.entity.Report;
import com.poct.android.entity.SeriesReport;
import com.poct.android.manager.BluetoothSetManager;
import com.poct.android.manager.EquipMentManager;
import com.poct.android.prase.DataPrase;
import com.poct.android.utils.AppUtils;
import com.poct.android.utils.DBHelper;
import com.poct.android.utils.ViewUtils;
import com.poct.android.view.activity.FadaTestActivity;

public class FadaTestHandler extends Handler {

    public static final int EVENT_UPDATA_CHARACTOR = 30600;
    public static final int EVENT_DISCOVER_SERVEICE = 30601;
    public static final int EVENT_DEVICE_CONNECTED = 30602;
    public static final int EVENT_DEVICE_CONNECTING = 30603;
    public static final int EVENT_DEVICE_DISCONNECT = 30604;
    public static final int EVENT_DEVICE_READY = 30605;
    public static final int EVENT_FADA_GET = 30606;
    public static final int EVENT_FADA_GET_CHECK = 30607;
    public static final int EVENT_FADA_LIST_UPDATA = 30608;
    FadaTestActivity mActivity;

    public FadaTestHandler(FadaTestActivity mFadaTestActivity) {
        mActivity = mFadaTestActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        FadaTestActivity theActivity = mActivity;
        Intent intent = new Intent();
        switch (msg.what) {
            case EVENT_UPDATA_CHARACTOR:
                if(theActivity.data.length() < 108)
                {
                    theActivity.data += msg.obj;
                    if(theActivity.data.length() == 108)
                    {
//                        theActivity.seriesReport = DataPrase.padaPrase(theActivity.data);
                        if(theActivity.isGetting == true)
                        {
                            if(theActivity.mTempGet != null)
                            {
                                theActivity.mTempGet.isGet = true;
                            }
                            if(theActivity.mTempGet != null)
                            theActivity.mReports.addAll(DataPrase.padaPrase(theActivity.data,theActivity.mTempGet,theActivity.mSeriesReport.mReports));

                            if(theActivity.mTempGet.realcount == -1)
                            {
                                theActivity.mTempGet.realcount = theActivity.mReports.size();
                                theActivity.mSeriesReport.tempId = DataPrase.praseTempid( theActivity.mSeriesReport.tempId,theActivity.mTempGet,EquipMentManager.DEVICE_FADA);
                                DBHelper.getInstance(theActivity).updataReport(theActivity.mSeriesReport);
                            }

                            theActivity.deviceReportAdapter.notifyDataSetChanged();
                            theActivity.isGetting = false;
                            if( theActivity.mAlertDialog != null)
                            {
                                theActivity.mAlertDialog.dismiss();
                            }
                            theActivity.mFadaTestPresenter.updataButtomImf();
                        }

                    }
                }
                else
                {
                    theActivity.data = "";
                    theActivity.data += msg.obj;
                }
                break;
            case EVENT_DISCOVER_SERVEICE:
                BluetoothSetManager.getInstance().prepareServiceData(theActivity);
                break;
            case EVENT_DEVICE_CONNECTED:
                ViewUtils.showMessage(theActivity,theActivity.getString(R.string.bluetooth_device_connect));
                break;
            case EVENT_DEVICE_CONNECTING:
                ViewUtils.showMessage(theActivity,theActivity.getString(R.string.bluetooth_device_connecting));
                theActivity.connectState.setText(theActivity.getString(R.string.device_state_connecting));
                break;
            case EVENT_DEVICE_DISCONNECT:
                ViewUtils.showMessage(theActivity,theActivity.getString(R.string.bluetooth_device_disconnect));
                theActivity.canConnected = true;
                theActivity.connectState.setText(theActivity.getString(R.string.device_state_disconnected));
//                if(BluetoothSetManager.getInstance().mBluetoothGatt != null)
//                BluetoothSetManager.getInstance().mBluetoothGatt.close();
                break;
            case EVENT_DEVICE_READY:
                theActivity.waitDialog.hide();
                ViewUtils.showMessage(theActivity,theActivity.getString(R.string.bluetooth_device_ready));
                theActivity.connectState.setText(theActivity.getString(R.string.device_state_connected));
                break;
            case EVENT_FADA_GET:
                DBHelper.getInstance(theActivity).upDateDevice(EquipMentManager.getInstance().deviceFada);
                EquipMentManager.getInstance().connectFada(theActivity,theActivity.connectState);
                break;
            case EVENT_FADA_GET_CHECK:
                if(EquipMentManager.getInstance().deviceFada.device == null)
                {
                    BluetoothSetManager.getInstance().stopLeScan();
                    theActivity.canConnected = true;
                    ViewUtils.showMessage(theActivity,theActivity.getString(R.string.qtest_error_device_find));
                    theActivity.connectState.setText(theActivity.getString(R.string.device_state_disconnected));
                }
                break;
            case EVENT_FADA_LIST_UPDATA:
                theActivity.mFadaTestPresenter.doDelete((Report) msg.obj);
                break;
            case ReportAsks.REPORT_ADD_FAIL:
                theActivity.waitDialog.hide();
                NetObject object2 = (NetObject) msg.obj;
                SeriesReport seriesReport2 = (SeriesReport) object2.item;
                seriesReport2.updatacount++;
                if(AppUtils.gotoMain(theActivity) == false)
                {
                    if(seriesReport2.updatacount != 4)
                        ReportAsks.reportAdd(seriesReport2,theActivity.mFadaTestPresenter.mFadaTestHandler,theActivity);
                }
//                ViewUtils.showMessage(theActivity, LoginPrase.loginFail(object2.result));
                break;
        }

    }
}
