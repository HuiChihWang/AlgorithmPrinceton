import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {

    public static void main(String[] args) {
        int printNum = Integer.parseInt(args[0]);
        RandomizedQueue<String> stringRandomQueue = new RandomizedQueue<>();

        while (!StdIn.isEmpty()) {
            stringRandomQueue.enqueue(StdIn.readString());
        }

        for (int idx = 0; idx < printNum; ++idx) {
            StdOut.println(stringRandomQueue.dequeue());
        }
    }
}
