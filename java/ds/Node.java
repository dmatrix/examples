package ds;

/**
 * Created by jules on 1/12/16.
 */
public class Node {

        private String device;
        private Node next;

        public Node(String pDevice) {

            device = pDevice;
            next = null;

        }
        public String toString() {
            return device;
        }
        public String getDevice() { return device; }
        public Node next() { return next; }
        public void setNext( Node n) { next = n;}
        public void displayNode() {
            System.out.println("Node = " + getDevice());
            System.out.println("    Next -> " + next().toString());
        }
}
