package misc;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jules on 3/2/16.
 */
public class CommonSubString {

    private String mFirst;
    private String mSecond;

    public CommonSubString(String pFirst, String pSecond) {
        mFirst = pFirst;
        mSecond = pSecond;
    }

    public String findCommonSubString() {
        Set<Character> subSet = new HashSet<Character>();
        int idxFirst = 0;
        int idxSecond = 0;
        int fLen = mFirst.length();
        int sLen = mSecond.length();
        for (int i = idxFirst ; (idxFirst < fLen && i < fLen); i++) {
            for (int j = idxSecond ; (idxSecond < sLen && j < sLen); j++) {
                if (mFirst.charAt(idxFirst) == mSecond.charAt(idxSecond)) {
                    subSet.add(mFirst.charAt(i));
                    idxFirst = i;
                    idxSecond = j;
                }
            }
        }
        if (subSet.isEmpty()) {
            return null;
        } else {
            return subSet.toString();
        }

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
