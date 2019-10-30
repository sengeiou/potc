package com.poct.android.view.activity;

import android.app.Fragment;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.poct.android.entity.Setting;
import com.poct.android.presenter.SettingPresenter;
import com.poct.android.view.adapter.FragmentTabAdapter;
import com.poct.android.view.adapter.WifiAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xpx on 2017/8/18.
 */

public class SettingActivity extends BaseActivity {

    public static final String  UPDTAT_PROGRESS = "UPDTAT_PROGRESS";
    public static final int WIFI_CONTENT = 0;
    public static final int BLUETOOTH_CONTENT = 1;
    public static final int AUDIO_CONTENT = 2;
    public static final int EQUIPMENT_CONTENT = 3;
    public static final int TIME_CONTENT = 4;
    public static final int SYS_UPDATA = 5;
    public WifiAdapter mWifiAdapter;
    public FragmentTabAdapter tabAdapter;
    public List<Fragment> fragments = new ArrayList<Fragment>();
    public LinearLayout listSetting;
    public TextView equip;
    public TextView wifi;
    public int nowPage = WIFI_CONTENT;
    public AudioManager mAudioManager;
    public Switch switchWifi;
    public Switch switchBluetooth;
    public RelativeLayout btnBack;
    public SettingPresenter mSettingPresenter = new SettingPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSettingPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mSettingPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener setContentListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            Setting setting = (Setting) v.getTag();
            mSettingPresenter.setContent(setting.mContentId);
        }
    };

    public View.OnClickListener doBacklistener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {

            mSettingPresenter.mSettingActivity.finish();
        }
    };

//    public View.OnClickListener setequip = new View.OnClickListener()
//    {
//
//        @Override
//        public void onClick(View v) {
//            mSettingPresenter.doEuqip();
//        }
//    };
//
//    public View.OnClickListener setWifiListener = new View.OnClickListener()
//    {
//
//        @Override
//        public void onClick(View v) {
//            mSettingPresenter.setWifi();
//        }
//    };
}
