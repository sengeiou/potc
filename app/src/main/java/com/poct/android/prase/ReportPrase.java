package com.poct.android.prase;

import com.poct.android.entity.PageSeraisReport;
import com.poct.android.entity.Report;
import com.poct.android.entity.SeriesReport;
import com.poct.android.manager.EquipMentManager;
import com.poct.android.utils.XpxJSONArray;
import com.poct.android.utils.XpxJSONObject;

import org.json.JSONException;

import java.util.ArrayList;

public class ReportPrase {

    public static PageSeraisReport praseReport(String json,String name,String sender) {
        PageSeraisReport pageSeraisReports = new PageSeraisReport(20);

        try {
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            XpxJSONArray ja = jsonObject.getJSONArray("data");
            for(int i = 0 ; i < ja.length() ; i++)
            {
                XpxJSONObject jo = ja.getJSONObject(i);
                SeriesReport info = new SeriesReport();
                info.mReportId = jo.getString("ReportID");
                info.mPId = jo.getString("IDNumber");
                info.mAccountId = jo.getString("StationID");
                info.mName = jo.getString("Name");
                info.mSex = jo.getString("Sex");
                info.mAge = jo.getString("Age");
                info.mSender = jo.getString("StationName");
                if(name.length() > 0)
                {
                    if(!info.mName.contains(name))
                    continue;
                }
                if(sender.length() > 0)
                {
                    if(!info.mName.contains(sender))
                        continue;
                }
                info.mSendTime = jo.getString("InspectionTime");
                info.mApprover = jo.getString("AuditPerson");
                info.mCNum = jo.getString("MRNumber");
                info.mHNum = jo.getString("OutpatientNumber");
                info.mDepart = jo.getString("Department");
                info.mBad = jo.getString("BedNumber");
                info.mDes = jo.getString("Diagnosis");
                info.mMemo = jo.getString("Memo");
                info.isApprove = jo.getString("AuditStatus");
                info.mApproveTime = jo.getString("AuditTime");
                info.tempId = "";
                info.isUpdate = true;
                XpxJSONArray ja2 = jo.getJSONArray("reportdetail");
                for(int j = 0 ; j < ja2.length() ; j++)
                {
                    Report mReport = new Report(EquipMentManager.DEVICE_NET);
                    XpxJSONObject jo2 = ja2.getJSONObject(j);
                    mReport.reportId = jo2.getString("ProjectID");
                    mReport.tempId = jo2.getString("SampleID");
                    mReport.projectName = jo2.getString("ProjectName");
                    mReport.mGod = "";
                    mReport.mResult = jo2.getString("Result");
                    mReport.unit = jo2.getString("Unit");
                    mReport.tempResult = jo2.getString("Reference");
                    mReport.time = jo2.getString("InspectTime");
                    info.mReports.add(mReport);
                }

                ArrayList<SeriesReport> seriesReports = pageSeraisReports.map.get(String.valueOf(pageSeraisReports.pageCount));
                if(seriesReports != null)
                {
                    if(seriesReports.size() == pageSeraisReports.pageMax)
                    {
                        pageSeraisReports.pageCount++;
                        seriesReports = pageSeraisReports.map.get(String.valueOf(pageSeraisReports.pageCount));
                    }
                }

                if(seriesReports == null) {
                    seriesReports = new ArrayList<SeriesReport>();
                    pageSeraisReports.map.put(String.valueOf(pageSeraisReports.pageCount),seriesReports);
                }
                seriesReports.add(info);
                pageSeraisReports.totalcount++;

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return pageSeraisReports;
    }
}
