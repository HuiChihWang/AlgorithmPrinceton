import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class BoardTest {

    private Board tester2;
    private Board tester3;
    private Board testerGoal3;

    @Before
    public void init() {
        int[][] blocks3 = new int[][]{{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        int[][] goal3 = new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
        int[][] block2 = new int[][]{{1, 3}, {0, 2}};
        tester2 = new Board(block2);
        tester3 = new Board(blocks3);
        testerGoal3 = new Board(goal3);
    }

    @Test
    public void testDimension() {
        assertEquals(3, tester3.dimension());
        assertEquals(2, tester2.dimension());
    }

    @Test
    public void testHammingDist() {
        assertEquals(5, tester3.hamming());
        assertEquals(0, testerGoal3.hamming());
        assertEquals(2, tester2.hamming());
    }

    @Test
    public void testManhattan() {
        assertEquals(10, tester3.manhattan());
        assertEquals(0, testerGoal3.manhattan());
        assertEquals(3, tester2.manhattan());
    }

    @Test
    public void testisGoal() {
        assertFalse(tester3.isGoal());
        assertTrue(testerGoal3.isGoal());
        assertFalse(tester2.isGoal());
    }

    @Test
    public void testEqual() {
        assertEquals(tester3, tester3);
        assertNotEquals(tester3,tester2);
        assertNotEquals(tester3, testerGoal3);
    }

    @Test
    public void testTwin() {
//        StdOut.println(tester3.twin());
//        StdOut.println(tester3);

        assertEquals(2, testerGoal3.twin().hamming());
        assertEquals(tester3.twin(), tester3.twin());
    }

    @Test
    public void testNeighbor() {
        ArrayList<Board> neighborsTrue = new ArrayList<>();

        int[][] neighborTrue = new int[][]{{8, 1, 3}, {0, 4, 2}, {7, 6, 5}};
        neighborsTrue.add(new Board(neighborTrue));

        neighborTrue = new int[][]{{8, 1, 3}, {4, 2, 0}, {7, 6, 5}};
        neighborsTrue.add(new Board(neighborTrue));

        neighborTrue = new int[][]{{8, 0, 3}, {4, 1, 2}, {7, 6, 5}};
        neighborsTrue.add(new Board(neighborTrue));

        neighborTrue = new int[][]{{8, 1, 3}, {4, 6, 2}, {7, 0, 5}};
        neighborsTrue.add(new Board(neighborTrue));

        int count = 0;
        for(Board neighbor: tester3.neighbors()) {
            assertEquals(neighborsTrue.get(count), neighbor);
            count += 1;
        }
    }



}
