package com.poct.android.view.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poct.R;
import com.poct.android.manager.UpDataManager;
import com.poct.android.presenter.SettingPresenter;
import com.poct.android.thread.AddApkThread;
import com.poct.android.utils.ViewUtils;
import com.poct.android.view.PoctApplication;


public class SystemFragment extends BaseFragment {

    public static final String SAFE_CODE = "1234567890";

    public TextView txtVersion;
    public TextView txtNew;
    public TextView txtUpdata;
    public ImageView array;
    public RelativeLayout btnCheck;
    public RelativeLayout btnUpdata;
    public RelativeLayout btnSafe;
    public ProgressBar mProgressBar;
    public SettingPresenter mSettingPresenter;
    public SystemFragment() {

    }

    @SuppressLint("ValidFragment")
    public SystemFragment(SettingPresenter mSettingPresenter) {
        // Required empty public constructor
        this.mSettingPresenter = mSettingPresenter;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_system, container, false);
        txtVersion = mView.findViewById(R.id.valueversion);
        txtNew = mView.findViewById(R.id.valuenew);
        txtUpdata = mView.findViewById(R.id.updatatxtname);
        btnCheck = mView.findViewById(R.id.btn_check);
        btnUpdata = mView.findViewById(R.id.btn_updata);
        btnSafe = mView.findViewById(R.id.btn_safe);
        btnSafe.setOnClickListener(safeListener);
        mProgressBar = mView.findViewById(R.id.seekbar_sys_audio_level);
        array = mView.findViewById(R.id.updataimgarray);
        btnCheck.setOnClickListener(doCheckllListener);
        btnUpdata.setOnClickListener(doInsatallListener);
        btnUpdata.setEnabled(false);
        PackageManager packageManager = mSettingPresenter.mSettingActivity.getPackageManager();
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(
                    mSettingPresenter.mSettingActivity.getPackageName(), 0);
            txtVersion.setText("V"+packInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mSettingPresenter.mSettingActivity.waitDialog.show();
        UpDataManager.checkVersin2(mSettingPresenter.mSettingActivity,mSettingPresenter.mSettingHandler);
        return mView;
    }

    @Override
    public void onResume() {

        super.onResume();

    }

    public View.OnClickListener doInsatallListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if(UpDataManager.isInstall == false)
            {
                AddApkThread mAddApkThread = new AddApkThread(mSettingPresenter.mSettingActivity, PoctApplication.mApp.getNewAppPath()+"/doctest.apk",mSettingPresenter.mSettingHandler);
                mAddApkThread.start();
            }
            else
            {
                ViewUtils.showMessage(mSettingPresenter.mSettingActivity,mSettingPresenter.mSettingActivity.getString(R.string.setting_title_system_install_already));
            }
        }
    };

    public View.OnClickListener doCheckllListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mSettingPresenter.mSettingActivity.waitDialog.show();
            UpDataManager.checkVersin2(mSettingPresenter.mSettingActivity,mSettingPresenter.mSettingHandler);
        }
    };

    public View.OnClickListener safeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doSafe();
        }
    };

    public void doSafe()
    {
        View view =  mSettingPresenter.mSettingActivity.getLayoutInflater().inflate(R.layout.menu_password,null);
        EditText text = view.findViewById(R.id.valuepassword);
        ViewUtils.creatDialogTowButton(mSettingPresenter.mSettingActivity,"请输入工程模式密码","请输入密码",mSettingPresenter.mSettingActivity.getString(R.string.button_word_cancle)
                ,mSettingPresenter.mSettingActivity.getString(R.string.wifi_connect),null,new SafeListener(text),view);
    }

    class SafeListener implements DialogInterface.OnClickListener {
        public EditText password;
        public SafeListener(EditText password)
        {
            this.password = password;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {

            if(password.getText().toString().equals(SAFE_CODE))
            {
                mSettingPresenter.mSettingActivity.startActivity(new Intent(Settings.ACTION_SETTINGS));
            }
        }
    }
}
