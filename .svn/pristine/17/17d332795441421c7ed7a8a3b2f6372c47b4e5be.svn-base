package com.poct.android.view.activity;

import android.os.Bundle;

import com.poct.android.presenter.ExamplePresenter;

/**
 * Created by xpx on 2017/8/18.
 */

public class ExampleActivity extends BaseActivity {

    public ExamplePresenter mExamplePresenter = new ExamplePresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mExamplePresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mExamplePresenter.Destroy();
        super.onDestroy();
    }
}
