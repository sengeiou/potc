package com.poct.android.asks;

import android.content.Context;
import android.os.Handler;

import com.poct.android.net.NetTaskManager;
import com.poct.android.net.NetUtils;
import com.poct.android.net.nettask.PostJsonNetTask;
import com.poct.android.view.PoctApplication;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginAsks {

    public static final String LOGIN_PATH = "/station/login";
    public static final int EVENT_LOGIN_SUCCESS = 10000;
    public static final int EVENT_LOGIN_FAIL = 20000;
    public static void doLoging(String account, String password, Handler mHandler, Context mContext)
    {
        try {
            String urlString = NetUtils.getInstance().createURLStringoa(LOGIN_PATH);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ID",account);
            jsonObject.put("Password",password);
            jsonObject.put("VersionNum", PoctApplication.mApp.mUpDataModel.mVersionName);
            String postBody =  jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, EVENT_LOGIN_SUCCESS,
                    EVENT_LOGIN_FAIL, mContext, postBody);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
