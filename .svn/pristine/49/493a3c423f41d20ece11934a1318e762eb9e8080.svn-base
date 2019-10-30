package com.poct.android.entity;

import android.bluetooth.BluetoothDevice;

public class EquipmentItem {

    public String mName = "";
    public String id = "";
    public String mAddress = "";
    public String type;
    public int rssi;
    public BluetoothDevice device;

    public EquipmentItem() {

    }

    public EquipmentItem(BluetoothDevice device,int rssi) {
        this.device = device;
        this.rssi = rssi;
        this.id = device.getName();
        this.mAddress = device.getAddress();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof EquipmentItem) {
            return device.equals(((EquipmentItem) o).device);
        }
        return false;
    }
}
