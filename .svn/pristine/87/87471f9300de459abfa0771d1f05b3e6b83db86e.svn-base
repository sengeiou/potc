package com.poct.android.view.activity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.poct.android.presenter.BasePresenter;
import com.poct.android.view.PoctApplication;


public class BaseActivity extends Activity implements GestureDetector.OnGestureListener, View.OnTouchListener {
    public static final int EVENT_FAIL = 0;
    public static final int EVENT_IMAGE_FAIL = 7001;
    public static final int EVENT_GET_INFO = 7003;
    public BasePresenter mBasePresenter = new BasePresenter(this);
    public Dialog waitDialog;
    public GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBasePresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mBasePresenter.Destroy();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        mBasePresenter.Start();
        super.onStart();
		if(mBasePresenter.isAppOnForeground())
		{
            PoctApplication.mApp.isActivity = true;
		}
		else
		{

		}

    }

    @Override
    protected void onStop() {
        super.onStop();
		if(mBasePresenter.isAppOnForeground())
		{
			Log.d("background", "前台");
		}
		else
		{
			Log.d("background", "后台");
            PoctApplication.mApp.isActivity = false;
		}
    }

    @Override
    protected void onResume() {
        mBasePresenter.Resume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mBasePresenter.Pause();
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(mBasePresenter.onKeyDown(keyCode, event))
        {
            return true;
        }
        else
        {
            return super.onKeyDown(keyCode, event);
        }

    }

    public View.OnClickListener mBackListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            finish();
        }
    };


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mGestureDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return mBasePresenter.onFling(motionEvent, motionEvent1, v, v1);
    }


}
