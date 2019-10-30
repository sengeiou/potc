package com.poct.android.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.poct.R;
import com.poct.android.presenter.SettingPresenter;
import com.poct.android.utils.AppUtils;
import com.poct.android.utils.DoubleDatePickerDialog;
import com.poct.android.utils.ViewUtils;


public class TimeFragment extends BaseFragment {

    public SettingPresenter mSettingPresenter;
    public Switch switchButtonAuto;
    public Switch switchButton24Hour;
    public TextView txtDete;
    public TextView txtTime;
    public RelativeLayout btnDete;
    public RelativeLayout btnTime;
    public TimeFragment() {

    }

    @SuppressLint("ValidFragment")
    public TimeFragment(SettingPresenter mSettingPresenter) {
        // Required empty public constructor
        this.mSettingPresenter = mSettingPresenter;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_time, container, false);
        switchButtonAuto = mView.findViewById(R.id.btn_swich_auto);
        switchButton24Hour = mView.findViewById(R.id.btn_swich_24h);
        btnDete = mView.findViewById(R.id.btn_dete);
        btnTime = mView.findViewById(R.id.btn_time);
        txtDete = mView.findViewById(R.id.txtstate_dete);
        txtTime = mView.findViewById(R.id.txtstate_time);
        txtDete.setText(AppUtils.getDate2());
        try {
            int a = Settings.Global.getInt(mSettingPresenter.mSettingActivity.getContentResolver(),
                    Settings.Global.AUTO_TIME);
            if( Settings.Global.getInt(mSettingPresenter.mSettingActivity.getContentResolver(),
                    Settings.Global.AUTO_TIME) > 0)
            {
                switchButtonAuto.setChecked(true);
                setTimeDisable();
            }
            else
            {
                switchButtonAuto.setChecked(false);
                setTimeEable();
            }
            if(DateFormat.is24HourFormat(mSettingPresenter.mSettingActivity))
            {
                switchButton24Hour.setChecked(true);

            }
            else
            {
                switchButton24Hour.setChecked(false);

            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        updateTime();
        btnDete.setOnClickListener(setDateListener);
        btnTime.setOnClickListener(setTimeListener);
        switchButtonAuto.setOnCheckedChangeListener(autoSwichListener);
        switchButton24Hour.setOnCheckedChangeListener(auto24SwichListener);
        return mView;
    }

    @Override
    public void onResume() {

        super.onResume();

    }

    public View.OnClickListener setDateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setDate();
        }
    };

    public View.OnClickListener setTimeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setTime();
        }
    };

    public CompoundButton.OnCheckedChangeListener auto24SwichListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if(!isChecked == DateFormat.is24HourFormat(mSettingPresenter.mSettingActivity))
            {
                if(isChecked)
                {
                    android.provider.Settings.System.putString(mSettingPresenter.mSettingActivity.getContentResolver(),
                            android.provider.Settings.System.TIME_12_24, "24");
                }
                else
                {
                    android.provider.Settings.System.putString(mSettingPresenter.mSettingActivity.getContentResolver(),
                            android.provider.Settings.System.TIME_12_24, "12");
                }
                mSettingPresenter.updataTime();
            }
        }
    };

    public CompoundButton.OnCheckedChangeListener autoSwichListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            try {
                boolean auto = false;
                int a = android.provider.Settings.Global.getInt(mSettingPresenter.mSettingActivity.getContentResolver(),
                        android.provider.Settings.Global.AUTO_TIME);
                if( android.provider.Settings.Global.getInt(mSettingPresenter.mSettingActivity.getContentResolver(),
                        android.provider.Settings.Global.AUTO_TIME) > 0)
                {
                    auto = true;
                }
                else
                {
                    auto = false;
                }
                if(!isChecked == auto)
                {
                    if(isChecked == true)
                    {
                        android.provider.Settings.Global.putInt(mSettingPresenter.mSettingActivity.getContentResolver(),
                                Settings.Global.AUTO_TIME_ZONE, 1);
                        android.provider.Settings.Global.putInt(mSettingPresenter.mSettingActivity.getContentResolver(),
                                android.provider.Settings.Global.AUTO_TIME, 1);
                        txtDete.setText(AppUtils.getDate2());
                        mSettingPresenter.updataTime();
                        setTimeDisable();

                    }
                    else
                    {
                        android.provider.Settings.Global.putInt(mSettingPresenter.mSettingActivity.getContentResolver(),
                                android.provider.Settings.Global.AUTO_TIME, 0);
                        txtDete.setText(AppUtils.getDate2());
                        setTimeEable();
                    }

                }

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();

            }

        }
    };

    class DataPickerListener implements DoubleDatePickerDialog.OnDateSetListener {

        public TextView textView;

        public DataPickerListener(TextView textView) {
            this.textView = textView;
        }

        @Override
        public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, int hour, int miniute) {
            if (textView != null) {
                mSettingPresenter.setSysDate(startYear,startMonthOfYear+1,startDayOfMonth);
                textView.setText(String.format("%04d年%02d月%02d日", startYear, startMonthOfYear+1, startDayOfMonth));
            }

        }
    }

    class TimePickerListener implements DoubleDatePickerDialog.OnDateSetListener {

        public TextView textView;

        public TimePickerListener(TextView textView) {
            this.textView = textView;
        }

        @Override
        public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, int hour, int miniute) {
            if (textView != null) {
                mSettingPresenter.setSysTime(hour,miniute);
                updateTime();
            }

        }
    }

    public void setDate() {

        ViewUtils.creatDataPicker(mSettingPresenter.mSettingActivity,AppUtils.getTime()
                , mSettingPresenter.mSettingActivity.getString(R.string.setting_dete),new DataPickerListener(txtDete));
    }

    public void setTime() {

        ViewUtils.creatTimePicker(mSettingPresenter.mSettingActivity,AppUtils.getTime()
                , mSettingPresenter.mSettingActivity.getString(R.string.setting_time),new TimePickerListener(txtDete));
    }

    public void updateTime()
    {
        if(mSettingPresenter != null)
        {
            if(DateFormat.is24HourFormat(mSettingPresenter.mSettingActivity))
            {
                txtTime.setText(AppUtils.getTime());
            }
            else
            {
                txtTime.setText(AppUtils.getTime12());
            }
        }
    }

    public void setTimeEable() {
        btnDete.setEnabled(true);
        btnTime.setEnabled(true);
    }

    public void setTimeDisable() {
        btnDete.setEnabled(false);
        btnTime.setEnabled(false);
    }
}
