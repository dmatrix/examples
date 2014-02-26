package threads;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TaskPoolExecutor {
	
	private int poolSize = 2;
    private int maxPoolSize = 2;
    private long keepAliveTime = 10;
	private ThreadPoolExecutor threadPool = null;	 
    private final ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(5);
	
    public TaskPoolExecutor()
	 {
		 threadPool = new ThreadPoolExecutor(poolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS, queue);
	 }
    
     public void executeTask(Runnable r) {
    	 threadPool.execute(r);
     }
     
     public void shutDown()
     {
         threadPool.shutdown();
     }
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		int tasks = 3;
		CountDownLatch latch = new CountDownLatch(tasks);
		if (args.length == 1) {
			try {
				tasks = Integer.valueOf(args[0]);
			} catch (Exception e) {
				//ignore
			}
		}
		TaskPoolExecutor poolExecutor = new TaskPoolExecutor();
		for (int i = 0; i < tasks; i++) {
			Task t = new Task("Task-" + i, latch);
			poolExecutor.executeTask(t);
		}
		try {
			latch.await();
			poolExecutor.shutDown();
	        System.out.println("main ends\n");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
