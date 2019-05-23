import org.junit.Test;
import static org.junit.Assert.*;

public class TestDeque {
    @Test
    public void testBasic() {
        Deque<Integer> tester = new Deque<>();
        assertEquals(0, tester.size());
        assertTrue(tester.isEmpty());
    }

    @Test
    public void testAddRemove() {
        Deque<Integer> tester = new Deque<>();

        for (int i = 0; i < 100; ++i) {
            tester.addFirst(i);
        }

        for (int i = 0 ; i < 100; ++i) {
           assertEquals(Integer.valueOf(i), tester.removeLast());
        }

        for (int i = 0; i < 100; ++i) {
            tester.addLast(i);
        }

        for (int i = 0 ; i < 100; ++i) {
            assertEquals(Integer.valueOf(i), tester.removeFirst());
        }
    }

    @Test
    public void testIterator() {
        Deque<Integer> tester = new Deque<>();
        int[] testCase = {10, 5, 6, 8, 10, 22, 55};

        for (int number: testCase) {
            tester.addLast(number);
        }

        int count = 0;
        for(Integer num: tester) {
            assertEquals(testCase[count], (int) num);
            count += 1;
        }

    }


}
