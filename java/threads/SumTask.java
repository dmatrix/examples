package threads;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SumTask<E> implements Runnable {
	private int mSleepTime;
	private String mTaskName;
	private static Random generator = new Random();
	private CountDownLatch mCountDown;
	
	public SumTask(String pName, CountDownLatch pLatch) {
		// TODO Auto-generated constructor stub
		mTaskName = pName;
		mCountDown = pLatch;
		mSleepTime = generator.nextInt(500);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
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
