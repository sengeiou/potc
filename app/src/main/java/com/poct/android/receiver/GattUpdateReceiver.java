package com.poct.android.receiver;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.poct.android.handler.FadaTestHandler;
import com.poct.android.handler.PotcTestHandler;
import com.poct.android.manager.BluetoothSetManager;
import com.poct.android.utils.Constants;
import com.poct.android.utils.Utils;

public class GattUpdateReceiver extends BroadcastReceiver {

    public final static String ACTION_GATT_CONNECTED =
            "com.usr.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.usr.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_DISCONNECTED_CAROUSEL =
            "com.usr.bluetooth.le.ACTION_GATT_DISCONNECTED_CAROUSEL";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.usr.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.usr.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String ACTION_GATT_DESCRIPTORWRITE_RESULT =
            "com.usr.bluetooth.le.ACTION_GATT_DESCRIPTORWRITE_RESULT";
    public final static String ACTION_GATT_CHARACTERISTIC_ERROR =
            "com.usr.bluetooth.le.ACTION_GATT_CHARACTERISTIC_ERROR";
    public final static String ACTION_GATT_CHARACTERISTIC_WRITE_SUCCESS =
            "com.usr.bluetooth.le.ACTION_GATT_CHARACTERISTIC_SUCCESS";
    public Handler handler;

    public GattUpdateReceiver(Handler handler)
    {
        this.handler = handler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        //There are four basic operations for moving data in BLE: read, write, notify,
        // and indicate. The BLE protocol specification requires that the maximum data
        // payload size for these operations is 20 bytes, or in the case of read operations,
        // 22 bytes. BLE is built for low power consumption, for infrequent short-burst data transmissions.
        // Sending lots of data is possible, but usually ends up being less efficient than classic Bluetooth
        // when trying to achieve maximum throughput.  从google查找的，解释了为什么android下notify无法解释超过
        //20个字节的数据
        Bundle extras = intent.getExtras();
        if (ACTION_DATA_AVAILABLE.equals(action)) {
            // Data Received
            if (extras.containsKey(Constants.EXTRA_BYTE_VALUE)) {
                if (extras.containsKey(Constants.EXTRA_BYTE_UUID_VALUE)) {
                    if (BluetoothSetManager.getInstance().mPadacharacteristic != null) {
                        BluetoothGattCharacteristic requiredCharacteristic = BluetoothSetManager.getInstance().mPadacharacteristic;
                        String uuidRequired = requiredCharacteristic.getUuid().toString();
                        String receivedUUID = intent.getStringExtra(Constants.EXTRA_BYTE_UUID_VALUE);
                        byte[] array = intent.getByteArrayExtra(Constants.EXTRA_BYTE_VALUE);
                        String data =formatMsgContent(array);
                        Message message = new Message();
                        message.obj = data;
                        message.what = FadaTestHandler.EVENT_UPDATA_CHARACTOR;
                        if(handler != null)
                        this.handler.sendMessage(message);
                    }
                }
            }
            if (extras.containsKey(Constants.EXTRA_DESCRIPTOR_BYTE_VALUE)) {
                if (extras.containsKey(Constants.EXTRA_DESCRIPTOR_BYTE_VALUE_CHARACTERISTIC_UUID)) {
                    BluetoothGattCharacteristic requiredCharacteristic = BluetoothSetManager.getInstance().mPadacharacteristic;
                    String uuidRequired = requiredCharacteristic.getUuid().toString();
                    String receivedUUID = intent.getStringExtra(
                            Constants.EXTRA_DESCRIPTOR_BYTE_VALUE_CHARACTERISTIC_UUID);

                    byte[] array = intent
                            .getByteArrayExtra(Constants.EXTRA_DESCRIPTOR_BYTE_VALUE);

//                        System.out.println("GattDetailActivity---------------------->descriptor:" + Utils.ByteArraytoHex(array));
//                    if (isDebugMode){
//                        updateButtonStatus(array);
//                    }else if (uuidRequired.equalsIgnoreCase(receivedUUID)) {
//                        updateButtonStatus(array);
//                    }
                }
            }
        }

        if (action.equals(ACTION_GATT_DESCRIPTORWRITE_RESULT)){
            if (extras.containsKey(Constants.EXTRA_DESCRIPTOR_WRITE_RESULT)){
                int status = extras.getInt(Constants.EXTRA_DESCRIPTOR_WRITE_RESULT);
                if (status != BluetoothGatt.GATT_SUCCESS){
//                    Snackbar.make(rlContent,R.string.option_fail,Snackbar.LENGTH_LONG).show();
                }
            }
        }

        if (action.equals(ACTION_GATT_CHARACTERISTIC_ERROR)) {
            if (extras.containsKey(Constants.EXTRA_CHARACTERISTIC_ERROR_MESSAGE)) {
                String errorMessage = extras.
                        getString(Constants.EXTRA_CHARACTERISTIC_ERROR_MESSAGE);
                System.out.println("GattDetailActivity---------------------->err:" + errorMessage);
//                showDialog(errorMessage);
            }

        }

        //write characteristics succcess
        if (action.equals(ACTION_GATT_CHARACTERISTIC_WRITE_SUCCESS)){
//            list.get(list.size()-1).setDone(true);
//            adapter.notifyItemChanged(list.size()-1);
        }

        if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
//                final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
//                if (state == BluetoothDevice.BOND_BONDING) {}
//                else if (state == BluetoothDevice.BOND_BONDED) {}
//                else if (state == BluetoothDevice.BOND_NONE) {}
        }
        if(intent.getAction().equals(BluetoothSetManager.ACTION_GATT_SERVICES_DISCOVERED))
        {
            if(handler != null)
            handler.sendEmptyMessage(FadaTestHandler.EVENT_DISCOVER_SERVEICE);
        }
        if(intent.getAction().equals(BluetoothSetManager.ACTION_DEVICE_CONNECTED))
        {
            if(handler != null)
            handler.sendEmptyMessage(FadaTestHandler.EVENT_DEVICE_CONNECTED);
        }
        if(intent.getAction().equals(BluetoothSetManager.ACTION_DEVICE_DISCONNECT))
        {
            if(handler != null)
            handler.sendEmptyMessage(FadaTestHandler.EVENT_DEVICE_DISCONNECT);
        }
        if(intent.getAction().equals(BluetoothSetManager.ACTION_DEVICE_CONNECTING))
        {
            if(handler != null)
            handler.sendEmptyMessage(FadaTestHandler.EVENT_DEVICE_CONNECTING);
        }
        if(intent.getAction().equals(BluetoothSetManager.ACTION_DEVICE_READY))
        {
            handler.sendEmptyMessage(FadaTestHandler.EVENT_DEVICE_READY);
        }
        if(intent.getAction().equals(BluetoothSetManager.ACTION_DEVICE_FADA_GET))
        {
            if(handler != null)
            handler.sendEmptyMessage(FadaTestHandler.EVENT_FADA_GET);
        }
        if(intent.getAction().equals(BluetoothSetManager.ACTION_DEVICE_CHECK_FADA))
        {
            if(handler != null)
            handler.sendEmptyMessage(FadaTestHandler.EVENT_FADA_GET_CHECK);
        }
        if(intent.getAction().equals(BluetoothSetManager.ACTION_DEVICE_POTC_GET))
        {
            if(handler != null)
            handler.sendEmptyMessage(PotcTestHandler.EVENT_POTC_GET);
        }
        if(intent.getAction().equals(BluetoothSetManager.ACTION_DEVICE_CHECK_POTC))
        {
            if(handler != null)
            handler.sendEmptyMessage(PotcTestHandler.EVENT_POTC_GET_CHECK);
        }
        if(intent.getAction().equals(BluetoothSetManager.ACTION_DEVICE_POTC_CONNECTED))
        {
            if(handler != null)
            handler.sendEmptyMessage(PotcTestHandler.EVENT_GET_POTC_BOND);
        }
        if(intent.getAction().equals(BluetoothSetManager.ACTION_DEVICE_POTC_UNCONNECTED))
        {
            if(handler != null)
            handler.sendEmptyMessage(PotcTestHandler.EVENT_GET_POTC_BONO);
        }
    }

    private String formatMsgContent(byte[] data){
//        return "HEX:"+ Utils.ByteArraytoHex(data)+"  (ASSCII:"+Utils.byteToASCII(data)+")";
        return Utils.ByteArraytoHex(data);
    }
}
