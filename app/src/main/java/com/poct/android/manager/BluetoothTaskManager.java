package com.poct.android.manager;

import com.poct.android.net.nettask.NetTask;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class BluetoothTaskManager {
	// UI请求队列
	private LinkedList<NetTask> mNetTasks;
	// 任务不能重复
	private Set<String> taskIdSet;
	private static BluetoothTaskManager mNetTaskMananger;

	private BluetoothTaskManager() {

		mNetTasks = new LinkedList<NetTask>();
		taskIdSet = new HashSet<String>();
		
	}

	public static synchronized BluetoothTaskManager getInstance() {
		if (mNetTaskMananger == null) {
			mNetTaskMananger = new BluetoothTaskManager();
		}
		return mNetTaskMananger;
	}

	//1.先执行
	public void addNetTask(NetTask mNetTask) {
		synchronized (mNetTasks) {
			if (!isTaskRepeat(mNetTask.mRecordId)) {
				mNetTasks.addLast(mNetTask);
			}
		}
	}

	public boolean isTaskRepeat(String mRecordId) {
		synchronized (taskIdSet) {
			if (taskIdSet.contains(mRecordId)) {
				return true;
			} else {
				taskIdSet.add(mRecordId);
				return false;
			}
		}
	}
	
	public NetTask getNetTask() {
		synchronized (mNetTasks) {
			if (mNetTasks.size() > 0) {
				NetTask mNetTask = mNetTasks.removeFirst();
				return mNetTask;
			}
		}
		return null;
	}


}
