package misc;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class LongestSubstring {

	private Map<Integer, String> mMap;
	private String mString;
	
	public LongestSubstring(String pString) {
		mString = pString;
		mMap = new HashMap<Integer, String>();
	}
	
	public String findLongestSubString() {
		
		char[] chars = mString.toCharArray();
		StringBuffer bf = new StringBuffer();
		for (int i=1; i<chars.length; i++) {
			bf.append(chars[i]);
			String s = bf.toString();
			if (mString.equalsIgnoreCase(s)) continue;
			if (mString.indexOf(s, 0) >= 0) {
				mMap.put(s.length(), s);
			}
		}
		Set<Integer> set = new TreeSet<Integer>(mMap.keySet());
		String lsubString = null;
		for (int i: set) {
			lsubString = mMap.get(i);
		}
		return lsubString;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 0) {
			System.out.println("java -cp . LongestSubstring string string ...string");
			System.exit(1);
		}
		for (String s: args) {
			LongestSubstring lsub = new LongestSubstring(s);
			System.out.println(String.format("Original String=%s; Longest Substring=%s",s, lsub.findLongestSubString()));
		}

	}

}
