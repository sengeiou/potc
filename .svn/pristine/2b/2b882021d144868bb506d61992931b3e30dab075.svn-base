package com.poct.android.manager;

import android.content.Intent;
import android.util.Log;

import com.poct.android.cache.CacheManager;
import com.poct.android.net.ProgressResponseListener;
import com.poct.android.receiver.AttachmentDownloadReceiver;
import com.poct.android.view.PoctApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AttachmentDownloadTask implements Runnable {

	private String url;
	private String path;
	private OkHttpClient mOkHttpClient;
	private RandomAccessFile randomAccessFile = null;
	private String mRecordid;
	private Call mCall;
	private long updateTime = 0;
	private long mSize;
	private boolean isRemove = false;
	private boolean isStart = false;
	public AttachmentDownloadTask(String url, String path, String recordid,long size) {
		this.url = url;
		this.path = path;
		this.mRecordid = recordid;
		this.mSize = size;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			File mfile = new File(path);
			if (mfile.exists()) {
				mfile.delete();
			}
			String dir = path.substring(0, path.lastIndexOf("/")+1);
			mfile = new File(dir);
			if(!mfile.exists())
			{
				mfile.mkdirs();
			}
			if(CacheManager.checkDownload(mSize))
			{
				randomAccessFile = new RandomAccessFile(path, "rwd");
				mOkHttpClient = AttachmentDownloadManager.getOkClient(progressResponseListener);
				Request request = new Request.Builder().url(url).build();
				mCall = mOkHttpClient.newCall(request);
				if(isRemove == true)
				{
					return;
				}
				Response response = mCall.execute();
				isStart = true;
				if (response.isSuccessful()) {
	                InputStream is = response.body().byteStream();
	                int len = -1;
	                byte[] buffer = new byte[1024];
	                while ((len = is.read(buffer)) != -1) {
	                	randomAccessFile.write(buffer, 0, len);
	                }
	                is.close();
				}
				else
				{
					Intent mfIntent = new Intent();
					mfIntent.putExtra("recordid", mRecordid);
					mfIntent.putExtra("path", path);
					mfIntent.setAction(AttachmentDownloadManager.EVENT_DOWNLOAD_FAIL);
					PoctApplication.mApp.sendBroadcast(mfIntent);
				}
			}
			
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	private ProgressResponseListener progressResponseListener = new ProgressResponseListener() {

		@Override
		public void onProgressResponse(long allBytes, long currentBytes, boolean done) {
			// TODO Auto-generated method stub
			Log.e("TAG", "bytesRead:" + currentBytes);
			Log.e("TAG", "contentLength:" + allBytes);
			Log.e("TAG", "done:" + done);
			if (allBytes != -1) {
				// 长度未知的情况下回返回-1
				Log.e("TAG", (100 * currentBytes) / allBytes + "% done");
			}
			Log.e("TAG", "================================");
			if(done == false && currentBytes > 0 && (System.currentTimeMillis() - updateTime) > 1000)
            {
                Intent mIntent = new Intent();
                mIntent.setAction(AttachmentDownloadReceiver.UPDATE_ATTACHMENT);
                mIntent.putExtra("finishsize", currentBytes);
                mIntent.putExtra("recordid", mRecordid);
                mIntent.putExtra("totalsize", allBytes);
				PoctApplication.mApp.sendBroadcast(mIntent);
                updateTime = System.currentTimeMillis();
            }
			else if(done == true)
			{
                Intent rIntent = new Intent();
                rIntent.setAction(AttachmentDownloadReceiver.FINISH_ATTACHMENT);
                rIntent.putExtra("recordid", mRecordid);
                rIntent.putExtra("totalsize", allBytes);
				PoctApplication.mApp.sendBroadcast(rIntent);
			}
		}
	};

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getmRecordid() {
		return mRecordid;
	}

	public void setmRecordid(String mRecordid) {
		this.mRecordid = mRecordid;
	}
	
	public void calclecall()
	{
		if(mCall != null)
		{
			if(mCall.isCanceled() == false)
			mCall.cancel();
		}
		
	}

	public boolean isRemove() {
		return isRemove;
	}

	public void setRemove(boolean isRemove) {
		this.isRemove = isRemove;
	}

	public boolean isStart() {
		return isStart;
	}

	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}

}
