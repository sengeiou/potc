package com.poct.android.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poct.R;
import com.poct.android.manager.EquipMentManager;
import com.poct.android.presenter.SettingPresenter;


public class EquipmentFragment extends BaseFragment {

    public SettingPresenter mSettingPresenter;
    public RelativeLayout btnPrint;
    public RelativeLayout btnPotc;
    public RelativeLayout btnFada;
    public TextView txtPrint;
    public TextView txtPotc;
    public TextView txtFada;
    public EquipmentFragment() {

    }

    @SuppressLint("ValidFragment")
    public EquipmentFragment(SettingPresenter mSettingPresenter) {
        // Required empty public constructor
        this.mSettingPresenter = mSettingPresenter;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_equipmentset, container, false);
        btnPrint = mView.findViewById(R.id.btn_print);
        btnPotc = mView.findViewById(R.id.btn_potc);
        btnFada = mView.findViewById(R.id.btn_fada);
        txtPrint = mView.findViewById(R.id.value_print);
        if(EquipMentManager.getInstance().devicePrint != null)
        {
            txtPrint.setText("IP:"+EquipMentManager.getInstance().devicePrint.id);
        }
        txtPotc = mView.findViewById(R.id.value_potc);
        if(EquipMentManager.getInstance().devicePotc != null)
        {
            txtPotc.setText("ID:"+EquipMentManager.getInstance().devicePotc.id);
        }
        txtFada = mView.findViewById(R.id.value_fada);
        if(EquipMentManager.getInstance().deviceFada != null)
        {
            txtFada.setText("SN:"+EquipMentManager.getInstance().deviceFada.id);
        }
        btnPrint.setOnClickListener(setPrintListener);
        btnPotc.setOnClickListener(setPotcListener);
        btnFada.setOnClickListener(setFadaListener);
        return mView;
    }

    @Override
    public void onResume() {

        super.onResume();

    }

    public View.OnClickListener setPrintListener = new  View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mSettingPresenter.setPrint();
        }
    };

    public View.OnClickListener setPotcListener = new  View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mSettingPresenter.setPotc();
        }
    };

    public View.OnClickListener setFadaListener = new  View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mSettingPresenter.setFada();
        }
    };


}
