package com.poct.android.receiver;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.poct.android.handler.LoginHandler;
import com.poct.android.handler.PotcTestHandler;
import com.poct.android.utils.ClsUtils;
import com.poct.android.view.activity.LoginActivity;


public class BluetoothReceiver extends BroadcastReceiver {

    public Handler handler;

    public BluetoothReceiver(Handler handler)
    {
        this.handler = handler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(BluetoothDevice.ACTION_FOUND)) {

            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            Message message = new Message();
            message.what = LoginHandler.EVENT_FIND_DEVICE;
            message.obj = device;
            handler.sendMessage(message);
        }
        else if (intent.getAction().equals(BluetoothDevice.ACTION_PAIRING_REQUEST)) {
            BluetoothDevice mBluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            try {
                //(三星)4.3版本测试手机还是会弹出用户交互页面(闪一下)，如果不注释掉下面这句页面不会取消但可以配对成功。(中兴，魅族4(Flyme 6))5.1版本手机两中情况下都正常
                //ClsUtils.setPairingConfirmation(mBluetoothDevice.getClass(), mBluetoothDevice, true);
                abortBroadcast();//如果没有将广播终止，则会出现一个一闪而过的配对框。
                //3.调用setPin方法进行配对...
                boolean ret = ClsUtils.setPin(mBluetoothDevice.getClass(), mBluetoothDevice, "1234");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(intent.getAction().equals(LoginActivity.ACTION_END_DISCOVER_DEVICE))
        {
            handler.removeMessages(LoginHandler.EVENT_END_DISCOVERY);
            handler.sendEmptyMessageDelayed(LoginHandler.EVENT_END_DISCOVERY,20000);
        }
        if (intent.getAction().equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            switch (device.getBondState()) {
                case BluetoothDevice.BOND_NONE:
                    handler.sendEmptyMessage(PotcTestHandler.EVENT_GET_POTC_BONO);
                    break;
                case BluetoothDevice.BOND_BONDING:
                    break;
                case BluetoothDevice.BOND_BONDED:
                    handler.sendEmptyMessage(PotcTestHandler.EVENT_GET_POTC_BOND);
                    break;
            }
        }
        else if(intent.getAction().equals(LoginActivity.ACTION_LOGOUT))
        {
            handler.sendEmptyMessage(LoginHandler.EVENT_LOGOUT);

        }
        else if(intent.getAction().equals(LoginActivity.ACTION_SYSTEM_UPDATA))
        {
            Message message = new Message();
            message.what = LoginHandler.EVENT_SYSTEM_UPDATA;
            message.obj = intent;
            handler.sendMessage(message);
        }
        else if(intent.getAction().equals(LoginActivity.ACTION_SYSTEM_UPDATA_FAIL))
        {
            Message message = new Message();
            message.what = LoginHandler.EVENT_SYSTEM_UPDATA_FAIL;
            message.obj = intent;
            handler.sendMessage(message);
        }
    }

}
