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
import com.poct.android.entity.WifiBean;
import com.poct.android.manager.WifiSetManager;

import java.util.ArrayList;

public class WifiAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private Handler mHandler;
    public ArrayList<WifiBean> itemWifis;

    public WifiAdapter(Context context, ArrayList<WifiBean> itemWifis, Handler mHandler)
    {
        this.mContext = context;
        this.mHandler = mHandler;
        this.itemWifis =  itemWifis;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return itemWifis.size();
    }

    @Override
    public WifiBean getItem(int position) {
        return itemWifis.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        WifiBean itemWifi = itemWifis.get(position);
        ViewHolder mViewHolder;
        if( null == convertView )
        {
            convertView =  mInflater.inflate( R.layout.item_wifi, null );
            mViewHolder = new ViewHolder();
            mViewHolder.txtName = (TextView) convertView.findViewById(R.id.txtname);
            mViewHolder.txtState = (TextView) convertView.findViewById( R.id.txtstate );
            mViewHolder.imgState = (ImageView) convertView.findViewById( R.id.imgstate );
            convertView.setTag(mViewHolder);
        }
        mViewHolder = (ViewHolder) convertView.getTag();
        mViewHolder.txtName.setText(itemWifi.wifiName);
        if(itemWifi.is5G)
        {
            mViewHolder.txtName.setText(itemWifi.wifiName+"-5g");
        }
        if(itemWifi.state.equals(WifiSetManager.WIFI_STATE_UNCONNECT))
        {
            String a = "";
            if(itemWifi.saved)
                a += " "+mContext.getString(R.string.wifi_saveed);
            if(WifiSetManager.getInstance().getSecurity(itemWifi) != WifiSetManager.SECURITY_NONE) {
                a+=" "+mContext.getString(R.string.wifi_addcode);
            }
            mViewHolder.txtState.setText(a);
        }
        else
            mViewHolder.txtState.setText(itemWifi.state);
        mViewHolder.imgState.setImageResource(getLeaveImg(Integer.valueOf(itemWifi.level),itemWifi.saved));
        return convertView;
    }

    private static class ViewHolder {
        private TextView txtName;
        private TextView txtState;
        private ImageView imgState;
    }

    private int getLeaveImg(int leave,boolean save) {
        if(leave >= -55)
        {
            if(save)
            return R.drawable.wifi_0;
            else
                return R.drawable.wifi_lock;
        }
        else if(leave >= -70)
        {
            if(save)
                return R.drawable.wifi_1;
            else
                return R.drawable.wifi_lock_1;
        }
        else if(leave >= -85)
        {
            if(save)
                return R.drawable.wifi_2;
            else
                return R.drawable.wifi_lock_2;
        }
        else
        {
            if(save)
                return R.drawable.wifi_4;
            else
                return R.drawable.wifi_lock_4;
        }
    }
}
