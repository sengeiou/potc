package com.poct.android.net.nettask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.poct.android.net.NetUtils;
import com.poct.android.utils.AppUtils;
import com.poct.android.view.PoctApplication;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;



//import com.intersky.android.view.activity.MainFuncsActivity;

public class NetTask implements Runnable {

	public String mUrl;
	public Handler mHandler;
	public int successEvent;
	public int failEvent;
	public Context mContext;
	public Call mCall;
	public String mRecordId = "";

	public NetTask(String url, Handler mHandler, int successEvent, int failEvent, Context mContext) {
		this.mUrl = url;
		this.mHandler = mHandler;
		this.successEvent = successEvent;
		this.failEvent = failEvent;
		this.mContext = mContext;
		this.mRecordId = AppUtils.getguid();
	}

	public NetTask(String url, Handler mHandler, int successEvent, int failEvent, Context mContext,String mRecordId) {
		this.mUrl = url;
		this.mHandler = mHandler;
		this.successEvent = successEvent;
		this.failEvent = failEvent;
		this.mContext = mContext;
		this.mRecordId = mRecordId;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (NetUtils.checkNetWorkState(PoctApplication.mApp)) {
			String result = "";
			mCall = NetUtils.getInstance().get(mUrl);
			result = NetUtils.getInstance().getUrl(mCall);
			if (result == null) {
				if (mHandler != null)
					mHandler.sendEmptyMessage(failEvent);
			}
			else {
				try {
					JSONObject mobj = new JSONObject(result);
					Message msg = new Message();
					msg.what = successEvent;
					msg.obj = result;
					mHandler.sendMessage(msg);
				}
				catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}

			}
		}
		else {
			if (mHandler != null)
				mHandler.sendEmptyMessageDelayed(failEvent,10*1000);

		}

	}
	
	public void onStop()
	{
		mCall.cancel();
	}
}
