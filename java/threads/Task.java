
package threads;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Task implements Runnable {

	private int mSleepTime;
	private String mTaskName;
	private static Random generator = new Random();
	private CountDownLatch mCountDown;
	
	
	public Task(String pThreadName, CountDownLatch pLatch) {
		mCountDown = pLatch;
		mTaskName = pThreadName;
		mSleepTime = generator.nextInt(500);
	}
	
	@Override
	public void run() {
		String thr = Thread.currentThread().getName();
		int sum = 0;
		System.out.println(String.format("%s-%s Computing tasks ...", mTaskName, thr));
		for (int i = 0; i < 25; i++) {
			System.out.println(String.format("%s:%d", mTaskName, i));
			System.out.println(String.format("%s sleeping for %d (millseconds)", mTaskName, mSleepTime));
			try {
				TimeUnit.MILLISECONDS.sleep(mSleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace()	;
			}
			sum = sum + i;
		}
		sum = sum + mSleepTime;
		System.out.println(String.format("%s-%s Finished: Sum + Time=%d", mTaskName, thr, sum));
		mCountDown.countDown();		
	}
}
