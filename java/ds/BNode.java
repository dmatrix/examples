package ds;

/**
 * Created by jules on 1/19/16.
 * Borrowed and modified from http://www.newthinktank.com/2013/03/binary-tree-in-java/
 */
public class BNode {
        private int key;
        private String name;

        private BNode leftChild;
        private BNode rightChild;

    /**
     * Constructors
     * @param key
     * @param name
     */
    BNode(int key, String name) {
            this.key = key;
            this.name = name;
     }

    /**
     * Getter
     * @return Bnode
     */
    public BNode getLeftChild() { return leftChild; }

    /**
     * Setter for the left child node
     * @param pLeftChild
     */
    public void setLeftChild(BNode pLeftChild) {
        leftChild = pLeftChild;
    }

    /**
     * Setter for the righ child node
     * @param pRightChild
     */
    public void setRightChild(BNode pRightChild) {
        rightChild = pRightChild;
    }
    /**
     * Getter
     * @return BNode
     */
    public BNode getRightChild() { return rightChild; }

    /**
     * Getter
     * @return key
     */
    public int getKey() { return key; }

    /**
     * Getter
     * @return name
     */
    public String getName() { return name; }
    /**
     *
     * @return
     */
    public String toString() {

            StringBuffer b = new StringBuffer();
            b.append("[Key="); b.append(key);
            if (leftChild == null) {
                b.append(";leftChild=null");
            } else {
                b.append(";leftChild=" + leftChild.getKey());
            }
            if (rightChild== null) {
                b.append(";rightChild=null");
            } else {
                b.append(";rightChild=" + rightChild.getKey());
            }
            b.append("]");
            return b.toString();
    }
}
