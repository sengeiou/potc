package com.poct.android.view.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.poct.R;
import com.poct.android.entity.Report;

import java.util.ArrayList;

public class ReportAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private Handler mHandler;
    public String type;
    public ArrayList<Report> mReports;

    public ReportAdapter(Context context, ArrayList<Report> mReports, Handler mHandler)
    {
        this.mContext = context;
        this.mHandler = mHandler;
        this.mReports =  mReports;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mReports.size();
    }

    @Override
    public Report getItem(int position) {
        return mReports.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Report mReport = mReports.get(position);
        ViewHolder mViewHolder;
        if( null == convertView )
        {
            convertView =  mInflater.inflate( R.layout.report_item, null );
            mViewHolder = new ViewHolder();
            mViewHolder.name = (TextView) convertView.findViewById(R.id.projecttitle);
            mViewHolder.temp = (TextView) convertView.findViewById( R.id.temptitle );
            mViewHolder.result = (TextView) convertView.findViewById( R.id.resulttitle );
            mViewHolder.time = (TextView) convertView.findViewById( R.id.testtimetitle );
            mViewHolder.unit = (TextView) convertView.findViewById( R.id.unittitle );

            convertView.setTag(mViewHolder);
        }
        mViewHolder = (ViewHolder) convertView.getTag();
        mViewHolder.temp.setText(mReport.tempResult);
        mViewHolder.name.setText( String.valueOf(position+1)+"."+mReport.projectName);
        mViewHolder.result.setText(mReport.mGod+ mReport.mResult);
        if(mReport.time.length() > 0)
        mViewHolder.time.setText( mReport.time.substring(0,16));
        mViewHolder.unit.setText(mReport.unit);
        return convertView;
    }

    private static class ViewHolder {
        private TextView temp;
        private TextView name;
        private TextView result;
        private TextView time;
        private TextView unit;

    }
}
