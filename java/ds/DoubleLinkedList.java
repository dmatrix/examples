package ds;

/**
 * Created by jules on 1/21/16.
 */
public class DoubleLinkedList {

    private Node first;
    private Node last;
    private int elements;

    /**
     * Constructor for the Double linked list
     */
    public DoubleLinkedList() {
        first = null;
        last = null;
        elements = 0;
    }

    /**
     * getter
     * @return
     */
    public Node getFirst() { return first; }

    /**
     * getter
     * @return
     */
    public Node getLast() { return last; }

    /**
     * getter
     * @return
     */
    public int getElements() { return elements; }
    /**
     * Insert node at the head of the list
     * @param node
     */
    public void insertAtHead(Node node) {
        // linked list is empty
        if (first == null) {
            first = node;
            last = node;
        } else {
            first.setPrevious(node);
            node.setNext(first);
            first = node;
        }
        elements++;
    }

    /**
     * Insert at the tail or end of the list
     * @param node
     */
    public void insertAtTail(Node node) {
        // corner case 1: linked list is empty
        if (first == null ) {
            first = node;
            last = node;
            elements++;
            return;
        }
        //since we keep track of the last node we don't need to traverse
        last.setNext(node);
        node.setPrevious(last);
        last = node;
        elements++;
        /**
        // traverse to the end and adjust the pointers
        Node current = first;
        while (current.next() != null) {
            current = current.next();
        }
        current.setNext(node);
        node.setPrevious(current);
        last = node;
         */
    }

    /**
     * Print recursivley starting at the last node
     * @param node, the reference to the last node
     */
    public void printReverseOrder(Node node) {

        if (node == null) {
            return;
        } else {
            node.displayNode();
            printReverseOrder(node.previous());
        }
    }

    /**
     * Print recursively in the normal order
     * @param node
     */
    public void print(Node node) {
        if (node == null) {
            return;
        } else {
            node.displayNode();
            print(node.next());
        }
    }

    /**
     * Driver for the doubly linked list
     * @param args
     */
    public static void main(String[] args) {
        DoubleLinkedList dblList = new DoubleLinkedList();
        String[] devices = new String[]{"A", "B", "C", "D"};
        for (String device : devices) {
            Node n = new Node(device);
            dblList.insertAtHead(n);
        }
        System.out.println("Number of elements: " + dblList.getElements());
        dblList.print(dblList.getFirst());
        System.out.println("\nPrinting in reverse order\n");
        dblList.printReverseOrder(dblList.getLast());
        dblList.insertAtHead(new Node("H"));
        dblList.insertAtTail(new Node("L"));
        System.out.println("Number of elements: " + dblList.getElements());
        dblList.print(dblList.getFirst());
        System.out.println("\nPrinting in reverse order\n");
        dblList.printReverseOrder(dblList.getLast());
    }
}
