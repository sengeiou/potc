package com.poct.android.presenter;


import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.TextView;

import com.poct.R;
import com.poct.android.asks.LoginAsks;
import com.poct.android.handler.LoginHandler;
import com.poct.android.manager.EquipMentManager;
import com.poct.android.receiver.BluetoothReceiver;
import com.poct.android.utils.AppUtils;
import com.poct.android.view.PoctApplication;
import com.poct.android.view.activity.LoginActivity;
import com.poct.android.view.activity.MainActivity;
import com.poct.android.view.activity.SettingActivity;

public class LoginPresenter implements Presenter {


    public LoginActivity mLoginActivity;
    public LoginHandler mLoginHandler;
    public BluetoothReceiver mBluetoothDevice;
    public LoginPresenter(LoginActivity mLoginActivity) {
        this.mLoginActivity = mLoginActivity;
        mLoginHandler = new LoginHandler(mLoginActivity);


    }

    @Override
    public void initView() {
        // TODO Autonerated method stub
        mLoginActivity.setContentView(R.layout.activity_login);
        mLoginActivity.etxtName = mLoginActivity.findViewById(R.id.etxt_name);
        mLoginActivity.etxtPassword = mLoginActivity.findViewById(R.id.etxt_password);
        mLoginActivity.checkSaveName = mLoginActivity.findViewById(R.id.check_name_save);
        mLoginActivity.checkSavePassword = mLoginActivity.findViewById(R.id.check_password_save);
        AppUtils.setBaseInputMthod(mLoginActivity);
        TextView version = mLoginActivity.findViewById(R.id.versioncode);
        PackageManager packageManager = mLoginActivity.getPackageManager();
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(
                    mLoginActivity.getPackageName(), 0);
            version.setText("V"+packInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        EquipMentManager.getInstance();
        mLoginActivity.checkSaveName.setOnCheckedChangeListener(mLoginActivity.saveNameChecked);
        mLoginActivity.checkSavePassword.setOnCheckedChangeListener(mLoginActivity.savePasswordChecked);
        mLoginActivity.btnLogin = mLoginActivity.findViewById(R.id.btn_login);
        mLoginActivity.btnLogin.setOnClickListener(mLoginActivity.loginListener);
        mLoginActivity.btnSetting = mLoginActivity.findViewById(R.id.buttom);
        mLoginActivity.btnSetting.setOnClickListener(mLoginActivity.settingListener);
        mLoginHandler.sendEmptyMessageDelayed(LoginHandler.EVENT_SYSTEM_UPDATA_CHECK_START,30*1000);
        initLogin();
    }


    @Override
    public void Start() {
        // TODO Auto-generated method stub

    }


    @Override
    public void Resume() {
        // TODO Auto-generated method stub
    }

    @Override
    public void Pause() {
        // TODO Auto-generated method stub
    }

    @Override
    public void Destroy() {
        // TODO Auto-generated method stub
        mLoginActivity.unregisterReceiver(mBluetoothDevice);
    }

    @Override
    public void Create() {
        // TODO Auto-generated method stub
        initView();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
        intentFilter.addAction(LoginActivity.ACTION_END_DISCOVER_DEVICE);
        intentFilter.addAction(LoginActivity.ACTION_LOGOUT);
        intentFilter.addAction(LoginActivity.ACTION_SYSTEM_UPDATA);
        intentFilter.addAction(LoginActivity.ACTION_SYSTEM_UPDATA_FAIL);
        mBluetoothDevice = new BluetoothReceiver(mLoginHandler);
        mLoginActivity.registerReceiver(mBluetoothDevice, intentFilter);

    }


    public void seveNameChecked(boolean ischeck)
    {
        mLoginActivity.isSaveName = ischeck;
        SharedPreferences sharedPre = mLoginActivity.getSharedPreferences(LoginActivity.LOGIN_INFO, 0);
        SharedPreferences.Editor e = sharedPre.edit();
        e.putBoolean(LoginActivity.LOGIN_SAVE_NAME,mLoginActivity.isSaveName);
        e.commit();
    }

    public void sevePasswordChecked(boolean ischeck)
    {
        mLoginActivity.isSavePassword = ischeck;
        SharedPreferences sharedPre = mLoginActivity.getSharedPreferences(LoginActivity.LOGIN_INFO, 0);
        SharedPreferences.Editor e = sharedPre.edit();
        e.putBoolean(LoginActivity.LOGIN_SAVE_PASSWORD,mLoginActivity.isSavePassword);
        e.commit();
    }

    public void doLogin() {
        if(mLoginActivity.isSaveName)
        {
            saveName();
        }
        if(mLoginActivity.isSavePassword)
        {
            savePassword();
        }
        PoctApplication.mApp.account.mAccountId = mLoginActivity.etxtName.getText().toString();
        if(mLoginActivity.etxtName.getText().toString().length() > 0)
        {
            mLoginActivity.waitDialog.show();
            LoginAsks.doLoging(mLoginActivity.etxtName.getText().toString(),mLoginActivity.etxtPassword.getText().toString()
                    ,mLoginHandler,mLoginActivity);
        }

//        startMain();
    }

    public void doLogout()
    {
        mLoginActivity.etxtName.setText("");
        mLoginActivity.etxtPassword.setText("");
        initLogin();
    }

    public void startMain() {
        Intent intent = new Intent(mLoginActivity, MainActivity.class);
        mLoginActivity.startActivity(intent);
    }

    public void doSetting() {

        Intent intent = new Intent(mLoginActivity, SettingActivity.class);
        mLoginActivity.startActivity(intent);
//        Intent intent =  new Intent(Settings.ACTION_SETTINGS);
//        mLoginActivity.startActivity(intent);
    }

    private void saveName() {
        SharedPreferences sharedPre = mLoginActivity.getSharedPreferences(LoginActivity.LOGIN_INFO, 0);
        SharedPreferences.Editor e = sharedPre.edit();
        e.putString(LoginActivity.LOGIN_NAME,mLoginActivity.etxtName.getText().toString());
        e.commit();
    }

    private void savePassword() {
        SharedPreferences sharedPre = mLoginActivity.getSharedPreferences(LoginActivity.LOGIN_INFO, 0);
        SharedPreferences.Editor e = sharedPre.edit();
        String name = mLoginActivity.etxtName.getText().toString();
        if(name.length() > 0)
        e.putString(name,mLoginActivity.etxtPassword.getText().toString());
        e.commit();
    }

    private void initLogin()
    {
        SharedPreferences sharedPre = mLoginActivity.getSharedPreferences(LoginActivity.LOGIN_INFO, 0);
        if(sharedPre.contains(LoginActivity.LOGIN_SAVE_NAME))
        mLoginActivity.isSaveName = sharedPre.getBoolean(LoginActivity.LOGIN_SAVE_NAME,false);
        mLoginActivity.isSavePassword = sharedPre.getBoolean(LoginActivity.LOGIN_SAVE_PASSWORD,false);
        mLoginActivity.checkSaveName.setChecked(mLoginActivity.isSaveName);
        mLoginActivity.checkSavePassword.setChecked(mLoginActivity.isSavePassword);
        if(mLoginActivity.isSaveName)
        {
            String name = sharedPre.getString(LoginActivity.LOGIN_NAME,"");
            mLoginActivity.etxtName.setText(name);
            if(name.length() > 0 && mLoginActivity.isSavePassword)
                mLoginActivity.etxtPassword.setText(sharedPre.getString(name,""));
        }
    }

}
