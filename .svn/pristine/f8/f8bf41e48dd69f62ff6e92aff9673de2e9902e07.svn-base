package com.poct.android.prase;

import com.poct.android.utils.XpxJSONObject;
import com.poct.android.view.PoctApplication;

import org.json.JSONException;

public class LoginPrase {

    public static String loginPrase(String json)
    {
        try {
            if(json!= null)
            {
                XpxJSONObject jsonObject = new XpxJSONObject(json);
                XpxJSONObject data = jsonObject.getJSONObject("data");
                PoctApplication.mApp.account.mAccountId = data.getString("ID");
                PoctApplication.mApp.account.mName = data.getString("Name");
                PoctApplication.mApp.account.mDoctor = data.getString("Inspector");
                return jsonObject.getString("msg");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String loginFail(String json)
    {
        try {
            if(json!= null)
            {
                XpxJSONObject jsonObject = new XpxJSONObject(json);
                return jsonObject.getString("msg");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "网络连接错误";
    }
}
