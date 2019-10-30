package com.poct.android.asks;

import android.content.Context;
import android.os.Handler;

import com.poct.android.entity.Report;
import com.poct.android.entity.SeriesReport;
import com.poct.android.net.NetTaskManager;
import com.poct.android.net.NetUtils;
import com.poct.android.net.nettask.PostJsonNetItemTask;
import com.poct.android.net.nettask.PostJsonNetTask;
import com.poct.android.view.PoctApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ReportAsks {

    public static final String REPORT_ADD_PATH = "/upload/examiningreport";
    public static final String REPORT_GET_PATH = "/query/queryexamreport";
    public static final int REPORT_ADD_SUCCESS = 10100;
    public static final int REPORT_ADD_FAIL = 20100;
    public static final int REPORT_GET_SUCCESS = 10101;
    public static final int REPORT_GET_FAIL = 20101;

    public static void reportAdd(SeriesReport seriesReport,Handler mHandler,Context mContext) {
        try {
            String urlString = NetUtils.getInstance().createURLStringoa(REPORT_ADD_PATH);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ReportID",seriesReport.mReportId);
            jsonObject.put("StationID", PoctApplication.mApp.account.mAccountId);
//            seriesReport.mSendTime = AppUtils.getDate()+" "+AppUtils.getTime();
            jsonObject.put("InspectionTime",seriesReport.mSendTime);
            jsonObject.put("IDNumber",seriesReport.mPId);
            jsonObject.put("Name",seriesReport.mName);
            jsonObject.put("Age",seriesReport.mAge);
            jsonObject.put("Sex",seriesReport.mSex);
            jsonObject.put("MRNumber",seriesReport.mCNum);
            jsonObject.put("BedNumber",seriesReport.mBad);
            jsonObject.put("Department",seriesReport.mDepart);
            jsonObject.put("OutpatientNumber",seriesReport.mHNum);
            jsonObject.put("Diagnosis",seriesReport.mDes);
            jsonObject.put("Memo",seriesReport.mMemo);
            JSONArray ja = new JSONArray();
            jsonObject.put("ReportDetail",ja);
            for(int i = 0 ; i < seriesReport.mReports.size() ; i++)
            {
                Report report = seriesReport.mReports.get(i);
                JSONObject jo = new JSONObject();
                jo.put("ProjectID",i+1);
                jo.put("SampleID",report.tempId);
                jo.put("Result",report.mGod+report.mResult);
                jo.put("Reference",report.tempResult);
                jo.put("Unit",report.unit);
                jo.put("ProjectName",report.projectName);
                jo.put("InspectTime",report.time);
                ja.put(jo);
            }

            String postBody =  jsonObject.toString();
            PostJsonNetItemTask mPostNetTask = new PostJsonNetItemTask(urlString, mHandler, REPORT_ADD_SUCCESS,
                    REPORT_ADD_FAIL, mContext, postBody,seriesReport);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getReports(String data1,String data2,Handler mHandler,Context mContext) {
        try {
            String urlString = NetUtils.getInstance().createURLStringoa(REPORT_GET_PATH);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ID",PoctApplication.mApp.account.mAccountId);
            jsonObject.put("StartDate",data1+" 00:00:00");
            jsonObject.put("EndDate",data2+" 23:59:59");
            String postBody =  jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, REPORT_GET_SUCCESS,
                    REPORT_GET_FAIL, mContext, postBody);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
