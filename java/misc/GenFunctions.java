package misc;

import ds.BNode;

import java.util.ArrayList;
import java.lang.Math;
import java.util.List;

/**
 * Created by jules on 3/6/16.
 * Some general interview questions and solutions fucntions to phone screen coding examples. By
 * no means these are comprehesnive.
 */
public class GenFunctions {

    /**Find the Kth elemement in the a binary search tree
    // build a arrayList of all nodes visited and return the Kth one
    // Notes
    // Time coomplexity:
    // 1. o, number of operations
    // 2. c, number of comparisons
    // 3. l, nuber of nested loops
    // 4. p, pointer references
    // 5. f, number of function calls
    //
    // T (n) = Sum(o + c + l + p + f)
    // Space Complexity
    // 1. v, number of variables
    // 2. d, data structures allocated and memory used
    // 3. f, function call
    //
    // S(n) = Sum (v + d + f)
    //
    */

    /**
     * Given an Binary Tree find its height, which is the longest path from the root node to its
     * leaf. You compute both paths, from the left sub tree and right sub tree and take its max value
     * @param node
     * @return int, its height.
     */
    public static int findHeight (BNode node) {
        int lch = getHeight(node, 0);
        int rch = getHeight(node.getLeftChild(), 1);

        return Math.max(lch, rch) + 1;
    }

    /**
     * Helpe function that recursivley traverses a left or right subtree tree
     * @param node
     * @param lr, 0 (means left subtree; 1 means right subtree)
     * @return length of the path, which is the sum of all edges traversed
     */
    public static int getHeight(BNode node, int lr) {
        if (node == null)
            return 0;
        else
            return (lr == 0)? getHeight(node.getLeftChild(), 0): getHeight(node.getRightChild(), 1);
    }

    /**
     * Given a BST, find the Kth largest element. Build a list of all nodes visited in in-order, which
     * will result in a sorted tree, and acces the Kth element from the begining.
     * @param node
     * @param kthElem
     * @return Kth largest element -1 if BST is empty
     */
    public static int findKthElement(BNode node, int kthElem) {

        List<Integer> lst = buildNodeList(node);
        if ( kthElem > lst.size()) {
            return -1;
        } else {
            return lst.get(kthElem);
        }

    }

    /**
     * Helper method to construct an ArrrayList in a sorted manner
     * @param node
     * @return List of integers in a List
     */
    public static List<Integer> buildNodeList(BNode node) {

        List<Integer> lst = new ArrayList<Integer>();

        if ( node != null) {
            // visit or recurse left subtree
            buildNodeList(node.getLeftChild());
            // add the node
            lst.add(node.getKey());
            //recurse the right subtree
            buildNodeList(node.getRightChild());
        }
        return lst;
    }

    /**
     * Given a BST, return the min value. Again the idea is the same as finding the Kth element in the
     * sorted array.
     * @param n
     * @return min value
     */
    public static int getMinValue(BNode n) {
        List<Integer> lst = new ArrayList<Integer>();
        if (lst.size() == 0) {
            return -1;
        }
        //return the first element from the list, since it's the smallest in the sorted list.
        return lst.get(0);
    }

    /**
     * Given a BST, return the max value. Again as above the algorithm is not dissimilar
     * @param n
     * @return max value
     */
    public static int getMaxValue(BNode n) {
        List<Integer> lst = new ArrayList<Integer>();
        if (lst.size() == 0) {
            return -1;
        }
        return lst.get(lst.size()-1);
    }

    /**
     * Trim extra spaces withouth using replace or reg Ex
     * @param str
     * @return trimmed string
     */
    public static String trimExtraSpaces(String str) {
        if (str == null) return null;
        if (str.length() == 1 || str.length() == 0) return str;
        StringBuffer b = new StringBuffer();
        String[] tokens = str.split(" ");
        for (String t: tokens) {
            if (t.length() == 0)
                continue;
            b.append(t);
            b.append(' ');
        }
        return b.toString();
    }

    /**
     * Find if a number can be expressed as an exponent of the base
     * @param N
     * @param base
     * @return exponent
     */
    public static int findExponent(int N, int base) {
        int sum = 0;
        int exp = -1;
        for (int i = 1; ; i++) {
            sum = sum * base;
            if (sum == 0) {
                sum = base;
            }
            if (sum == N) {
                if (sum == base)
                    exp = 0;
                else
                    exp = i++;
                break;
            }
            if (sum > N)
                break;
        }
        return exp;
    }
    /**
     *
     * @param args
     */
    public static void main(String[] args) {

        if (args.length < 0) {
            System.out.println("need at least one String argument");
        }
        for (String arg: args) {
            String trim = trimExtraSpaces(arg);
            System.out.println("Original string:" + arg);
            System.out.println("Trimmed string :" + trim);
        }
        int [] numbers = {25, 5, 17, 10, 21, 125, 625};
        for (int n: numbers) {
            int exp = findExponent(n, 5);
            if (exp >= 0) {
                System.out.println(n + " == " + 5 + " ^ " + exp);
            } else {
                System.out.println(n + " ==  Not expressible as an exponent of " + 5 );
            }
        }

    }
}
