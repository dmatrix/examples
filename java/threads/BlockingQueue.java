/**
 * 
 */
package threads;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author jules damji
 * @param <T>
 *
 */
public class BlockingQueue<T> {

	@SuppressWarnings("hiding")
	public class QCallable<T> implements QueueCallalable<T> {
		public QCallable(Object pObj) {
			
		}
		public void call() {
			// code to work on the object
			
		}
	}
	private Queue<T> mQueue;
	private int mCapacity;
	
	public BlockingQueue(int pCapacity) {
		mQueue = new LinkedList<T>();
		mCapacity = pCapacity; 
	}
	
	public void put(ArrayList<T> pElements) throws InterruptedException {
		
		//synchronize the block; wait while the queue is full until someone puts an element
		synchronized (mQueue) {
			while (mQueue.size() == mCapacity) {
				wait();
			}
			mQueue.addAll(pElements);
			notify();
		}
		
	}
	public synchronized void put(T pElement) throws InterruptedException {
		//block or wait while queue is full.
		while (mQueue.size() == mCapacity) {
			wait();
		}
		//someone removed element and notified us; we got woken up
		//add the element; notify anyone waiting is the queue has an element.
		mQueue.add(pElement);
		notify();
	}
	
	public synchronized void appplyToAll(QCallable<T> pFunc) throws Exception {
		
		while (mQueue.isEmpty()) {
			wait();
		}
		Iterator<T> it = mQueue.iterator();
		while (it.hasNext()) {
			pFunc.call();
		}
	}
	public synchronized T take() throws InterruptedException {
		// block or wait while the queue is empty
		while (mQueue.isEmpty()) {
			wait();
		}
		//wakeup as someone notified us that an element has been added.
		//notify anyone who is waiting to put an element onto the queue while
		//it was full.
		T item = mQueue.remove();
		notify();
		
		return item;		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
