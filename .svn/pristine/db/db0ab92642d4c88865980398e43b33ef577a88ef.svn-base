package com.poct.android.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.poct.R;
import com.poct.android.manager.BluetoothSetManager;
import com.poct.android.presenter.SettingPresenter;



public class BluetoothFragment extends BaseFragment {

    public SettingPresenter mSettingPresenter;
    public Switch switchButtonBluetooth;
    public BluetoothFragment() {

    }

    @SuppressLint("ValidFragment")
    public BluetoothFragment(SettingPresenter mSettingPresenter) {
        // Required empty public constructor
        this.mSettingPresenter = mSettingPresenter;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_bluetoothset, container, false);
        switchButtonBluetooth = mView.findViewById(R.id.btn_swich);
        if(BluetoothSetManager.getInstance().blueToothAdapter.isEnabled() == true)
        {
            switchButtonBluetooth.setChecked(true);
        }
        else
        {
            switchButtonBluetooth.setChecked(false);

        }
        switchButtonBluetooth.setOnCheckedChangeListener(mSettingPresenter.bluetoothSwichListener);
        return mView;
    }

    @Override
    public void onResume() {

        super.onResume();

    }


}
