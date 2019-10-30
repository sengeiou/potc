package com.poct.android.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.poct.android.utils.XpxJSONArray;
import com.poct.android.utils.XpxJSONObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SeriesReport implements Parcelable {

    public String mReportId = "";
    public String mPId = "";
    public String mAccountId = "";
    public String mName = "";
    public String mSex = "";
    public String mAge = "";
    public String mSender = "";
//    public String mTime = "";
    public String mSendTime = "";
    public String mApprover = "";
    public String mCNum = "";
    public String mHNum = "";
    public String mDepart = "";
    public String mBad = "";
    public String mDes = "";
    public String mMemo = "";
    public String tempId = "";
    public String mApproveTime = "";
    public boolean isUpdate = false;
    public String isApprove = "";
    public int updatacount = 0;
    public ArrayList<Report> mReports = new ArrayList<Report>();

    public SeriesReport() {

    }

    protected SeriesReport(Parcel in) {
        mReportId = in.readString();
        mPId = in.readString();
        mAccountId = in.readString();
        mName = in.readString();
        mSex = in.readString();
        mAge = in.readString();
        mSender = in.readString();
        mSendTime = in.readString();
        mApprover = in.readString();
        mCNum = in.readString();
        mHNum = in.readString();
        mDepart = in.readString();
        mBad = in.readString();
        mDes = in.readString();
        mMemo = in.readString();
        tempId = in.readString();
        mApproveTime = in.readString();
        isUpdate = in.readByte() != 0;
        isApprove = in.readString();
        updatacount = in.readInt();
        praseReport(in.readString(),mReports);
    }

    public static final Creator<SeriesReport> CREATOR = new Creator<SeriesReport>() {
        @Override
        public SeriesReport createFromParcel(Parcel in) {
            return new SeriesReport(in);
        }

        @Override
        public SeriesReport[] newArray(int size) {
            return new SeriesReport[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mReportId);
        dest.writeString(mPId);
        dest.writeString(mAccountId);
        dest.writeString(mName);
        dest.writeString(mSex);
        dest.writeString(mAge);
        dest.writeString(mSender);
        dest.writeString(mSendTime);
        dest.writeString(mApprover);
        dest.writeString(mCNum);
        dest.writeString(mHNum);
        dest.writeString(mDepart);
        dest.writeString(mBad);
        dest.writeString(mDes);
        dest.writeString(mMemo);
        dest.writeString(tempId);
        dest.writeString(mApproveTime);
        dest.writeByte((byte) (isUpdate ? 1 : 0));
        dest.writeString(isApprove);
        dest.writeInt(updatacount);
        dest.writeString(getReportsString(mReports));
    }

    public String getReportsString(ArrayList<Report> mReports) {
        try {
            JSONArray ja = new JSONArray();
            for(int i = 0 ; i < mReports.size() ; i++) {
                Report report = mReports.get(i);
                JSONObject jo = new JSONObject();
                jo.put("type",report.type);
                jo.put("reportId",report.reportId);
                jo.put("tempId",report.tempId);
                jo.put("projectName",report.projectName);
                jo.put("mGod",report.mGod);
                jo.put("mResult",report.mResult);
                jo.put("unit",report.unit);
                jo.put("tempResult",report.tempResult);
                jo.put("time",report.time);
                jo.put("isSelect",report.isSelect);
                ja.put(jo);
            }
            return ja.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }

    }

    public void praseReport(String json,ArrayList<Report> mReports)
    {
        try {
            XpxJSONArray ja = new XpxJSONArray(json);
            for(int i = 0 ; i < ja.length() ; i++) {

                XpxJSONObject jo = ja.getJSONObject(i);
                Report report = new Report(jo.getString("type"));
                report.reportId = jo.getString("reportId");
                report.tempId = jo.getString("tempId");
                report.projectName = jo.getString("projectName");
                report.mGod = jo.getString("mGod");
                report.mResult = jo.getString("mResult");
                report.unit = jo.getString("unit");
                report.tempResult = jo.getString("tempResult");
                report.time = jo.getString("time");
                report.isSelect = jo.getBoolean("isSelect",false);
                mReports.add(report);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
