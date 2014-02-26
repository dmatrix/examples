package threads;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskRunner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		int nThreads = 3;
		CountDownLatch latch;
		if (args.length == 1) {
			try {
				nThreads = Integer.valueOf(args[0]);
			} catch (Exception e) {
				//ignore
			}
		}
		latch = new CountDownLatch(nThreads);
		ExecutorService threadExecutor = Executors.newFixedThreadPool(nThreads);
		System.out.println(String.format("Executing %d threads for the Executor Service", nThreads));
		System.out.println(String.format("Starting %d threads", nThreads));
		for (int i=0; i< nThreads; i++) {
			threadExecutor.execute(new SumTask<Integer>("Sum-Task-" + i, latch));
		}
		try {
			latch.await();
			threadExecutor.execute(new Task("Task-1", new CountDownLatch(1)));
			threadExecutor.shutdown(); // shutdown worker threads			 
	        System.out.println("main ends\n");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
