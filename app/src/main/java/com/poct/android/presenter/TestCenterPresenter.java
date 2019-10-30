package com.poct.android.presenter;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poct.R;
import com.poct.android.asks.ReportAsks;
import com.poct.android.entity.MenuItem;
import com.poct.android.entity.NetObject;
import com.poct.android.entity.SeriesReport;
import com.poct.android.entity.TempGet;
import com.poct.android.handler.TestCenterHandler;
import com.poct.android.manager.EquipMentManager;
import com.poct.android.prase.DataPrase;
import com.poct.android.receiver.TestCenterReceiver;
import com.poct.android.utils.AppUtils;
import com.poct.android.utils.DBHelper;
import com.poct.android.utils.DoubleDatePickerDialog;
import com.poct.android.utils.ViewUtils;
import com.poct.android.view.PoctApplication;
import com.poct.android.view.activity.FadaTestActivity;
import com.poct.android.view.activity.PotcTestActivity;
import com.poct.android.view.activity.ReportPrintActivity;
import com.poct.android.view.activity.TestCenterActivity;
import com.poct.android.view.adapter.SeriasReportAdapter;
import com.poct.android.view.adapter.TempAdapter;

import java.util.ArrayList;

/**
 * Created by xpx on 2017/8/18.
 */

public class TestCenterPresenter implements Presenter {

    public TestCenterHandler mTestCenterHandler;
    public TestCenterActivity mTestCenterActivity;
    public TestCenterReceiver mTestCenterReceiver;
    public TestCenterPresenter(TestCenterActivity mTestCenterActivity)
    {
        this.mTestCenterActivity = mTestCenterActivity;
        this.mTestCenterHandler = new TestCenterHandler(mTestCenterActivity);
        this.mTestCenterReceiver = new TestCenterReceiver(mTestCenterHandler);
    }

    @Override
    public void Create() {
        initView();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(TestCenterActivity.ACTION_UPDETE_REPORT_LIST);
        intentFilter.addAction(TestCenterActivity.ACTION_UPLOAD_REPORT);
        mTestCenterActivity.registerReceiver(mTestCenterReceiver,intentFilter);
    }

    @Override
    public void initView() {
        mTestCenterActivity.setContentView(R.layout.activity_testcenter);
        mTestCenterActivity.btnBack = mTestCenterActivity.findViewById(R.id.layerback);
        mTestCenterActivity.btnCreat = mTestCenterActivity.findViewById(R.id.layercreat);
        mTestCenterActivity.btnSearch = mTestCenterActivity.findViewById(R.id.btnsearch);
        mTestCenterActivity.btnReset = mTestCenterActivity.findViewById(R.id.btnreset);
        mTestCenterActivity.etxtName = mTestCenterActivity.findViewById(R.id.etxtname);
        mTestCenterActivity.btnDete1 = mTestCenterActivity.findViewById(R.id.layerbegin);
        mTestCenterActivity.btnDete2 = mTestCenterActivity.findViewById(R.id.layerend);
        mTestCenterActivity.etxtSender = mTestCenterActivity.findViewById(R.id.etxtdoctor);
        mTestCenterActivity.etxtDete1 = mTestCenterActivity.findViewById(R.id.etxtbegin);
        mTestCenterActivity.etxtDete2 = mTestCenterActivity.findViewById(R.id.etxtend);
        mTestCenterActivity.listReport = mTestCenterActivity.findViewById(R.id.reportlist);
        mTestCenterActivity.txtImf = mTestCenterActivity.findViewById(R.id.txtimf);
        mTestCenterActivity.btnBegin = mTestCenterActivity.findViewById(R.id.btnbegin);
        mTestCenterActivity.btnEnd = mTestCenterActivity.findViewById(R.id.btnend);
        mTestCenterActivity.btnPre = mTestCenterActivity.findViewById(R.id.btnpre);
        mTestCenterActivity.btnNext = mTestCenterActivity.findViewById(R.id.btnnext);
        mTestCenterActivity.shade = mTestCenterActivity.findViewById(R.id.shade);
        mTestCenterActivity.etxtDete1.setText(AppUtils.getDate());
        mTestCenterActivity.etxtDete2.setText(AppUtils.getDate());
        mTestCenterActivity.mPageSeraisReport = DBHelper.getInstance(mTestCenterActivity).getReports(PoctApplication.mApp.account.mAccountId,"",""
                ,mTestCenterActivity.etxtDete1.getText().toString(),mTestCenterActivity.etxtDete2.getText().toString());
        setEndImf();
        mTestCenterActivity.mSeriesReports.clear();
        mTestCenterActivity.mSeriesReports.addAll(mTestCenterActivity.mPageSeraisReport.getShow());
        mTestCenterActivity.mSeriasReportAdapter = new SeriasReportAdapter(mTestCenterActivity,mTestCenterActivity.mSeriesReports,mTestCenterHandler);
        mTestCenterActivity.listReport.setAdapter(mTestCenterActivity.mSeriasReportAdapter);
        mTestCenterActivity.btnBack.setOnClickListener(mTestCenterActivity.doBacklistener);
        mTestCenterActivity.btnCreat.setOnClickListener(mTestCenterActivity.doCreatlistener);
        mTestCenterActivity.btnSearch.setOnClickListener(mTestCenterActivity.doSearchlistener);
        mTestCenterActivity.btnReset.setOnClickListener(mTestCenterActivity.doResetlistener);
        mTestCenterActivity.btnBegin.setOnClickListener(mTestCenterActivity.doBeginListener);
        mTestCenterActivity.btnEnd.setOnClickListener(mTestCenterActivity.doEndListener);
        mTestCenterActivity.btnPre.setOnClickListener(mTestCenterActivity.doPreListener);
        mTestCenterActivity.btnNext.setOnClickListener(mTestCenterActivity.doNextListener);
        mTestCenterActivity.btnDete1.setOnClickListener(mTestCenterActivity.doSetDeteBeginlistener);
        mTestCenterActivity.btnDete2.setOnClickListener(mTestCenterActivity.doSetDeteEndlistener);
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
        mTestCenterActivity.unregisterReceiver(mTestCenterReceiver);
    }

    public void doSearch() {
        mTestCenterActivity.mPageSeraisReport = DBHelper.getInstance(mTestCenterActivity).getReports(PoctApplication.mApp.account.mAccountId,mTestCenterActivity.etxtName.getText().toString()
                ,mTestCenterActivity.etxtSender.getText().toString(),mTestCenterActivity.etxtDete1.getText().toString(),mTestCenterActivity.etxtDete2.getText().toString());
        mTestCenterActivity.mSeriesReports.clear();
        mTestCenterActivity.mSeriesReports.addAll(mTestCenterActivity.mPageSeraisReport.getShow());
        mTestCenterActivity.mSeriasReportAdapter.notifyDataSetChanged();
        setEndImf();
    }

    public void doSearch(int page) {
        mTestCenterActivity.mPageSeraisReport = DBHelper.getInstance(mTestCenterActivity).getReports(PoctApplication.mApp.account.mAccountId,mTestCenterActivity.etxtName.getText().toString()
                ,mTestCenterActivity.etxtSender.getText().toString(),mTestCenterActivity.etxtDete1.getText().toString(),mTestCenterActivity.etxtDete2.getText().toString());
        mTestCenterActivity.mSeriesReports.clear();
        if(page <= mTestCenterActivity.mPageSeraisReport.pageCount)
        {
            mTestCenterActivity.mPageSeraisReport.showPage = page;
        }
        else
        {
            mTestCenterActivity.mPageSeraisReport.showPage = mTestCenterActivity.mPageSeraisReport.pageCount;
        }
        mTestCenterActivity.mSeriesReports.addAll(mTestCenterActivity.mPageSeraisReport.getShow());
        mTestCenterActivity.mSeriasReportAdapter.notifyDataSetChanged();
        setEndImf();
    }

    public void setDateBegin() {
        ViewUtils.creatDataPicker(mTestCenterActivity,mTestCenterActivity.etxtDete1.getText().toString()
                , mTestCenterActivity.getString(R.string.setting_dete_start),new DataPickerListener(mTestCenterActivity.etxtDete1));
    }

    public void setDateEnd() {
        ViewUtils.creatDataPicker(mTestCenterActivity,mTestCenterActivity.etxtDete1.getText().toString()
                , mTestCenterActivity.getString(R.string.setting_dete_end),new DataPickerListener(mTestCenterActivity.etxtDete2));
    }

    class DataPickerListener implements DoubleDatePickerDialog.OnDateSetListener {

        public TextView textView;

        public DataPickerListener(TextView textView) {
            this.textView = textView;
        }

        @Override
        public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, int hour, int miniute) {
            if (textView != null) {
                textView.setText(String.format("%04d-%02d-%02d", startYear, startMonthOfYear+1, startDayOfMonth));
            }

        }
    }

    public void doReset() {
        mTestCenterActivity.etxtName.setText("");
        mTestCenterActivity.etxtSender.setText("");
        mTestCenterActivity.etxtDete1.setText(AppUtils.getDate());
        mTestCenterActivity.etxtDete2.setText(AppUtils.getDate());
    }

    public void doBegin() {
        mTestCenterActivity.mSeriesReports.clear();
        mTestCenterActivity.mPageSeraisReport.setBegin();
        mTestCenterActivity.mSeriesReports.addAll(mTestCenterActivity.mPageSeraisReport.getShow());
        mTestCenterActivity.mSeriasReportAdapter.notifyDataSetChanged();
        setEndImf();
    }

    public void doEnd() {
        mTestCenterActivity.mSeriesReports.clear();
        mTestCenterActivity.mPageSeraisReport.setEnd();
        mTestCenterActivity.mSeriesReports.addAll(mTestCenterActivity.mPageSeraisReport.getShow());
        mTestCenterActivity.mSeriasReportAdapter.notifyDataSetChanged();
        setEndImf();
    }

    public void doNext() {
        if(mTestCenterActivity.mPageSeraisReport.setNext())
        {
            mTestCenterActivity.mSeriesReports.clear();
            mTestCenterActivity.mSeriesReports.addAll(mTestCenterActivity.mPageSeraisReport.getShow());
            mTestCenterActivity.mSeriasReportAdapter.notifyDataSetChanged();
            setEndImf();
        }
        else
        {
            ViewUtils.showMessage(mTestCenterActivity,mTestCenterActivity.getString(R.string.imf_word_error_end));
        }
    }

    public void doPre() {
        if(mTestCenterActivity.mPageSeraisReport.setPre())
        {
            mTestCenterActivity.mSeriesReports.clear();
            mTestCenterActivity.mSeriesReports.addAll(mTestCenterActivity.mPageSeraisReport.getShow());
            mTestCenterActivity.mSeriasReportAdapter.notifyDataSetChanged();
            setEndImf();
        }
        else
        {
            ViewUtils.showMessage(mTestCenterActivity,mTestCenterActivity.getString(R.string.imf_word_error_begin));
        }
    }

    public void creatPeport() {
        View popupWindowView = LayoutInflater.from(mTestCenterActivity).inflate(R.layout.report_creat, null);
        RelativeLayout lsyer = (RelativeLayout) popupWindowView.findViewById(R.id.layer);
        lsyer.setFocusable(true);
        lsyer.setFocusableInTouchMode(true);
        mTestCenterActivity.popupWindow1 = new PopupWindow(popupWindowView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
        popupWindowView.setFocusable(true);
        popupWindowView.setFocusableInTouchMode(true);
        mTestCenterActivity.popupWindow1.setAnimationStyle(R.style.PopupAnimation);
        final PopupWindow finalPopupWindow = mTestCenterActivity.popupWindow1;
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
        mTestCenterActivity.popupWindow1.setBackgroundDrawable(dw);
        mTestCenterActivity.cetxtName = popupWindowView.findViewById(R.id.etxtname);
        mTestCenterActivity.cetxtAge = popupWindowView.findViewById(R.id.etxtage);
        mTestCenterActivity.mMaleRb = popupWindowView.findViewById(R.id.male_rb);
        mTestCenterActivity.mMaleRb.setChecked(true);
        mTestCenterActivity.mFamaleRb = popupWindowView.findViewById(R.id.famale_rb);
        mTestCenterActivity.cetxtId = popupWindowView.findViewById(R.id.etxtid);
        mTestCenterActivity.cetxtCnum = popupWindowView.findViewById(R.id.etxtcnum);
        mTestCenterActivity.cetxtHnum = popupWindowView.findViewById(R.id.etxthnum);
        mTestCenterActivity.cetxtDep = popupWindowView.findViewById(R.id.etxtdep);
        mTestCenterActivity.cetxtBad = popupWindowView.findViewById(R.id.etxtbad);
        mTestCenterActivity.cetxtDes = popupWindowView.findViewById(R.id.etxtdes);
        mTestCenterActivity.cetxtMemo = popupWindowView.findViewById(R.id.etxtmemo);
        mTestCenterActivity.btnClose = popupWindowView.findViewById(R.id.layerclose);
        mTestCenterActivity.btnOk = popupWindowView.findViewById(R.id.btnok);
        mTestCenterActivity.btnCancle = popupWindowView.findViewById(R.id.btncancle);
        mTestCenterActivity.btnOk.setOnClickListener(mTestCenterActivity.doOklistener);
        mTestCenterActivity.btnCancle.setOnClickListener(mTestCenterActivity.doCancleListener);
        mTestCenterActivity.btnClose.setOnClickListener(mTestCenterActivity.doCancleListener);
//        RelativeLayout mRelativeLayout = popupWindowView.findViewById(R.id.head);
//        mRelativeLayout.setOnClickListener(mTestCenterActivity.doOklistener);
        mTestCenterActivity.popupWindow1.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mTestCenterActivity.shade.setVisibility(View.INVISIBLE);
            }
        });
        mTestCenterActivity.popupWindow1.showAtLocation(mTestCenterActivity.findViewById(R.id.activity_test_center),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        mTestCenterActivity.shade.setVisibility(View.VISIBLE);

    }

    public void doOk(SeriesReport inreport) {

        if(mTestCenterActivity.cetxtName.getText().toString().length() == 0) {
            ViewUtils.showMessage(mTestCenterActivity,mTestCenterActivity.getString(R.string.test_center_name_empty));
        }
        else if(mTestCenterActivity.cetxtAge.getText().toString().length() == 0)
        {
            ViewUtils.showMessage(mTestCenterActivity,mTestCenterActivity.getString(R.string.test_center_name_age));
        }
        else{
            SeriesReport seriesReport;
            if(inreport == null )
            {
                 seriesReport = new SeriesReport();
            }
            else
            {
                seriesReport = inreport;
            }
            seriesReport.mName = mTestCenterActivity.cetxtName.getText().toString();
            seriesReport.mAge = mTestCenterActivity.cetxtAge.getText().toString();
            seriesReport.mPId = mTestCenterActivity.cetxtCnum.getText().toString();
            seriesReport.mHNum = mTestCenterActivity.cetxtHnum.getText().toString();
            seriesReport.mSender = PoctApplication.mApp.account.mName;
            if(mTestCenterActivity.mMaleRb.isChecked())
            {
                seriesReport.mSex = "男";
            }
            else{
                seriesReport.mSex = "女";
            }
            doCreatReport(seriesReport);
        }

    }

    public void doCreatReport(SeriesReport seriesReport) {
        boolean change = true;
        if(seriesReport.mReportId.length() == 0)
        {
            change = false;
            seriesReport.mReportId = AppUtils.getguid();
        }
        seriesReport.mName = mTestCenterActivity.cetxtName.getText().toString();
        seriesReport.mAge = mTestCenterActivity.cetxtAge.getText().toString();
        seriesReport.mPId = mTestCenterActivity.cetxtId.getText().toString();
        seriesReport.mCNum = mTestCenterActivity.cetxtCnum.getText().toString();
        seriesReport.mHNum = mTestCenterActivity.cetxtHnum.getText().toString();
        seriesReport.mDepart = mTestCenterActivity.cetxtDep.getText().toString();
        seriesReport.mBad = mTestCenterActivity.cetxtBad.getText().toString();
        seriesReport.mDes = mTestCenterActivity.cetxtDes.getText().toString();
        seriesReport.mMemo = mTestCenterActivity.cetxtMemo.getText().toString();
        seriesReport.mAccountId = PoctApplication.mApp.account.mAccountId;
        seriesReport.mSendTime = AppUtils.getDate()+" "+AppUtils.getTime();
        if(change)
        DBHelper.getInstance(mTestCenterActivity).updataReport(seriesReport);
        else
            DBHelper.getInstance(mTestCenterActivity).saveReport(seriesReport);
        int page = mTestCenterActivity.mPageSeraisReport.showPage;
        if(page == 0)
        {
            page = 1;
        }
        doSearch(page);
        mTestCenterActivity.popupWindow1.dismiss();
        if(change == false)
        doAddSelect(seriesReport,true);
    }

    public void doDeleteSelect(SeriesReport seriesReport) {
        if(mTestCenterActivity.popupWindow1 != null)
        mTestCenterActivity.popupWindow1.dismiss();
        ViewUtils.creatDialogTowButton(mTestCenterActivity,mTestCenterActivity.getString(R.string.imf_word_delete),mTestCenterActivity.getString(R.string.button_delete)
                ,mTestCenterActivity.getString(R.string.button_word_cancle),mTestCenterActivity.getString(R.string.button_word_ok),null,new DeleteListener(seriesReport));
    }

    public void showMore(SeriesReport mSeriesReport) {
        if(mSeriesReport.mSendTime.substring(0,10).equals(AppUtils.getDate()))
        {
            ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
            MenuItem item = new MenuItem(mTestCenterActivity.getString(R.string.button_detial),mTestCenterActivity.detialListener);
            item.item = mSeriesReport;
            MenuItem item2 = new MenuItem(mTestCenterActivity.getString(R.string.button_add),mTestCenterActivity.addListener);
            item2.item = mSeriesReport;
            MenuItem item3 = new MenuItem(mTestCenterActivity.getString(R.string.button_change),mTestCenterActivity.changeListener);
            item3.item = mSeriesReport;
            MenuItem item4 = new MenuItem(mTestCenterActivity.getString(R.string.button_delete),mTestCenterActivity.deleteListener);
            item4.item = mSeriesReport;
            menuItems.add(item);
            menuItems.add(item2);
            menuItems.add(item3);
            menuItems.add(item4);
            creatButtomMenu(menuItems);
        }
        else
        {
            ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
            MenuItem item = new MenuItem(mTestCenterActivity.getString(R.string.button_detial),mTestCenterActivity.detialListener);
            item.item = mSeriesReport;
            MenuItem item3 = new MenuItem(mTestCenterActivity.getString(R.string.button_delete),mTestCenterActivity.deleteListener);
            item3.item = mSeriesReport;
            menuItems.add(item);
            menuItems.add(item3);
            creatButtomMenu(menuItems);
        }
    }

    public void changeButtomMenu(SeriesReport mSeriesReport) {
        ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
        MenuItem item = new MenuItem(mTestCenterActivity.getString(R.string.button_change_1),mTestCenterActivity.changeListener1);
        item.item = mSeriesReport;
        MenuItem item2 = new MenuItem(mTestCenterActivity.getString(R.string.button_change_2),mTestCenterActivity.changeListener2);
        item2.item = mSeriesReport;
        MenuItem item3 = new MenuItem(mTestCenterActivity.getString(R.string.button_change_3),mTestCenterActivity.changeListener3);
        item3.item = mSeriesReport;
        menuItems.add(item);
        menuItems.add(item2);
        menuItems.add(item3);
        creatButtomMenu(menuItems);
    }

    public void changeCreat(SeriesReport mSeriesReport) {
        View popupWindowView = LayoutInflater.from(mTestCenterActivity).inflate(R.layout.report_creat, null);
        RelativeLayout lsyer = (RelativeLayout) popupWindowView.findViewById(R.id.layer);
        lsyer.setFocusable(true);
        lsyer.setFocusableInTouchMode(true);
        mTestCenterActivity.popupWindow1 = new PopupWindow(popupWindowView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
        popupWindowView.setFocusable(true);
        popupWindowView.setFocusableInTouchMode(true);
        mTestCenterActivity.popupWindow1.setAnimationStyle(R.style.PopupAnimation);
        final PopupWindow finalPopupWindow = mTestCenterActivity.popupWindow1;
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
        mTestCenterActivity.popupWindow1.setBackgroundDrawable(dw);
        mTestCenterActivity.cetxtName = popupWindowView.findViewById(R.id.etxtname);
        mTestCenterActivity.cetxtAge = popupWindowView.findViewById(R.id.etxtage);
        mTestCenterActivity.mMaleRb = popupWindowView.findViewById(R.id.male_rb);
        mTestCenterActivity.mMaleRb.setChecked(true);
        mTestCenterActivity.mFamaleRb = popupWindowView.findViewById(R.id.famale_rb);
        mTestCenterActivity.cetxtId = popupWindowView.findViewById(R.id.etxtid);
        mTestCenterActivity.cetxtCnum = popupWindowView.findViewById(R.id.etxtcnum);
        mTestCenterActivity.cetxtHnum = popupWindowView.findViewById(R.id.etxthnum);
        mTestCenterActivity.cetxtDep = popupWindowView.findViewById(R.id.etxtdep);
        mTestCenterActivity.cetxtBad = popupWindowView.findViewById(R.id.etxtbad);
        mTestCenterActivity.cetxtDes = popupWindowView.findViewById(R.id.etxtdes);
        mTestCenterActivity.cetxtMemo = popupWindowView.findViewById(R.id.etxtmemo);
        mTestCenterActivity.btnClose = popupWindowView.findViewById(R.id.layerclose);
        mTestCenterActivity.cetxtName.setText(mSeriesReport.mName);
        mTestCenterActivity.cetxtAge.setText(mSeriesReport.mAge);
        mTestCenterActivity.cetxtId.setText(mSeriesReport.mPId);
        mTestCenterActivity.cetxtCnum.setText(mSeriesReport.mCNum);
        mTestCenterActivity.cetxtHnum.setText(mSeriesReport.mHNum);
        mTestCenterActivity.cetxtDep.setText(mSeriesReport.mDepart);
        mTestCenterActivity.cetxtBad.setText(mSeriesReport.mBad);
        mTestCenterActivity.cetxtDes.setText(mSeriesReport.mDes);
        mTestCenterActivity.cetxtMemo.setText(mSeriesReport.mMemo);
        if(mSeriesReport.mSex.equals("男"))
        {
            mTestCenterActivity.mMaleRb.setChecked(true);
        }
        else
        {
            mTestCenterActivity.mFamaleRb.setChecked(true);
        }

        mTestCenterActivity.btnOk = popupWindowView.findViewById(R.id.btnok);
        mTestCenterActivity.btnCancle = popupWindowView.findViewById(R.id.btncancle);
        mTestCenterActivity.btnOk.setOnClickListener(mTestCenterActivity.doOklistener);
        mTestCenterActivity.btnOk.setTag(mSeriesReport);
        mTestCenterActivity.btnCancle.setOnClickListener(mTestCenterActivity.doCancleListener);
        mTestCenterActivity.btnClose.setOnClickListener(mTestCenterActivity.doCancleListener);
//        RelativeLayout mRelativeLayout = popupWindowView.findViewById(R.id.head);
//        mRelativeLayout.setOnClickListener(mTestCenterActivity.doOklistener);
        mTestCenterActivity.popupWindow1.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mTestCenterActivity.shade.setVisibility(View.INVISIBLE);
            }
        });
        mTestCenterActivity.popupWindow1.showAtLocation(mTestCenterActivity.findViewById(R.id.activity_test_center),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        mTestCenterActivity.shade.setVisibility(View.VISIBLE);
    }

    public void changeTemp(SeriesReport mSeriesReport,String type) {
        if(mTestCenterActivity.popupWindow1 != null)
        {
            mTestCenterActivity.popupWindow1.dismiss();
        }
        View popupWindowView = LayoutInflater.from(mTestCenterActivity).inflate(R.layout.temp_select, null);
        RelativeLayout lsyer = (RelativeLayout) popupWindowView.findViewById(R.id.layer);
        lsyer.setFocusable(true);
        lsyer.setFocusableInTouchMode(true);
        mTestCenterActivity.popupWindow1 = new PopupWindow(popupWindowView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
        popupWindowView.setFocusable(true);
        popupWindowView.setFocusableInTouchMode(true);
        mTestCenterActivity.popupWindow1.setAnimationStyle(R.style.PopupAnimation);
        final PopupWindow finalPopupWindow = mTestCenterActivity.popupWindow1;
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
        mTestCenterActivity.popupWindow1.setBackgroundDrawable(dw);
        ListView listView = popupWindowView.findViewById(R.id.templist);
        ArrayList<TempGet> tempGets = new ArrayList<TempGet>();
        tempGets.addAll(DataPrase.getAllTempid(mSeriesReport.tempId,type,mSeriesReport,mSeriesReport.mReports));
        mTestCenterActivity.mTempAdapter = new TempAdapter(mTestCenterActivity,tempGets,mTestCenterHandler,type,DBHelper.getInstance(mTestCenterActivity).getTempId(EquipMentManager.getTempId(type)),mSeriesReport.mReports);
        listView.setAdapter(mTestCenterActivity.mTempAdapter);
        RelativeLayout btnClose = popupWindowView.findViewById(R.id.layerclose);
        TextView btnOk = popupWindowView.findViewById(R.id.btnok);
        TextView btnCancle = popupWindowView.findViewById(R.id.btncancle);
        NetObject object = new NetObject();
        object.item = mSeriesReport;
        object.result = type;
        btnOk.setTag(object);
        btnOk.setOnClickListener(mTestCenterActivity.doEditlistener);
        btnCancle.setOnClickListener(mTestCenterActivity.doCancleListener);
        btnClose.setOnClickListener(mTestCenterActivity.doCancleListener);
        mTestCenterActivity.popupWindow1.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mTestCenterActivity.shade.setVisibility(View.INVISIBLE);
            }
        });
        mTestCenterActivity.popupWindow1.showAtLocation(mTestCenterActivity.findViewById(R.id.activity_test_center),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        mTestCenterActivity.shade.setVisibility(View.VISIBLE);
    }

    public void doEditTempId(SeriesReport mSeriesReport,String type)
    {
        mSeriesReport.tempId = DataPrase.updataTempid(mSeriesReport.tempId,mTestCenterActivity.mTempAdapter.mTemps,type);
        DBHelper.getInstance(mTestCenterActivity).saveTempId(EquipMentManager.getTempId(type),mTestCenterActivity.mTempAdapter.oldTempid);
        DBHelper.getInstance(mTestCenterActivity).updataReport(mSeriesReport);
        mTestCenterActivity.mSeriasReportAdapter.notifyDataSetChanged();
        mTestCenterActivity.popupWindow1.dismiss();
    }

    public void doAddSelect(SeriesReport seriesReport,boolean isadd) {
        if(mTestCenterActivity.popupWindow1 != null)
        {
            mTestCenterActivity.popupWindow1.dismiss();
        }
        View popupWindowView = LayoutInflater.from(mTestCenterActivity).inflate(R.layout.report_select, null);
        RelativeLayout lsyer = (RelativeLayout) popupWindowView.findViewById(R.id.layer);
        lsyer.setFocusable(true);
        lsyer.setFocusableInTouchMode(true);
        mTestCenterActivity.popupWindow1 = new PopupWindow(popupWindowView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
        popupWindowView.setFocusable(true);
        popupWindowView.setFocusableInTouchMode(true);
        mTestCenterActivity.popupWindow1.setAnimationStyle(R.style.PopupAnimation);
        final PopupWindow finalPopupWindow = mTestCenterActivity.popupWindow1;
        lsyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalPopupWindow.dismiss();

            }
        });
        ColorDrawable dw = new ColorDrawable(0x00ffffff);
        mTestCenterActivity.btnPotc = popupWindowView.findViewById(R.id.potc);
        mTestCenterActivity.btnFada = popupWindowView.findViewById(R.id.fada);
        mTestCenterActivity.btnPotc.setTag(seriesReport);
        mTestCenterActivity.btnFada.setTag(seriesReport);
        if(isadd)
        {
            mTestCenterActivity.btnPotc.setOnClickListener(mTestCenterActivity.doAddPotcListener);
            mTestCenterActivity.btnFada.setOnClickListener(mTestCenterActivity.doAddFadaListener);
        }
        else
        {
            mTestCenterActivity.btnPotc.setOnClickListener(mTestCenterActivity.doChangePotcListener);
            mTestCenterActivity.btnFada.setOnClickListener(mTestCenterActivity.doChangeFadaListener);
        }

        mTestCenterActivity.popupWindow1.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mTestCenterActivity.shade.setVisibility(View.INVISIBLE);
            }
        });
        mTestCenterActivity.popupWindow1.showAtLocation(mTestCenterActivity.findViewById(R.id.activity_test_center),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        mTestCenterActivity.shade.setVisibility(View.VISIBLE);
    }

    public void doTempSelect(SeriesReport seriesReport) {
        if(mTestCenterActivity.popupWindow1 != null)
        {
            mTestCenterActivity.popupWindow1.dismiss();
        }
        View popupWindowView = LayoutInflater.from(mTestCenterActivity).inflate(R.layout.report_select, null);
        RelativeLayout lsyer = (RelativeLayout) popupWindowView.findViewById(R.id.layer);
        lsyer.setFocusable(true);
        lsyer.setFocusableInTouchMode(true);
        mTestCenterActivity.popupWindow1 = new PopupWindow(popupWindowView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
        popupWindowView.setFocusable(true);
        popupWindowView.setFocusableInTouchMode(true);
        mTestCenterActivity.popupWindow1.setAnimationStyle(R.style.PopupAnimation);
        final PopupWindow finalPopupWindow = mTestCenterActivity.popupWindow1;
        lsyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalPopupWindow.dismiss();

            }
        });
        ColorDrawable dw = new ColorDrawable(0x00ffffff);
        mTestCenterActivity.btnPotc = popupWindowView.findViewById(R.id.potc);
        mTestCenterActivity.btnFada = popupWindowView.findViewById(R.id.fada);
        mTestCenterActivity.btnPotc.setTag(seriesReport);
        mTestCenterActivity.btnFada.setTag(seriesReport);
        mTestCenterActivity.btnPotc.setOnClickListener(mTestCenterActivity.doTempPotcListener);
        mTestCenterActivity.btnFada.setOnClickListener(mTestCenterActivity.doTempFadaListener);

        mTestCenterActivity.popupWindow1.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mTestCenterActivity.shade.setVisibility(View.INVISIBLE);
            }
        });
        mTestCenterActivity.popupWindow1.showAtLocation(mTestCenterActivity.findViewById(R.id.activity_test_center),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        mTestCenterActivity.shade.setVisibility(View.VISIBLE);
    }

    public void doDelete(SeriesReport seriesReport) {
        DBHelper.getInstance(mTestCenterActivity).removeReport(seriesReport);
        int page = mTestCenterActivity.mPageSeraisReport.showPage;
        doSearch(page);
    }

    public void addPotc(SeriesReport seriesReport) {
        if(EquipMentManager.getInstance().devicePotc!= null)
        {
            Intent intent = new Intent(mTestCenterActivity, PotcTestActivity.class);
            String idjson = DBHelper.getInstance(mTestCenterActivity).getTempId(EquipMentManager.getTempId(EquipMentManager.DEVICE_POTC));
            String id = DataPrase.addTempDbData(idjson,seriesReport.mReportId,mTestCenterActivity,EquipMentManager.getTempId(EquipMentManager.DEVICE_POTC));
            seriesReport.tempId = DataPrase.addTempid(seriesReport.tempId,id, EquipMentManager.DEVICE_POTC);
            DBHelper.getInstance(mTestCenterActivity).updataReport(seriesReport);
            updataist(seriesReport.mReportId);
            intent.putExtra(EquipMentManager.REPORT_ID,seriesReport.mReportId);
            intent.putExtra("report",seriesReport);
            mTestCenterActivity.startActivity(intent);
            mTestCenterActivity.popupWindow1.dismiss();

        }
        else
        {
            ViewUtils.showMessage(mTestCenterActivity,mTestCenterActivity.getString(R.string.qtest_error_device));
        }
    }

    public void updataist(String mReportId) {
        SeriesReport mSeriesReport = DBHelper.getInstance(mTestCenterActivity).getSeriesReport(mReportId);
        for(int i = 0 ; i < mTestCenterActivity.mSeriasReportAdapter.mSeriesReports.size() ; i++)
        {
            if(mTestCenterActivity.mSeriasReportAdapter.mSeriesReports.get(i).mReportId.equals(mSeriesReport.mReportId))
            {
                mTestCenterActivity.mSeriasReportAdapter.mSeriesReports.get(i).tempId = mSeriesReport.tempId;
                mTestCenterActivity.mSeriasReportAdapter.mSeriesReports.get(i).isUpdate = mSeriesReport.isUpdate;
                mTestCenterActivity.mSeriasReportAdapter.mSeriesReports.get(i).mSendTime = mSeriesReport.mSendTime;
                mTestCenterActivity.mSeriasReportAdapter.mSeriesReports.get(i).mReports.clear();
                mTestCenterActivity.mSeriasReportAdapter.mSeriesReports.get(i).mReports.addAll(mSeriesReport.mReports);
                break;
            }
        }
        mTestCenterActivity.mSeriasReportAdapter.notifyDataSetChanged();
    }

    public void addFada(SeriesReport seriesReport) {

        if(EquipMentManager.getInstance().deviceFada != null)
        {
            Intent intent = new Intent(mTestCenterActivity, FadaTestActivity.class);
            String idjson = DBHelper.getInstance(mTestCenterActivity).getTempId(EquipMentManager.getTempId(EquipMentManager.DEVICE_FADA));
            String id = DataPrase.addTempDbData(idjson,seriesReport.mReportId,mTestCenterActivity,EquipMentManager.getTempId(EquipMentManager.DEVICE_FADA));
            seriesReport.tempId = DataPrase.addTempid(seriesReport.tempId,id, EquipMentManager.DEVICE_FADA);
            DBHelper.getInstance(mTestCenterActivity).updataReport(seriesReport);
            updataist(seriesReport.mReportId);
            intent.putExtra("report",seriesReport);
            intent.putExtra(EquipMentManager.REPORT_ID,seriesReport.mReportId);
            mTestCenterActivity.startActivity(intent);
            mTestCenterActivity.popupWindow1.dismiss();

        }
        else
        {
            ViewUtils.showMessage(mTestCenterActivity,mTestCenterActivity.getString(R.string.qtest_error_device));
        }
    }

    public void changePotc(SeriesReport seriesReport) {
        if(EquipMentManager.getInstance().devicePotc != null)
        {
            if(DataPrase.getTempid(seriesReport.tempId,EquipMentManager.DEVICE_POTC))
            {
                Intent intent = new Intent(mTestCenterActivity, PotcTestActivity.class);
                intent.putExtra(EquipMentManager.REPORT_ID,seriesReport.mReportId);
                intent.putExtra("report",seriesReport);
                mTestCenterActivity.startActivity(intent);
                mTestCenterActivity.popupWindow1.dismiss();
            }
            else
            {
                ViewUtils.showMessage(mTestCenterActivity,mTestCenterActivity.getString(R.string.qtest_error_devicetemp_add));
            }
        }
        else
        {
            ViewUtils.showMessage(mTestCenterActivity,mTestCenterActivity.getString(R.string.qtest_error_device));
        }
    }

    public void changeFada(SeriesReport seriesReport) {

        if(EquipMentManager.getInstance().deviceFada != null)
        {
            if(DataPrase.getTempid(seriesReport.tempId,EquipMentManager.DEVICE_FADA))
            {
                Intent intent = new Intent(mTestCenterActivity, FadaTestActivity.class);
                intent.putExtra(EquipMentManager.REPORT_ID,seriesReport.mReportId);
                intent.putExtra("report",seriesReport);
                mTestCenterActivity.startActivity(intent);
                mTestCenterActivity.popupWindow1.dismiss();
            }
            else
            {
                ViewUtils.showMessage(mTestCenterActivity,mTestCenterActivity.getString(R.string.qtest_error_devicetemp_add));
            }
        }
        else
        {
            ViewUtils.showMessage(mTestCenterActivity,mTestCenterActivity.getString(R.string.qtest_error_device));
        }
    }

    private void setEndImf() {
        String imgf = mTestCenterActivity.getString(R.string.imf_word_now)+String.valueOf(mTestCenterActivity.mPageSeraisReport.showPage)+mTestCenterActivity.getString(R.string.imf_word_page)
                +","+mTestCenterActivity.getString(R.string.imf_word_all)+String.valueOf(mTestCenterActivity.mPageSeraisReport.pageCount)+mTestCenterActivity.getString(R.string.imf_word_page)
                +","+mTestCenterActivity.getString(R.string.imf_word_every)+String.valueOf(mTestCenterActivity.mPageSeraisReport.pageMax)+mTestCenterActivity.getString(R.string.imf_word_one)
                +","+mTestCenterActivity.getString(R.string.imf_word_all)+String.valueOf(mTestCenterActivity.mPageSeraisReport.totalcount)+mTestCenterActivity.getString(R.string.imf_word_one)
                +mTestCenterActivity.getString(R.string.imf_word_data);
        mTestCenterActivity.txtImf.setText(imgf);
    }

    class DeleteListener implements DialogInterface.OnClickListener {
        public SeriesReport seriesReport;
        public DeleteListener(SeriesReport seriesReport)
        {
            this.seriesReport = seriesReport;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            doDelete(seriesReport);

        }
    }

    public void startPrint(SeriesReport seriesReport) {
        Intent intent = new Intent(mTestCenterActivity, ReportPrintActivity.class);
        intent.putExtra("report",seriesReport);
//        intent.putExtra(EquipMentManager.REPORT_ID,DataPrase.praseReportJson(seriesReport));
        mTestCenterActivity.startActivity(intent);
    }

    public void doUpload(SeriesReport seriesReport) {

//        if(seriesReport.mReports.size() > 0)
        {
            mTestCenterActivity.waitDialog.show();
            if(AppUtils.gotoMain(mTestCenterActivity) == false)
            {
                ReportAsks.reportAdd(seriesReport,mTestCenterHandler,mTestCenterActivity);
            }

        }
//        else
//        {
//            ViewUtils.showMessage(mTestCenterActivity,mTestCenterActivity.getString(R.string.test_center_report_updete));
//        }
//        mTestCenterActivity.popupWindow1.dismiss();
    }

    public void creatButtomMenu(ArrayList<MenuItem> mMenuItems) {

        if(mTestCenterActivity.popupWindow1 != null)
        {
            mTestCenterActivity.popupWindow1.dismiss();
        }
        View popupWindowView = LayoutInflater.from(mTestCenterActivity).inflate(R.layout.buttom_menu, null);
        RelativeLayout lsyer = (RelativeLayout) popupWindowView.findViewById(R.id.layer);
        lsyer.setFocusable(true);
        lsyer.setFocusableInTouchMode(true);
        mTestCenterActivity.popupWindow1 = new PopupWindow(popupWindowView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
        popupWindowView.setFocusable(true);
        popupWindowView.setFocusableInTouchMode(true);
        mTestCenterActivity.popupWindow1.setAnimationStyle(R.style.PopupAnimation);
        lsyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTestCenterActivity.popupWindow1.dismiss();

            }
        });
        ColorDrawable dw = new ColorDrawable(0x00ffffff);
        mTestCenterActivity.popupWindow1.setBackgroundDrawable(dw);
        LinearLayout linelayer = (LinearLayout) popupWindowView.findViewById(R.id.pop_layout);
        View view;
        Button btn;
        if(mMenuItems.size() == 1)
        {
            view = LayoutInflater.from(mTestCenterActivity).inflate(R.layout.buttom_menu_btn2, null);
            btn = (Button) view.findViewById(R.id.btn);
            btn.setText(mMenuItems.get(0).btnName);
            btn.setTag(mMenuItems.get(0).item);
            btn.setOnClickListener(mMenuItems.get(0).mListener);
            linelayer.addView(view);
        }
        else
        {

            for(int i = 0 ; i < mMenuItems.size() ; i++)
            {

                if(i == 0 )
                {
                    view = LayoutInflater.from(mTestCenterActivity).inflate(R.layout.buttom_menu_btn1, null);btn = (Button) view.findViewById(R.id.btn);
                    btn.setText(mMenuItems.get(i).btnName);
                    btn.setTag(mMenuItems.get(i).item);
                    btn.setOnClickListener(mMenuItems.get(i).mListener);
                    linelayer.addView(view);
                }
                else if(i == mMenuItems.size()-1)
                {
                    view = LayoutInflater.from(mTestCenterActivity).inflate(R.layout.buttom_menu_btn3, null);btn = (Button) view.findViewById(R.id.btn);
                    btn.setText(mMenuItems.get(i).btnName);
                    btn.setTag(mMenuItems.get(i).item);
                    btn.setOnClickListener(mMenuItems.get(i).mListener);
                    linelayer.addView(view);
                }
                else
                {
                    view = LayoutInflater.from(mTestCenterActivity).inflate(R.layout.buttom_menu_btn4, null);btn = (Button) view.findViewById(R.id.btn);
                    btn.setText(mMenuItems.get(i).btnName);
                    btn.setTag(mMenuItems.get(i).item);
                    btn.setOnClickListener(mMenuItems.get(i).mListener);
                    linelayer.addView(view);
                }
                if(i != mMenuItems.size()-1)
                {
                    view = LayoutInflater.from(mTestCenterActivity).inflate(R.layout.buttom_menu_line, null);
                    linelayer.addView(view);
                }
            }
        }
        view = LayoutInflater.from(mTestCenterActivity).inflate(R.layout.emptylayer, null);
        linelayer.addView(view);
        view = LayoutInflater.from(mTestCenterActivity).inflate(R.layout.buttom_menu_btn2, null);
        btn = (Button) view.findViewById(R.id.btn);
        btn.setText(mTestCenterActivity.getString(R.string.button_word_cancle));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTestCenterActivity.popupWindow1.dismiss();
            }
        });
        linelayer.addView(view);

        mTestCenterActivity.shade.setVisibility(View.VISIBLE);
        mTestCenterActivity.popupWindow1.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mTestCenterActivity.shade.setVisibility(View.INVISIBLE);
            }
        });
        mTestCenterActivity.popupWindow1.showAtLocation(mTestCenterActivity.findViewById(R.id.activity_test_center),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }
}
