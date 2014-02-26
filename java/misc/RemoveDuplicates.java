package misc;

import java.util.HashSet;
import java.util.Set;

public class RemoveDuplicates {

	private String mString;
	
	public RemoveDuplicates(String pString) {
		mString = pString;
	}
	
	public String removeDuplicates() {
		
		StringBuffer b = new StringBuffer();
		Set<Character> set = new HashSet<Character>();
		char[] arr = mString.toCharArray();
		for (int i = 0; i < arr.length; i++) {
			char c = arr[i];
			if (c == ' ') {
				b.append(c);
				continue;
			}
			if (!set.contains(c)) {
				set.add(c);
				b.append(c);
			}
		}
		return b.toString();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 0) {
			System.out.println("usage: java RemoveDuplicates string1 string2...stringN");
			System.exit(1);
		}
		for (String s: args) {
			RemoveDuplicates rs = new RemoveDuplicates(s);
			System.out.println(String.format("Original string=%s; Duplicates Removed string=%s", s, rs.removeDuplicates()));
		}

	}

}
