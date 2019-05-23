import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class TestRandomizedQueue {

    @Test
    public void testBasic() {
        RandomizedQueue<Integer> tester = new RandomizedQueue<>();
        assertEquals(0, tester.size());
        assertTrue(tester.isEmpty());
    }

    @Test
    public void testEnqueueDequeue() {
        RandomizedQueue<Integer> tester = new RandomizedQueue<>();
        HashSet<Integer> testCase = new HashSet<>();

        for (int i = 0; i < 1000; ++i) {
            tester.enqueue(i);
            testCase.add(i);
        }
        assertEquals(1000, tester.size());

        for(int i = 0; i < 1000; ++i) {
            Integer item = tester.dequeue();
            testCase.remove(item);
        }

        assertTrue(testCase.isEmpty());
        assertTrue(tester.isEmpty());
    }

    @Test
    public void testSample() {
        RandomizedQueue<Integer> tester = new RandomizedQueue<>();
        HashSet<Integer> testCase = new HashSet<>();

        for (int i = 0; i < 1000; ++i) {
            tester.enqueue(i);
            testCase.add(i);
            assertTrue(testCase.contains(tester.sample()));
        }
    }

    @Test
    public void testIterator() {
        RandomizedQueue<Integer> tester = new RandomizedQueue<>();
        HashSet<Integer> testCase = new HashSet<>();
        Random randGen = new Random();

        for (int i = 0; i < 2000; ++i) {
            Integer item = randGen.nextInt();
            tester.enqueue(item);
            testCase.add(item);
        }

        for (int i = 0; i < 1000; ++i) {
            Integer item = tester.dequeue();
            testCase.remove(item);
        }

        for (Integer item: tester) {
            assertTrue(testCase.contains(item));
        }
    }

    @Test
    public void testParallelIterator() {
        RandomizedQueue<Integer> tester = new RandomizedQueue<>();
        ArrayList<Integer> result1 = new ArrayList<>();
        ArrayList<Integer> result2 = new ArrayList<>();

        for (int i = 0; i < 10; ++i) {
            tester.enqueue(i);
        }

        for (Integer num: tester) {
            result1.add(num);
        }

        for (Integer num: tester) {
            result2.add(num);
        }

        StdOut.println(Arrays.toString(result1.toArray()));
        StdOut.println(Arrays.toString(result2.toArray()));
        assertNotEquals(result1, result2);
    }

    @Test
    public void testNestedIterator() {
        RandomizedQueue<Integer> tester = new RandomizedQueue<>();

        for (int i = 0; i < 10; ++i) {
            tester.enqueue(i);
        }

        for(Integer num1: tester) {
            for (Integer num2: tester) {
                StdOut.printf("%d %d", num1, num2);
                StdOut.println();
            }
        }

    }




}
