import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class SolverTest {

    private Board testerSolvable2;
    private Board testerSolvable3;
    private Board testerUnsolvable3;

    @Before
    public void init() {
        int[][] blockNotSolve = new int[][]{{1, 2, 3}, {4, 5, 6}, {8, 7, 0}};
        int[][] blocks3 = new int[][]{{0, 1, 3}, {4, 2, 5}, {7, 8, 6}};
        int[][] block2 = new int[][]{{0, 1}, {3, 2}};
        testerSolvable2 = new Board(block2);
        testerSolvable3 = new Board(blocks3);
        testerUnsolvable3 = new Board(blockNotSolve);

    }

    @Test
    public void testSolution2() {
        Solver testSolver = new Solver(testerSolvable2);
        assertEquals(2, testSolver.moves());
        assertTrue(testSolver.isSolvable());

        ArrayList<Board> pathExpect = new ArrayList<>();
        pathExpect.add(testerSolvable2);

        int[][] block2 = new int[][]{{1, 0}, {3, 2}};
        pathExpect.add(new Board(block2));

        block2 = new int[][]{{1, 2}, {3, 0}};
        pathExpect.add(new Board(block2));

        int count = 0;
        for(Board boardActual: testSolver.solution()) {
            assertEquals(pathExpect.get(count), boardActual);
            count += 1;
        }
    }

    @Test
    public void testSolution3() {
        Solver testSolver = new Solver(testerSolvable3);
        assertTrue(testSolver.isSolvable());
        assertEquals(4, testSolver.moves());
    }

    @Test
    public void testUnsolvable() {
        Solver testSolver = new Solver(testerUnsolvable3);
        assertFalse(testSolver.isSolvable());
        assertEquals(0, testSolver.moves());
        assertNull(null, testSolver.solution());
    }
}
