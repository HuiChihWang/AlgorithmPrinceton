import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Solver {
    private Board initial;
    private Board initialFlip;

    private LinkedList<Board> pathHolder;
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
        return isSolved ? pathHolder.size() - 1 : -1;
    }

    public Iterable<Board> solution() {
        return pathHolder;
    }

    private void solveMinMoveByAstar() {
        MinPQ<BoardPQNode> stateHolder = new MinPQ<>();
        MinPQ<BoardPQNode> stateHolderFlip = new MinPQ<>();

        stateHolder.insert(new BoardPQNode(initial));
        stateHolderFlip.insert(new BoardPQNode(initialFlip));

        while (!isReachGoal(stateHolder) && !isDetectUnSolve(stateHolderFlip)) {
            BoardPQNode current = stateHolder.delMin();
            BoardPQNode currentFlip = stateHolderFlip.delMin();

            for (Board neighbor: current.board.neighbors()) {
                Board prev = current.from != null ? current.from.board : null;
                if (!neighbor.equals(prev)) {
                    stateHolder.insert(new BoardPQNode(neighbor, current));
                }
            }

            for (Board neighbor: currentFlip.board.neighbors()) {
                Board prev = currentFlip.from != null ? currentFlip.from.board : null;
                if (!neighbor.equals(prev)) {
                    stateHolderFlip.insert(new BoardPQNode(neighbor, currentFlip));
                }
            }
        }
    }

    private void findPath(BoardPQNode goalNode) {
        pathHolder = new LinkedList<>();
        BoardPQNode start = goalNode;
        while(start != null) {
            pathHolder.addFirst(start.board);
            start = start.from;
        }
    }

    private boolean isReachGoal(MinPQ<BoardPQNode> stateHolder) {
        if (stateHolder.min().board.isGoal()) {
            isSolved = true;
            findPath(stateHolder.min());
            return true;
        }

        return false;
    }

    private boolean isDetectUnSolve(MinPQ<BoardPQNode> stateHolderFlip) {
        if (stateHolderFlip.min().board.isGoal()) {
            isSolved = false;
            return true;
        }
        return false;
    }

    private void checkValidBoard(Board board) {
        if (board == null) {
            throw new IllegalArgumentException();
        }
    }

    private class BoardPQNode implements Comparable<BoardPQNode> {
        Board board;
        int distFromSource;
        int priority;
        BoardPQNode from;

        BoardPQNode(Board startBoard) {
            board = startBoard;
            from = null;
            distFromSource = 0;
            priority = distFromSource + board.manhattan();
        }

        BoardPQNode(Board board, BoardPQNode from) {
            this.board = board;
            this.from = from;
            distFromSource = this.from.distFromSource + 1;
            priority = distFromSource + board.manhattan();
        }

        @Override
        public int compareTo(BoardPQNode other) {
            return Integer.compare(priority, other.priority);
        }
    }


}
