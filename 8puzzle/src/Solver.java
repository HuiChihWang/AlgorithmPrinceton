import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.List;

public class Solver {
    private Board initial;
    private Board initialFlip;

    private List<Board> pathHolder;
    private boolean isSolved;

    public Solver(Board initial) {
        checkValidBoard(initial);
        this.initial = initial;
        initialFlip = initial.twin();

        solveMinMoveByAstar();
    }

    public boolean isSolvable() {
        return isSolved;
    }

    public int moves() {
        return isSolved ? pathHolder.size() - 1 : 0;
    }

    public Iterable<Board> solution() {
        return pathHolder;
    }

    private void solveMinMoveByAstar() {
        pathHolder = new ArrayList<>();

        MinPQ<BoardPQNode> stateHolder = new MinPQ<>();
        MinPQ<BoardPQNode> stateHolderFlip = new MinPQ<>();
        stateHolder.insert(new BoardPQNode(initial, 0));
        stateHolderFlip.insert(new BoardPQNode(initialFlip, 0));


        Board prevBoard = null;
        Board prevBoardFlip = null;

        while (!isReachGoal(stateHolder) && !isDetectUnSolve(stateHolderFlip)) {
            BoardPQNode current = stateHolder.delMin();
            BoardPQNode currentFlip = stateHolderFlip.delMin();

            for (Board neighbor: current.board.neighbors()) {
                if (!neighbor.equals(prevBoard)) {
                    stateHolder.insert(new BoardPQNode(neighbor, current.distFromSource + 1));
                }
            }

            for (Board neighbor: currentFlip.board.neighbors()) {
                if (!neighbor.equals(prevBoardFlip)) {
                    stateHolderFlip.insert(new BoardPQNode(neighbor, currentFlip.distFromSource + 1));
                }
            }

            prevBoard = current.board;
            prevBoardFlip = currentFlip.board;

            pathHolder.add(current.board);
        }
    }

    private void checkValidBoard(Board board) {
        if (board == null) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isReachGoal(MinPQ<BoardPQNode> stateHolder) {
        if (stateHolder.min().board.isGoal()) {
            isSolved = true;
            pathHolder.add(stateHolder.min().board);
            return true;
        }

        return false;
    }

    private boolean isDetectUnSolve(MinPQ<BoardPQNode> stateHolderFlip) {
        if (stateHolderFlip.min().board.isGoal()) {
            pathHolder = null;
            isSolved = false;
            return true;
        }
        return false;
    }

    private class BoardPQNode implements Comparable<BoardPQNode> {
        Board board;
        int distFromSource;
        int priority;

        BoardPQNode(Board board, int distFromSource) {
            this.board = board;
            this.distFromSource = distFromSource;
            priority = distFromSource + board.manhattan();
        }

        @Override
        public int compareTo(BoardPQNode o) {
            return Integer.compare(priority, o.priority);
        }
    }


}
