package com.poct.android.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.poct.R;
import com.poct.android.handler.MainHandler;
import com.poct.android.manager.WifiSetManager;
import com.poct.android.receiver.MainReceiver;
import com.poct.android.utils.AppUtils;
import com.poct.android.view.PoctApplication;
import com.poct.android.view.activity.LoginActivity;
import com.poct.android.view.activity.MainActivity;
import com.poct.android.view.activity.ReportCenterActivity;
import com.poct.android.view.activity.SettingActivity;
import com.poct.android.view.activity.TestCenterActivity;

/**
 * Created by xpx on 2017/8/18.
 */

public class MainPresenter implements Presenter {

    public MainHandler mMainHandler;
    public MainActivity mMainActivity;
    public MainReceiver mMainReceiver;
    public MainPresenter(MainActivity mMainActivity)
    {
        this.mMainActivity = mMainActivity;
        this.mMainHandler = new MainHandler(mMainActivity);
        this.mMainReceiver= new MainReceiver(mMainHandler);
    }

    @Override
    public void Create() {
        initView();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);//监听wifi是开关变化的状态
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);//监听wifiwifi连接状态广播
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);//监听wifi列表变化
        intentFilter.addAction(MainActivity.ACTION_UPDATA_SETTING);
        mMainActivity.registerReceiver(mMainReceiver,intentFilter);
    }

    @Override
    public void initView() {
        mMainActivity.setContentView(R.layout.activity_mian);
        mMainActivity.mAudioManager = (AudioManager) mMainActivity.getSystemService(Context.AUDIO_SERVICE);
        mMainActivity.btnTest = mMainActivity.findViewById(R.id.funtest);
        mMainActivity.btnReport = mMainActivity.findViewById(R.id.funreport);
        mMainActivity.showWifi = mMainActivity.findViewById(R.id.iconwifi);
        mMainActivity.btnAudio = mMainActivity.findViewById(R.id.iconaudio);
        mMainActivity.btnSetting = mMainActivity.findViewById(R.id.iconsetting);
        mMainActivity.txtTime = mMainActivity.findViewById(R.id.txttime);
        mMainActivity.txtTime1 = mMainActivity.findViewById(R.id.txttime1);
        mMainActivity.txtDete = mMainActivity.findViewById(R.id.txtdete);
        mMainActivity.txtWeek = mMainActivity.findViewById(R.id.txtweek);
        mMainActivity.txtHead = mMainActivity.findViewById(R.id.txtname);
        mMainActivity.btnLogout = mMainActivity.findViewById(R.id.imagelogout);
        mMainActivity.txtHead.setText(PoctApplication.mApp.account.mName);
//        updataAudio();
        updataWeek();
        updataTime();
        updataDete();
        updataWifi2();
        mMainActivity.btnTest.setOnClickListener(mMainActivity.startTestListener);
        mMainActivity.btnReport.setOnClickListener(mMainActivity.startReportListener);
        mMainActivity.btnSetting.setOnClickListener(mMainActivity.doSettingListener);
        mMainActivity.btnAudio.setOnClickListener(mMainActivity.doAudioListener);
        mMainActivity.btnLogout.setOnClickListener(mMainActivity.doLogoutListener);
    }

    @Override
    public void Start() {

    }

    @Override
    public void Resume() {
        mMainHandler.removeMessages(MainHandler.TIME_UPDATE);
        mMainHandler.sendEmptyMessageDelayed(MainHandler.TIME_UPDATE,1000);
    }

    @Override
    public void Pause() {
        mMainHandler.removeMessages(MainHandler.TIME_UPDATE);
    }

    @Override
    public void Destroy() {
        if(mMainHandler != null)
        mMainHandler.removeMessages(MainHandler.TIME_UPDATE);
        if(mMainHandler != null)
        mMainHandler.removeMessages(MainHandler.DETE_UPDATE);
        if(mMainHandler != null)
        mMainHandler.removeMessages(MainHandler.DETE_UPDATE);
        if(mMainHandler != null)
        mMainHandler.removeMessages(MainHandler.WIFI_CLOSE);
        if(mMainHandler != null)
        mMainHandler.removeMessages(MainHandler.WIFI_OPEN);
        if(mMainHandler != null)
        mMainHandler.removeMessages(MainHandler.WIFI_UPDATA);
        if(mMainHandler != null)
        mMainHandler.removeMessages(MainHandler.WIFI_CONNECT_SUCCESS);
        if(mMainHandler != null)
        mMainHandler.removeMessages(MainHandler.WIFI_CONNECT_FAIL);
        if(mMainHandler != null)
        mMainHandler.removeMessages(MainHandler.WIFI_CONNECT_ING);
        mMainActivity.unregisterReceiver(mMainReceiver);
    }

    public void startReport()
    {
        if(AppUtils.gotoMain(mMainActivity) == false)
        {
            Intent intent = new Intent(mMainActivity, ReportCenterActivity.class);
            mMainActivity.startActivity(intent);
        }

    }

    public void startTest()
    {
        if(AppUtils.gotoMain(mMainActivity) == false)
        {
            Intent intent = new Intent(mMainActivity, TestCenterActivity.class);
            mMainActivity.startActivity(intent);
        }

    }

    public void updataTime() {
        if(mMainHandler != null)
        mMainHandler.removeMessages(MainHandler.TIME_UPDATE);
        if(DateFormat.is24HourFormat(mMainActivity))
        {
            if(mMainActivity.txtTime1 != null)
            {
                if(mMainActivity.txtTime1.getVisibility() == View.VISIBLE) {
                    mMainActivity.txtTime1.setVisibility(View.INVISIBLE);
                }
                if(mMainActivity.txtTime.getVisibility() == View.INVISIBLE) {
                    mMainActivity.txtTime.setVisibility(View.VISIBLE);
                }
                mMainActivity.txtTime.setText(AppUtils.getTimeSecond());
            }
        }
        else
        {
            if(mMainActivity.txtTime != null)
            {
                if(mMainActivity.txtTime.getVisibility() == View.VISIBLE) {
                    mMainActivity.txtTime.setVisibility(View.INVISIBLE);
                }
                if(mMainActivity.txtTime1.getVisibility() == View.INVISIBLE) {
                    mMainActivity.txtTime1.setVisibility(View.VISIBLE);
                }
                mMainActivity.txtTime1.setText(AppUtils.getTimeSecond12());
            }
        }
        if(PoctApplication.mApp.day != AppUtils.getday())
        {
            PoctApplication.mApp.day = AppUtils.getday();
            if(mMainHandler != null)
            {
                mMainHandler.removeMessages(MainHandler.DETE_UPDATE);
                mMainHandler.sendEmptyMessageDelayed(MainHandler.DETE_UPDATE,1000);
            }
        }
        if(mMainHandler != null)
        mMainHandler.sendEmptyMessageDelayed(MainHandler.TIME_UPDATE,1000);
    }

    public void updataDete() {
        if(mMainHandler != null)
        mMainHandler.removeMessages(MainHandler.DETE_UPDATE);

        if(mMainActivity.txtDete != null)
        {
            if(!mMainActivity.txtDete.getText().toString().equals(AppUtils.getDate3()))
            {
                Intent intent = new Intent();
                intent.setAction(TestCenterActivity.ACTION_UPDETE_LIST);
                mMainActivity.sendBroadcast(intent);
            }
            mMainActivity.txtDete.setText(AppUtils.getDate3());
        }
        if(mMainActivity.txtWeek != null)
            mMainActivity.txtWeek.setText(AppUtils.getWeek());
        if(mMainHandler != null)
        mMainHandler.sendEmptyMessageDelayed(MainHandler.DETE_UPDATE,30*60*1000);
    }

    public void updataWeek() {
        if(mMainHandler != null)
        mMainHandler.removeMessages(MainHandler.DETE_UPDATE);
        if(mMainActivity.txtWeek != null)
        mMainActivity.txtWeek.setText(AppUtils.getWeek());
        if(mMainHandler != null)
        mMainHandler.sendEmptyMessageDelayed(MainHandler.DETE_UPDATE,30*60*1000);
    }

    public void updataAudio() {
        mMainActivity.seekBar.setProgress(mMainActivity.mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM));
    }

    public void updataWifi() {
        if(mMainActivity.showWifi != null)
        {
            if(WifiSetManager.getInstance().isWifiConnect())
            {
                mMainActivity.showWifi.setImageResource(R.drawable.tool_wifi);
            }
            else
            {
                mMainActivity.showWifi.setImageResource(R.drawable.tool_badwifi);
            }
        }
    }

    public void updataWifi2() {
        if(mMainHandler != null)
        mMainHandler.removeMessages(MainHandler.WIFI_UPDATA_ALWAYS);
        if(mMainActivity.showWifi != null)
        {
            if(WifiSetManager.getInstance().isWifiConnect())
            {
                mMainActivity.showWifi.setImageResource(R.drawable.tool_wifi);
            }
            else
            {
                mMainActivity.showWifi.setImageResource(R.drawable.tool_badwifi);
            }
        }
        if(mMainHandler != null)
        mMainHandler.sendEmptyMessageDelayed(MainHandler.WIFI_UPDATA_ALWAYS,60*1000);
    }

    public void doSetting() {
        Intent intent = new Intent(mMainActivity, SettingActivity.class);
        mMainActivity.startActivity(intent);
    }

    public void doAudio() {
//        if(mMainActivity.layerAudio.getVisibility() == View.INVISIBLE)
//        {
//            mMainActivity.layerAudio.setVisibility(View.VISIBLE);
//        }
//        else
//        {
//            mMainActivity.layerAudio.setVisibility(View.INVISIBLE);
//        }
        creatAutio();
    }

    public void dologout() {
        mMainActivity.finish();
        mMainActivity.sendBroadcast(new Intent(LoginActivity.ACTION_LOGOUT));
    }

    public void creatAutio()
    {
        View popupWindowView = LayoutInflater.from(mMainActivity).inflate(R.layout.audio_menu, null);
        RelativeLayout lsyer = (RelativeLayout) popupWindowView.findViewById(R.id.layer);
        lsyer.setFocusable(true);
        lsyer.setFocusableInTouchMode(true);
        mMainActivity.popupWindow1 = new PopupWindow(popupWindowView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
        popupWindowView.setFocusable(true);
        popupWindowView.setFocusableInTouchMode(true);
//        mMainActivity.popupWindow1.setAnimationStyle(R.style.PopupAnimation);
        final PopupWindow finalPopupWindow = mMainActivity.popupWindow1;
        lsyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalPopupWindow.dismiss();

            }
        });
        RelativeLayout layout2 = (RelativeLayout) popupWindowView.findViewById(R.id.layer2);
        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ColorDrawable dw = new ColorDrawable(0x00ffffff);
        mMainActivity.popupWindow1.setBackgroundDrawable(dw);
        mMainActivity.layerAudio = popupWindowView.findViewById(R.id.autio_setting);
        mMainActivity.btnLogout = popupWindowView.findViewById(R.id.imagelogout);
        mMainActivity.seekBar = popupWindowView.findViewById(R.id.seekbar_sys_audio_level);
        mMainActivity.seekBar.setMax(mMainActivity.mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM));
        mMainActivity.seekBar.setOnSeekBarChangeListener(mMainActivity.seekBar1Listener);
        updataAudio();
//        RelativeLayout mRelativeLayout = popupWindowView.findViewById(R.id.head);
//        mRelativeLayout.setOnClickListener(mTestCenterActivity.doOklistener);
        mMainActivity.popupWindow1.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                mMainActivity.shade.setVisibility(View.INVISIBLE);
            }
        });
        mMainActivity.popupWindow1.showAtLocation(mMainActivity.findViewById(R.id.activity_main),
                Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
        //mMainActivity.shade.setVisibility(View.VISIBLE);

    }
}
