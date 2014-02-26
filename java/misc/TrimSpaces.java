package misc;

public class TrimSpaces {

	private String mString;
	
	public TrimSpaces(String pString) {
		mString = pString;
	}
	
	public String trim() {
		char[] arr = mString.toCharArray();
		StringBuffer b = new StringBuffer();
		boolean front = false, back = false;
		int frontIndex = 0, len = arr.length, backIndex = len-1;
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
		for (int i = frontIndex; i <= backIndex; i++) {
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
