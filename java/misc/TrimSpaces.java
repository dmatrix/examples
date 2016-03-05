package misc;

public class TrimSpaces {

	private String mString;
	
	public TrimSpaces(String pString) {
		mString = pString;
	}

	/**
	 * trim front, embedded and trailing spaces
	 * @return
     */
	public String trim() {
		char[] arr = mString.toCharArray();
		StringBuffer b = new StringBuffer();
		/** flags for where to stop for the front and back
		 * set front to true when first non-space is encountered.
		 * set back to true when the first non-space characers is encountered from the back
		 */
		boolean front = false, back = false;
		// keep track of the front and back indexes to keep track
		int frontIndex = 0, len = arr.length, backIndex = len-1;
		//since we are starting both from the front and back only traverse to the midpoint
		for (int i = 0; i < len/2; i++) {
			if (front && back)
				break;
			if (!front && arr[i] != ' ') {
				frontIndex=i;
				front = true;
			}
			if (!back && arr[backIndex-i] != ' ') {
				backIndex=backIndex-i;
				back=true;
			}
		}
		//copy the array from the frontIndex to the backIndex and if there are embedded spaces remove them
		for (int i = frontIndex; i <= backIndex; i++) {
			if (arr[i] != ' ')
				b.append(arr[i]);
		}

		return b.toString();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("usage: java cp .  TrimSpaces string string ... string");
			System.exit(1);
		}
		for (String s: args) {
			TrimSpaces d = new TrimSpaces(s);
			String trim = d.trim();
			System.out.println(String.format("Original=--%s--; Trimmed=--%s--", s, trim));
			System.out.println(String.format("Original Length =%d; Trimmed Lengthr=%d", s.length(), trim.length()));
		}
	}

}
