package ds;

/**
 * Created by jules on 1/19/16.
 * Borrowed from http://www.newthinktank.com/2013/03/java-hash-tables-3/
 * and modified and commented.
 */

public class Word {

    public String theWord;
    public String definition;
    public int key;
    // Reference to next Word made in the WordList
    public Word next;

    /**
     * Constructor for the Node in the linked list represented as a Word
     * @param theWord
     * @param definition
     */
    public Word(String theWord, String definition) {

        this.theWord = theWord;
        this.definition = definition;

        }

    /**
     * Override to the toString for this object
     * @return
     */
    public String toString() {
        return "key: "+ key + " word: " + theWord + " definition: " + definition;

    }
}
