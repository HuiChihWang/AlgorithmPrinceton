import java.util.ArrayList;

public class Board {
    private int[][] blocks;
    private int size;
    private int[] blankPosition;
    private int[][] swapPos;

    public Board(int[][] blocks) {
        this.blocks = cloneArray(blocks);
        size = blocks.length;
        blankPosition = findBlankPosition();
        swapPos = chooseSwapPair();
    }

    public int dimension() {
        return size;
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
        int[][] blocksTwin = cloneArray(blocks);
        swap(blocksTwin, swapPos[0], swapPos[1]);
        return new Board(blocksTwin);
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
                if (blocks[i][j] != yBoard.blocks[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    public Iterable<Board> neighbors() {
        ArrayList<Board> neighbors = new ArrayList<>();
        int[][] shiftTable = new int[][]{{0, -1}, {0, 1}, {-1, 0}, {1, 0}};

        for (int[] shift: shiftTable) {
            int[] posShift = {blankPosition[0] + shift[0], blankPosition[1] + shift[1]};
            if (isValidIdx(posShift[0], posShift[1])) {
                int[][] blocksCopy = cloneArray(blocks);
                swap(blocksCopy, blankPosition, posShift);
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

    private int[] toRowCol(int idx) {
        idx -= 1;
        int[] pos = {idx / size, idx % size};
        return pos;
    }

    private int[][] chooseSwapPair() {
        int[][] posPair = new int[2][];

        int[][] shiftTable = new int[][]{{0, -1}, {0, 1}, {-1, 0}, {1, 0}};

        int count = 0;
        for (int[] shift: shiftTable) {
            if(count >= 2) {
                break;
            }

            if(isValidIdx(shift[0] + blankPosition[0], shift[1] + blankPosition[1])) {
                posPair[count] = new int[]{shift[0] + blankPosition[0], shift[1] + blankPosition[1]};
                count += 1;
            }
        }
        return posPair;
    }

    private void swap(int[][] array, int[] pos0, int[] pos1) {
        int temp = array[pos0[0]][pos0[1]];
        array[pos0[0]][pos0[1]] = array[pos1[0]][pos1[1]];
        array[pos1[0]][pos1[1]] = temp;
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

}
