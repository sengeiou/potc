package com.poct.android.view.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.poct.R;
import com.poct.android.entity.Report;
import com.poct.android.handler.FadaTestHandler;
import com.poct.android.handler.PotcTestHandler;
import com.poct.android.manager.EquipMentManager;

import java.util.ArrayList;

public class PatientReportAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private Handler mHandler;
    public String type;
    public ArrayList<Report> mReports;

    public PatientReportAdapter(Context context, ArrayList<Report> mReports, Handler mHandler,String type)
    {
        this.mContext = context;
        this.mHandler = mHandler;
        this.mReports =  mReports;
        this.type = type;
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
            convertView =  mInflater.inflate( R.layout.item_patient_report, null );
            mViewHolder = new ViewHolder();
            mViewHolder.name = (TextView) convertView.findViewById(R.id.pname);
            mViewHolder.tempid = (TextView) convertView.findViewById( R.id.tempid );
            mViewHolder.result = (TextView) convertView.findViewById( R.id.result );
            mViewHolder.time = (TextView) convertView.findViewById( R.id.ptime );
            mViewHolder.unit = (TextView) convertView.findViewById( R.id.unit );
            mViewHolder.delete = convertView.findViewById( R.id.oper );

            convertView.setTag(mViewHolder);
        }
        mViewHolder = (ViewHolder) convertView.getTag();
        mViewHolder.tempid.setText(mReport.tempId);
        mViewHolder.name.setText( mReport.projectName);
        mViewHolder.result.setText(mReport.mGod+ mReport.mResult );
        mViewHolder.time.setText( mReport.time);
        mViewHolder.unit.setText(mReport.unit);
        if(this.type.equals(mReport.type))
        {
            mViewHolder.delete.setVisibility(View.VISIBLE);
            mViewHolder.delete.setTag(mReport);
            mViewHolder.delete.setOnClickListener(deleteListener);
        }
        else
        {
            mViewHolder.delete.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    public View.OnClickListener deleteListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if(mHandler != null)
            {
                Message message = new Message();
                if(type.equals(EquipMentManager.DEVICE_FADA))
                message.what = FadaTestHandler.EVENT_FADA_LIST_UPDATA;
                else
                    message.what = PotcTestHandler.EVENT_POTC_LIST_UPDATA;
                message.obj = v.getTag();
                if(mHandler != null)
                mHandler.sendMessage(message);
            }
        }
    };

    private static class ViewHolder {
        private TextView delete;
        private TextView tempid;
        private TextView name;
        private TextView result;
        private TextView time;
        private TextView unit;
    }
}
