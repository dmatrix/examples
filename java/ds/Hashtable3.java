package ds;

import java.util.Scanner;

/**
 * Created by jules on 1/19/16.
 * Borrowed from http://www.newthinktank.com/2013/03/java-hash-tables-3/
 * and modified and commented.
 */
public class Hashtable3 {

    WordList[] theArray;
    int arraySize;

    // this is dictionary with definitions that we want to create and store as a hash table
    // note that keys are similar and we use chaining strategy when collision occurs
    // So effectively, the algorithm for this is O(n/k), which is really O(n), since k is a constant number,
    // and n/k results is really n. However, when N is n/k will make a difference.
    //
    public String[][] elementsToAdd = {
            { "ace", "Very good" },
            { "act", "Take action" },
            { "add", "Join (something) to something else" },
            { "age", "Grow old" },
            { "ago", "Before the present" },
            { "aid", "Help, assist, or support" },
            { "aim", "Point or direct" },
            { "air", "Invisible gaseous substance" },
            { "all", "Used to refer to the whole quantity" },
            { "amplification",
                    "Unit of measure for the strength of an electrical current" },
            { "and", "Used to connect words" }, { "ant", "A small insect" },
            { "any", "Used to refer to one or some of a thing" },
            { "ape", "A large primate" },
            { "apt", "Appropriate or suitable in the circumstances" },
            { "arc", "A part of the circumference of a curve" },
            { "are", "Unit of measure, equal to 100 square meters" },
            { "ark", "The ship built by Noah" },
            { "arm", "Two upper limbs of the human body" },
            { "art", "Expression or application of human creative skill" },
            { "ash", "Powdery residue left after the burning" },
            { "ask", "Say something in order to obtain information" },
            { "asp", "Small southern European viper" },
            { "ass", "Hoofed mammal" },
            { "ate", "To put (food) into the mouth and swallow it" },
            { "atm", "Unit of pressure" },
            { "awe", "A feeling of reverential respect" },
            { "axe", "Edge tool with a heavy bladed head" },
            { "aye", "An affirmative answer" } };

    /**
     * Hash function to compute the index into the array[hashValue]-->Wordlist->[Word]->[Word]-> ... [null]
     * @param wordToHash
     * @return computed Hashvalue or index into the array
     */
    public int stringHashFunction(String wordToHash) {

        int hashKeyValue = 0;
        for (int i = 0; i < wordToHash.length(); i++) {
            // 'a' has the character code of 97 so subtract
            // to make our letters start at 1
            int charCode = wordToHash.charAt(i) - 96;
            int hKVTemp = hashKeyValue;
            // Calculate the hash key using the 26 letters
            // plus blank
            hashKeyValue = (hashKeyValue * 27 + charCode) % arraySize;
            System.out.println("Hash Key Value " + hKVTemp
                    + " * 27 + Character Code " + charCode + " % arraySize "
                    + arraySize + " = " + hashKeyValue);
        }
        System.out.println();
        return hashKeyValue;
    }

    /**
     * constructor
     * @param size
     */
    public Hashtable3(int size) {
        arraySize = size;
        theArray = new WordList[size];
        // Fill the array with WordLists
        for (int i = 0; i < arraySize; i++) {
            theArray[i] = new WordList();
        }
        // construct the hash table and insert all the values
        addTheArray(elementsToAdd);
    }

    /**
     * Insert a new word into the Dictionary stored into an array as a hash table
     * @param newWord
     */
    public void insert(Word newWord) {
        String wordToHash = newWord.theWord;
        // Calculate the hashkey for the Word
        int hashKey = stringHashFunction(wordToHash);
        // Add the new word to the array and set
        // the key for the word
        theArray[hashKey].insert(newWord, hashKey);
    }

    /**
     * Given a word to search, lookup in the hash table, and retrieve its Word object
     * @param wordToFind
     * @return Word object in the Array of Wordlist implemented as chained words or null
     */
    public Word find(String wordToFind) {
        // Calculate the hash key for the word
        int hashKey = stringHashFunction(wordToFind);
        // at the indexed key into array, search its linked list of Word until you find the word
        Word theWord = theArray[hashKey].find(hashKey, wordToFind);
        return theWord;

    }

    /**
     * Add the two dimensional array of [word][definition]
     * @param elementsToAdd
     */
    public void addTheArray(String[][] elementsToAdd) {
        //iterate over the dictionary and insert each word
        for (int i = 0; i < elementsToAdd.length; i++) {
            String word = elementsToAdd[i][0];
            String definition = elementsToAdd[i][1];
            // Create the Word with the word name and
            // definition
            Word newWord = new Word(word, definition);
            // Add the Word to theArray
            insert(newWord);
        }
    }

    /**
     * Helper method to print the hash table
     */
    public void displayTheArray() {
        for (int i = 0; i < arraySize; i++) {
            System.out.println("theArray Index " + i);
            theArray[i].displayWordList();
        }
    }

    /**
     * Driver for the program
     * @param args
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        // Make a 11 item array that will hold words
        // and definitions
        Hashtable3 wordHashTable = new Hashtable3(11);
        String wordLookUp = "a";
        // Keep retrieve requests until x is entered
        while (!wordLookUp.equalsIgnoreCase("x")) {
            System.out.println(": ");
            wordLookUp = input.nextLine();
            // Look for the word requested and print
            // it out to screen
            System.out.println(wordHashTable.find(wordLookUp));
        }

        // Display every item in the array with
        // the index they are associated with
        wordHashTable.displayTheArray();
    }
}
