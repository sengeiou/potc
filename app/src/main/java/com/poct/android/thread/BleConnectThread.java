package com.poct.android.thread;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Build;

import com.poct.android.manager.BluetoothSetManager;

public class BleConnectThread extends Thread {
    public Context context;

    public BleConnectThread(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        super.run();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            BluetoothSetManager.getInstance().mBluetoothGatt = BluetoothSetManager.getInstance().netBluetoothDevice.connectGatt(context, true, BluetoothSetManager.mGattCallback, BluetoothDevice.TRANSPORT_LE);
        }
        else
        {
            BluetoothSetManager.getInstance().mBluetoothGatt = BluetoothSetManager.getInstance().netBluetoothDevice.connectGatt(context, false, BluetoothSetManager.getInstance().mGattCallback);
        }
    }

}
