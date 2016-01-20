package ds;

/**
 * Created by jules on 1/19/16.
 * Borrowed from http://www.newthinktank.com/2013/03/java-hash-tables-3/
 * and modified and commented.
 */
public class WordList {

        // Reference to first Link in list
        // The last Link added to the LinkedList

        public Word firstWord = null;

    /**
     * Function to insert the word in the chain of words at the hashed valued index in the array
     * Words are added to the front of the list, so in effect they are in descending order of their
     * @param newWord
     * @param hashKey
     */
    public void insert(Word newWord, int hashKey) {
            Word previous = null;
            Word current = firstWord;
            newWord.key = hashKey;
            while (current != null && newWord.key > current.key) {
                previous = current;
                current = current.next;
            }
            if (previous == null)
                firstWord = newWord;
            else
                previous.next = newWord;
            newWord.next = current;
        }

    /**
     * Display the Word linked list
     */
    public void displayWordList() {
            Word current = firstWord;
            while (current != null) {
                System.out.println(current);
                current = current.next;
            }
        }

    /**
     * Given to word and hashkey find the word in the indexex array's linked list of words
     * @param hashKey
     * @param wordToFind
     * @return Word if found otherwise null
     */
        public Word find(int hashKey, String wordToFind) {

            Word current = firstWord;

            // Search for key, but stop searching if
            // the hashKey < what we are searching for
            // Because the list is sorted this allows
            // us to avoid searching the whole list
            while (current != null && current.key <= hashKey) {
                // found the cord looking for in the word list
                if (current.theWord.equals(wordToFind))
                    return current;
                current = current.next;
            }
            // none found so return null
            return null;
        }
}

