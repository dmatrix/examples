package ds;

import java.util.Arrays;

/**
 * Created by jules on 1/12/16.
 * Queue implemented as the bounded array
 */
public class Queue {

    private int[] theQueue;
    private int frontOfTheQ;
    private int backOfTheQ;
    private int queueSize;
	private int numberOfItems;

	/**
	 * The constructor for the Queue
	 * @param size
     */
	public Queue (int size) {
		queueSize = size;
		frontOfTheQ = 0;
		backOfTheQ = 0;
		numberOfItems = 0;
		theQueue = new int[queueSize];
		Arrays.fill(theQueue, -1);
	}

	/**
	 * Remove an item from the front of the queue.
	 * @return item or -1 if empty
     */
	public int remove() {
		int item = -1;
		if (numberOfItems > 0) {
			item = theQueue[frontOfTheQ];
			theQueue[frontOfTheQ] = -1;
			frontOfTheQ++;
			numberOfItems--;
		}
		return item;
	}

	/**
	 * Insert an item to the rear of the queue
	 * @param item
	 * @return item or -1 if queue is full
     */
	public int insert (int item){
		if (numberOfItems + 1 < queueSize){
			//check if we reached the end of the queue
			if (backOfTheQ == queueSize)
				backOfTheQ=0;
			//insert items
			theQueue[backOfTheQ] =item ;
			backOfTheQ++;
			numberOfItems++;
			return item;
		} else {
			return -1;
		}
	}

	/**
	 * Peek at the front of the queue with removing it
	 * @return item or -1 if queue is empty
     */
	public int peek() {
		if (numberOfItems > 0) {
			return theQueue[frontOfTheQ];
		} else {
			return -1;
		}
	}

	/**
	 * Display the contents of the queue as a formated, squared array.
	 */
    public void displayTheQueue(){
        for(int n = 0; n < 61; n++)System.out.print("-");
	        System.out.println();
            for(int n = 0; n < queueSize; n++){
	            System.out.format("| %2s "+ " ", n);
	        }
	        System.out.println("|");
	        for(int n = 0; n < 61; n++)System.out.print("-");
	        System.out.println();
	        for(int n = 0; n < queueSize; n++) {
                if(theQueue[n] == -1 )
                    System.out.print("|     ");
                else
                    System.out.print(String.format("| %2s "+ " ", theQueue[n]));
             }
	        System.out.println("|");
	        for(int n = 0; n < 61; n++)System.out.print("-");
                System.out.println();
	        // Number of spaces to put before the F
	        int spacesBeforeFront = 3*(2*(frontOfTheQ +1)-1);
	        for(int k = 1; k < spacesBeforeFront; k++)
                System.out.print(" ");
	        System.out.print("F");
	        // Number of spaces to put before the R
	        int spacesBeforeRear = (2*(3*backOfTheQ)-1) - (spacesBeforeFront);
	        for(int l = 0; l < spacesBeforeRear; l++)
                System.out.print(" ");
	        System.out.print("R");
	        System.out.println("\n");
	}

    /**
     * Main or driver program
     * @param args
     */
	public static void main(String[] args) {

		Queue theQueue = new Queue(10);

		int item = -1;
		if ((item = theQueue.insert(67)) > 0) {
			System.out.println(item + " inserted into the Queue");
		}
		theQueue.displayTheQueue();
		if ((item = theQueue.insert(6)) > 0) {
			System.out.println(item + " inserted into the Queue");
		}
		theQueue.displayTheQueue();
		if ((item = theQueue.insert(17)) > 0) {
			System.out.println(item + " inserted into the Queue");
		}
		theQueue.displayTheQueue();
		if ((item = theQueue.insert(7)) > 0) {
			System.out.println(item + " inserted into the Queue");
		}
		theQueue.displayTheQueue();
		if ((item = theQueue.insert(27)) > 0) {
			System.out.println(item + " inserted into the Queue");
		}
		theQueue.displayTheQueue();
		if ((item = theQueue.remove()) > 0) {
			System.out.println(item + " removed from the Queue");
		}
		theQueue.displayTheQueue();
		if ((item = theQueue.remove()) > 0) {
			System.out.println(item + " removed from the Queue");
		}
		theQueue.displayTheQueue();
		if ((item = theQueue.remove()) > 0) {
			System.out.println(item + " removed from the Queue");
		}
		theQueue.displayTheQueue();
		if ((item = theQueue.peek()) > 0) {
			System.out.println(item + " peeked from the Queue");
		}
		theQueue.displayTheQueue();
		if ((item = theQueue.remove()) > 0) {
			System.out.println(item + " removed from the Queue");
		}
		theQueue.displayTheQueue();
		if ((item = theQueue.remove()) > 0) {
			System.out.println(item + " removed from the Queue");
		}
		theQueue.displayTheQueue();
		if ((item = theQueue.insert(27)) > 0) {
			System.out.println(item + " inserted into the Queue");
		}
		theQueue.displayTheQueue();
		if ((item = theQueue.insert(7)) > 0) {
			System.out.println(item + " inserted into the Queue");
		}
		theQueue.displayTheQueue();
		if ((item = theQueue.remove()) > 0) {
			System.out.println(item + " removed from the Queue");
		}
		theQueue.displayTheQueue();
		for (int i=1; i <= 9; i++) {
			if ((item = theQueue.insert(i)) > 0) {
				System.out.println(item + " inserted into the Queue");
				theQueue.displayTheQueue();
			}
		}
		if ((item = theQueue.remove()) > 0) {
			System.out.println(item + " removed from the Queue");
		}
		theQueue.displayTheQueue();
	}
}
