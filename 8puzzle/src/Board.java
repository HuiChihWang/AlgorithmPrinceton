import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private int[][] blocks;
    private int size;

    public Board(int[][] blocks) {
        this.blocks = blocks;
        size = dimension();
    }

    public int dimension() {
        return blocks.length;
    }

    public int hamming() {
        int cost = 0;
        int number = 1;
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if (blocks[i][j] != number && number != size * size) {
                    cost += 1;
                }
                number += 1;
            }
        }
        return cost;
    }

    public int manhattan() {
        int cost = 0;

        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if (blocks[i][j] != 0) {
                    int[] actual = toRowCol(blocks[i][j]);
                    int verticalDiff = Math.abs(actual[0] - i);
                    int horizonDiff = Math.abs(actual[1] - j);
                    cost += (verticalDiff + horizonDiff);
                }
            }
        }

        return cost;
    }

    public boolean isGoal() {
        return hamming() == 0;
    }

    public Board twin() {
        int[][] blockCopy = cloneArray(blocks);
        randomSwapPair(blockCopy);
        return new Board(blockCopy);
    }

    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }

        if (this == y) {
            return true;
        }

        if (y.getClass() != getClass()) {
            return false;
        }

        Board yBoard = (Board) y;

        if (dimension() != yBoard.dimension()) {
            return false;
        }

        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if (blocks[i][j] != yBoard.getBlocks()[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    public Iterable<Board> neighbors() {
        ArrayList<Board> neighbors = new ArrayList<>();
        int[] posBlank = findBlankPosition();
        int[][] shiftTable = new int[][]{{0, -1}, {0, 1}, {-1, 0}, {1, 0}};

        for (int[] shift: shiftTable) {
            int[] posShift = {posBlank[0] + shift[0], posBlank[1] + shift[1]};
            if (isValidIdx(posShift[0], posShift[1])) {
                int[][] blocksCopy = cloneArray(blocks);
                swap(blocksCopy, posBlank, posShift);
                neighbors.add(new Board(blocksCopy));
            }
        }

        return neighbors;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(dimension());
        sb.append("\n");

        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                sb.append(blocks[i][j]);
                sb.append(" ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    int[][] getBlocks() {
        return blocks;
    }

    private int[] toRowCol(int idx) {
        idx -= 1;
        int[] pos = {idx / size, idx % size};
        return pos;
    }

    private void randomSwapPair(int[][] array) {
        int[] pos0 = {0, 0};
        int[] pos1 = {0, 0};

        while (!isCorrectSwapPair(pos0, pos1)) {
            pos0[0] = StdRandom.uniform(array.length);
            pos0[1] = StdRandom.uniform(array[0].length);
            pos1[0] = StdRandom.uniform(array.length);
            pos1[1] = StdRandom.uniform(array[0].length);
        }

        swap(array, pos0, pos1);
    }

    private void swap(int[][] array, int[] pos0, int[] pos1) {
        int temp = array[pos0[0]][pos0[1]];
        array[pos0[0]][pos0[1]] = array[pos1[0]][pos1[1]];
        array[pos1[0]][pos1[1]] = temp;
    }

    private int[][] cloneArray(int[][] array) {
        int[][] copy = new int[array.length][];

        for (int i = 0; i < array.length; ++i) {
            copy[i] = new int[array[i].length];
            for (int j = 0; j < array[i].length; ++j) {
                copy[i][j] = array[i][j];
            }
        }
        return copy;
    }

    private boolean isCorrectSwapPair(int[] pos0, int[] pos1) {
        return !Arrays.equals(pos0, pos1) && blocks[pos0[0]][pos0[1]] != 0 && blocks[pos1[0]][pos1[1]] != 0;
    }

    private boolean isValidIdx(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    private int[] findBlankPosition() {
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if (blocks[i][j] == 0) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

}
