package com.poct.android.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import com.poct.android.net.nettask.BluetoothTask;
import com.poct.android.utils.StringUtil;
import com.poct.android.utils.XpxJSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetUtils {
	public static final int TimeOut = 10000;
	public static final int TimeOut2 = 15000;
	public static final int NO_NET_WORK = 60001;
	public static final int EQUIPMENT_ERROR = 60000;
	public static final MediaType MEDIA_TYPE_MARKDOWN  = MediaType.parse("text/x-markdown; charset=utf-8");
	public static final String NET_PATH = "http://47.96.41.188/iinspection";
	public static NetUtils mNetUtils;
	public static OkHttpClient mOkHttpClient;
	public XpxJSONArray mUsers;
	public static String CLOUND_HOST = "";
	public String token = "";


	public NetUtils() {
		mOkHttpClient = new OkHttpClient.Builder()
				.connectTimeout(TimeOut, TimeUnit.MILLISECONDS).readTimeout(TimeOut2, TimeUnit.MILLISECONDS).writeTimeout(TimeOut2, TimeUnit.MILLISECONDS).build();
	}

	public static synchronized NetUtils getInstance() {
		if (mNetUtils == null) {
			mNetUtils = new NetUtils();
		}
		return mNetUtils;
	}



	public final String createURLStringoa(String path) {
		return (NET_PATH // HOST
				// PORT
				 + path); // PAGE
	}

	public String post(String url , RequestBody formBody) {

		Request request = new Request.Builder()
			      .url(url)
			      .post(formBody)
			      .build();
		Response response;
		try {
			 response = mOkHttpClient.newCall(request).execute();
				if (response.isSuccessful()) {
					return response.body().string();
				}
				else {
					return response.body().string();
				}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}

	public String postJson(String url , String formBody) {

		Request request = new Request.Builder()
				.url(url)
				.post(RequestBody.create(NetUtils.MEDIA_TYPE_MARKDOWN, formBody))
				.build();
		Response response;
		try {
			response = mOkHttpClient.newCall(request).execute();
			if (response.isSuccessful()) {
				return response.body().string();
			}
			else {
				return response.body().string();
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public Call get(String url) {

		Request request = new Request.Builder().url(url).build();
		return mOkHttpClient.newCall(request);
	}
	
	public String getUrl(Call mCall)
	{
		Response response;
		try {
			response = mCall.execute();
			if (response.isSuccessful()) {
				return response.body().string();
			}
			else {
				return null;
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}



	
	public InputStream getStresm(String url)
	{
		Request request = new Request.Builder().url(url).build();
		Response response;
		try {
			response = mOkHttpClient.newCall(request).execute();
			return response.body().byteStream();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}


	public static Boolean checkNetWorkState(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getApplicationContext().getSystemService(
						Context.CONNECTIVITY_SERVICE);

		if (manager == null) {
			return false;
		}

		NetworkInfo networkinfo = manager.getActiveNetworkInfo();

		if (networkinfo == null || !networkinfo.isAvailable()) {
			return false;
		}

		return true;
	}

	public void sendFail(Handler handler,Context context,int success)
	{
		String url ="0B " + StringUtil.str2HexStr("AE|") + " "+"1C 0D";
		BluetoothTask mBluetoothTask = new BluetoothTask(url,handler, success,0,context,"EA",1,2);
		NetTaskManager.getInstance().addNetTask(mBluetoothTask);
	}

	public void sendSuccess(Handler handler,Context context)
	{
		String url ="0B " + StringUtil.str2HexStr("AA|") + " "+"1C 0D";
		BluetoothTask mBluetoothTask = new BluetoothTask(url,handler,0,0,context,"AA",1,2);
		NetTaskManager.getInstance().addNetTask(mBluetoothTask);
	}

	public void sendSuccess(Handler handler,Context context,int success ,int faile,int count,int count2)
	{
		String url ="0B " + StringUtil.str2HexStr("AA|") + " "+"1C 0D";
		BluetoothTask mBluetoothTask = new BluetoothTask(url,handler,0,0,context,"AA",count,count2);
		NetTaskManager.getInstance().addNetTask(mBluetoothTask);
	}

	public static String praseData(String hex,String commend)
	{
		String data = "";
		if(hex.contains("\r"))
		{
			data = hex.substring(hex.indexOf(commend),hex.indexOf("\r")-2);
		}
		return data;
	}

	public static String praseData(String hex,String commend,String id)
	{
		String data = "";
		String data2 = "";
		if(hex.contains("\r"))
		{
			if(hex.indexOf(commend+"|"+id) > -1)
			{
				data = hex.substring(hex.indexOf(commend+"|"+id),hex.length());
				data2 = data.substring(data.indexOf(commend),data.indexOf("\r")-2);
			}
		}
		return data2;
	}
}
