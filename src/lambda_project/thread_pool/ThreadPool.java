package lambda_project.thread_pool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ThreadPool {
	private BlockingQueue<Runnable> taskQueue;
	private List<WorkerThread> workerThreads = new ArrayList<>();
	private boolean isStopped = false;
	
	public ThreadPool(int size, int tasksQueueSize) {
		taskQueue = new ArrayBlockingQueue<>(tasksQueueSize);
		
		for (int i = 0; i < size; i++) {
			WorkerThread workerThread = new WorkerThread(taskQueue);
			workerThreads.add(workerThread);
		}
		
		for (Thread thread : workerThreads) {
			thread.start();
		}
	}
	
	public synchronized void execute(Runnable task) {
		if (this.isStopped) throw new RuntimeException("Pool is stopped");
		
		this.taskQueue.offer(task);
	}
	
	public synchronized void stop() {
		this.isStopped = true;
		for (WorkerThread workerThread : workerThreads) {
			workerThread.doStop();
		}
	}
	
	public synchronized void waitUntilAllTasksFinished() {
		while (this.taskQueue.size() > 0) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
