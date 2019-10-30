package com.poct.android.manager;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class AttachmentDownloadTaskManager {
	// UI请求队列
	private LinkedList<AttachmentDownloadTask> mAttachmentDownloadTasks;
	// 任务不能重复
	private Set<String> taskIdSet;
	
	private static AttachmentDownloadTaskManager mNetTaskMananger;

	private AttachmentDownloadTaskManager() {

		mAttachmentDownloadTasks = new LinkedList<AttachmentDownloadTask>();
		taskIdSet = new HashSet<String>();
		
	}

	public static synchronized AttachmentDownloadTaskManager getInstance() {
		if (mNetTaskMananger == null) {
			mNetTaskMananger = new AttachmentDownloadTaskManager();
		}
		return mNetTaskMananger;
	}

	//1.先执行
	public void addDownloadTask(AttachmentDownloadTask mAttachmentDownloadTask) {
		synchronized (mAttachmentDownloadTasks) {
				mAttachmentDownloadTasks.addLast(mAttachmentDownloadTask);
				AttachmentDownloadManager.getInstance().mAttachmentDownloadTasks.add(mAttachmentDownloadTask);
		}

	}
	public boolean isTaskRepeat(String fileId) {
		synchronized (taskIdSet) {
			if (taskIdSet.contains(fileId)) {
				return true;
			} else {
				System.out.println("下载管理器增加下载任务："+ fileId);
				taskIdSet.add(fileId);
				return false;
			}
		}
	}
	
	public AttachmentDownloadTask getAttachmentDownloadTask() {
		synchronized (mAttachmentDownloadTasks) {
			if (mAttachmentDownloadTasks.size() > 0) {
				System.out.println("下载管理器增加下载任务："+"取出任务");
				AttachmentDownloadTask mAttachmentDownloadTask = mAttachmentDownloadTasks.removeFirst();
				return mAttachmentDownloadTask;
			}
		}
		return null;
	}
	
	public void removeall()
	{
		mAttachmentDownloadTasks.clear();
	}

	public int getCount()
	{
		return mAttachmentDownloadTasks.size();
	}
	
	public void removeTaskbyItem(String recordid)
	{
		for(int i = 0 ; i < mAttachmentDownloadTasks.size() ; i++)
		{
			AttachmentDownloadTask mAttachmentDownloadTask= mAttachmentDownloadTasks.get(i);
			if(mAttachmentDownloadTask.getmRecordid().equals(recordid))
			{
				mAttachmentDownloadTasks.remove(mAttachmentDownloadTask);
			}
		}
	}

	public LinkedList<AttachmentDownloadTask> getmDownloadTasks() {
		return mAttachmentDownloadTasks;
	}

	public void setmDownloadTasks(LinkedList<AttachmentDownloadTask> mAttachmentDownloadTasks) {
		this.mAttachmentDownloadTasks = mAttachmentDownloadTasks;
	}
	
}
