package com.poct.android.manager;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.TextView;

import com.poct.R;
import com.poct.android.entity.EquipmentItem;
import com.poct.android.utils.AppUtils;
import com.poct.android.utils.DBHelper;
import com.poct.android.utils.ViewUtils;
import com.poct.android.view.PoctApplication;

import java.lang.reflect.Method;

public class EquipMentManager {


    public static final String DEVICE_POTC = "DEVICE_POTC";
    public static final String DEVICE_FADA = "DEVICE_FADA";
    public static final String DEVICE_NET = "DEVICE_NET";
    public static final String DEVICE_PRINT = "DEVICE_PRINT";
    public static final String REPORT_ID = "reportid";
    public static EquipMentManager mEquipMentManager;
    public EquipmentItem devicePotc;
    public EquipmentItem deviceFada;
    public EquipmentItem devicePrint;
    public boolean hasPotc = false;
    public boolean hasFada = false;
    public boolean hasPrint = false;
    public EquipMentManager() {
        devicePotc = DBHelper.getInstance(PoctApplication.mApp).getDevice(DEVICE_POTC);
        if(devicePotc != null)
        {
            hasPotc = true;
        }
        deviceFada = DBHelper.getInstance(PoctApplication.mApp).getDevice(DEVICE_FADA);
        if(deviceFada != null)
        {
            hasFada = true;
        }
        devicePrint = DBHelper.getInstance(PoctApplication.mApp).getDevice(DEVICE_PRINT);
        if(devicePrint != null)
        {
            hasPrint = true;
        }

    }

    public static synchronized EquipMentManager getInstance() {
        if (mEquipMentManager == null) {
            mEquipMentManager = new EquipMentManager();
        }
        return mEquipMentManager;
    }

    public static String getTempId(String type) {
        return AppUtils.getDate4()+type;
    }

    public void addDevice(BluetoothDevice device, Handler handler) {
        if(devicePotc != null)
        {
            if(devicePotc.device == null)
            {
                if(devicePotc.id.equals(device.getName()))
                {
                    devicePotc.device = device;
                    devicePotc.mAddress = device.getAddress();
                    PoctApplication.mApp.sendBroadcast(new Intent(BluetoothSetManager.ACTION_DEVICE_POTC_GET));
                    if(deviceFada == null)
                    {
                        BluetoothSetManager.getInstance().stopDisvery();
                    }
                    else
                    {
                        if(deviceFada != null)
                        {
                            if(deviceFada.device != null)
                            {
                                BluetoothSetManager.getInstance().stopDisvery();
                            }
                        }
                    }
                    return;
                }
            }
        }
        if(deviceFada != null)
        {
            if(deviceFada.device == null)
            {
                if(deviceFada.id.equals(device.getName()))
                {
                    deviceFada.device = device;
                    deviceFada.mAddress = device.getAddress();
                    PoctApplication.mApp.sendBroadcast(new Intent(BluetoothSetManager.ACTION_DEVICE_FADA_GET));
                    if(devicePotc == null)
                    {
                        BluetoothSetManager.getInstance().stopDisvery();
                    }
                    else
                    {
                        if(devicePotc != null)
                        {
                            if(devicePotc.device != null) {
                                BluetoothSetManager.getInstance().stopDisvery();
                            }
                        }
                    }
                    return;
                }
            }
        }

    }

    public void scanDevice() {
        if(devicePotc != null)
        {
            if(devicePotc.device == null) {
                BluetoothSetManager.getInstance().doDiscovery();
            }
        }
        else if(deviceFada != null)
        {
            if(deviceFada.device == null) {
                BluetoothSetManager.getInstance().doDiscovery();
            }
        }
        else
        {

        }
    }

    public void connectFada(Context context,TextView state) {


        if(deviceFada.mAddress.length() == 0)
        {
            ViewUtils.showMessage(context,context.getString(R.string.bluetooth_device_search));
            BluetoothSetManager.getInstance().stopDisvery();
            BluetoothSetManager.getInstance().scanLeDevice();
            state.setText(context.getString(R.string.device_state_search));
        }
        else
        {
            state.setText(context.getString(R.string.device_state_connecting));
            BluetoothSetManager.getInstance().stopDisvery();
            BluetoothSetManager.getInstance().connectDevice(deviceFada,context);
        }
    }

    public void connectPotc(Context context, TextView state) {

        if(devicePotc.device == null)
        {
            ViewUtils.showMessage(context,context.getString(R.string.bluetooth_device_search));
            BluetoothSetManager.getInstance().stopDisvery();
            BluetoothSetManager.getInstance().doDiscovery();
            state.setText(context.getString(R.string.device_state_search));
        }
        else
        {
            state.setText(context.getString(R.string.device_state_connecting));
            BluetoothSetManager.getInstance().stopDisvery();
            if(devicePotc.device != null)
            {

                BluetoothSetManager.getInstance().netBluetoothDevice = devicePotc.device;
                if(devicePotc.device.getBondState() == BluetoothDevice.BOND_BONDED)
                {
                    ViewUtils.showMessage(context,context.getString(R.string.bluetooth_device_ready));
                    state.setText(context.getString(R.string.device_state_connected));

                }
                else if(devicePotc.device.getBondState() == BluetoothDevice.BOND_NONE)
                {
                    BluetoothSetManager.getInstance().netBluetoothDevice.createBond();
                }
                else
                {
                    ViewUtils.showMessage(context,context.getString(R.string.bluetooth_device_search));
                    BluetoothSetManager.getInstance().stopDisvery();
                    BluetoothSetManager.getInstance().doDiscovery();
                    state.setText(context.getString(R.string.device_state_search));
                }
            }
            else
            {
                ViewUtils.showMessage(context,context.getString(R.string.bluetooth_device_connect_error));
                state.setText(context.getString(R.string.device_state_disconnected));
            }

        }
//        if(devicePotc.mAddress.length() == 0) {
//            ViewUtils.showMessage(context,context.getString(R.string.bluetooth_device_search));
//            BluetoothSetManager.getInstance().stopDisvery();
//            BluetoothSetManager.getInstance().doDiscovery();
//           state.setText(context.getString(R.string.device_state_connecting));
//        }
//        else {
//            state.setText(context.getString(R.string.device_state_connecting));
//            BluetoothSetManager.getInstance().stopDisvery();
//            if(devicePotc.device != null) {
//                BluetoothSetManager.getInstance().netBluetoothDevice = BluetoothSetManager.getInstance().blueToothAdapter.getRemoteDevice(devicePotc.mAddress);
//                if(devicePotc.device != null)
//                {
//                    state.setText(context.getString(R.string.device_state_connected));
//                }
//                else
//                {
//                    state.setText(context.getString(R.string.device_state_disconnected));
//                }
//            }
//            else
//            {
//                state.setText(context.getString(R.string.device_state_connected));
//            }
//        }
    }

    public void disConnectPotc() {
        cancelBondProcess(BluetoothSetManager.getInstance().netBluetoothDevice);
        BluetoothSetManager.getInstance().netBluetoothDevice = null;

    }

    public void cancelBondProcess(BluetoothDevice device){
        try {
            Method m = device.getClass()
                    .getMethod("removeBond", (Class[]) null);
            m.invoke(device, (Object[]) null);
        } catch (Exception e) {
        }
    }
}
