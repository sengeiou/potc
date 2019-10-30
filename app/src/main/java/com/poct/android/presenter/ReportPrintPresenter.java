package com.poct.android.presenter;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.poct.R;
import com.poct.android.handler.ReportPrintHandler;
import com.poct.android.manager.EquipMentManager;
import com.poct.android.manager.PrintManager;
import com.poct.android.utils.ViewUtils;
import com.poct.android.view.activity.ReportPrintActivity;
import com.poct.android.view.adapter.ReportAdapter;

/**
 * Created by xpx on 2017/8/18.
 */

public class ReportPrintPresenter implements Presenter {

    public ReportPrintHandler mReportPrintHandler;
    public ReportPrintActivity mReportPrintActivity;

    public ReportPrintPresenter(ReportPrintActivity mReportPrintActivity) {
        this.mReportPrintActivity = mReportPrintActivity;
        this.mReportPrintHandler = new ReportPrintHandler(mReportPrintActivity);
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        mReportPrintActivity.setContentView(R.layout.activity_print_report);
        mReportPrintActivity.printReport = mReportPrintActivity.getIntent().getParcelableExtra("report");
//        mReportPrintActivity.printReport = DataPrase.getJsonSerieaReport(mReportPrintActivity.getIntent().getStringExtra(EquipMentManager.REPORT_ID));
        mReportPrintActivity.btnPrint = mReportPrintActivity.findViewById(R.id.layerprint);
        mReportPrintActivity.btnBack = mReportPrintActivity.findViewById(R.id.layerback);
        mReportPrintActivity.txtName = mReportPrintActivity.findViewById(R.id.namevalue);
        mReportPrintActivity.txtCNUM = mReportPrintActivity.findViewById(R.id.pnumbervalue);
        mReportPrintActivity.txtDep = mReportPrintActivity.findViewById(R.id.departvalue);
        mReportPrintActivity.txtSender = mReportPrintActivity.findViewById(R.id.sendervalue);
        mReportPrintActivity.txtAge = mReportPrintActivity.findViewById(R.id.agevalue);
        mReportPrintActivity.txtHNUM = mReportPrintActivity.findViewById(R.id.dnumbervalue);
        mReportPrintActivity.txtBad = mReportPrintActivity.findViewById(R.id.bnumbervalue);
        mReportPrintActivity.txtSendTime = mReportPrintActivity.findViewById(R.id.senddetevalue);
        mReportPrintActivity.txtSex = mReportPrintActivity.findViewById(R.id.sexvalue);
        mReportPrintActivity.txtDes = mReportPrintActivity.findViewById(R.id.desvalue);
        mReportPrintActivity.mRelativeLayout = mReportPrintActivity.findViewById(R.id.printdata);
        mReportPrintActivity.mListView = mReportPrintActivity.findViewById(R.id.listreport);
        ReportAdapter mReportAdapter = new ReportAdapter(mReportPrintActivity,mReportPrintActivity.printReport.mReports,mReportPrintHandler);
        mReportPrintActivity.mListView.setAdapter(mReportAdapter);
        mReportPrintActivity.txtName.setText(mReportPrintActivity.printReport.mName);
        mReportPrintActivity.txtCNUM.setText(mReportPrintActivity.printReport.mCNum);
        mReportPrintActivity.txtDep.setText(mReportPrintActivity.printReport.mDepart);
        mReportPrintActivity.txtSender.setText(mReportPrintActivity.printReport.mSender);
        mReportPrintActivity.txtAge.setText(mReportPrintActivity.printReport.mAge);
        mReportPrintActivity.txtHNUM.setText(mReportPrintActivity.printReport.mHNum);
        mReportPrintActivity.txtBad.setText(mReportPrintActivity.printReport.mBad);
//        if(EquipMentManager.getInstance().printReport.mSendTime.length() > 0)
        mReportPrintActivity.txtSendTime.setText(mReportPrintActivity.printReport.mSendTime.substring(0,10));
//        else
//            mReportPrintActivity.txtSendTime.setText(EquipMentManager.getInstance().printReport.mSendTime);
        mReportPrintActivity.txtSex.setText(mReportPrintActivity.printReport.mSex);
        mReportPrintActivity.txtDes.setText(mReportPrintActivity.printReport.mDes);
        mReportPrintActivity.btnBack.setOnClickListener(mReportPrintActivity.doBacklistener);
        mReportPrintActivity.btnPrint.setOnClickListener(mReportPrintActivity.printListener);
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
        mReportPrintHandler = null;
    }


    public static Bitmap getBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.RGB_565);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        // Draw background
        Drawable bgDrawable = v.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(c);
        else
            c.drawColor(Color.WHITE);
        // Draw view to canvas
        v.draw(c);
        return b;
    }

    public void doPrint() {

        if(EquipMentManager.getInstance().devicePrint == null)
        {
            ViewUtils.showMessage(mReportPrintActivity,mReportPrintActivity.getString(R.string.error_no_print));

        }
        else
        {
            if(!mReportPrintActivity.printReport.isApprove.equals("通过"))
            {
                ViewUtils.creatDialogTowButton(mReportPrintActivity,mReportPrintActivity.getString(R.string.print_approve_msg),mReportPrintActivity.getString(R.string.print_approve_title)
                        ,mReportPrintActivity.getString(R.string.button_word_cancle),mReportPrintActivity.getString(R.string.button_still_print),null,new PrintListener());
            }
            else
            {
//            if(mReportPrintActivity.printsetting == false)
//            {
//                mReportPrintActivity.printsetting = true;
//                mReportPrintActivity.waitDialog.show();
//                ViewUtils.showMessage(mReportPrintActivity,mReportPrintActivity.getString(R.string.print_message_view));
//                if(mReportPrintHandler != null)
//                mReportPrintHandler.sendEmptyMessage(ReportPrintHandler.CREAT_PRINT_VIEW_START);
//            }
//            else
//            {
//                ViewUtils.showMessage(mReportPrintActivity,mReportPrintActivity.getString(R.string.print_message_error));
//            }
                ViewUtils.showMessage(mReportPrintActivity,mReportPrintActivity.getString(R.string.print_message_view));
                if(mReportPrintHandler != null)
                    mReportPrintHandler.sendEmptyMessage(ReportPrintHandler.CREAT_PRINT_VIEW_START);
            }

        }


    }

    class PrintListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
//            if(mReportPrintActivity.printsetting == false)
//            {
//                mReportPrintActivity.printsetting = true;
//                mReportPrintActivity.waitDialog.show();
//
//                if(mReportPrintHandler != null)
//                mReportPrintHandler.sendEmptyMessage(ReportPrintHandler.CREAT_PRINT_VIEW_START);
//            }
//            else
//            {
//                ViewUtils.showMessage(mReportPrintActivity,mReportPrintActivity.getString(R.string.print_message_error));
//            }
//            ViewUtils.showMessage(mReportPrintActivity,mReportPrintActivity.getString(R.string.print_message_view));
            ViewUtils.showMessage(mReportPrintActivity,mReportPrintActivity.getString(R.string.print_message_view));
            if(mReportPrintHandler != null)
            mReportPrintHandler.sendEmptyMessage(ReportPrintHandler.CREAT_PRINT_VIEW_START);
        }
    }

    public void creatView()
    {
        mReportPrintActivity.mRelativeLayout.addView(PrintManager.getInstance().creatPrintView(mReportPrintActivity,mReportPrintActivity.printReport));
        if(mReportPrintHandler != null)
        {
            mReportPrintHandler.removeMessages(ReportPrintHandler.CREAT_PRINT_VIEW);
            mReportPrintHandler.sendEmptyMessageDelayed(ReportPrintHandler.CREAT_PRINT_VIEW,100);
        }

    }

    public void startPrint() {
        Bitmap mBitmap = getBitmapFromView(mReportPrintActivity.mRelativeLayout);
        mReportPrintActivity.waitDialog.hide();
        ViewUtils.showMessage(mReportPrintActivity,mReportPrintActivity.getString(R.string.print_message_view_success));
        PrintManager.getInstance().addPrint(mBitmap,mReportPrintActivity.printReport.mReportId);
    }
}
