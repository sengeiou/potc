package com.poct.android.presenter;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.poct.android.view.activity.ExampleActivity;

import java.lang.ref.WeakReference;

/**
 * Created by xpx on 2017/8/18.
 */

public class ExamplePresenter implements Presenter {

    public ExampleHandler mExampleHandler;
    public ExampleActivity mExampleActivity;
    public ExamplePresenter(ExampleActivity mExampleActivity)
    {
        this.mExampleActivity = mExampleActivity;
        this.mExampleHandler = new ExampleHandler(mExampleActivity);
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
    }

    @Override
    public void Start() {

    }

    @Override
    public void Resume() {

    }

    @Override
    public void Pause() {

    }

    @Override
    public void Destroy() {

    }


    public static class ExampleHandler extends Handler {
        WeakReference<ExampleActivity> mActivity;

        ExampleHandler(ExampleActivity mExampleActivity) {
            mActivity = new WeakReference<ExampleActivity>(mExampleActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            ExampleActivity theActivity = mActivity.get();
            Intent intent = new Intent();
            switch (msg.what) {
            }

        }
    }
}
