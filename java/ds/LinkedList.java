package ds;

import java.util.List;

/**
 * A linked list implementation, which really is Queue, since elements are appended to the end, while elements
 * can be removed from the front, FIFO; however, it can also remove nodes by searching or by indexing.
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
     * Add all devices in one swoop.
     * @param devices
     * @return list if devices added
     */
    public List<Node> insertAll(String[] devices) {
        List<Node> lst = new java.util.LinkedList<Node>();
        for (String device: devices) {
            Node n = insert(new Node(device));
            lst.add(n);
        }
        return lst;
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
        if (!isEmpty()) {
            while (link != null) {
                link.displayNode();
                link = link.next();
            }
        }
    }

    /**
     * Remove a particular device from the linked list
     * @param device
     * @return
     */
    public Node remove(String device) {
        Node link = first;
        Node prev = first;
        Node found = null;
        if (!isEmpty()) {
            while (link != null) {
                if (link.equals(device)) {
                    found = link;
                    if (link == first) {
                        first = link.next();
                        break;
                    }
                    if (link == last) {
                        last = prev;
                        break;
                    } else {
                        //this breaks the chain, removes the link, and attaches chain again
                        prev.setNext(link.next());
                    }
                } else {
                    prev = link;
                    link = link.next();
                }
            }
        }
        return found;
    }

    /** Remove at Node containing the device
     * @param device
     * @return Node with device name, device, otherwise return null
     */
    public Node findNode(String device) {
        Node link = first;
        Node found = null;
        if (!isEmpty()) {
            while (link != null) {
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
        node = list.remove("iMac");
        if (node != null) {
            System.out.println("\nRemoved Node:" + node.getDevice());
        }
        list.display();
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
        //add a bunch of new devices
        devices = new String[]{"Compac Notebook", "Chrome Notebook", "iCar", "iWatch", "Fitbit HR"};
        System.out.println("\nAdding new devicies\n");
        list.insertAll(devices);
        list.display();
    }
}
