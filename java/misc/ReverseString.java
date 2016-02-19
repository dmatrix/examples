package misc;

public class ReverseString {
	
	private String mString;
	
	public ReverseString(String pString) {
		mString = pString;
	}

	public String reverse() {
		char[] arr = mString.toCharArray();
		char c;
		int idx = arr.length-1;
		for (int i=0; i < arr.length/2; i++) {
			c = arr[idx-i];
			arr[idx-i] = arr[i];
			arr[i] = c;
		}
		return new String(arr);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 0) {
			System.out.println("usage: java ReverseString string1 string2...stringN");
			System.exit(1);
		}
		for (String s: args) {
			ReverseString rs = new ReverseString(s);
			System.out.println(String.format("Original string=%s; Reversed string=%s", s, rs.reverse()));
		}
	}

}
