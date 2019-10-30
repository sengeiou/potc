package com.poct.android.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.poct.R;
import com.poct.android.manager.UpDataManager;
import com.poct.android.manager.WifiSetManager;
import com.poct.android.utils.AppUtils;
import com.poct.android.utils.ViewUtils;
import com.poct.android.view.activity.SettingActivity;

public class SettingHandler extends Handler {

    public static final int WIFI_UPDATA_LIST = 30200;
    public static final int WIFI_CLOSE = 30201;
    public static final int WIFI_OPEN = 30202;
    public static final int WIFI_UPDATA = 30905;
    public static final int WIFI_CONNECT_SUCCESS = 30203;
    public static final int WIFI_CONNECT_FAIL = 30204;
    public static final int WIFI_CONNECT_ING = 30206;
    public static final int TIME_UPDATE = 30207;
    public static final int EQUIP_UPDATE_PRINT = 30208;
    public static final int EQUIP_UPDATE_POTC = 30209;
    public static final int EQUIP_UPDATE_FADA = 30210;
    public static final int STSETM_UPDATE_CHECK = 30211;
    public static final int STSETM_UPDATE_CHECK_FAIL = 30216;
    public static final int PROGRESS_UPDATA = 30212;
    public static final int PROGRESS_UPDATA_FINISH = 30215;
    SettingActivity mActivity;

    public SettingHandler(SettingActivity mWifiSetAActivity) {
        mActivity = mWifiSetAActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        SettingActivity theActivity = mActivity;
        Intent intent = new Intent();
        switch (msg.what) {
            case WIFI_UPDATA_LIST:
                if(theActivity.mWifiAdapter != null)
                theActivity.mWifiAdapter.notifyDataSetChanged();
                break;
            case WIFI_CLOSE:
                WifiSetManager.getInstance().wifiList.clear();
                if(theActivity.mWifiAdapter != null)
                theActivity.mWifiAdapter.notifyDataSetChanged();
                break;
            case WIFI_OPEN:
                WifiSetManager.getInstance().scanWifi();
                break;
            case WIFI_CONNECT_FAIL:
                if(WifiSetManager.getInstance().mWifiBeanConnect != null)
                WifiSetManager.getInstance().mWifiBeanConnect.state = WifiSetManager.WIFI_STATE_UNCONNECT;
                if(theActivity.mWifiAdapter != null)
                theActivity.mWifiAdapter.notifyDataSetChanged();
                break;
            case WIFI_CONNECT_SUCCESS:
                if(WifiSetManager.getInstance().mWifiBeanConnect != null)
                WifiSetManager.getInstance().mWifiBeanConnect.state = WifiSetManager.WIFI_STATE_CONNECT;
                if(theActivity.mWifiAdapter != null)
                theActivity.mWifiAdapter.notifyDataSetChanged();
                break;
            case WIFI_CONNECT_ING:
                if(WifiSetManager.getInstance().mWifiBeanConnect != null)
                WifiSetManager.getInstance().mWifiBeanConnect.state = WifiSetManager.WIFI_STATE_ON_CONNECTING;
                if(theActivity.mWifiAdapter != null)
                theActivity.mWifiAdapter.notifyDataSetChanged();
                break;
            case WIFI_UPDATA:
                WifiSetManager.getInstance().sortScaResult();
                if(theActivity.mWifiAdapter != null)
                theActivity.mWifiAdapter.notifyDataSetChanged();
                break;
            case TIME_UPDATE:
                theActivity.mSettingPresenter.updataTime();
                break;
            case EQUIP_UPDATE_PRINT:
                theActivity.mSettingPresenter.upDataPrint();
                break;
            case EQUIP_UPDATE_POTC:
                theActivity.mSettingPresenter.upDataPotc();
                break;
            case EQUIP_UPDATE_FADA:
                theActivity.mSettingPresenter.upDataFada();
                break;
            case STSETM_UPDATE_CHECK:
                theActivity.waitDialog.hide();
                theActivity.mSettingPresenter.checkUpdata(UpDataManager.initJson((String) msg.obj,theActivity,theActivity.mSettingPresenter.mSettingHandler));
                break;
            case STSETM_UPDATE_CHECK_FAIL:
                theActivity.waitDialog.hide();
                ViewUtils.showMessage(theActivity,theActivity.getString(R.string.setting_title_system_updata_imf_fail));
                break;
            case PROGRESS_UPDATA:
                theActivity.mSettingPresenter.updataProgress((Intent) msg.obj);
                break;
            case LoginHandler.EVENT_SYSTEM_UPDATA_INSTALL:
                theActivity.waitDialog = AppUtils.createLoadingDialog(theActivity,theActivity.getString(R.string.updata_new_install));
                theActivity.waitDialog.setCancelable(false);
                theActivity.waitDialog.show();
                break;
            case LoginHandler.EVENT_SYSTEM_UPDATA_INSTALL_END:
                theActivity.waitDialog.hide();
                theActivity.waitDialog = AppUtils.createLoadingDialog(theActivity,"");
                theActivity.waitDialog.setCancelable(true);
                break;
            case PROGRESS_UPDATA_FINISH:
                theActivity.mSettingPresenter.finishProgress();
                break;
        }

    }
}
