package com.poct.android.net.nettask;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.poct.android.manager.BluetoothSetManager;
import com.poct.android.net.NetUtils;
import com.poct.android.utils.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BluetoothTask extends NetTask {

    public String commend;
    public int count;
    public int count2;

    public BluetoothTask(String url, Handler mHandler, int successEvent, int failEvent, Context mContext, String commend, int count, int count2) {
        super(url, mHandler, successEvent, failEvent, mContext);
        this.commend = commend;
        this.count = count;
        this.count2 = count2;
    }

    @Override
    public void run() {
        try {
            if ( BluetoothSetManager.getInstance().netBluetoothDevice != null) {
                BluetoothSocket mmSocket = BluetoothSetManager.getInstance().netBluetoothDevice.createRfcommSocketToServiceRecord(BluetoothSetManager.getInstance().MY_UUID_SECURE);
                mmSocket.connect();
                OutputStream outputStream = mmSocket.getOutputStream();
                InputStream inputStream = mmSocket.getInputStream();
                outputStream.write(StringUtil.HexCommandtoByte(mUrl.getBytes()));
                int bytes = 0;
                String data = "";
                //从输入流中读取数据
                int readcount = inputStream.available();
                if (readcount == 0)
                {
                    String url = "0B " + StringUtil.str2HexStr("AA|") + " " + "1C 0D";
                    outputStream.write(StringUtil.HexCommandtoByte(url.getBytes()));
                    readcount = inputStream.available();
                }
                while (bytes < readcount) {
                    byte[] buffer = new byte[1024];
                    bytes += inputStream.read(buffer);
                    data += new String(buffer, 0, bytes, "GBK");
                }
                String get = "";
                if (data.length() > 0 && data.contains(commend)) {
                    get = NetUtils.praseData(data, commend);
                }

                //发送数据到界面
                if (get.startsWith(commend)) {
                    String url = "0B " + StringUtil.str2HexStr("AA|") + " " + "1C 0D";
                    outputStream.write(StringUtil.HexCommandtoByte(url.getBytes()));
                    int readcount2 = inputStream.available();
                    Message msg = new Message();
                    msg.what = successEvent;
                    msg.arg1 = count2;
                    msg.arg2 = count;
                    msg.obj = get;
                    mHandler.sendMessage(msg);
                } else {
                    String url = "0B " + StringUtil.str2HexStr("AA|") + " " + "1C 0D";
                    outputStream.write(StringUtil.HexCommandtoByte(url.getBytes()));

                    Message msg = new Message();
                    msg.what = failEvent;
                    mHandler.sendMessage(msg);
                }

                outputStream.close();
                inputStream.close();
                mmSocket.close();
            } else {
                Message msg = new Message();
                msg.what = failEvent;
                mHandler.sendMessage(msg);

            }

        } catch (IOException e) {
            e.printStackTrace();
            Message msg = new Message();
            msg.what = failEvent;
            mHandler.sendMessage(msg);
        }
    }
}
