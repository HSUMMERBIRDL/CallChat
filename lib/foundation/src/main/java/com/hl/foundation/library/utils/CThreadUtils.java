/**
 * 
 */
package com.hl.foundation.library.utils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Freddy on 2016/3/1.
 * chenshichao@outlook.com
 * 线程工具类，提供线程池
 */
public final class CThreadUtils {

	private static final int CORE_CONCURRENT = 3; // 线程池至少有3个线程
	private static final int MAX_CONCURRENT = 64; // 最大数，不做限制，如果不够资源，由系统抛异常
	private static final long KEEP_ALIVE_TIME = 30; // 120秒，就清理多余的线程
	private static final int WAIT_COUNT = 256; // 最多排队个数，这里控制线程创建的频率
	protected static final String TAG = CThreadUtils.class.getSimpleName();
	
	public static class CThreadFactory implements ThreadFactory {
		private AtomicInteger counter = new AtomicInteger(1);
		private String prefix = "";
		private int priority = Thread.NORM_PRIORITY;
		
		public CThreadFactory(String prefix, int priority) {
			this.prefix = prefix;
			this.priority = priority;
		}
		
		public CThreadFactory(String prefix) {
			this.prefix = prefix;
		}
		
		public Thread newThread(Runnable r) {
			Thread executor = new Thread(r, prefix + " #" + counter.getAndIncrement());
			executor.setDaemon(true);
			executor.setPriority(priority);
			return executor;
		}
	}

	
	/**
	 * 启动一个消耗线程，常驻后台
	 * @param r
	 */
	public static void startConsumer(final Runnable r, final String name) {
		runInBackground(new Runnable() {
			public void run() {
				new CThreadFactory(name, Thread.NORM_PRIORITY - 3).newThread(r).start();		
			}
		});
	}
	
	private static class LogAndAbortPolicy extends ThreadPoolExecutor.AbortPolicy {
		public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
			Log.e(TAG, "rejectedExecution: " + r);
			Log.e(TAG, logAllThreadStackTrace().toString());
			threadPoolExecutor.shutdownNow();
			threadPoolExecutor = createThreadPool();
		}
	}
	
	private static ExecutorService jobsForUI = Executors.newFixedThreadPool(
			CORE_CONCURRENT, new CThreadFactory("CJobsForUI", Thread.NORM_PRIORITY - 1));
	
	private static ThreadPoolExecutor createThreadPool() {
		return new ThreadPoolExecutor(
				CORE_CONCURRENT, MAX_CONCURRENT, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(WAIT_COUNT),
				new CThreadFactory("CThreadPool", Thread.NORM_PRIORITY - 2),
				new LogAndAbortPolicy());
	}

	private static ThreadPoolExecutor threadPoolExecutor = createThreadPool();
	
	public static Executor getExecutor() {
		return threadPoolExecutor;
	}

	/**
	 * 提交到其他线程去跑，需要取数据的时候会等待任务完成再继续
	 * @param task
	 * @return
	 */
	public static <T> Future<T> submitTask(Callable<T> task) {
		return jobsForUI.submit(task);
	}
	
	/**
	 * 强制清理任务
	 * @param task
	 * @return
	 */
	public static <T> void cancelTask(Future<T> task) {
		if (task != null) {
			task.cancel(true);
		}
	}
	
	/**
	 * 从 Future 中获取值，如果发生异常，打日志
	 * @param future
	 * @param tag
	 * @param name
	 * @return
	 */
	public static <T> T getFromTask(Future<T> future, String tag, String name) {
		try {
			return future.get();
		} catch (Exception e) {
			Log.e(tag, (name != null ? name + ": " : "") + e.toString());
		}
		return null;
	}

	private static Thread mainThread;
	private static Handler mainHandler;
	static {
		Looper mainLooper = Looper.getMainLooper();
		mainThread = mainLooper.getThread();
		mainHandler = new Handler(mainLooper);
	}


	public static boolean isOnMainThread() {
		return mainThread == Thread.currentThread();
	}

	public static void runOnMainThread(Runnable r) {
		if (isOnMainThread()) {
			r.run();
		} else {
			mainHandler.post(r);
		}
	}

	public static void runOnMainThread(Runnable r, long delayMillis) {
		if (delayMillis <= 0) {
			runOnMainThread(r);
		} else {
			mainHandler.postDelayed(r, delayMillis);
		}
	}

	/**
	 * 对runOnMainThread的，移除Runnable
	 * 
	 * @param r
	 */
	public static void removeCallbackOnMain(Runnable r) {
		mainHandler.removeCallbacks(r);
	}

	public static void runInBackground(Runnable r) {
		threadPoolExecutor.execute(r);
	}

	// 用于记录后台等待的Runnable，第一个参数外面的Runnable，第二个参数是等待中的Runnable
	private static HashMap<Runnable, Runnable> mapToMainHandler = new HashMap<Runnable, Runnable>();

	public static void runInBackground(final Runnable r,
			long delayMillis) {
		if (delayMillis <= 0) {
			runInBackground(r);
		} else {
			//如果相同r传入两次，第一次的r就不能remove
			Runnable mainRunnable = new Runnable() {
				public void run() {
					mapToMainHandler.remove(r);
					threadPoolExecutor.execute(r);
				}
			};
			mapToMainHandler.put(r, mainRunnable);
			mainHandler.postDelayed(mainRunnable, delayMillis);
		}
	}

	public static void runOnNotMainThread(Runnable r) {
		if (!isOnMainThread()) {
			r.run();
		} else {
			runInBackground(r);
		}
	}

	public static void removeCallbackInBackground(Runnable r) {
		Runnable mainRunnable = mapToMainHandler.get(r);
		if (mainRunnable != null) {
			mainHandler.removeCallbacks(mainRunnable);
		}
	}
	
	public static void logStatus() {
		StringBuilder sb = new StringBuilder();
		sb.append("getActiveCount");
		sb.append(threadPoolExecutor.getActiveCount());
		sb.append("\ngetTaskCount");
		sb.append(threadPoolExecutor.getTaskCount());
		sb.append("\ngetCompletedTaskCount");
		sb.append(threadPoolExecutor.getCompletedTaskCount());
		Log.e(TAG, sb.toString());
	}

	public static StringBuilder logAllThreadStackTrace() {
		StringBuilder builder = new StringBuilder();
		Map<Thread, StackTraceElement[]> liveThreads = Thread.getAllStackTraces();
		for (Iterator<Thread> i = liveThreads.keySet().iterator(); i.hasNext(); ) {
			Thread key = i.next();
			builder.append("Thread ").append(key.getName())
				   .append("\n");
			StackTraceElement[] trace = liveThreads.get(key);
			for (int j = 0; j < trace.length; j++) {
				builder.append("\tat ").append(trace[j]).append("\n");
			}
		}
		return builder;
	}
}
