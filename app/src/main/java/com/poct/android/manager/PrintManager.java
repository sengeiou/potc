package com.poct.android.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poct.R;
import com.poct.android.entity.PrintTask;
import com.poct.android.entity.Report;
import com.poct.android.entity.SeriesReport;
import com.poct.android.handler.PrintHandler;
import com.poct.android.thread.PrintThread;
import com.poct.android.utils.AppActivityManager;
import com.poct.android.utils.ViewUtils;
import com.poct.android.view.PoctApplication;

import java.util.ArrayList;


public class PrintManager {

    public static final int SEND_PRINT_INIT = 70000;
    public static final int SEND_DATA_PRINT = 70001;
    public static final int SEND_SUCCESS = 70002;
    public static final int SEND_FAIL = 70003;
    public static final int NONE_PRINT = 70004;
    public static PrintManager mPrintManager;
    public PrintHandler mPrintHandler;
    public ArrayList<PrintTask> threads = new ArrayList<PrintTask>();
    public PrintThread startTask = null;
    public boolean reset = false;
    public static synchronized PrintManager getInstance() {
        if (mPrintManager == null) {
            mPrintManager = new PrintManager();
        }
        return mPrintManager;
    }

    public PrintManager() {
        mPrintHandler = new PrintHandler();
    }

    public void addPrint(Bitmap bitmap,String reportid) {

        PrintTask task = new PrintTask();
        task.bitmap = bitmap;
        task.id = reportid;
        threads.add(task);
        startPrintNewThread();
    }

    public void startPrintNewThread() {
        if(threads.size() > 0)
        {
            if(startTask == null)
            {

                startTask = new PrintThread(threads.get(0).bitmap,mPrintHandler);
                startTask.start();
            }
        }
        else
        {
            ViewUtils.showMessage(AppActivityManager.getAppActivityManager().getCurrentActivity(),PoctApplication.mApp.getString(R.string.print_message_print_all));
        }
    }

    public void startPrintNewThread1() {
        reset = true;
        if(threads.size() > 0)
        {
            if(startTask == null)
            {
                startTask = new PrintThread(threads.get(0).bitmap,mPrintHandler);
                startTask.start();
            }
        }
    }

    public void restart()
    {
        if(reset)
        {
            reset = false;
            ViewUtils.showMessage(AppActivityManager.getAppActivityManager().getCurrentActivity(),"检测到重新设置过打印机，将再次尝试打印");
            startTask = new PrintThread(threads.get(0).bitmap,mPrintHandler);
            startTask.start();
        }

    }


    public View creatPrintView(Context context, SeriesReport seriesReport) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.print_data_view, null);
        TextView txtDName;
        TextView txtDAge;
        TextView txtDSex;
        TextView txtDCNUM;
        TextView txtDHNUM;
        TextView txtDDes;
        TextView txtDDep;
        TextView txtDBad;
        TextView txtDSender;
        TextView txtDSendTime;
        TextView txtDTester;
        TextView txtDApporver;
        TextView txtDApporvTime;
        TextView txtDMemo;
        txtDName = view.findViewById(R.id.namevalue);
        txtDCNUM = view.findViewById(R.id.pnumbervalue);
        txtDDep = view.findViewById(R.id.departvalue);
        txtDSender = view.findViewById(R.id.sendervalue);
        txtDAge = view.findViewById(R.id.agevalue);
        txtDHNUM = view.findViewById(R.id.dnumbervalue);
        txtDBad = view.findViewById(R.id.bnumbervalue);
        txtDSendTime = view.findViewById(R.id.senddetevalue);
        txtDSex = view.findViewById(R.id.sexvalue);
        txtDDes = view.findViewById(R.id.desvalue);
        txtDTester = view.findViewById(R.id.testnamevalue);
        txtDApporver = view.findViewById(R.id.approvenamevalue);
        txtDApporvTime = view.findViewById(R.id.reporttimevalue);
        txtDMemo = view.findViewById(R.id.memovalue);
        txtDName.setText(seriesReport.mName);
        txtDCNUM.setText(seriesReport.mCNum);
        txtDDep.setText(seriesReport.mDepart);
        txtDSender.setText(seriesReport.mSender);
        txtDAge.setText(seriesReport.mAge);
        txtDHNUM.setText(seriesReport.mHNum);
        txtDBad.setText(seriesReport.mBad);
//        if(EquipMentManager.getInstance().printReport.mSendTime.length() > 0)
        txtDSendTime.setText(seriesReport.mSendTime.substring(0,10));
//        else
//            txtDSendTime.setText(EquipMentManager.getInstance().printReport.mTime);
        txtDSex.setText(seriesReport.mSex);
        txtDDes.setText(seriesReport.mDes);
//        if (seriesReport.mAccountId.equals(PoctApplication.mApp.account.mAccountId))
//            txtDTester.setText(PoctApplication.mApp.account.mDoctor);
//        if(EquipMentManager.getInstance().printReport.isApprove.equals("通过"))
        txtDTester.setText("沃妙琳");
//        else
//            txtDTester.setText("");
//        txtDApporver.setText(seriesReport.mApprover);
//        if(EquipMentManager.getInstance().printReport.isApprove.equals("通过"))
//        txtDApporver.setText("姚燕峰");
//        else
//            txtDApporver.setText("");
        txtDApporver.setText(seriesReport.mApprover);
//        if(seriesReport.mApproveTime.length() > 0)
        txtDApporvTime.setText(seriesReport.mSendTime.substring(0,10));
        if (seriesReport.mMemo.length() > 0)
            txtDMemo.setText(seriesReport.mMemo);

        if (seriesReport != null) {
            for (int i = 0; i < seriesReport.mReports.size(); i++) {
                initReportView(seriesReport.mReports.get(i), (LinearLayout) view.findViewById(R.id.reportlist),context,i);
            }
        }
        return view;
    }

    public void initReportView(Report report, LinearLayout linearLayout,Context context,int i) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.report_item, null);
        TextView textView = view.findViewById(R.id.projecttitle);
        textView.setText(String.valueOf(i+1)+"."+report.projectName);
        textView = view.findViewById(R.id.resulttitle);
        textView.setText(report.mGod + report.mResult);
        textView = view.findViewById(R.id.temptitle);
        textView.setText(report.tempResult);
        textView = view.findViewById(R.id.unittitle);
        textView.setText(report.unit);
        textView = view.findViewById(R.id.testtimetitle);
        if(report.time.length() > 0)
        textView.setText(report.time.substring(0,16));
        linearLayout.addView(view);
    }


    public int unPack(String printDataIn) {
        int PackageResult = 0;

        System.out.println("log_pt_ui:" + printDataIn.length());

        if (printDataIn.length() <= 10240) {
            char[] printDataArray = printDataIn.toCharArray();

            int printDataSize = printDataArray.length;
            Runtime.getRuntime().gc();
            PackageResult = PoctApplication.mApp.mPrintUtil.combinationPackage(printDataArray, printDataSize, printDataSize, printDataSize);
        }
        else {
            int countsend = 0;
            for (countsend = 0; printDataIn.length() / 10240 > countsend; countsend++) {

                char[] printDataArray = printDataIn.substring(10240 * (countsend), 10240 * (countsend + 1)).toCharArray();
                int printDataSize = printDataArray.length;
                PackageResult += PoctApplication.mApp.mPrintUtil.combinationPackage(printDataArray, printDataSize, printDataSize, printDataIn.length());
            }
            if (printDataIn.length() % 10240 != 0) {
                char[] printDataArray = printDataIn.substring((printDataIn.length() / 10240) * 10240, printDataIn.length())
                        .toCharArray();
                int printDataSize = printDataArray.length;
                PackageResult += PoctApplication.mApp.mPrintUtil.combinationPackage(printDataArray, printDataSize, printDataSize, printDataIn.length());
            }
        }
        return PackageResult;
    }

    public byte[] getBmpToByte(Bitmap bitmap) {

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] pixels = new int[w * h];
        bitmap.getPixels(pixels, 0, w, 0, 0, w, h);
        byte[] rgb = addBMP_RGB_888(pixels, w, h);
        byte[] header = addBMPImageHeader(rgb.length);
        byte[] infos = addBMPImageInfosHeader(rgb.length, w, h);

        byte[] buffer = new byte[54 + rgb.length];
        System.arraycopy(header, 0, buffer, 0, header.length);
        System.arraycopy(infos, 0, buffer, 14, infos.length);
        System.arraycopy(rgb, 0, buffer, 54, rgb.length);

        return buffer;
    }

    private byte[] addBMP_RGB_888(int[] b, int w, int h) {
        int len = b.length;
        System.out.println(b.length);
        byte[] buffer = new byte[w * h * 3];
        int offset = 0;

        for (int i = len - 1; i >= w - 1; i -= w)
        {
            int end = i, start = i - w + 1;
            for (int j = start; j <= end; j++)
            {
                buffer[offset] = (byte) (b[j]);
                buffer[offset + 1] = (byte) (b[j] >> 8);
                buffer[offset + 2] = (byte) (b[j] >> 16);
                offset += 3;
            }
        }

        return buffer;

    }

    private byte[] addBMPImageHeader(int size) {
        byte[] buffer = new byte[14];
        buffer[0] = 0x42;
        buffer[1] = 0x4D;
        buffer[2] = (byte) ((size + 54)>> 0);
        buffer[3] = (byte) ((size + 54)>> 8);
        buffer[4] = (byte) ((size + 54)>> 16);
        buffer[5] = (byte) ((size + 54)>> 24);
        buffer[6] = 0x00;
        buffer[7] = 0x00;
        buffer[8] = 0x00;
        buffer[9] = 0x00;
        buffer[10] = 0x36;
        buffer[11] = 0x00;
        buffer[12] = 0x00;
        buffer[13] = 0x00;
        return buffer;
    }

    private byte[] addBMPImageInfosHeader(int size, int w, int h) {
        byte[] buffer = new byte[40];

        // 这个是固定的 BMP 信息头要40个字节
        buffer[0] = 0x28;
        buffer[1] = 0x00;
        buffer[2] = 0x00;
        buffer[3] = 0x00;

        // 宽度 地位放在序号前的位置 高位放在序号后的位置
        buffer[4] = (byte) (w >> 0);
        buffer[5] = (byte) (w >> 8);
        buffer[6] = (byte) (w >> 16);
        buffer[7] = (byte) (w >> 24);

        // 长度 同上
        buffer[8] = (byte) (h >> 0);
        buffer[9] = (byte) (h >> 8);
        buffer[10] = (byte) (h >> 16);
        buffer[11] = (byte) (h >> 24);

        // 总是被设置为1
        buffer[12] = 0x01;
        buffer[13] = 0x00;

        // 比特数 像素 32位保存一个比特 这个不同的方式(ARGB 32位 RGB24位不同的!!!!
        buffer[14] = 0x18;
        buffer[15] = 0x00;

        // 0-不压缩 1-8bit位图
        // 2-4bit位图 3-16/32位图
        // 4 jpeg 5 png
        buffer[16] = 0x00;
        buffer[17] = 0x00;
        buffer[18] = 0x00;
        buffer[19] = 0x00;

        // 说明图像大小
        buffer[20] = (byte)size;
        buffer[21] = (byte)(size>>8);
        buffer[22] = (byte)(size>>16);
        buffer[23] = (byte)(size>>24);

        // 水平分辨率
        buffer[24] = 0x00;
        buffer[25] = 0x00;
        buffer[26] = 0x00;
        buffer[27] = 0x00;

        // 垂直分辨率
        buffer[28] = 0x00;
        buffer[29] = 0x00;
        buffer[30] = 0x00;
        buffer[31] = 0x00;

        // 0 使用所有的调色板项
        buffer[32] = 0x00;
        buffer[33] = 0x00;
        buffer[34] = 0x00;
        buffer[35] = 0x00;

        // 不开颜色索引
        buffer[36] = 0x00;
        buffer[37] = 0x00;
        buffer[38] = 0x00;
        buffer[39] = 0x00;
        return buffer;
    }
}
