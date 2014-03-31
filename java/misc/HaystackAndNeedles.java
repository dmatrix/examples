package misc;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author jules damji
 *
 */
public class HaystackAndNeedles {

	public HaystackAndNeedles() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * 
	 * @param mHaystack A string of words
	 * @param mNeedles a String[] array of words for which we wish to compute the number of occurrences of each 
	 * element (or word) in the haystack.
	 * @throws Exception
	 */
	public static void findNeedles(String mHaystack, String[] mNeedles) throws Exception {
		// hold all unique words in a hash map. Keep a count of each occurrence of the
		// needle in the haystack.
		Map<String, Integer> needlesCount = new HashMap<String, Integer>();
		// check for null arguments
		if (mNeedles == null) {
			throw new IllegalArgumentException("Null word argument array");
		}
		// check if there are too many words
		if (mNeedles.length > 5) {
			throw new Exception ("Too Many words");
		}
		//split the Haystack string into words
		//and create a hash map. A lot faster to access map than to iterate
		//through an array of strings.
		String[] words = mHaystack.split("\\s+");
		for (String word: words) {
			Integer count = needlesCount.get(word);
			//set the initial value to 1
			if (count == null) {
				needlesCount.put(word, 1);
			// found an occurrence, increment its value
			} else {
				count++;
				needlesCount.put(word, count);
			}
		}
		//iterate over needle and fetch its value if the word exits in the haystack hash map 
		for (String needle: mNeedles) {
			System.out.print(String.format("[%s:%d]", needle, needlesCount.get(needle)));
		}
		
	}
	public static void main(String[] args) {
		String[] needles = {"prose", "who", "side"};
	
		try {
			HaystackAndNeedles.findNeedles("A Programmer who loves to write code and prose and who utilies left side and the right side of his or her brain", needles);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
