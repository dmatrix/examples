package misc;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;


/**
 * Find an element in an array.
 */
public class FindElement {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int[] mArray = new int[20];
		for (int i=0; i < mArray.length;i++) {
			mArray[i] = ThreadLocalRandom.current().nextInt(1, 50);
		}
		mArray = sort(mArray);
		printArray(mArray, mArray.length);
		System.out.println("\nSearching for random numbers inside the array\n");
		for (int i=0; i < mArray.length;i++) {
			int n = ThreadLocalRandom.current().nextInt(1, 50);
			if (hasElement(mArray, n, 0, mArray.length-1)) {
				System.out.println(String.format("Element %d in the Array", n));
			} else {
				System.out.println(String.format("Element %d not in the Array", n));
			}
		}

		System.out.println("\nMedian: " + findMedian(mArray));
		System.out.println("Mean: " + findMean(mArray));
		System.out.println("13 th Largest Number:" + findNthLargestElement(mArray, 13));
	}

	public static void printArray(int[]array, int n) {
		for (int i =0; i < n; i++) {
			System.out.println("Array[" + i +"] = " + array[i]);
		}
	}

	/**
	 * Use the underlying sort algorithm to sort the random number of arrays
	 * @param array
	 * @return sorted array
     */
	public static int[] sort(int[] array) {
		Arrays.sort(array);
		return array;

	}

	/**
	 * Find the median or middle point in this sorted array
	 * @param array
	 * @return
     */
	public static int findMedian(int[] array) {
		int middle = array.length / 2;
		return array[middle];
	}

	public static int computeSum(int[] array, int index) {
		if (index == array.length) {
			return 0;
		} else {
			return (array[index] + computeSum(array, index+1));
		}
	}
	/**
	 * Find average or mean of this sorted array
	 * @param array
	 * @return avearge or mean
     */
	public static int findMean(int[] array) {

		int sum = computeSum(array, 0);
		return sum / array.length;
	}

	public static int findNthLargestElement(int[] array, int nthLargetst) {
		if (nthLargetst > array.length) {
			return -1;
		} else {
			return array[nthLargetst];
		}
	}

	/**
	 * Recusrive way to find an element in a large array. Split the array into halves, and acerstain
	 * if the sought element is to the left of the middle element or right. Repeat process unitl you find it.
	 * This funciton presumes the array is sorted in an ascending order.
	 * @param array
	 * @param value
	 * @param start
	 * @param end
     * @return
     */
	private static boolean hasElement(int[] array, int value, int start, int end) {
		int middle;
		if (end <= start) {
			//didn't find it return false, our anchor to stop recursion.
			return false;
		}
		// find the mid point
		middle = (start + end) / 2;
		// Woo! found it return
		if (array[middle] == value)
			return true;
		//recurse on the left part of the array
		else if (value < array[middle]) {
			return hasElement(array, value, start, middle);
			// recurse to the right of the middle element
		} else {
			return hasElement(array, value, middle+1, end);
		}
	}

}
