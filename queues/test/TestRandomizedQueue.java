import org.junit.Test;
import static org.junit.Assert.*;

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

}
