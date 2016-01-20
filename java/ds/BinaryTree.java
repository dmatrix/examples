package ds;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by jules on 1/19/16.
 * Borrowed and modified from http://www.newthinktank.com/2013/03/binary-tree-in-java/
 *
 */
public class BinaryTree {
    BNode root;
    int numOfNodes = 0;

    /**
     * Constructor
     */
    public BinaryTree() {
        root = null;
    }

    /** getter
     *
     * @return number of nodes in the tree
     */
    public int getNumOfNodes() { return numOfNodes; }

    public BNode getRoot() { return root; }
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
            numOfNodes++;
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
                        numOfNodes++;
                        return; // All Done
                    }
                } else { // If we get here put the node on the right
                    focusNode = focusNode.getRightChild();
                    // If the right child has no children
                    if (focusNode == null) {
                        // then place the new node on the right of it
                        parent.setRightChild(newNode);
                        numOfNodes++;
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

    /**
     * Given a key, locate the appropriate node to replace and do all the magic of reassigning and replacing the right
     * and letf child nodes
     * @param key
     * @return true if deleted, false otherwise
     */
    public boolean remove(int key) {
        // Start at the top of the tree
        BNode focusNode = root;
        BNode parent = root;

        // special case if the tree is empty
        if (focusNode == null) {
            return false;
        }
        // When searching for a Node this will
        // tell us whether to search to the
        // right or left
        boolean isItALeftChild = true;
        // While we haven't found the Node
        // keep looking
        while (focusNode.getKey() != key) {
            parent = focusNode;
            // If we should search to the left
            if (key < focusNode.getKey()) {
                isItALeftChild = true;
                // Shift the focus Node to the left child
                focusNode = focusNode.getLeftChild();
            } else {
                // Greater than focus node so go to the right
                isItALeftChild = false;
                // Shift the focus Node to the right child
                focusNode = focusNode.getRightChild();
            }
            // The node wasn't found
            if (focusNode == null)
                return false;
        }
        // If Node doesn't have children delete it
        if (focusNode.getLeftChild() == null && focusNode.getRightChild() == null) {
            // If root delete it
            if (focusNode == root)
                root = null;
                // If it was marked as a left child
                // of the parent delete it in its parent
            else if (isItALeftChild)
                parent.setLeftChild(null);
                // Vice versa for the right child
            else
                parent.setRightChild(null);
        }
        // If no right child
        else if (focusNode.getRightChild() == null) {
            if (focusNode == root)
                root = focusNode.getLeftChild();
                // If focus Node was on the left of parent
                // move the focus Nodes left child up to the
                // parent node
            else if (isItALeftChild)
                parent.setLeftChild(focusNode.getLeftChild());
                // Vice versa for the right child
            else
                parent.setRightChild(focusNode.getLeftChild());
        }
        // If no left child
        else if (focusNode.getLeftChild() == null) {
            if (focusNode == root)
                root = focusNode.getRightChild();
                // If focus Node was on the left of parent
                // move the focus Nodes right child up to the
                // parent node
            else if (isItALeftChild)
                parent.setLeftChild(focusNode.getRightChild());
                // Vice versa for the left child
            else
                parent.setRightChild(focusNode.getRightChild());

        }
        // Two children so I need to find the deleted nodes
        // replacement
        else {
            BNode replacement = getReplacementNode(focusNode);
            // If the focusNode is root replace root
            // with the replacement
            if (focusNode == root)
                root = replacement;
                // If the deleted node was a left child
                // make the replacement the left child
            else if (isItALeftChild)
                parent.setLeftChild(replacement);
                // Vice versa if it was a right child
            else
                parent.setRightChild(replacement);
            replacement.setLeftChild(focusNode.getLeftChild());
        }
        numOfNodes--;
        return true;
    }

    /**
     * Find the replacement node
     * @param replacedNode
     * @return BNode or null;
     */
    public BNode getReplacementNode(BNode replacedNode) {
        BNode replacementParent = replacedNode;
        BNode replacement = replacedNode;

        BNode focusNode = replacedNode.getRightChild();
        // While there are no more left children
        while (focusNode != null) {
            replacementParent = replacement;
            replacement = focusNode;
            focusNode = focusNode.getLeftChild();
        }
        // If the replacement isn't the right child
        // move the replacement into the parent's
        // leftChild slot and move the replaced nodes
        // right child into the replacements rightChild
        if (replacement != replacedNode.getRightChild()) {
            replacementParent.setLeftChild(replacement.getRightChild());
            replacement.setRightChild(replacedNode.getRightChild());
        }
        return replacement;
    }

    /**
     * Driver for the program
     * @param args
     */
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
        System.out.println("Total Number of Nodes:" + theTree.getNumOfNodes());
        System.out.println("Root key: "+ theTree.getRoot().getKey());
        theTree.inOrderTraverseTree(theTree.root);
        System.out.println("\nPre-order Traversal: ");
        theTree.preorderTraverseTree(theTree.root);
        System.out.println("\nPost-order Traversal: ");
        theTree.postOrderTraverseTree(theTree.root);
        // Add and Find the node with key 75
        theTree.addNode(75, "75");
        System.out.println("\nNode with the key 75");
        System.out.println(theTree.findNode(75));
        //remove root
        System.out.println("Removing the root key: "+ theTree.getRoot().getKey());
        theTree.remove(theTree.getRoot().getKey());
        System.out.println("In-order Traversal: ");
        System.out.println("Root key: "+ theTree.getRoot().getKey());
        theTree.inOrderTraverseTree(theTree.root);
        theTree.remove(75);
        System.out.println("In-order Traversal: ");
        theTree.inOrderTraverseTree(theTree.root);

        for (int i = 0 ; i < maxKeys; i++) {
            int key = ThreadLocalRandom.current().nextInt(1, 85);
            //only delete if key  exits
            if (theTree.findNode(key) != null) {
                theTree.remove(key);
                System.out.println("Key "+ key + " Deleted!.");
            } else {
                // got a match don't count that key.
                System.out.println("Key "+ key + " Not found.");
            }
        }
        System.out.println("In-order Traversal: ");
        System.out.println("Total Number of Nodes:" + theTree.getNumOfNodes());
        System.out.println("Root key: "+ theTree.getRoot().getKey());
        theTree.inOrderTraverseTree(theTree.root);
    }
}
