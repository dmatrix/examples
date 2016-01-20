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

            return "[ " + name + " has the key " + key + " ]";
    }
}
