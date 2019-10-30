package com.poct.android.view.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.poct.R;
import com.poct.android.entity.EquipmentItem;

import java.util.ArrayList;

public class EquipmentAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private Handler mHandler;
    public ArrayList<EquipmentItem> equipmentItems;

    public EquipmentAdapter(Context context, ArrayList<EquipmentItem> equipmentItems, Handler mHandler)
    {
        this.mContext = context;
        this.mHandler = mHandler;
        this.equipmentItems =  equipmentItems;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return equipmentItems.size();
    }

    @Override
    public EquipmentItem getItem(int position) {
        return equipmentItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        EquipmentItem mLableDataItem = equipmentItems.get(position);
        ViewHolder mViewHolder;
        if( null == convertView )
        {
            convertView =  mInflater.inflate( R.layout.label_item, null );
            mViewHolder = new ViewHolder();
            mViewHolder.name = (TextView) convertView.findViewById(R.id.name);
            mViewHolder.id = (TextView) convertView.findViewById( R.id.value );
            mViewHolder.connect = (TextView) convertView.findViewById( R.id.connect );
            mViewHolder.address = (TextView) convertView.findViewById( R.id.address );
            convertView.setTag(mViewHolder);
        }
        mViewHolder = (ViewHolder) convertView.getTag();
        mViewHolder.id.setText( mLableDataItem.id );
        mViewHolder.name.setText( mLableDataItem.mName );
        mViewHolder.address.setText( mLableDataItem.mAddress );
        return convertView;
    }

    private static class ViewHolder {
        private TextView name;
        private TextView id;
        private TextView connect;
        private TextView address;
    }
}
