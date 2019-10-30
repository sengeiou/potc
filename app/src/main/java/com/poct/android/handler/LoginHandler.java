package com.poct.android.handler;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.poct.R;
import com.poct.android.asks.LoginAsks;
import com.poct.android.manager.BluetoothSetManager;
import com.poct.android.manager.EquipMentManager;
import com.poct.android.manager.UpDataManager;
import com.poct.android.net.NetUtils;
import com.poct.android.prase.LoginPrase;
import com.poct.android.utils.AppUtils;
import com.poct.android.utils.ViewUtils;
import com.poct.android.view.PoctApplication;
import com.poct.android.view.activity.LoginActivity;

public class LoginHandler extends Handler {

    public static final int PERMISSION_REQUEST_COARSE_LOCATION = 40001;
    public static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 40002;
    public static final int PERMISSION_REQUEST_WRITE_SETTINGS = 40003;
    public static final int PERMISSION_REQUEST_WRITE_SECURE_SETTINGS = 40004;
    public static final int EVENT_FIND_DEVICE = 300004;
    public static final int EVENT_END_DISCOVERY = 300000;
    public static final int EVENT_LOGOUT = 300001;
    public static final int EVENT_CHECK_UPDATA_FAIL = 300003;
    public static final int EVENT_CHECK_UPDATA = 300005;
    public static final int EVENT_SYSTEM_UPDATA = 300006;
    public static final int EVENT_SYSTEM_UPDATA_CHECK = 300007;
    public static final int EVENT_SYSTEM_UPDATA_INSTALL = 300008;
    public static final int EVENT_SYSTEM_UPDATA_FAIL = 300009;
    public static final int EVENT_SYSTEM_UPDATA_INSTALL_END = 300010;
    public static final int EVENT_SYSTEM_UPDATA_CHECK_START = 300011;
    public static final int EVENT_START_GETDATA_SUCCESS = 100000;
    public static final int EVENT_START_GETDATA_FAIL = 200000;



    LoginActivity mActivity;

    public LoginHandler(LoginActivity mSplashActivity) {
        mActivity = mSplashActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        LoginActivity theActivity = mActivity;
        switch (msg.what) {
            case PERMISSION_REQUEST_COARSE_LOCATION:
                AppUtils.getPermission(Manifest.permission.ACCESS_FINE_LOCATION,theActivity, LoginHandler.PERMISSION_REQUEST_ACCESS_FINE_LOCATION,theActivity.mLoginPresenter.mLoginHandler);
                break;
            case PERMISSION_REQUEST_ACCESS_FINE_LOCATION:
                AppUtils.getPermission(Manifest.permission.WRITE_SETTINGS, theActivity, LoginHandler.PERMISSION_REQUEST_WRITE_SETTINGS, theActivity.mLoginPresenter.mLoginHandler);
                break;
            case PERMISSION_REQUEST_WRITE_SETTINGS:
                AppUtils.getPermission(Manifest.permission.WRITE_SECURE_SETTINGS, theActivity, LoginHandler.PERMISSION_REQUEST_WRITE_SECURE_SETTINGS, theActivity.mLoginPresenter.mLoginHandler);
                break;
            case PERMISSION_REQUEST_WRITE_SECURE_SETTINGS:
                EquipMentManager.getInstance().scanDevice();
                break;
            case EVENT_START_GETDATA_SUCCESS:
                NetUtils.getInstance().sendSuccess(theActivity.mLoginPresenter.mLoginHandler,theActivity);
                break;
            case EVENT_START_GETDATA_FAIL:
                NetUtils.getInstance().sendFail(theActivity.mLoginPresenter.mLoginHandler,theActivity,EVENT_START_GETDATA_SUCCESS);
                break;
            case EVENT_FIND_DEVICE:
//                theActivity.mLoginPresenter.mLoginHandler.removeMessages(LoginHandler.EVENT_END_DISCOVERY);
                EquipMentManager.getInstance().addDevice((BluetoothDevice) msg.obj,theActivity.mLoginPresenter.mLoginHandler);
                break;
            case EVENT_END_DISCOVERY:
                BluetoothSetManager.getInstance().stopDisvery();
                if(EquipMentManager.getInstance().deviceFada != null)
                {
                    if(EquipMentManager.getInstance().deviceFada.device == null)
                    {
                        theActivity.sendBroadcast(new Intent(BluetoothSetManager.ACTION_DEVICE_CHECK_FADA));
                    }
                }
                if(EquipMentManager.getInstance().devicePotc != null)
                {
                    if(EquipMentManager.getInstance().devicePotc.device == null)
                    {
                        theActivity.sendBroadcast(new Intent(BluetoothSetManager.ACTION_DEVICE_CHECK_POTC));
                    }
                }
                break;
            case LoginAsks.EVENT_LOGIN_SUCCESS:
                theActivity.waitDialog.hide();
                LoginPrase.loginPrase((String) msg.obj);
                theActivity.mLoginPresenter.startMain();
                break;
            case LoginAsks.EVENT_LOGIN_FAIL:
                theActivity.waitDialog.hide();
                ViewUtils.showMessage(theActivity, LoginPrase.loginFail((String) msg.obj));
                break;
            case PotcTestHandler.EVENT_GET_POTC_BOND:
                theActivity.sendBroadcast(new Intent(BluetoothSetManager.ACTION_DEVICE_POTC_CONNECTED));
                break;
            case PotcTestHandler.EVENT_GET_POTC_BONO:
                theActivity.sendBroadcast(new Intent(BluetoothSetManager.ACTION_DEVICE_POTC_UNCONNECTED));
                break;
            case EVENT_LOGOUT:
                theActivity.mLoginPresenter.doLogout();
                break;
            case NetUtils.NO_NET_WORK:
                theActivity.waitDialog.hide();
                ViewUtils.showMessage(theActivity, theActivity.getString(R.string.error_net_network));
                break;
            case EVENT_SYSTEM_UPDATA:
                Intent intent = (Intent) msg.obj;
                UpDataManager.saveDownloadApkImf(theActivity,intent.getStringExtra("vname"),intent.getIntExtra("vcode",0));
                UpDataManager.doUpDataAppView(theActivity,intent.getStringExtra("vname"),theActivity.mLoginPresenter.mLoginHandler);
                break;
            case EVENT_SYSTEM_UPDATA_CHECK:
                UpDataManager.checkVersin(theActivity,theActivity.mLoginPresenter.mLoginHandler);
                break;
            case EVENT_CHECK_UPDATA:
                UpDataManager.initJson((String) msg.obj,theActivity,theActivity.mLoginPresenter.mLoginHandler);
                break;
            case EVENT_CHECK_UPDATA_FAIL:
                UpDataManager.checkVersin(theActivity,theActivity.mLoginPresenter.mLoginHandler);
                break;
            case EVENT_SYSTEM_UPDATA_INSTALL:
                theActivity.waitDialog = AppUtils.createLoadingDialog(theActivity,theActivity.getString(R.string.updata_new_install));
                theActivity.waitDialog.setCancelable(false);
                theActivity.waitDialog.show();
                break;
            case EVENT_SYSTEM_UPDATA_FAIL:
                PoctApplication.mApp.mDownloadTask = null;
                break;
            case EVENT_SYSTEM_UPDATA_INSTALL_END:
                theActivity.waitDialog.hide();
                theActivity.waitDialog = AppUtils.createLoadingDialog(theActivity,"");
                theActivity.waitDialog.setCancelable(true);
                break;
            case EVENT_SYSTEM_UPDATA_CHECK_START:
                UpDataManager.checkVersin(theActivity,theActivity.mLoginPresenter.mLoginHandler);
                theActivity.mLoginPresenter.mLoginHandler.removeMessages(LoginHandler.EVENT_SYSTEM_UPDATA_CHECK);
                theActivity.mLoginPresenter.mLoginHandler.sendEmptyMessageDelayed(LoginHandler.EVENT_SYSTEM_UPDATA_CHECK,60*60*1000);
                break;
        }
    }
}
