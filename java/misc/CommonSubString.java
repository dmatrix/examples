package misc;

/**
 * Created by jules on 3/2/16.
 */
public class CommonSubString {

    private String mFirst;
    private String mSecond;

    /**
     * Constructor that takes two strings first <= second
     * @param pFirst
     * @param pSecond
     */
    public CommonSubString(String pFirst, String pSecond) {
        mFirst = pFirst;
        mSecond = pSecond;
    }

    /**
     * method that returns the substring
     * @return
     */
    public String findCommonSubString() {
            if (mFirst == null || mFirst.length() == 0) {
                return null;
            }
            int fLen = mFirst.length();
            if (mFirst.length() >= mSecond.length()) {
                return null;
            }
            int sLen = mSecond.length();
            // two pointers that walk up the length of each string
            int fp = 0;
            int sp = 0;
            StringBuffer b = new StringBuffer();
            while (fp < fLen) {
                int fpBegin = fp;
                // iterate until either first pointer reaches the end and if characters within each
                // string match
                // append the matching characters to the buffer
                while ( fp < fLen && sp < sLen && (mFirst.charAt(fp) == mSecond.charAt(sp))) {
                    b.append(mFirst.charAt(fp));
                    fp++; sp++;
                }
                //if are at the end of second string break
                if (sp == mSecond.length()) {
                    break;
                }
                //advance the first pointer
                fp = fpBegin + 1;
            }
            return b.toString();
        }
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("need two strings as arguments");
            System.out.println();
        }
        CommonSubString cs = new CommonSubString(args[0], args[1]);
        System.out.println(cs.findCommonSubString());
    }
}
