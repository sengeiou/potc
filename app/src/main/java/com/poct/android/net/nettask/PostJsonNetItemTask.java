package com.poct.android.net.nettask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.poct.R;
import com.poct.android.entity.NetObject;
import com.poct.android.net.NetUtils;
import com.poct.android.view.PoctApplication;

import org.json.JSONException;
import org.json.JSONObject;

public class PostJsonNetItemTask extends NetTask {

    private String formBody;
    public Object item;
    public PostJsonNetItemTask(String url, Handler mHandler, int successEvent, int failEvent, Context mContext, String formBody,Object object) {
        super(url, mHandler, successEvent, failEvent, mContext);
        // TODO Auto-generated constructor stub
        this.formBody = formBody;
        this.item = object;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        if (NetUtils.checkNetWorkState(PoctApplication.mApp)) {
            String result = "";
            result = NetUtils.getInstance().postJson(mUrl, formBody);
            if (result == null) {
                if (mHandler != null)
                    mHandler.sendEmptyMessage(failEvent);
            } else {
                try {
                    JSONObject mobj = new JSONObject(result);
                    if(mobj.has("code"))
                    {
                        if(mobj.getInt("code") == 0)
                        {
                            if(mHandler != null) {
                                Message msg = new Message();
                                msg.what = successEvent;
                                NetObject object = new NetObject();
                                object.result = result;
                                object.item = item;
                                msg.obj = object;
                                mHandler.sendMessage(msg);
                            }

                        } else {
                            if (mHandler != null) {
                                Message msg = new Message();
                                msg.what = failEvent;
                                NetObject object = new NetObject();
                                object.result = result;
                                object.item = item;
                                msg.obj = object;
                                mHandler.sendMessage(msg);
                            }
                        }
                    }
                    else
                    {
                        if(mHandler != null) {
                            Message msg = new Message();
                            msg.what = successEvent;
                            NetObject object = new NetObject();
                            object.result = result;
                            object.item = item;
                            msg.obj = object;
                            mHandler.sendMessage(msg);
                        }
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    if (mHandler != null) {
                        Message msg = new Message();
                        msg.what = failEvent;
                        NetObject object = new NetObject();
                        object.result = result;
                        object.item = item;
                        msg.obj = object;
                        mHandler.sendMessage(msg);
                    }
                }

            }
        } else {
            if (mHandler != null) {
                Message msg = new Message();
                msg.what = NetUtils.NO_NET_WORK;
                msg.obj = this.mContext.getString(R.string.error_net_network);
                mHandler.sendEmptyMessage(NetUtils.NO_NET_WORK);
            }

        }

    }
}
