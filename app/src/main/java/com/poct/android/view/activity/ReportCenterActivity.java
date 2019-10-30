package com.poct.android.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poct.android.entity.PageSeraisReport;
import com.poct.android.entity.SeriesReport;
import com.poct.android.presenter.ReportCenterPresenter;
import com.poct.android.view.adapter.ApproveSeriasReportAdapter;

import java.util.ArrayList;

/**
 * Created by xpx on 2017/8/18.
 */

public class ReportCenterActivity extends BaseActivity {

    public RelativeLayout btnBack;
    public RelativeLayout btnSearch;
    public RelativeLayout btnReset;
    public RelativeLayout btnDete1;
    public RelativeLayout btnDete2;
    public EditText etxtName;
    public EditText etxtSender;
    public TextView etxtDete1;
    public TextView etxtDete2;
    public ListView listReport;
    public TextView txtImf;
    public TextView btnBegin;
    public TextView btnNext;
    public TextView btnPre;
    public TextView btnEnd;
    public PageSeraisReport mPageSeraisReport = new PageSeraisReport(20);
    public ArrayList<SeriesReport> mSeriesReports = new ArrayList<SeriesReport>();
    public ApproveSeriasReportAdapter mSeriasReportAdapter;
    public PopupWindow popupWindow1;
    public ReportCenterPresenter mReportCenterPresenter = new ReportCenterPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReportCenterPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mReportCenterPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener doSetDeteBeginlistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mReportCenterPresenter.setDateBegin();
        }
    };

    public View.OnClickListener doSetDeteEndlistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mReportCenterPresenter.setDateEnd();
        }
    };

    public View.OnClickListener doBacklistener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {

            mReportCenterPresenter.mReportCenterActivity.finish();
        }
    };

    public View.OnClickListener doSearchlistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mReportCenterPresenter.doSearch();
        }
    };

    public View.OnClickListener doResetlistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mReportCenterPresenter.doReset();
        }
    };

    public View.OnClickListener doBeginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mReportCenterPresenter.doBegin();
        }
    };

    public View.OnClickListener doEndListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mReportCenterPresenter.doEnd();
        }
    };

    public View.OnClickListener doNextListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mReportCenterPresenter.doNext();
        }
    };

    public View.OnClickListener doPreListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mReportCenterPresenter.doPre();
        }
    };

    public AdapterView.OnItemClickListener onPrintClickListener = new AdapterView.OnItemClickListener()
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mReportCenterPresenter.startPrint((SeriesReport) parent.getAdapter().getItem(position));
        }
    };
}
