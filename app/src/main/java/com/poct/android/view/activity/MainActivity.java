package com.poct.android.view.activity;

import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.poct.android.presenter.MainPresenter;

/**
 * Created by xpx on 2017/8/18.
 */

public class MainActivity extends BaseActivity {

    public static final String ACTION_UPDATA_SETTING = "ACTION_UPDATA_SETTING";
    public MainPresenter mMainPresenter = new MainPresenter(this);
    public PopupWindow popupWindow1;
    public RelativeLayout btnTest;
    public RelativeLayout btnReport;
    public ImageView showWifi;
    public ImageView btnAudio;
    public ImageView btnSetting;
    public TextView txtTime;
    public TextView txtTime1;
    public TextView txtDete;
    public TextView txtWeek;
    public TextView txtHead;
    public ImageView btnLogout;
    public SeekBar seekBar;
    public RelativeLayout layerAudio;
    public AudioManager mAudioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mMainPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener startReportListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mMainPresenter.startReport();
        }
    };

    public View.OnClickListener startTestListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMainPresenter.startTest();
        }
    };

    public View.OnClickListener doSettingListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMainPresenter.doSetting();
        }
    };

    public View.OnClickListener doAudioListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMainPresenter.doAudio();
        }
    };

    public View.OnClickListener doLogoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMainPresenter.dologout();
        }
    };

    public SeekBar.OnSeekBarChangeListener seekBar1Listener = new SeekBar.OnSeekBarChangeListener()
    {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mMainPresenter.mMainActivity.mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM,progress,AudioManager.FLAG_PLAY_SOUND);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
}
