package misc;

import java.io.*;
import java.util.Arrays;

/**
 * Created by jules on 3/2/16.
 */
public class StringsSerDer {

    /**
     * Serialize a String[]
     * @param pStrings
     * @return
     * @throws IOException
     */
    public byte[] serialize(String[] pStrings ) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new ObjectOutputStream(out).writeObject(pStrings);

        return out.toByteArray();
    }

    /**
     * Deserialize an object
     * @param arr
     * @return String[]
     */
    public String[] deserialize(byte[] arr) {

        String[] arrays = null;

        ByteArrayInputStream in;
        in = new ByteArrayInputStream(arr);
        try {
            arrays = (String[]) new ObjectInputStream(in).readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return arrays;

    }

    /**
     * Main program that uses the serializer and deseriaizer
     * @param args
     */
    public static void main(String[] args) {

        StringsSerDer serDer = new StringsSerDer();
        try {
            byte[] serBytes = serDer.serialize(args);
            String[] array= serDer.deserialize(serBytes);
            for (String s: array)
                System.out.println(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
