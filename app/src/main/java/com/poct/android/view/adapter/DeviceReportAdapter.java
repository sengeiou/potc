package com.poct.android.view.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.poct.R;
import com.poct.android.entity.Report;

import java.util.ArrayList;

public class DeviceReportAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private Handler mHandler;
    public ArrayList<Report> mReports;

    public DeviceReportAdapter(Context context, ArrayList<Report> mReports, Handler mHandler)
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
            convertView =  mInflater.inflate( R.layout.item_device_report, null );
            mViewHolder = new ViewHolder();
            mViewHolder.check = (ImageView) convertView.findViewById(R.id.check);
            mViewHolder.name = (TextView) convertView.findViewById(R.id.name);
            mViewHolder.tempid = (TextView) convertView.findViewById( R.id.tempid );
            mViewHolder.result = (TextView) convertView.findViewById( R.id.result );
            mViewHolder.time = (TextView) convertView.findViewById( R.id.time );
            mViewHolder.temp = (TextView) convertView.findViewById( R.id.temp );
            mViewHolder.unit = (TextView) convertView.findViewById( R.id.unit );
            convertView.setTag(mViewHolder);
        }
        mViewHolder = (ViewHolder) convertView.getTag();
        mViewHolder.tempid.setText(mReport.tempId);
        mViewHolder.name.setText( mReport.projectName);
        mViewHolder.result.setText( mReport.mGod+mReport.mResult );
        mViewHolder.time.setText( mReport.time);
        mViewHolder.temp.setText(mReport.tempResult);
        mViewHolder.unit.setText(mReport.unit);
        if(mReport.isSelect)
        {
            mViewHolder.check.setImageResource(R.drawable.checkon);
        }
        else
        {
            mViewHolder.check.setImageResource(R.drawable.check);
        }
        return convertView;
    }

    private static class ViewHolder {
        private ImageView check;
        private TextView tempid;
        private TextView name;
        private TextView result;
        private TextView time;
        private TextView temp;
        private TextView unit;
    }
}
