package ds;

/**
 * Created by jules on 1/12/16.
 */
public class Node {
    private String device;
    private Node next;
    private Node prev;

    /**
     * Constructor for the Node
     * @param pDevice
     */
    public Node(String pDevice) {
        device = pDevice;
        next = null;
        prev = null;

     }

    /**
     * Override equal method
     * @param n
     * @return true or false
     */
    public boolean equals(Node n) {
        return device.equals(n.getDevice());
    }

    /**
     * Another way to check if Node is equal
     * @param dev
     * @return true or false
     */
    public boolean equals(String dev) {
        return device.equals(dev);
    }
    /**
     * override toString for the object Node
     * @return
     */
    public String toString() {
        return device;
    }

    /**
     * getter
     * @return device
     */
    public String getDevice() { return device; }

    /**
     * Getter
     * @return ref to the next node in the list
     */
    public Node next() { return next; }

    public Node previous() { return prev; }

    /**
     * setter
     * @param n
     */
    public void setNext( Node n) { next = n;}

    /**
     * setter
     * @param n
     */
    public void setPrevious(Node n) { prev = n; }

    public void displayNode() {
        System.out.println("Node = " + getDevice());
        if (next() == null)
            System.out.println("  Next -> null");
        else
            System.out.println("  Next -> " + next().toString());

        if (previous() == null)
            System.out.println(" Prev -> null");
        else
            System.out.println(" Prev -> " + previous().toString());
    }
}
