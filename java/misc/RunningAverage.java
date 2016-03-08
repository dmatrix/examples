package misc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by jules on 3/8/16.
 */
public class RunningAverage {

    private int mWindow;
    private List<Integer> mWindowList;
    private int mRunnnigAvg;

    public RunningAverage (int pWindow) {
        mWindow = pWindow;
        mRunnnigAvg = 0;
        mWindowList = new ArrayList<Integer>();
    }

    /**
     * This funciton runs in o(1) time.
     * @param num
     * @return Running average
     */
    public int next(int num) {
        mRunnnigAvg = mRunnnigAvg + num;
        if (mWindowList.size() == 0) {
            mWindowList.add(num);
        } else if (mWindowList.size() > mWindow) {
            mRunnnigAvg = mRunnnigAvg - mWindowList.remove(0);
        } else {
            mWindowList.add(num);
        }
        return Math.abs(mRunnnigAvg = mRunnnigAvg / mWindowList.size());
    }

    public static void main(String[] args) {

        int window = ThreadLocalRandom.current().nextInt(10, 25);
        RunningAverage rAvg = new RunningAverage(window);

        for (int i = 0; i < window * 2; i++) {
            int num = ThreadLocalRandom.current().nextInt(15, 85);
            int avg = rAvg.next(num);
            System.out.println("[Current N = " + num + "; Running Avg = " + avg + "]");
        }
    }
}
