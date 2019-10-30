package com.poct.android.manager;

import com.poct.android.net.NetUtils;
import com.poct.android.net.ProgressResponseBody;
import com.poct.android.net.ProgressResponseListener;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class AttachmentDownloadManager {

	public static final String EVENT_UPADA_DOWNLOAD = "updata_downlod";
	public static final String EVENT_START_DOWNLOAD = "start_downlod";
	public static final String EVENT_FINISH_DOWNLOAD = "finish_downlod";
	public static final String EVENT_DOWNLOAD_FAIL = "uplod_fail";
	private static AttachmentDownloadManager mAttachmentDownloadManager;
	public ArrayList<AttachmentDownloadTask> mAttachmentDownloadTasks = new ArrayList<AttachmentDownloadTask>();
	private static AttachmentDownloadTaskManagerThread mAttachmentDownloadTaskManagerThread = new AttachmentDownloadTaskManagerThread();

	public AttachmentDownloadManager() {

	}

	public static synchronized AttachmentDownloadManager getInstance() {
		if (mAttachmentDownloadManager == null) {
			mAttachmentDownloadManager = new AttachmentDownloadManager();
			mAttachmentDownloadTaskManagerThread.setStop(false);
			new Thread(mAttachmentDownloadTaskManagerThread).start();
		}
		return mAttachmentDownloadManager;
	}


	 public static OkHttpClient getOkClient(final ProgressResponseListener listener) {
	        //克隆
	        OkHttpClient.Builder builder= NetUtils.getInstance().mOkHttpClient.newBuilder();
	        Interceptor interceptor = new Interceptor() {
	            @Override
	            public Response intercept(Chain chain) throws IOException {
	                Response response = chain.proceed(chain.request());

	                return response.newBuilder().body(new ProgressResponseBody(listener, response.body())).build();
	            }
	        };

	        builder.networkInterceptors().add(interceptor);
	        return builder.build();
	    }
	 
	 
	 public void doStop(String recorrdid)
	 {
		 for(int i = 0 ; i < mAttachmentDownloadTasks.size() ; i++)
		 {
			 if(mAttachmentDownloadTasks.get(i).getmRecordid().equals(recorrdid))
			 {
				 mAttachmentDownloadTasks.get(i).calclecall();
				 AttachmentDownloadTaskManager.getInstance().removeTaskbyItem(mAttachmentDownloadTasks.get(i).getmRecordid());
				 mAttachmentDownloadTasks.get(i).setRemove(true);
				 mAttachmentDownloadTasks.remove(mAttachmentDownloadTasks.get(i));
			 } 
		 }
	 }
	 
	 public void onDestory()
	 {
		 AttachmentDownloadTaskManager.getInstance().removeall();
		 for(int i = 0 ; i < mAttachmentDownloadTasks.size() ; i++)
		 {
			 mAttachmentDownloadTasks.get(i).calclecall();
			 mAttachmentDownloadTasks.get(i).setRemove(true);
		 }
		 mAttachmentDownloadTasks.clear();
		 AttachmentDownloadTaskManager.getInstance().removeall();
	 }
}
