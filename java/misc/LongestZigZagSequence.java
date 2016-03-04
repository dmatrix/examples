package misc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jules on 3/3/16.
 */
public class LongestZigZagSequence {

    public int sequence(int[] sequence) {
        List<Integer> pLst = new ArrayList<Integer>();
        List<Integer> nLst = new ArrayList<Integer>();
        int fIdx = 0;
        int sIdx = 1;
        int size = sequence.length;

        for (int i = fIdx; sIdx < size; fIdx++, sIdx++) {
            // check positive numbers
           if (sIdx % 2 != 0 && (sequence[sIdx] - sequence[fIdx] > 0)) {
                   pLst.add(1);
           }
            // check for negative difference
            if (sIdx % 2 == 0 && (sequence[sIdx] - sequence[fIdx] < 0)) {
                    nLst.add(-1);
            }
        }
        return pLst.size() + nLst.size();

    }

    public static void main(String[] args) {

        int[] seqArray= new int[args.length];
        LongestZigZagSequence seq = new LongestZigZagSequence();
        for (int i = 0; i< seqArray.length; i++) {
            seqArray[i] = Integer.valueOf(args[i]);
        }
        int longestSeq = seq.sequence(seqArray);

        System.out.println(longestSeq);
    }
}
