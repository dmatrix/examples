package io;
/**
 * Created by IntelliJ IDEA.
 * User: jules
 * Date: Jul 24, 2008
 * Time: 1:04:46 PM
 * Java Examples
 */
import java.io.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PipedJava extends Object {

    public static void writeStuff(OutputStream rawout) {
        try {
                DataOutputStream out = new DataOutputStream(new BufferedOutputStream(rawout));
                int[] data = { 82, 105, 99, 104, 97, 114, 100, 32,
                        72, 121, 100, 101, 33, 85, 76, 83, 1239, 10, 0 };
                for (int i = 0; i < data.length; i++) {
                	System.out.println("just wrote " + data[i]);
                    out.writeInt(data[i]);
                }
                out.flush();
                out.close();
                System.out.println("Wrote all data to the pipe");
        } catch (IOException x) {
            x.printStackTrace();
        }
    }

    public static void readStuff(InputStream rawinput) {
        try {
            DataInputStream in = new DataInputStream(new BufferedInputStream(rawinput));
            boolean eof = false;
            int sum = 0;
            while (! eof) {
                try {
                    int i = in.readInt();
                    System.out.println("just read " + i);
                    sum = sum + i;
                } catch (EOFException ex) {
                    eof = true;
                }
            }
            in.close();
            System.out.println(String.format("Read all data from the pipe:Sum=%d", sum));
        } catch (IOException x) {
            x.printStackTrace();
        }
    }
    public static void main (String[] args) {
        try {
        	ExecutorService threadExecutor = Executors.newFixedThreadPool(1);
            final PipedOutputStream pout = new PipedOutputStream();
            final PipedInputStream pin = new PipedInputStream(pout);
            final CountDownLatch latch = new CountDownLatch(1);
            Runnable runA = new Runnable() {
                public void run() {
                    writeStuff(pout);
                    latch.countDown();
                }
            };
            Runnable runB = new Runnable() {
                public void run() {
                	try {
						latch.await();
						readStuff(pin);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
            };
            threadExecutor.execute(runA);
            threadExecutor.execute(runB);
            threadExecutor.shutdown();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
