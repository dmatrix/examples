package misc;

public class Palindrome {

    private String mString;

    /**
     * Constructor
     * @return
     */
    public Palindrome() {
        mString = null;
    }

    private char[] strip(String s) {
        StringBuffer b = new StringBuffer();
        char[] arr = s.toLowerCase().toCharArray();
        int len = arr.length;
        for (int i = 0; i < len; i++) {
            if (! Character.isLetter(arr[i]))
                continue;
            b.append(arr[i]);
        }
        return b.toString().toCharArray();
    }
    /**
     * Check if the string is a palindrom. That is, it reads the same back words
     * @return true or false
     */
    private boolean isPalindrome()  {
        char[] arr = strip(mString);
        char c;
        int idx = arr.length-1;
        int hp = idx/2;
        for (int i = 0; i < hp; i++) {
            if (arr[i] != arr[idx-i])
                return false;
        }
        return true;
    }

    /**
     * Check if the string is a Palindrome. Ignore all punction marks.
     * @param s
     * @return true or false
     */
    public boolean isPalindrome(String s) {
        mString = s;
        return isPalindrome();
    }

    /**
     * Main program that takes a list of palindromes strings or phrases
     * @param args
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java Palindorm '<string>' ... '<string>'");
            System.exit(1);
        }
        Palindrome p = new Palindrome();
        for (String arg: args) {
            System.out.println(arg + ": " + p.isPalindrome(arg));
        }
    }
}