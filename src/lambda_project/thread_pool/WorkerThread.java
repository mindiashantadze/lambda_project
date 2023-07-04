package lambda_project.thread_pool;

import java.util.concurrent.BlockingQueue;

public class WorkerThread extends Thread {
	private boolean isStopped = false;
	private BlockingQueue<Runnable> taskQueue;
	
	public WorkerThread(BlockingQueue<Runnable> taskQueue) {
		this.taskQueue = taskQueue;
	}
	
	@Override
	public void run() {
		while (!isStopped()) {
			try {
				Runnable runnable = taskQueue.take();
				runnable.run();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public synchronized void doStop() {
		this.isStopped = true;
		this.interrupt();
	}
	
	public synchronized boolean isStopped() {
		return isStopped;
	}
}
