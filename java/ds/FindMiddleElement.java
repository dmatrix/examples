package ds;

import java.util.LinkedList;
import java.util.List;


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
            lst.add(i);
        }
        for (int i = fp; fp < lst.size(); fp++) {
            if (fp % 2 == 0) {
                sp++;
            }
        }
        System.out.println("Size of Linked List: " + size);
        System.out.println("Mid point of linked List: " + sp);
    }
}