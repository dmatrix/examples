package ds;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class FindMiddleElement {

    public static void main(String[] args) {
        int size = 0;
        try {
            size = Integer.valueOf(args[0]);
        } catch (Exception e) {
            e.printStackTrace();
            size = 20;
        }
        int fp = 0;
        int sp = 0;
        List<Integer> lst = new LinkedList<Integer>();
        for (int i= 0; i < size; i++) {
            int n = ThreadLocalRandom.current().nextInt(-10, 25);
            lst.add(n);
        }
        /**
        have two pointers at the starting point
        have the trailing pointer increment when first pointer mod 2 == 0
        cause by the time first pointer has reached the end, the second pointer
        will have been at mid point. Note: midpoint * 2 == end, hence mod by 2
        */
        for (int i = fp; fp < lst.size(); fp++) {
            if (fp % 2 == 0) {
                sp++;
            }
        }
        System.out.println("Size of Linked List: " + size);
        System.out.println("Mid point of linked List: " + sp);
        System.out.println("Mid point number is:" + lst.get(sp));
    }
}