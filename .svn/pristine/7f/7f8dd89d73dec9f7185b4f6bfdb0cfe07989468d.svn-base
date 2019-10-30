package com.poct.android.view.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.poct.R;
import com.poct.android.entity.SeriesReport;

import java.util.ArrayList;

public class ApproveSeriasReportAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private Handler mHandler;
    public ArrayList<SeriesReport> mSeriesReports;

    public ApproveSeriasReportAdapter(Context context, ArrayList<SeriesReport> mSeriesReports, Handler mHandler)
    {
        this.mContext = context;
        this.mHandler = mHandler;
        this.mSeriesReports =  mSeriesReports;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mSeriesReports.size();
    }

    @Override
    public SeriesReport getItem(int position) {
        return mSeriesReports.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SeriesReport mSeriesReport = mSeriesReports.get(position);
        ViewHolder mViewHolder;
        if( null == convertView )
        {
            convertView =  mInflater.inflate( R.layout.item_a_seriasreport, null );
            mViewHolder = new ViewHolder();
            mViewHolder.txtName = (TextView) convertView.findViewById(R.id.txtpname);
            mViewHolder.txtSex = (TextView) convertView.findViewById( R.id.txtpsex );
            mViewHolder.txtAge = (TextView) convertView.findViewById( R.id.txtpage );
            mViewHolder.txtSender = (TextView) convertView.findViewById( R.id.txtsender );
            mViewHolder.txtApproveState = (TextView) convertView.findViewById( R.id.approvestate );
            mViewHolder.txtApprover = (TextView) convertView.findViewById( R.id.approver );
            mViewHolder.txtApproveTime = (TextView) convertView.findViewById( R.id.approvertime );
            mViewHolder.txtCount = (TextView) convertView.findViewById( R.id.txtcount );
            mViewHolder.txtSendTime = (TextView) convertView.findViewById( R.id.txttimes );
            convertView.setTag(mViewHolder);
        }
        mViewHolder = (ViewHolder) convertView.getTag();
        mViewHolder.txtName.setText(mSeriesReport.mName);
        mViewHolder.txtSex.setText(mSeriesReport.mSex);
        mViewHolder.txtAge.setText(mSeriesReport.mAge);
        mViewHolder.txtSender.setText(mSeriesReport.mSender);
        mViewHolder.txtApproveState.setText(mSeriesReport.isApprove);
        mViewHolder.txtApprover.setText(mSeriesReport.mApprover);
        if(mSeriesReport.mApproveTime.length() >9)
        mViewHolder.txtApproveTime.setText(mSeriesReport.mApproveTime.substring(0,16));
        mViewHolder.txtCount.setText(String.valueOf(mSeriesReport.mReports.size()));
        mViewHolder.txtSendTime.setText(mSeriesReport.mSendTime);
        return convertView;
    }

    private static class ViewHolder {
        private TextView txtName;
        private TextView txtSex;
        private TextView txtAge;
        private TextView txtSender;
        private TextView txtCount;
        private TextView txtSendTime;
        private TextView txtApproveState;
        private TextView txtApprover;
        private TextView txtApproveTime;
    }

}
