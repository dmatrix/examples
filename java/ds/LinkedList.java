package ds;

/**
 * Created by jules on 1/12/16.
 */
public class LinkedList {

    //instance variables for the LinkedList
    private Node first, last;
    private int elements;
    
    /**
     * Constructor for the linked list
     */
    public LinkedList() {
        first = last = null;
        elements = 0;
    }

    /**
     * getters
     * @return reference to the first element
     */
    public Node getFirst() { return first; }

    /**
     * Check if list is empty
     * @return
     */
    public boolean isEmpty() {
        if (elements == 0 || first == null)
            return true;
        else
            return false;
    }

    /**
     *
     * @param node
     * @return
     */
    public Node insert(Node node) {
        //check special case if this is the first element
        if (first == null && last == null) {
            first = node;
            last = node;
        } else {
            //not empty append to the end of the list
            last.setNext(node);
            last = node;
        }
        return node;
    }

    /**
     * Display the lnked list
     */
    public void display() {
        Node link = first;
        while (link.next() != null) {
            link.displayNode();
            link = link.next();
        }
    }

    /**
     * Main driver of the program
     * @param args
     */
    public static void main(String[] args) {
        ds.LinkedList list = new ds.LinkedList();

        String[] devices = new String[] {"iMac", "iPhone 6S Plus","iPad", "Dell", "MacAir", "MacBook Pro"};
        for (String device: devices) {
            Node n = new Node(device);
            list.insert(n);
        }
        list.display();
    }
}
