package ds;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by jules on 1/16/16.
 * But borrowed and modified from http://www.newthinktank.com/2013/03/java-hash-table-2/
 * to comprehend hashing strategies such as using prime numbers for the size of the array,
 * increasing the size of array and avoiding clustering by using double-hashing.
 *
 */
public class HashTable2 {
    String[] theArray;
    int arraySize;

    public HashTable2(int size) {
        arraySize = size;
        theArray = new String[arraySize];
        Arrays.fill(theArray, "-1");

    }

    /**
     * Check if a number is prime
     * @param number
     * @return
     */
    public boolean isPrime(int number) {
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
     * Receives a number and returns the next prime
     * number that follows
     * @param minNumberToCheck
     * @return next prime close to minNumberToCheck
     */
    public int getNextPrime(int minNumberToCheck) {
        for (int i = minNumberToCheck; true; i++) {
            if (isPrime(i))
                return i;
        }
    }

    /**
     *  Increase array size to a prime number and move the old array into the newer, larger one
     * @param minArraySize
     */
    public void increaseArraySize(int minArraySize) {
        // Get a prime number bigger than the array
        // requested
        int newArraySize = getNextPrime(minArraySize);
        // Move the array into a bigger array with the
        // larger prime size
        moveOldArray(newArraySize);
    }

    /**
     * Move the old array to the newer. This is resizing the array
     * @param newArraySize
     */
    public void moveOldArray(int newArraySize) {
        // Create an array that has all of the values of
        // theArray, but no empty spaces
        String[] cleanArray = removeEmptySpacesInArray(theArray);
        // Increase the size for theArray
        theArray = new String[newArraySize];
        arraySize = newArraySize;
        // Fill theArray with -1 in every space
        fillArrayWithNeg1();
        // Send the values previously in theArray into
        // the new larger array using the newer hash function
        hashFunction2(cleanArray, theArray);
    }

    /**
     * Will remove all empty spaces in an array
     * @param arrayToClean
     * @return
     */
    public String[] removeEmptySpacesInArray(String[] arrayToClean) {
        ArrayList<String> stringList = new ArrayList<String>();
        // Cycle through the array and if a space doesn't
        // contain -1, or isn't empty add it to the ArrayList
        for (String theString : arrayToClean)
            if (!theString.equals("-1") && !theString.equals(""))
                stringList.add(theString);
        return stringList.toArray(new String[stringList.size()]);
    }

    /**
     * The new double hash funciton to reduce clustering
     *
     * @param stringsForArray
     * @param theArray
     */
    public void doubleHashFunc(String[] stringsForArray, String[] theArray) {
        for (int n = 0; n < stringsForArray.length; n++) {
            // Store value in array index
            String newElementVal = stringsForArray[n];
            // Create an index to store the value in by taking
            // the modulus
            int arrayIndex = Integer.parseInt(newElementVal) % arraySize;
            // Get the distance to skip down in the array
            // after a collision occurs. We are doing this
            // rather than just going to the next index to
            // avoid creating clusters
            int stepDistance = 7 - (Integer.parseInt(newElementVal) % 7);
            System.out.println("step distance: " + stepDistance);

            // Cycle through the array until we find an empty space
            while (theArray[arrayIndex] != "-1") {
                arrayIndex += stepDistance;
                // System.out.println("Collision Try " + arrayIndex +
                // " Instead");
                // If we get to the end of the array go back to index 0
                arrayIndex %= arraySize;
            }
            theArray[arrayIndex] = newElementVal;
        }
    }

    /**
     * Use the modified double hash function to locate the new value
     * @param key
     * @return
     */
    public String findKeyDblHashed(String key) {
        // Find the keys original hash key
        int arrayIndexHash = Integer.parseInt(key) % arraySize;
        // Find the keys original step distance
        int stepDistance = 5 - (Integer.parseInt(key) % 5);
        while (theArray[arrayIndexHash] != "-1") {
            if (theArray[arrayIndexHash] == key) {
                // Found the key so return it
                System.out.println(key + " was found in index "
                        + arrayIndexHash);
                return theArray[arrayIndexHash];
            }
            // Look in the next index
            arrayIndexHash += stepDistance;
            // If we get to the end of the array go back to index 0
            arrayIndexHash %= arraySize;
        }
        // Couldn't locate the key
        return null;
    }

    public void hashFunction2(String[] stringsForArray, String[] theArray) {
        for (int n = 0; n < stringsForArray.length; n++) {
            String newElementVal = stringsForArray[n];
            // Create an index to store the value in by taking
            // the modulus
            int arrayIndex = Integer.parseInt(newElementVal) % arraySize;

            // Cycle through the array until we find an empty space
            while (theArray[arrayIndex] != "-1") {
                ++arrayIndex;
                 System.out.println("Collision Try " + arrayIndex +
                 " Instead");
                // If we get to the end of the array go back to index 0
                arrayIndex %= arraySize;

            }
            theArray[arrayIndex] = newElementVal;
        }
    }

    /**
     * Fill up the array with string "-1"
     */
    public void fillArrayWithNeg1() {

        Arrays.fill(theArray, "-1");

    }

    /**
     * Display the array implemented as a hash table
     */
    public void displayTheHashTable() {
        int increment = 0;
        int numberOfRows = (arraySize / 10) + 1;
        for (int m = 0; m < numberOfRows; m++) {
            increment += 10;
            for (int n = 0; n < 71; n++)
                System.out.print("-");
            System.out.println();
            for (int n = increment - 10; n < increment; n++) {
                System.out.format("| %3s " + " ", n);
            }
            System.out.println("|");
            for (int n = 0; n < 71; n++)
                System.out.print("-");
            System.out.println();
            for (int n = increment - 10; n < increment; n++) {
                if (n >= arraySize)
                    System.out.print("|      ");
                else if (theArray[n].equals("-1"))
                    System.out.print("|      ");
                else
                    System.out
                            .print(String.format("| %3s " + " ", theArray[n]));
            }
            System.out.println("|");
            for (int n = 0; n < 71; n++)
                System.out.print("-");
            System.out.println();
        }
    }

    public static void main(String[] args) {
        HashTable2 theFunc = new HashTable2(31);
        // data that are similar to create some collision and see how double hashing
        // alleviates it.
        String[] elementsToAdd3 = {"30", "60", "90", "120", "150", "180",
                "210", "240", "270", "300", "330", "360", "390", "420", "450",
                "480", "510", "540", "570", "600", "989", "984", "320", "321",
                "400", "415", "450", "50", "660", "624"};

        // Use the first hash function that will create some collision
        theFunc.hashFunction2(elementsToAdd3, theFunc.theArray);
        //increase the size of the array
        theFunc.increaseArraySize(60);
        //show the large array
        theFunc.displayTheHashTable();
        // clear out the array
        theFunc.fillArrayWithNeg1();
        // try with newer data with double-hashing function
        theFunc.doubleHashFunc(elementsToAdd3, theFunc.theArray);
        theFunc.displayTheHashTable();
        theFunc.findKeyDblHashed("984");
    }
}
