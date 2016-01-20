package ds;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by jules on 1/19/16.
 * Borrowed and modified from http://www.newthinktank.com/2013/03/binary-tree-in-java/
 *
 */
public class BinaryTree {
    BNode root;

    /**
     * Constructor
     */
    public BinaryTree() {
        root = null;
    }
    /**
     * Add node traversing the tree until you find the right place for insertion.
     * @param key
     * @param name
     */
    public void addNode(int key, String name) {
        // Create a new Node and initialize it
        BNode newNode = new BNode(key, name);
        // If there is no root this becomes root
        if (root == null) {
            root = newNode;
        } else {
            // Set root as the Node we will start
            // with as we traverse the tree
            BNode focusNode = root;
            // Future parent for our new Node
            BNode parent;
            while (true) {
                // root is the top parent so we start
                // there
                parent = focusNode;
                // Check if the new node should go on
                // the left side of the parent node
                if (key < focusNode.getKey()) {
                    // Switch focus to the left child
                    focusNode = focusNode.getLeftChild();
                    // If the left child has no children
                    if (focusNode == null) {
                        // then place the new node on the left of it
                        parent.setLeftChild(newNode);
                        return; // All Done
                    }
                } else { // If we get here put the node on the right
                    focusNode = focusNode.getRightChild();
                    // If the right child has no children
                    if (focusNode == null) {
                        // then place the new node on the right of it
                        parent.setRightChild(newNode);
                        return; // All Done
                    }
                }
            }
        }
    }

    /**
     * Traverse in the inorder.
     * 1. Go down the left's nodes' children recursively until no children
     * 2. Print Node's key
     * 3. Go down the right nodes' children recursively until no children
     * Recursion is used to go to one node and
     * then go to its child nodes and so forth
     * @param focusNode, the root node sent to it
     */
    public void inOrderTraverseTree(BNode focusNode) {
        if (focusNode != null) {
            // Traverse the left node's children recursively
            inOrderTraverseTree(focusNode.getLeftChild());
            // Visit the currently focused on node
            System.out.println(focusNode);
            // Traverse the right node
            inOrderTraverseTree(focusNode.getRightChild());
        }
    }

    /**
     * Travefse in the preorder
     * 1. Print Node's key
     * 2. Go down the left's nodes' children recursively until no children
     * 3. Go down the right nodes' children recursively until no children
     * @param focusNode
     */
    public void preorderTraverseTree(BNode focusNode) {
        if (focusNode != null) {
            System.out.println(focusNode);
            preorderTraverseTree(focusNode.getLeftChild());
            preorderTraverseTree(focusNode.getRightChild());
        }
    }

    /**
     * traverse postorder
     * 1. Go down the left's nodes' children recursively until no children
     * 2. Go down the right nodes' children recursively until no children
     * 3. Print Node's key
     * @param focusNode
     * @param focusNode
     */
    public void postOrderTraverseTree(BNode focusNode) {
        if (focusNode != null) {
            postOrderTraverseTree(focusNode.getLeftChild());
            postOrderTraverseTree(focusNode.getRightChild());
            System.out.println(focusNode);
        }
    }

    /**
     * Find if the key exists in the tree. This is 0(N/k), where k is the heigth of the tree.
     * @param key
     * @return return Bnode if found otherwise null
     */
    public BNode findNode(int key) {
        // Start at the top of the tree
        BNode focusNode = root;
        // check if empty tree
        if (root == null) {
            return null;
        }
        // While we haven't found the Node
        // keep looking
        while ( focusNode.getKey() != key)  {
            // If we should search to the left
            if (key < focusNode.getKey()) {
                // Shift the focus Node to the left child
                focusNode = focusNode.getLeftChild();
            } else {
                // Shift the focus Node to the right child
                focusNode = focusNode.getRightChild();
            }
            // The node wasn't found
            if (focusNode == null)
                return null;
        }
        return focusNode;
    }

    public static void main(String[] args) {
        BinaryTree theTree = new BinaryTree();
        //generate random numbers so we don't get an unbalanced tree
        int maxKeys = 15;
        for (int i = 0 ; i < maxKeys; i++) {
            int key = ThreadLocalRandom.current().nextInt(1, 85);
            //only insert if no key already exits
            if (theTree.findNode(key) == null) {
                theTree.addNode(key, Integer.toString(key));
            } else {
                // got a match don't count that key.
                System.out.println("Key "+ key + " in already in the tree. Not added");
                maxKeys++;
            }
        }
        // Different ways to traverse binary trees
        System.out.println("In-order Traversal: ");
        theTree.inOrderTraverseTree(theTree.root);
        System.out.println("\nPre-order Traversal: ");
        theTree.preorderTraverseTree(theTree.root);
        System.out.println("\nPost-order Traversal: ");
        theTree.postOrderTraverseTree(theTree.root);
        // Add and Find the node with key 75
        theTree.addNode(75, "75");
        System.out.println("\nNode with the key 75");
        System.out.println(theTree.findNode(75));
    }
}
