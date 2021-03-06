package com.poct.android.presenter;

import android.app.AlarmManager;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.poct.R;
import com.poct.android.entity.EquipmentItem;
import com.poct.android.entity.Setting;
import com.poct.android.entity.WifiBean;
import com.poct.android.handler.SettingHandler;
import com.poct.android.manager.BluetoothSetManager;
import com.poct.android.manager.EquipMentManager;
import com.poct.android.manager.PrintManager;
import com.poct.android.manager.UpDataManager;
import com.poct.android.manager.WifiSetManager;
import com.poct.android.receiver.WifiReceiver;
import com.poct.android.utils.DBHelper;
import com.poct.android.utils.ViewUtils;
import com.poct.android.view.PoctApplication;
import com.poct.android.view.activity.LoginActivity;
import com.poct.android.view.activity.MainActivity;
import com.poct.android.view.activity.SettingActivity;
import com.poct.android.view.adapter.FragmentTabAdapter;
import com.poct.android.view.adapter.WifiAdapter;
import com.poct.android.view.fragment.AudioFragment;
import com.poct.android.view.fragment.BluetoothFragment;
import com.poct.android.view.fragment.EquipmentFragment;
import com.poct.android.view.fragment.SystemFragment;
import com.poct.android.view.fragment.TimeFragment;
import com.poct.android.view.fragment.WifiFragment;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xpx on 2017/8/18.
 */

public class SettingPresenter implements Presenter {

    public SettingHandler mSettingHandler;
    public SettingActivity mSettingActivity;
    public WifiReceiver mWifiReceiver;
    public SettingPresenter(SettingActivity mSettingActivity)
    {
        this.mSettingActivity = mSettingActivity;
        this.mSettingHandler = new SettingHandler(mSettingActivity);
        this.mWifiReceiver= new WifiReceiver(mSettingHandler);
    }

    @Override
    public void Create() {
        initView();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);//监听wifi是开关变化的状态
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);//监听wifiwifi连接状态广播
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);//监听wifi列表变化
        intentFilter.addAction(SettingActivity.UPDTAT_PROGRESS);//监听wifi列表变化
        intentFilter.addAction(LoginActivity.ACTION_SYSTEM_UPDATA);//监听wifi列表变化
        mSettingActivity.registerReceiver(mWifiReceiver,intentFilter);
    }

    @Override
    public void initView() {
        mSettingActivity.setContentView(R.layout.activity_setting);
        mSettingActivity.mAudioManager = (AudioManager) mSettingActivity.getSystemService(Context.AUDIO_SERVICE);
        mSettingActivity.listSetting = mSettingActivity.findViewById(R.id.list_teb);
        mSettingActivity.btnBack = mSettingActivity.findViewById(R.id.layerback);
        mSettingActivity.mWifiAdapter = new WifiAdapter(mSettingActivity, WifiSetManager.getInstance().wifiList, mSettingHandler);
        initSettingItem(new Setting(mSettingActivity.getString(R.string.setting_title_wifi)
                ,R.drawable.wifi,SettingActivity.WIFI_CONTENT,true),new WifiFragment(this));
        initSettingItem(new Setting(mSettingActivity.getString(R.string.setting_title_bluetooth)
                ,R.drawable.bluetooth,SettingActivity.BLUETOOTH_CONTENT,true),new BluetoothFragment(this));
        initSettingItem(new Setting(mSettingActivity.getString(R.string.setting_title_audio)
                ,R.drawable.audio,SettingActivity.AUDIO_CONTENT,false),new AudioFragment(this));
        initSettingItem(new Setting(mSettingActivity.getString(R.string.setting_title_equipment)
                ,R.drawable.equipment,SettingActivity.EQUIPMENT_CONTENT,false),new EquipmentFragment(this));
        initSettingItem(new Setting(mSettingActivity.getString(R.string.setting_title_time)
                ,R.drawable.time,SettingActivity.TIME_CONTENT,false),new TimeFragment(this));
        initSettingItem(new Setting(mSettingActivity.getString(R.string.setting_system)
                ,R.drawable.system,SettingActivity.SYS_UPDATA,false),new SystemFragment(this));
        mSettingActivity.tabAdapter = new FragmentTabAdapter(mSettingActivity, mSettingActivity.fragments, R.id.tab_content);
        mSettingActivity.btnBack.setOnClickListener(mSettingActivity.doBacklistener);
        setContent(SettingActivity.WIFI_CONTENT);
        updataTime();
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
        mSettingActivity.unregisterReceiver(mWifiReceiver);
        mSettingHandler.removeMessages(SettingHandler.TIME_UPDATE);
        mSettingActivity.sendBroadcast(new Intent(MainActivity.ACTION_UPDATA_SETTING));
    }

    public void setContent(int id) {

        if(id != mSettingActivity.nowPage)
        {
            mSettingActivity.nowPage = id;
            mSettingActivity.tabAdapter.onCheckedChanged(id);
        }

    }

    //wifi
    public CompoundButton.OnCheckedChangeListener wifiSwichListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(WifiSetManager.getInstance().mainWifi.isWifiEnabled() != isChecked)
            {
                if(isChecked == true) {
                    WifiSetManager.getInstance().openWifi();
                }
                else {
                    WifiSetManager.getInstance().closeWifi();
                }
            }
            if(mSettingActivity.fragments.size() > 0)
            {
                WifiFragment mWifiFragment = (WifiFragment) mSettingActivity.fragments.get(SettingActivity.WIFI_CONTENT);
                if(mWifiFragment != null)
                {
                    if(mWifiFragment.switchButtonWifi != null)
                    {
                        if(mWifiFragment.switchButtonWifi.isChecked() != isChecked)
                        mWifiFragment.switchButtonWifi.setChecked(isChecked);
                    }
                }
            }
            if(mSettingActivity.switchWifi.isChecked() != isChecked)
                mSettingActivity.switchWifi.setChecked(isChecked);

        }
    };

    public void clickWifi(WifiBean mWifiBean) {

        if(mWifiBean.state == WifiSetManager.WIFI_STATE_CONNECT)
        {

            View view =  mSettingActivity.getLayoutInflater().inflate(R.layout.menu_wifi,null);

            ViewUtils.creatDialogTowButton(mSettingActivity,"",mWifiBean.wifiName,mSettingActivity.getString(R.string.button_word_cancle)
                    ,mSettingActivity.getString(R.string.wifi_nosave),null,new NosaveListener(mWifiBean),view);
        }
        else if(mWifiBean.state == WifiSetManager.WIFI_STATE_UNCONNECT)
        {
            View view;
            if(mWifiBean.saved)
            {
                view =  mSettingActivity.getLayoutInflater().inflate(R.layout.menu_wifi,null);

                ViewUtils.creatDialogTowButton(mSettingActivity,"",mWifiBean.wifiName,mSettingActivity.getString(R.string.button_word_cancle)
                        ,mSettingActivity.getString(R.string.wifi_connect),mSettingActivity.getString(R.string.wifi_nosave),null,new ConnectListener(mWifiBean,null),new NosaveListener(mWifiBean),view);
            }
            else
            {
                view =  mSettingActivity.getLayoutInflater().inflate(R.layout.menu_wifi_password,null);
                EditText text = view.findViewById(R.id.valuepassword);
                ViewUtils.creatDialogTowButton(mSettingActivity,"",mWifiBean.wifiName,mSettingActivity.getString(R.string.button_word_cancle)
                        ,mSettingActivity.getString(R.string.wifi_connect),null,new ConnectListener(mWifiBean,text),view);
            }

        }
    }

    class ConnectListener implements DialogInterface.OnClickListener {
        public WifiBean mWifiBean;
        public EditText password;
        public ConnectListener(WifiBean mWifiBean,EditText password)
        {
            this.mWifiBean = mWifiBean;
            this.password = password;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            String pass = "";
            if(password != null)
            {
                pass = password.getText().toString();
            }
//            if(WifiSetManager.getInstance().mWifiBeanConnect != null)
//            {
//                WifiSetManager.getInstance().mWifiBeanConnect.state = WifiSetManager.WIFI_STATE_UNCONNECT;
//                if(mSettingHandler != null)
//                    mSettingHandler.sendEmptyMessage(SettingHandler.WIFI_UPDATA_LIST);
//            }
            WifiSetManager.getInstance().connectNetWork(mWifiBean,pass);
            if(mSettingHandler != null)
            mSettingHandler.sendEmptyMessage(SettingHandler.WIFI_UPDATA_LIST);
        }
    }

    class NosaveListener implements DialogInterface.OnClickListener {
        public WifiBean mWifiBean;

        public NosaveListener(WifiBean mWifiBean)
        {
            this.mWifiBean = mWifiBean;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            WifiSetManager.getInstance().removeNetwork(mWifiBean);
            if(mSettingHandler != null)
                mSettingHandler.sendEmptyMessage(SettingHandler.WIFI_UPDATA_LIST);
        }
    }

    //bluetooth
    public CompoundButton.OnCheckedChangeListener bluetoothSwichListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(BluetoothSetManager.getInstance().blueToothAdapter.isEnabled() != isChecked)
            {
                if(isChecked == true) {
                    BluetoothSetManager.getInstance().blueToothAdapter.enable();
                }
                else {
                    BluetoothSetManager.getInstance().stopDisvery();
                    BluetoothSetManager.getInstance().stopLeScan();
                    if(BluetoothSetManager.getInstance().mBluetoothGatt != null)
                    BluetoothSetManager.getInstance().mBluetoothGatt.close();
//                    if(BluetoothSetManager.getInstance().netBluetoothDevice != null)
//                    {
//                        EquipMentManager.getInstance().disConnectPotc();
//                    }
                    BluetoothSetManager.getInstance().blueToothAdapter.disable();
                }
            }
            if(mSettingActivity.fragments.size() > 1)
            {
                BluetoothFragment mBluetoothFragment = (BluetoothFragment) mSettingActivity.fragments.get(SettingActivity.BLUETOOTH_CONTENT);
                if(mBluetoothFragment != null)
                {
                    if(mBluetoothFragment.switchButtonBluetooth != null)
                    {
                        if(mBluetoothFragment.switchButtonBluetooth.isChecked() != isChecked)
                        mBluetoothFragment.switchButtonBluetooth.setChecked(isChecked);
                    }
                }
            }
            if(mSettingActivity.switchBluetooth.isChecked() != isChecked)
                mSettingActivity.switchBluetooth.setChecked(isChecked);
        }
    };

    //equipment
    public void setPrint() {
        View view =  mSettingActivity.getLayoutInflater().inflate(R.layout.menu_set_print,null);
        EditText text = view.findViewById(R.id.valuepassword);
        if(EquipMentManager.getInstance().devicePrint != null)
        {
            text.setText(EquipMentManager.getInstance().devicePrint.id);
        }
        ViewUtils.creatDialogTowButton(mSettingActivity,mSettingActivity.getString(R.string.equip_print_set_mess),mSettingActivity.getString(R.string.equip_print_set)
                ,mSettingActivity.getString(R.string.button_word_cancle),mSettingActivity.getString(R.string.button_word_ok),null,new PrintListener(text),view);
    }

    public void setPotc() {
        View view =  mSettingActivity.getLayoutInflater().inflate(R.layout.menu_set_potc,null);
        EditText text = view.findViewById(R.id.valuepassword);
        if(EquipMentManager.getInstance().devicePotc != null)
        {
            text.setText(EquipMentManager.getInstance().devicePotc.id);
        }
        ViewUtils.creatDialogTowButton(mSettingActivity,mSettingActivity.getString(R.string.equip_print_potc_mess),mSettingActivity.getString(R.string.equip_print_device_set)
                ,mSettingActivity.getString(R.string.button_word_cancle),mSettingActivity.getString(R.string.button_word_ok),null,new PotcListener(text),view);
    }

    public void setFada() {
        View view =  mSettingActivity.getLayoutInflater().inflate(R.layout.menu_set_fada,null);
        EditText text = view.findViewById(R.id.valuepassword);
        if(EquipMentManager.getInstance().deviceFada != null)
        {
            text.setText(EquipMentManager.getInstance().deviceFada.id);
        }
        ViewUtils.creatDialogTowButton(mSettingActivity,mSettingActivity.getString(R.string.equip_print_fada_mess),mSettingActivity.getString(R.string.equip_print_device_set)
                ,mSettingActivity.getString(R.string.button_word_cancle),mSettingActivity.getString(R.string.button_word_ok),null,new FadaListener(text),view);
    }

    class PrintListener implements DialogInterface.OnClickListener {
        public EditText ip;
        public PrintListener(EditText ip)
        {
            this.ip = ip;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            String ipadd = "";
            if(ip != null)
            {
                ipadd = ip.getText().toString();
            }
            Pattern p=  Pattern.compile("((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)");
            Matcher m=p.matcher(ipadd);
            boolean b=m.matches();
            if(b == true)
            {
                if(EquipMentManager.getInstance().devicePrint == null)
                {
                    EquipMentManager.getInstance().devicePrint = new EquipmentItem();
                    EquipMentManager.getInstance().devicePrint.type = EquipMentManager.DEVICE_PRINT;
                    EquipMentManager.getInstance().devicePrint.id = ipadd;
                    EquipMentManager.getInstance().devicePrint.mAddress = "9100";
                    EquipMentManager.getInstance().devicePrint.mName = mSettingActivity.getString(R.string.device_print_p2200w);
                    DBHelper.getInstance(mSettingActivity).upDateDevice(EquipMentManager.getInstance().devicePrint);
                    if(mSettingHandler != null)
                        mSettingHandler.sendEmptyMessage(SettingHandler.EQUIP_UPDATE_PRINT);

                    PrintManager.getInstance().startPrintNewThread1();
                }
                else
                {
                    if(!EquipMentManager.getInstance().devicePrint.id.equals(ipadd))
                    {
                        EquipMentManager.getInstance().devicePrint.id = ipadd;
                        DBHelper.getInstance(mSettingActivity).upDateDevice(EquipMentManager.getInstance().devicePrint);
                        if(mSettingHandler != null)
                            mSettingHandler.sendEmptyMessage(SettingHandler.EQUIP_UPDATE_PRINT);

                    }
                    PrintManager.getInstance().startPrintNewThread1();
                }
            }
            else
            {
                ViewUtils.showMessage(mSettingActivity,mSettingActivity.getString(R.string.error_ip_wrong));
            }
        }
    }

    class PotcListener implements DialogInterface.OnClickListener {
        public EditText ip;
        public PotcListener(EditText ip)
        {
            this.ip = ip;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            String ipadd = "";
            if(ip != null)
            {
                ipadd = ip.getText().toString();
            }
            if(EquipMentManager.getInstance().devicePotc == null)
            {
                EquipMentManager.getInstance().devicePotc = new EquipmentItem();
                EquipMentManager.getInstance().devicePotc.type = EquipMentManager.DEVICE_POTC;
                EquipMentManager.getInstance().devicePotc.id = ipadd;
                EquipMentManager.getInstance().devicePotc.mName = mSettingActivity.getString(R.string.device_potc);
                DBHelper.getInstance(mSettingActivity).upDateDevice(EquipMentManager.getInstance().devicePotc);
                if(mSettingHandler != null)
                    mSettingHandler.sendEmptyMessage(SettingHandler.EQUIP_UPDATE_POTC);
            }
            else
            {
                if(!EquipMentManager.getInstance().devicePotc.id.equals(ipadd))
                {
                    EquipMentManager.getInstance().devicePotc.id = ipadd;
                    EquipMentManager.getInstance().devicePotc.device = null;
                    EquipMentManager.getInstance().devicePotc.mAddress = "";
                    DBHelper.getInstance(mSettingActivity).upDateDevice(EquipMentManager.getInstance().devicePotc);
                    if(mSettingHandler != null)
                        mSettingHandler.sendEmptyMessage(SettingHandler.EQUIP_UPDATE_POTC);

                }

            }

        }
    }

    class FadaListener implements DialogInterface.OnClickListener {
        public EditText ip;
        public FadaListener(EditText ip)
        {
            this.ip = ip;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            String ipadd = "";
            if(ip != null)
            {
                ipadd = ip.getText().toString();
            }
            if(EquipMentManager.getInstance().deviceFada == null)
            {
                EquipMentManager.getInstance().deviceFada = new EquipmentItem();
                EquipMentManager.getInstance().deviceFada.type = EquipMentManager.DEVICE_FADA;
                EquipMentManager.getInstance().deviceFada.id = ipadd;
                EquipMentManager.getInstance().deviceFada.mName = mSettingActivity.getString(R.string.device_fada);
                DBHelper.getInstance(mSettingActivity).upDateDevice(EquipMentManager.getInstance().deviceFada);
                if(mSettingHandler != null)
                    mSettingHandler.sendEmptyMessage(SettingHandler.EQUIP_UPDATE_FADA);
            }
            else
            {
                if(!EquipMentManager.getInstance().deviceFada.id.equals(ipadd))
                {
                    EquipMentManager.getInstance().deviceFada.id = ipadd;
                    EquipMentManager.getInstance().deviceFada.device = null;
                    EquipMentManager.getInstance().deviceFada.mAddress = "";
                    DBHelper.getInstance(mSettingActivity).upDateDevice(EquipMentManager.getInstance().deviceFada);
                    if(mSettingHandler != null)
                        mSettingHandler.sendEmptyMessage(SettingHandler.EQUIP_UPDATE_FADA);

                }

            }

        }
    }

    public void upDataPrint() {
        if(mSettingActivity.fragments.size() > 3)
        {
            EquipmentFragment mEquipmentFragment = (EquipmentFragment) mSettingActivity.fragments.get(SettingActivity.EQUIPMENT_CONTENT);
            if(mEquipmentFragment != null)
            {
                if(mEquipmentFragment.txtPrint!= null)
                {
                    if(EquipMentManager.getInstance().devicePrint != null)
                    mEquipmentFragment.txtPrint.setText("IP:"+EquipMentManager.getInstance().devicePrint.id);
                }
            }
        }
    }

    public void upDataPotc() {
        if(mSettingActivity.fragments.size() > 3)
        {
            EquipmentFragment mEquipmentFragment = (EquipmentFragment) mSettingActivity.fragments.get(SettingActivity.EQUIPMENT_CONTENT);
            if(mEquipmentFragment != null)
            {
                if(mEquipmentFragment.txtPotc!= null)
                {
                    if(EquipMentManager.getInstance().devicePotc != null)
                        mEquipmentFragment.txtPotc.setText("ID:"+EquipMentManager.getInstance().devicePotc.id);
                }
            }
        }
    }

    public void upDataFada() {
        if(mSettingActivity.fragments.size() > 3)
        {
            EquipmentFragment mEquipmentFragment = (EquipmentFragment) mSettingActivity.fragments.get(SettingActivity.EQUIPMENT_CONTENT);
            if(mEquipmentFragment != null)
            {
                if(mEquipmentFragment.txtFada!= null)
                {
                    if(EquipMentManager.getInstance().deviceFada != null)
                        mEquipmentFragment.txtFada.setText("SN:"+EquipMentManager.getInstance().deviceFada.id);
                }
            }
        }
    }

    //time
    public void updataTime() {

        if(mSettingHandler != null)
        mSettingHandler.removeMessages(SettingHandler.TIME_UPDATE);
        if(mSettingActivity.fragments.size() > 4)
        {
            TimeFragment mTimeFragment = (TimeFragment) mSettingActivity.fragments.get(SettingActivity.TIME_CONTENT);
            if(mTimeFragment != null)
            {
                if(mTimeFragment.btnTime!= null)
                {
                    mTimeFragment.updateTime();
                }
            }
        }
        if(mSettingHandler != null)
        {
            mSettingHandler.sendEmptyMessageDelayed(SettingHandler.TIME_UPDATE,20000);
        }
    }

//    public void setSystemTime() {
//        long currentTime = System.currentTimeMillis();
//        Date date = new Date(currentTime);
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//        return formatter.format(date);
//    }
//
//    public static boolean setSytemTime(long value){
//        long settingTime = value;
//        if (value/1000 < Integer.MAX_VALUE){
//            SystemClock.setCurrentTimeMillis(settingTime);
//        }
//    }

    private void initSettingItem(Setting setting, Fragment fragment) {
        LayoutInflater mInflater = (LayoutInflater) mSettingActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.item_setting, null);
        view.setTag(setting);
        ImageView imageView = view.findViewById(R.id.img);
        TextView textView = view.findViewById(R.id.txtname);
        imageView.setImageResource(setting.mImageId);
        textView.setText(setting.mName);
        if(setting.isSwitch == true)
        {
            Switch ms = view.findViewById(R.id.btn_swich);
            if(setting.mContentId == SettingActivity.WIFI_CONTENT)
            {
                mSettingActivity.switchWifi = ms;
                ms.setOnCheckedChangeListener(wifiSwichListener);
                if(WifiSetManager.getInstance().mainWifi.isWifiEnabled() == true)
                {
                    ms.setChecked(true);
                }
                else
                {
                    ms.setChecked(false);
                }
            }
            else if(setting.mContentId == SettingActivity.BLUETOOTH_CONTENT)
            {
                mSettingActivity.switchBluetooth = ms;
                ms.setOnCheckedChangeListener(bluetoothSwichListener);
                if(BluetoothSetManager.getInstance().blueToothAdapter.isEnabled() == true)
                {
                    ms.setChecked(true);
                }
                else
                {
                    ms.setChecked(false);
                }
            }
        }
        else
        {
            Switch ms = view.findViewById(R.id.btn_swich);
            ms.setVisibility(View.INVISIBLE);
        }

        mSettingActivity.fragments.add(fragment);
        view.setOnClickListener(mSettingActivity.setContentListener);
        mSettingActivity.listSetting.addView(view);
    }

    public void setSysDate(int year,int month,int day){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        long when = c.getTimeInMillis();
        if(when / 1000 < Integer.MAX_VALUE){
            ((AlarmManager)mSettingActivity.getSystemService(Context.ALARM_SERVICE)).setTime(when);
        }
    }

    public void setSysTime(int hour,int minute){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long when = c.getTimeInMillis();
        if(when / 1000 < Integer.MAX_VALUE){
            ((AlarmManager)mSettingActivity.getSystemService(Context.ALARM_SERVICE)).setTime(when);
        }
    }

    public void checkUpdata(int action) {
        SystemFragment mSystemFragment = (SystemFragment) mSettingActivity.fragments.get(SettingActivity.SYS_UPDATA);
        if(mSystemFragment != null)
        {
            if(action == UpDataManager.UPDATA_NONE)
            {
                if(mSystemFragment.txtNew != null) {
                    mSystemFragment.txtNew.setText(mSettingActivity.getString(R.string.setting_title_system_version_none));
                    mSystemFragment.txtUpdata.setText(mSettingActivity.getString(R.string.setting_title_system_latest));
                    mSystemFragment.btnUpdata.setEnabled(false);
                    mSystemFragment.mProgressBar.setVisibility(View.INVISIBLE);
                    mSystemFragment.array.setVisibility(View.INVISIBLE);
                }
            }
            else if(action == UpDataManager.UPDATA_DOWNLOAD)
            {
                if(mSystemFragment.txtNew != null) {
                    mSystemFragment.txtNew.setText("V"+UpDataManager.version);
                    mSystemFragment.txtUpdata.setText(mSettingActivity.getString(R.string.setting_title_system_download));
                    mSystemFragment.btnUpdata.setEnabled(false);
                    if(PoctApplication.mApp.mDownloadTask != null) {
                        mSystemFragment.mProgressBar.setVisibility(View.VISIBLE);
                        mSystemFragment.array.setVisibility(View.INVISIBLE);
                        long finish = PoctApplication.mApp.mDownloadTask .finishsize;
                        long all = PoctApplication.mApp.mDownloadTask .mSize;
                        if(all != 0) {
                            mSystemFragment.mProgressBar.setMax((int) all);
                            mSystemFragment.mProgressBar.setProgress((int) finish);
                        }
                    }
                }

            }
            else
            {
                if(mSystemFragment.txtNew != null) {
                    mSystemFragment.txtNew.setText(UpDataManager.version);
                    mSystemFragment.txtUpdata.setText(mSettingActivity.getString(R.string.setting_title_system_updata));
                    mSystemFragment.btnUpdata.setEnabled(true);
                    mSystemFragment.mProgressBar.setVisibility(View.INVISIBLE);
                    mSystemFragment.array.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void updataProgress(Intent intent) {
        SystemFragment mSystemFragment = (SystemFragment) mSettingActivity.fragments.get(SettingActivity.SYS_UPDATA);
        if(mSystemFragment.txtNew != null) {
            long finish = intent.getLongExtra("finishsize",0);
            long all = intent.getLongExtra("totalsize",0);
            if(all != 0) {
                mSystemFragment.mProgressBar.setMax((int) all);
                mSystemFragment.mProgressBar.setProgress((int) finish);
            }
        }
    }

    public void finishProgress() {
        SystemFragment mSystemFragment = (SystemFragment) mSettingActivity.fragments.get(SettingActivity.SYS_UPDATA);
        if(mSystemFragment.txtNew != null) {
            mSystemFragment.txtNew.setText(UpDataManager.version);
            mSystemFragment.txtUpdata.setText(mSettingActivity.getString(R.string.setting_title_system_updata));
            mSystemFragment.btnUpdata.setEnabled(true);
            mSystemFragment.mProgressBar.setVisibility(View.INVISIBLE);
            mSystemFragment.array.setVisibility(View.VISIBLE);
        }
    }
}
