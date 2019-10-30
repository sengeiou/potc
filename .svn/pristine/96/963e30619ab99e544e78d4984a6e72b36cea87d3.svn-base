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

import okhttp3.RequestBody;

//import com.intersky.android.view.activity.MainFuncsActivity;

public class PostNetItemTask extends NetTask {

	private RequestBody formBody;
	private NetObject item = new NetObject();
	public PostNetItemTask(String url, Handler mHandler, int successEvent, int failEvent, Context mContext, RequestBody formBody,Object item) {
		super(url, mHandler, successEvent, failEvent, mContext);
		// TODO Auto-generated constructor stub
		this.formBody = formBody;
		this.item.item= item;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (NetUtils.checkNetWorkState(PoctApplication.mApp)) {
			String result = "";
			item.result = NetUtils.getInstance().post(mUrl, formBody);
			if (result == null) {
				if (mHandler != null)
					mHandler.sendEmptyMessage(failEvent);
			}
			else {
				try {
					JSONObject mobj = new JSONObject(result);

					if (mobj.has("status")) {
						if(mobj.getString("status").equals("500"))
						{
							if(mHandler != null) {
								Message msg = new Message();
								msg.what = failEvent;
								msg.obj = item.result;
								mHandler.sendMessage(msg);
							}
							return;
						}
					}

					if (mobj.has("token")) {
						NetUtils.getInstance().token = mobj.getString("token");
					}
					if(mobj.has("code"))
					{
						if(mobj.getInt("code") == 0)
						{
							if(mHandler != null) {
								Message msg = new Message();
								msg.what = successEvent;
								msg.obj = item;
								mHandler.sendMessage(msg);
							}

						} else {
							if (mHandler != null) {
								Message msg = new Message();
								msg.what = failEvent;
								msg.obj = item.result;
								mHandler.sendMessage(msg);
							}
						}
					}
					else
					{
						if(mHandler != null) {
							Message msg = new Message();
							msg.what = successEvent;
							msg.obj = item;
							mHandler.sendMessage(msg);
						}
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					if (mHandler != null) {
						Message msg = new Message();
						msg.what = failEvent;
						msg.obj = item.result;
						mHandler.sendMessage(msg);
					}
				}

			}
		}
		else {
			if (mHandler != null) {
				Message msg = new Message();
				msg.what = NetUtils.NO_NET_WORK;
				msg.obj = this.mContext.getString(R.string.error_net_network);
				mHandler.sendEmptyMessage(NetUtils.NO_NET_WORK);
			}

		}

	}
}
