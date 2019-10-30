package com.poct.android.view.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poct.android.entity.Report;
import com.poct.android.entity.SeriesReport;
import com.poct.android.entity.TempGet;
import com.poct.android.presenter.PotcTestPresenter;
import com.poct.android.view.adapter.DeviceReportAdapter;
import com.poct.android.view.adapter.PatientReportAdapter;

import java.util.ArrayList;

/**
 * Created by xpx on 2017/8/18.
 */

public class PotcTestActivity extends BaseActivity {

    public RelativeLayout btnBack;
    public RelativeLayout btnGet;
    public TextView btnPrint;
    public TextView buttomimf2;
    public TextView imfText;
    public TextView headTime;
    public TextView buttomimf;
    public TextView btnGive;
    public PotcTestPresenter mPotcTestPresenter = new PotcTestPresenter(this);
    public TextView connectState;
    public TextView btnGetData;
    public ArrayList<Report> mReports = new ArrayList<Report>();
    public ListView deviceList;
    public ListView userList;
    public String data = "";
    public boolean isGetting = false;
    public DeviceReportAdapter deviceReportAdapter;
    public PatientReportAdapter patientReportAdapter;
    public SeriesReport mSeriesReport;
    public ArrayList<TempGet> tempGets = new ArrayList<TempGet>();
    public TempGet mTempGet;
    public AlertDialog mAlertDialog;
    public int lastcount = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPotcTestPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mPotcTestPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener doBacklistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPotcTestPresenter.mPotcTestActivity.finish();
        }
    };

    public View.OnClickListener doGetDeviceData = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPotcTestPresenter.getDeviceData();
        }
    };

    public View.OnClickListener doGaveData = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPotcTestPresenter.doGaveData();
        }
    };

    public AdapterView.OnItemClickListener listDeviceListener = new AdapterView.OnItemClickListener()
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mPotcTestPresenter.doSelect((Report) parent.getAdapter().getItem(position));
        }
    };

    public View.OnClickListener doConnectDevice = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mPotcTestPresenter.doConnectDevice();
        }
    };

    public View.OnClickListener printListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mPotcTestPresenter.startPrint();
        }
    };
}
