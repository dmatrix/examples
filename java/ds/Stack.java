package ds;

import java.util.Arrays;

/**
 * Created by jules on 1/12/16.
 */
public class Stack {

    private int[] theStack;
    private int stackSize;
    private int topOfStack;

    public Stack (int size) {
        stackSize = size;
        theStack = new int[size];
        topOfStack = -1;
        Arrays.fill(theStack, -1);
    }

    int push(int elem) {
        if (topOfStack >= stackSize) {
            return -1;
        } else {
            topOfStack++;
            theStack[topOfStack] = elem;
            return elem;
        }
    }

    int pushAll (int[] elems) {
        if (elems.length + topOfStack > stackSize) {
            return -1;
        } else {
            for(int n: elems) {
                push(n);
            }
        }
        return elems.length;

    }
    int pop() {
        if (topOfStack == -1) {
            //stack is empty
            return -1;
        } else {
            int elem = theStack[topOfStack];
            theStack[topOfStack--] = -1;
            return elem;
        }
    }

    int peek() {
        if (topOfStack == -1) {
            return -1;
        } else {
            return theStack[topOfStack];
        }
    }

    public void displayTheStack() {

        for (int n = 0; n < 61; n++) System.out.print("-");
        System.out.println();
        for (int n = 0; n < stackSize; n++) {
            System.out.format("| %2s " + " ", n);
        }
        System.out.println("|");
        for (int n = 0; n < 61; n++) System.out.print("-");
        System.out.println();
        for (int n = 0; n < stackSize; n++) {
            if (theStack[n] == -1)
                System.out.print("|     ");
            else
                System.out.print(String.format("| %2s " + " ", theStack[n]));
        }
        System.out.println("|");
        for (int n = 0; n < 61; n++)
            System.out.print("-");
        System.out.println();
    }

    public static void main(String[] args) {

        Stack theStack = new Stack(10);

        if (theStack.push(2) > 0) {
            System.out.println(" 2 pushed onto the stack");
        }
        theStack.displayTheStack();
        if (theStack.push(5) > 0) {
            System.out.println(" 5 pushed onto the stack");
        }
        theStack.displayTheStack();
        if (theStack.push(7) > 0) {
            System.out.println("7 pushed onto the stack");
        }
        theStack.displayTheStack();
        if (theStack.push (1) > 0) {
            System.out.println("1 pushed onto the stack");
        }
        theStack.displayTheStack();
        if (theStack.push(10) > 0 ) {
            System.out.println("10 pushed onto the stack");
        }
        theStack.displayTheStack();
        int p = theStack.pop();
        if (p > 0) {
            System.out.println(p + " popped off  the stack");
        }
        theStack.displayTheStack();
        p = theStack.pop();
        if (p > 0) {
            System.out.println(p + " popped off  the stack");
        }
        theStack.displayTheStack();
        int[] data = new int[] {30,20,3,5};
        p = theStack.pushAll(data);
        if (p > 0) {
            System.out.println(p + " elements pushed onto the stack");
            theStack.displayTheStack();
        } else {
            System.out.println("Not enough space on the stack");
        }

    }
}
