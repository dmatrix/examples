package ds;

import java.util.Iterator;

/**
 * Created by jules on 3/3/16.
 * Example question how to create a generic interator that will repeat on an <T> N times
 */
public class DSIterator {

    /**
     * @param elem
     * @param pTimes
     * @param <T>
     * @return Iterator<T>
     */
    public static <T> Iterator<T> repeat (T elem, int pTimes) {

        // make the variables final so they can't change
        final T t = elem;
        final int num = pTimes;

        // create an anonymous class of type Iterator() and override the functions
        return new Iterator<T>() {

            private T object = t;

            // keep track of the number of times you want to repeat
            private int repeatNumber = num;
            private int counter = 0;

            @Override
            public boolean hasNext() {
                return (counter < repeatNumber);
            }

            @Override
            public T next() {
                counter++;
                return object;
            }

            @Override
            public void remove() {

            }
        };
    }

    /**
     * Test the Iterator with a String
     * @param args
     */
    public static void main(String[] args) {
        // test with object of type String
        Iterator<String> iter = repeat("Test repeater", 7);
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
        // test with object of type Integer
        Iterator<Integer> iter2 = repeat(35, 5);
        while (iter2.hasNext()) {
            System.out.println(iter2.next());
        }
    }

}
