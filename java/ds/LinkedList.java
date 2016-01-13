package ds;

/**
 * A linked list implementation, which really is Queue, since elments are appended to the end, while elements
 * are removed from the front. FIFO
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
     * Return number of elements in the list
     * @return elments in the list
     */
    public int size() { return elements; }

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
        elements++;
        return node;
    }

    /**
     * Remove elment from the front of th list.
     * @return
     */
    public Node remove() {
        Node node = getFirst();
        if (!isEmpty()) {
            first = first.next();
            elements--;
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
     * @param device
     * @return Node with device name, device, otherwise return null
     */
    public Node findNode(String device) {
        Node link = first;
        Node found = null;
        if (!isEmpty()) {
            while (link.next() != null) {
                if (link.equals(device)) {
                    found = link;
                    break;
                } else {
                    link = link.next();
                }
            }
        }
        return found;
    }

    /**
     * Main driver of the program
     * @param args
     */
    public static void main(String[] args) {
        ds.LinkedList list = new ds.LinkedList();

        String[] devices = new String[]{"iMac", "iPhone 6S Plus", "iPad", "Dell", "MacAir", "MacBook Pro", "IBM NoteBook", "Windows Surface"};
        for (String device : devices) {
            Node n = new Node(device);
            list.insert(n);
        }
        list.display();
        // find a node
        Node node = list.findNode("iPad");
        if (node != null) {
            System.out.println("\nFound Node:" + node.toString());
        } else {
            System.out.println("\nNot Found Node");
        }
        int size = list.size();
        if (size > 2) {
            for (int i = 1; i <= size - 2; i++) {
                Node n = list.remove();
                if (n != null) {
                    System.out.println("\nRemoved Node:" + n.toString() + "\n");
                } else {
                    System.out.println("Linked List is Empty!");
                }
                list.display();
            }
            list.display();
        }
    }
}
