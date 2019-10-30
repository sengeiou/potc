package com.poct.android.presenter;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.poct.android.utils.AppActivityManager;
import com.poct.android.utils.AppUtils;
import com.poct.android.view.activity.BaseActivity;

import java.util.List;


//import com.umeng.analytics.MobclickAgent;
//import cn.jpush.android.api.JPushInterface;

@SuppressLint("NewApi")
public class BasePresenter implements Presenter{

	private BaseActivity mBaseActivity;
	
	public BasePresenter(BaseActivity mBaseActivity)
	{
		
		this.mBaseActivity = mBaseActivity;
	}
	
	@Override
	public void Create() {
		// TODO Auto-generated method stub
		AppActivityManager.getAppActivityManager().addActivity(mBaseActivity);
		mBaseActivity.mGestureDetector = new GestureDetector(mBaseActivity, mBaseActivity);
		mBaseActivity.waitDialog = AppUtils.createLoadingDialog(mBaseActivity,"");
		mBaseActivity.waitDialog.setCancelable(true);
//        ToolBarHelper.setSutColor(mBaseActivity, Color.rgb(44,167,234));
		mBaseActivity.getWindow().getDecorView().setSystemUiVisibility(View.GONE);
	}
	
	@Override
	public void Start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void Resume() {
		// TODO Auto-generated method stub
		mBaseActivity.getWindow().getDecorView().setSystemUiVisibility(View.GONE);
	}

	@Override
	public void Pause() {
		// TODO Auto-generated method stub
	}

	@Override
	public void Destroy() {
		// TODO Auto-generated method stub
		mBaseActivity.waitDialog.dismiss();
		AppActivityManager.getAppActivityManager().finishActivity(mBaseActivity);

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			return true;
		}
		else {
			return false;
		}
	}

	public void setGesture(int layoutResID)
	{
		LayoutInflater mInflater = LayoutInflater.from(mBaseActivity);
		View mUserView = mInflater.inflate(layoutResID, null);
		mUserView.setOnTouchListener(mBaseActivity);
	}


	@Override
	public void initView() {
		// TODO Auto-generated method stub
		
	}
	
	public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device
        
        ActivityManager activityManager = (ActivityManager) mBaseActivity.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = mBaseActivity.getApplicationContext().getPackageName();

        List<RunningAppProcessInfo> appProcesses = activityManager
                        .getRunningAppProcesses();
        if (appProcesses == null)
                return false;

        for (RunningAppProcessInfo appProcess : appProcesses) {
                // The name of the process that this object is associated with.
                if (appProcess.processName.equals(packageName)
                                && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        return true;
                }
        }
        return false;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
	{
		return false;
	}
}
