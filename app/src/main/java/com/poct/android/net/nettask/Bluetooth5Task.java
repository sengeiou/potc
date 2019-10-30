package com.poct.android.net.nettask;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.poct.android.asks.TestReportAsks;
import com.poct.android.entity.NetObject;
import com.poct.android.entity.TempGet;
import com.poct.android.handler.PotcTestHandler;
import com.poct.android.manager.BluetoothSetManager;
import com.poct.android.net.NetUtils;
import com.poct.android.prase.DataPrase;
import com.poct.android.utils.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class Bluetooth5Task extends NetTask {

    public String time;
    public int begin;
    public BluetoothSocket mmSocket;
    public OutputStream outputStream;
    public InputStream inputStream;
    public ArrayList<TempGet> item;
    public long systime = 0;
    public Bluetooth5Task(String url, Handler mHandler, int successEvent, int failEvent, Context mContext, int count, ArrayList<TempGet> objects, String itime) {
        super(url, mHandler, successEvent, failEvent, mContext);
        this.begin = count;
        this.time = itime;
        this.item = objects;
    }

    @Override
    public void run() {
        try {
            if (BluetoothSetManager.getInstance().netBluetoothDevice != null) {
                mmSocket = BluetoothSetManager.getInstance().netBluetoothDevice.createRfcommSocketToServiceRecord(BluetoothSetManager.getInstance().MY_UUID_SECURE);
                mmSocket.connect();
                outputStream = mmSocket.getOutputStream();
                inputStream = mmSocket.getInputStream();
                outputStream.flush();
                outputStream.write(StringUtil.HexCommandtoByte(mUrl.getBytes()));
                outputStream.flush();
                int readcount = inputStream.available();
                systime = System.currentTimeMillis();
                while (readcount == 0 && (System.currentTimeMillis() -systime) < 15000)
                {
                    readcount = inputStream.available();
                }
                int bytes = 0;
                String data = "";
                while (readcount != 0) {
                    byte[] buffer = new byte[1024];
                    bytes += inputStream.read(buffer);
                    data += new String(buffer, 0, bytes, "GBK");
                    readcount = inputStream.available();
                }
                String get = "";
                if (data.length() > 0 && data.contains("SXW")) {
                    get = NetUtils.praseData(data, "SXW");
                }
                int count = 0;
                if (get.length() > 0) {
                    count = DataPrase.praseGetReportCount(get);
                }
                String url = "0B " + StringUtil.str2HexStr("AA|") + " " + "1C 0D";
                outputStream.flush();
                outputStream.write(StringUtil.HexCommandtoByte(url.getBytes()));
                outputStream.flush();
                readcount = inputStream.available();
                while (readcount != 0) {
                    byte[] buffer = new byte[1024];
                    bytes += inputStream.read(buffer);
                    data += new String(buffer, 0, bytes, "GBK");
                    readcount = inputStream.available();
                }
                int begin = Integer.valueOf(item.get(0).mTempid);
                int end = Integer.valueOf(item.get(item.size()-1).mTempid);
                if(count < begin)
                {
                    Message msg = new Message();
                    msg.what = PotcTestHandler.EVENT_GET_POTC_DATA_NONE;
                    mHandler.sendMessage(msg);
                }
                else
                {
                    if(end > count)
                    {
                        end = count;
                    }
                    outputStream.flush();
                    outputStream.write(StringUtil.HexCommandtoByte(TestReportAsks.getReportUrl(time, end, begin).getBytes()));
                    outputStream.flush();
                    int n = 0;
                    for (int i = begin; i < end+1; i++) {

                        bytes = 0;
                        data = "";
                        String get2 = "";
                        //从输入流中读取数据
                        TempGet mTempGet = null;
                        if(n < item.size())
                        {
                            mTempGet = item.get(n);
                        }
                        else
                        {
                            break;
                        }
                        readcount = inputStream.available();
                        systime = System.currentTimeMillis();
                        while (readcount == 0 && (System.currentTimeMillis() -systime) < 15000)
                        {
                            readcount = inputStream.available();
                        }
                        while (readcount != 0) {
                            byte[] buffer = new byte[1024];
                            bytes += inputStream.read(buffer);
                            data += new String(buffer, 0, bytes, "GBK");
                            readcount = inputStream.available();
                        }
                        get2 = "";
                        if (data.length() > 0 && data.contains("SZD") && mTempGet != null) {
                            get2 = NetUtils.praseData(data, "SZD", mTempGet.mTempid);
                        }
                        if(mTempGet == null)
                        {
                            url = "0B " + StringUtil.str2HexStr("AA|") + " " + "1C 0D";
                            outputStream.flush();
                            outputStream.write(StringUtil.HexCommandtoByte(url.getBytes()));
                            outputStream.flush();
                            continue;
                        }
                        else
                        {
                            if(get2.length() > 0)
                            n++;
                            //发送数据到界面
                            if (get2.startsWith("SZD") && mTempGet != null) {
                                url = "0B " + StringUtil.str2HexStr("AA|") + " " + "1C 0D";
                                outputStream.flush();
                                outputStream.write(StringUtil.HexCommandtoByte(url.getBytes()));
                                outputStream.flush();
                                NetObject object = new NetObject();
                                object.result = get2;
                                object.item = mTempGet;
                                Message msg = new Message();
                                msg.what = PotcTestHandler.EVENT_GET_POTC_DATA_ITEM_SUCCESS;
                                msg.arg1 = 0;
                                msg.arg2 = count;
                                msg.obj = object;
                                mHandler.sendMessage(msg);
                            } else {
                                url = "0B " + StringUtil.str2HexStr("AA|") + " " + "1C 0D";
                                outputStream.flush();
                                outputStream.write(StringUtil.HexCommandtoByte(url.getBytes()));
                                outputStream.flush();
                                Message msg = new Message();
                                msg.obj = mTempGet;
                                msg.what = PotcTestHandler.EVENT_GET_POTC_DATA_ITEM_FAIL;
                                mHandler.sendMessage(msg);
                            }
                        }

                    }
                }
                Message msg = new Message();
                msg.what = PotcTestHandler.EVENT_GET_POTC_DATA_FINISH;
                mHandler.sendMessage(msg);
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
        }
    }
}
