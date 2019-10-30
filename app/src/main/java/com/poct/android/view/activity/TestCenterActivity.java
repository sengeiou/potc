package com.poct.android.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poct.android.entity.NetObject;
import com.poct.android.entity.PageSeraisReport;
import com.poct.android.entity.SeriesReport;
import com.poct.android.manager.EquipMentManager;
import com.poct.android.presenter.TestCenterPresenter;
import com.poct.android.view.adapter.SeriasReportAdapter;
import com.poct.android.view.adapter.TempAdapter;

import java.util.ArrayList;

/**
 * Created by xpx on 2017/8/18.
 */

public class TestCenterActivity extends BaseActivity {
    public static final String ACTION_UPDETE_REPORT_LIST = "ACTION_UPDETE_REPORT_LIST";
    public static final String ACTION_UPLOAD_REPORT = "ACTION_UPLOAD_REPORT";
    public static final String ACTION_UPDETE_LIST = "ACTION_UPDETE_LIST";
    public RelativeLayout btnBack;
    public RelativeLayout btnCreat;
    public RelativeLayout btnSearch;
    public RelativeLayout btnReset;
    public RelativeLayout btnDete1;
    public RelativeLayout btnDete2;
    public EditText etxtName;
    public EditText etxtSender;
    public TextView etxtDete1;
    public TextView etxtDete2;
    public ListView listReport;
    public EditText cetxtName;
    public EditText cetxtAge;
    public RadioButton mMaleRb;
    public RadioButton mFamaleRb;
    public EditText cetxtId;
    public EditText cetxtCnum;
    public EditText cetxtHnum;
    public EditText cetxtDep;
    public EditText cetxtBad;
    public EditText cetxtDes;
    public EditText cetxtMemo;
    public TextView btnOk;
    public TextView btnCancle;
    public RelativeLayout btnClose;
    public TextView txtImf;
    public TextView btnBegin;
    public TextView btnNext;
    public TextView btnPre;
    public TextView btnEnd;
    public RelativeLayout btnPotc;
    public RelativeLayout btnFada;
    public PageSeraisReport mPageSeraisReport;
    public ArrayList<SeriesReport> mSeriesReports = new ArrayList<SeriesReport>();
    public SeriasReportAdapter mSeriasReportAdapter;
    public PopupWindow popupWindow1;
    public RelativeLayout shade;
    public TempAdapter mTempAdapter;
    public TestCenterPresenter mTestCenterPresenter = new TestCenterPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTestCenterPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mTestCenterPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener doBacklistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTestCenterPresenter.mTestCenterActivity.finish();
        }
    };

    public View.OnClickListener doCreatlistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTestCenterPresenter.creatPeport();
        }
    };

    public View.OnClickListener doOklistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getTag() != null)
            mTestCenterPresenter.doOk((SeriesReport) v.getTag());
            else
                mTestCenterPresenter.doOk(null);
        }
    };

    public View.OnClickListener doEditlistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            NetObject netObject = (NetObject) v.getTag();
            mTestCenterPresenter.doEditTempId((SeriesReport) netObject.item,netObject.result);
        }
    };

    public View.OnClickListener doSearchlistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTestCenterPresenter.doSearch();
        }
    };

    public View.OnClickListener doResetlistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTestCenterPresenter.doReset();
        }
    };

    public View.OnClickListener doSetDeteBeginlistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTestCenterPresenter.setDateBegin();
        }
    };

    public View.OnClickListener doSetDeteEndlistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTestCenterPresenter.setDateEnd();
        }
    };

    public View.OnClickListener doBeginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTestCenterPresenter.doBegin();
        }
    };

    public View.OnClickListener doEndListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTestCenterPresenter.doEnd();
        }
    };

    public View.OnClickListener doNextListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTestCenterPresenter.doNext();
        }
    };

    public View.OnClickListener doPreListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTestCenterPresenter.doPre();
        }
    };

    public View.OnClickListener doCancleListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(popupWindow1 != null)
            {
                popupWindow1.dismiss();
            }
        }
    };

    public View.OnClickListener doAddPotcListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTestCenterPresenter.addPotc((SeriesReport) v.getTag());
        }
    };

    public View.OnClickListener doAddFadaListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTestCenterPresenter.addFada((SeriesReport) v.getTag());
        }
    };

    public View.OnClickListener doChangePotcListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTestCenterPresenter.changePotc((SeriesReport) v.getTag());
        }
    };

    public View.OnClickListener doChangeFadaListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTestCenterPresenter.changeFada((SeriesReport) v.getTag());
        }
    };


    public View.OnClickListener doTempPotcListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTestCenterPresenter.changeTemp((SeriesReport) v.getTag(), EquipMentManager.DEVICE_POTC);
        }
    };

    public View.OnClickListener doTempFadaListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTestCenterPresenter.changeTemp((SeriesReport) v.getTag(),EquipMentManager.DEVICE_FADA);
        }
    };

    public View.OnClickListener deleteListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            if(popupWindow1 != null)
                popupWindow1.dismiss();
            mTestCenterPresenter.doDeleteSelect((SeriesReport) v.getTag());
        }
    };

    public View.OnClickListener detialListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if(popupWindow1 != null)
                popupWindow1.dismiss();
            mTestCenterPresenter.startPrint((SeriesReport) v.getTag());
        }
    };

    public View.OnClickListener printListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

        }
    };

    public View.OnClickListener uploadListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if(popupWindow1 != null)
                popupWindow1.dismiss();
            mTestCenterPresenter.doUpload((SeriesReport) v.getTag());
        }
    };

    public View.OnClickListener addListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if(popupWindow1 != null)
                popupWindow1.dismiss();

            mTestCenterPresenter.doAddSelect((SeriesReport) v.getTag(),true);
//            mTestCenterPresenter.doAddSelect((SeriesReport) v.getTag(),false);
        }
    };

    public View.OnClickListener changeListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if(popupWindow1 != null)
                popupWindow1.dismiss();
            mTestCenterPresenter.changeButtomMenu((SeriesReport) v.getTag());
        }
    };

    public View.OnClickListener changeListener1 = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if(popupWindow1 != null)
                popupWindow1.dismiss();
            mTestCenterPresenter.doAddSelect((SeriesReport) v.getTag(),false);
        }
    };

    public View.OnClickListener changeListener2 = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if(popupWindow1 != null)
                popupWindow1.dismiss();
            mTestCenterPresenter.changeCreat((SeriesReport) v.getTag());
        }
    };

    public View.OnClickListener changeListener3 = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if(popupWindow1 != null)
                popupWindow1.dismiss();
            mTestCenterPresenter.doTempSelect((SeriesReport) v.getTag());
        }
    };
}
