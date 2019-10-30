package com.poct.android.net;

import com.poct.android.net.nettask.NetTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class NetTaskManagerThread implements Runnable {

	private NetTaskManager mNetTaskManager;

	// 创建一个可重用固定线程数的线程池
	private ExecutorService mPool;
	// 线程池大小
	private final int POOL_SIZE = 1;
	// 轮询时间
	private final int SLEEP_TIME = 1000;
	// 是否停止
	private boolean isStop = false;

	public NetTaskManagerThread() {
		mNetTaskManager = NetTaskManager.getInstance();
		mPool = Executors.newFixedThreadPool(POOL_SIZE);

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (!isStop) {
			NetTask mNetTask = mNetTaskManager.getNetTask();
			if (mNetTask != null) {
				mPool.execute(mNetTask);
			} else {  //如果当前未有downloadTask在任务队列中
				try {
					// 查询任务完成失败的,重新加载任务队列
					// 轮询,
					Thread.sleep(SLEEP_TIME);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		if (isStop) {
			mPool.shutdown();
		}

	}

	/**
	 * @param isStop
	 *            the isStop to set
	 */
	public void setStop(boolean isStop) {
		this.isStop = isStop;
	}

}
