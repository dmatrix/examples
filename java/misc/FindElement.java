package misc;

public class FindElement {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int[] mArray = new int[10000];
		for (int i=0; i < mArray.length;i++) {
			mArray[i] = i % 2;
		}
		for (String s: args) {
			int i = Integer.parseInt(s);
			if (hasElement(mArray, i, 0, mArray.length-1)) {
				System.out.println(String.format("Element %d in the Array", i));
			} else {
				System.out.println(String.format("Element %d not in the Array", i));
			}
		}

	}

	private static boolean hasElement(int[] array, int value, int start, int end) {
		int middle;
		if (end <= start) {
			return false;
		}
		middle = (start + end) / 2;
		if (array[middle] == value)
			return true;
		else if (array[middle] < value) {
			return hasElement(array, value, start, middle);
		} else {
			return hasElement(array, value, middle+1, end);
		}
	}

}
