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
import com.poct.android.presenter.FadaTestPresenter;
import com.poct.android.view.adapter.DeviceReportAdapter;
import com.poct.android.view.adapter.PatientReportAdapter;

import java.util.ArrayList;

/**
 * Created by xpx on 2017/8/18.
 */

public class FadaTestActivity extends BaseActivity {

    public RelativeLayout btnBack;
    public RelativeLayout btnGet;
    public TextView btnPrint;
    public TextView buttomimf2;
    public TextView imfText;
    public TextView headTime;
    public TextView buttomimf;
    public TextView btnGive;
    public FadaTestPresenter mFadaTestPresenter = new FadaTestPresenter(this);
    public TextView connectState;
    public TextView btnGetData;
    public ArrayList<Report> mReports = new ArrayList<Report>();
    public ArrayList<Report> mPReports = new ArrayList<Report>();
    public ListView deviceList;
    public ListView userList;
    public boolean canConnected = false;
    public String data = "";
    public boolean isGetting = false;
    public DeviceReportAdapter deviceReportAdapter;
    public PatientReportAdapter patientReportAdapter;
    public SeriesReport mSeriesReport;
    public ArrayList<TempGet> tempGets = new ArrayList<TempGet>();
    public TempGet mTempGet;
    public AlertDialog mAlertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFadaTestPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mFadaTestPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener doBacklistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mFadaTestPresenter.mFadaTestActivity.finish();
        }
    };

    public View.OnClickListener doGetDeviceData = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mFadaTestPresenter.getDeviceData();
        }
    };

    public View.OnClickListener doGaveData = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mFadaTestPresenter.doGaveData();
        }
    };

    public AdapterView.OnItemClickListener listDeviceListener = new AdapterView.OnItemClickListener()
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mFadaTestPresenter.doSelect((Report) parent.getAdapter().getItem(position));
        }
    };

    public View.OnClickListener doConnectDevice = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mFadaTestPresenter.doConnectDevice();
        }
    };

    public View.OnClickListener printListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {

            mFadaTestPresenter.startPrint();
        }
    };
}
