package misc;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by jules on 3/3/16.
 */
public class FindPeakElement {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        int[] mArray = new int[20];
        //create an array of 20 elements between
        for (int i=0; i < mArray.length;i++) {
            mArray[i] = ThreadLocalRandom.current().nextInt(-10, 25);
        }
        FindElement.printArray(mArray, mArray.length);
        int peak = findPeak(mArray, 0, mArray.length);

        System.out.println("Peak =" + peak);
        if (isPrime(peak)) {
            System.out.println("Peak is prime");
        } else {
            System.out.println("Pick is not prime");
        }

    }

    /**
     * Check if a number is prime
     * @param number
     * @return
     */
    public static boolean isPrime(int number) {
        // Eliminate the need to check versus even numbers, since no prime number can be an even, except for 2
        if (number == 2 ) return true;
        // eliminate all even numbers
        if (number % 2 == 0)
            return false;
        // Check against all odd numbers: any number multiplied by 3 will always result in an odd number
        for (int i = 3; 3 * i <= number; i += 2) {
            if (number % i == 0)
                return false;
        }
        // If we make it here we know it is odd
        return true;

    }
    /**
     * Recursively find the Peak. It uses the divide an conquer strategy to find the Peak in the sorted array.
     * An ith position in the array is a peak if and only if array[i] >= array[i-1] && array[i] >= array[i+i]
     * @param numbers
     * @param start
     * @param end
     * @return peak or maximum point.
     */
    public static int findPeak(int[] numbers, int start, int end) {

        if (end == 0) return numbers[end];

        int midpoint = end/ 2;
        if (numbers[midpoint] < numbers[midpoint-1]) {
            return findPeak (numbers, start, midpoint-1);
        } else if (numbers[midpoint] < numbers[midpoint+ 1]) {
            return findPeak(numbers, midpoint+ 1, end);
        } else {
            return numbers[midpoint];
        }
    }
}
