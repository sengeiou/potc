package com.poct.android.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Switch;

import com.poct.R;
import com.poct.android.entity.WifiBean;
import com.poct.android.manager.WifiSetManager;
import com.poct.android.presenter.SettingPresenter;


public class WifiFragment extends BaseFragment {

    public SettingPresenter mSettingPresenter;
    public ListView listWifi;
    public Switch switchButtonWifi;
    public WifiFragment() {

    }

    @SuppressLint("ValidFragment")
    public WifiFragment(SettingPresenter mSettingPresenter) {
        // Required empty public constructor
        this.mSettingPresenter = mSettingPresenter;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_wifiset, container, false);
        switchButtonWifi = mView.findViewById(R.id.btn_swich);
        listWifi = mView.findViewById(R.id.wifilist);
        if(WifiSetManager.getInstance().mainWifi.isWifiEnabled() == true)
        {
            switchButtonWifi.setChecked(true);
        }
        else
        {
            switchButtonWifi.setChecked(false);

        }
        switchButtonWifi.setOnCheckedChangeListener(mSettingPresenter.wifiSwichListener);
        listWifi.setAdapter(mSettingPresenter.mSettingActivity.mWifiAdapter);
        listWifi.setOnItemClickListener(onItemClickListener);
        return mView;
    }

    @Override
    public void onResume() {

        super.onResume();

    }

    public AdapterView.OnItemClickListener onItemClickListener = new  AdapterView.OnItemClickListener()
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mSettingPresenter.clickWifi((WifiBean) parent.getAdapter().getItem(position));
        }
    };




}
