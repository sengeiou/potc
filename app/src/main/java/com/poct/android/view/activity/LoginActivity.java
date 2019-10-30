package com.poct.android.view.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poct.android.handler.LoginHandler;
import com.poct.android.manager.EquipMentManager;
import com.poct.android.presenter.LoginPresenter;
import com.poct.android.utils.AppUtils;

public class LoginActivity extends BaseActivity
{
	public static final String ACTION_END_DISCOVER_DEVICE = "ACTION_END_DISCOVER_DEVICE";
	public static final String ACTION_SYSTEM_UPDATA = "ACTION_SYSTEM_UPDATA";
	public static final String ACTION_SYSTEM_UPDATA_FAIL = "ACTION_SYSTEM_UPDATA_FAIL";
	public static final String ACTION_LOGOUT = "ACTION_LOGOUT";
	public static final String LOGIN_INFO = "login_info";
	public static final String LOGIN_SAVE_PASSWORD = "login_save_password";
	public static final String LOGIN_SAVE_NAME = "login_save_name";
	public static final String LOGIN_NAME = "login_name";

	public EditText etxtName;
	public EditText etxtPassword;
	public CheckBox checkSaveName;
	public CheckBox checkSavePassword;
	public RelativeLayout btnSetting;
	public TextView btnLogin;
	public boolean isSaveName = false;
	public boolean isSavePassword = false;
	public LoginPresenter mLoginPresenter = new LoginPresenter(this);

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		mLoginPresenter.Create();
	}
	
	@Override
	protected void onDestroy()
	{
		mLoginPresenter.Destroy();
		super.onDestroy();
	}

	@Override
	protected void onStart()
	{
		mLoginPresenter.Start();
		super.onStart();
	}

	@Override
	protected void onStop()
	{
		super.onStop();
	}
	
	@Override
	protected void onPause()
	{
		mLoginPresenter.Pause();
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		mLoginPresenter.Resume();
		super.onResume();
	}


	public View.OnClickListener loginListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mLoginPresenter.doLogin();
		}
	};

	public View.OnClickListener settingListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mLoginPresenter.doSetting();
		}
	};

	public CompoundButton.OnCheckedChangeListener saveNameChecked = new CompoundButton.OnCheckedChangeListener()
	{

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			mLoginPresenter.seveNameChecked(isChecked);
		}
	};

	public CompoundButton.OnCheckedChangeListener savePasswordChecked = new CompoundButton.OnCheckedChangeListener()
	{

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			mLoginPresenter.sevePasswordChecked(isChecked);
		}
	};

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		switch (requestCode) {
			case LoginHandler.PERMISSION_REQUEST_COARSE_LOCATION:
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// MyPermission Granted
					AppUtils.getPermission(Manifest.permission.ACCESS_FINE_LOCATION,this, LoginHandler.PERMISSION_REQUEST_ACCESS_FINE_LOCATION,this.mLoginPresenter.mLoginHandler);
				} else {
				}
				break;
			case LoginHandler.PERMISSION_REQUEST_ACCESS_FINE_LOCATION:
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// MyPermission Granted
					AppUtils.getPermission(Manifest.permission.WRITE_SETTINGS, this, LoginHandler.PERMISSION_REQUEST_WRITE_SETTINGS, mLoginPresenter.mLoginHandler);
				} else {
				}
				break;
			case LoginHandler.PERMISSION_REQUEST_WRITE_SETTINGS:
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					AppUtils.getPermission(Manifest.permission.WRITE_SECURE_SETTINGS, this, LoginHandler.PERMISSION_REQUEST_WRITE_SECURE_SETTINGS, mLoginPresenter.mLoginHandler);
					// MyPermission Granted
				} else {
				}
				break;
			case LoginHandler.PERMISSION_REQUEST_WRITE_SECURE_SETTINGS:
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// MyPermission Granted
					EquipMentManager.getInstance().scanDevice();
				} else {
				}
				break;
			default:
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}
}
