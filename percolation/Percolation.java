import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final WeightedQuickUnionUF unionTopVirtualSite;
    private final WeightedQuickUnionUF unionBottomVirtualSite;
    private final PercolationBoard statesHolder;
    private final int gridSize;
    private final int virtualSiteIndex;
    private boolean isPercolate;
    private int numOpenSites;

    public Percolation(int gridSize) {
        if (gridSize <= 0) {
            throw new IllegalArgumentException();
        }

        this.gridSize = gridSize;
        numOpenSites = 0;
        virtualSiteIndex = this.gridSize * this.gridSize;
        statesHolder = new PercolationBoard(this.gridSize);
        unionTopVirtualSite = new WeightedQuickUnionUF(this.gridSize * this.gridSize + 1);
        unionBottomVirtualSite = new WeightedQuickUnionUF(this.gridSize * this.gridSize + 1);
    }

    public void open(int row, int col) {
        checkValidIndex(row, col);

        if (statesHolder.getState(row, col) == DataType.State.OPEN) {
            return;
        }

        connectedToSource(row, col);
        connectedToTarget(row, col);

        statesHolder.setState(DataType.State.OPEN, row, col);
        numOpenSites += 1;

        connectToNeigbor(row, col);

        if (!isPercolate) {
            checkIsPercolate(row, col);
        }
    }

    public boolean isOpen(int row, int col) {
        checkValidIndex(row, col);
        return statesHolder.getState(row, col) == DataType.State.OPEN;
    }

    public boolean isFull(int row, int col) {
        checkValidIndex(row, col);
        return unionTopVirtualSite.connected(virtualSiteIndex, transformTo1DIndex(row, col));
    }

    public int numberOfOpenSites() {
        return numOpenSites;
    }

    public boolean percolates() {
        return isPercolate;
    }

    private void checkValidIndex(int row, int col) {
        if (row > gridSize || row < 0 || col > gridSize || col < 0) {
            throw new IllegalArgumentException();
        }
    }

    private int transformTo1DIndex(int row, int col) {
        return row * gridSize + col;
    }

    private int[] getTranslationByDirection(int dir, int row, int col) {
        if (dir == DataType.Direction.LEFT) {
            return new int[]{row, col - 1};
        } else if (dir == DataType.Direction.RIGHT) {
            return new int[]{row, col + 1};
        } else if (dir == DataType.Direction.TOP) {
            return new int[]{row - 1, col};
        } else if (dir == DataType.Direction.BOTTOM) {
            return new int[]{row + 1, col};
        } else {
            throw new IllegalArgumentException();
        }

    }

    private void connectToNeigbor(int row, int col) {
        int currentIndex = transformTo1DIndex(row, col);
        DataType.State[] neighbors;
        neighbors = statesHolder.getNeighborStates(row, col);
        for (int dir = 0; dir < DataType.Direction.NEIGHBOR_SIZE; ++dir) {
            if (neighbors[dir] == DataType.State.OPEN) {
                int[] neigborPos = getTranslationByDirection(dir, row, col);
                int neigborIndex = transformTo1DIndex(neigborPos[0], neigborPos[1]);
                unionTopVirtualSite.union(neigborIndex, currentIndex);
                unionBottomVirtualSite.union(neigborIndex, currentIndex);
            }
        }
    }

    private void connectedToSource(int row, int col) {
        if (row == 0) {
            unionTopVirtualSite.union(virtualSiteIndex, transformTo1DIndex(row, col));
        }
    }

    private void connectedToTarget(int row, int col) {
        if (row == gridSize - 1) {
            unionBottomVirtualSite.union(virtualSiteIndex, transformTo1DIndex(row, col));
        }
    }

    private void checkIsPercolate(int row, int col) {
        int currentIdx = transformTo1DIndex(row, col);
        boolean isConnectToBottom = unionBottomVirtualSite.connected(virtualSiteIndex, currentIdx);
        boolean isConnectToTop = unionTopVirtualSite.connected(virtualSiteIndex, currentIdx);
        isPercolate = isConnectToBottom && isConnectToTop;
    }

    private class PercolationBoard {
        private DataType.State[][] statesHolder;
    
        PercolationBoard(int gridSize) {
            statesHolder =  new DataType.State[gridSize + 2][gridSize + 2];
            for (int i = 0; i < gridSize + 2; ++i) {
                for (int j = 0; j < gridSize + 2; ++j) {
                    statesHolder[i][j] = DataType.State.BLOCK;
                }
            }
        }
    
        void setState(DataType.State state, int row, int col) {
            statesHolder[row + 1][col + 1] = state;
        }
    
        DataType.State getState(int row, int col) {
            return statesHolder[row + 1][col + 1];
        }

        DataType.State[] getNeighborStates(int row, int col) {
            DataType.State[] neigborState = new DataType.State[DataType.Direction.NEIGHBOR_SIZE];
            neigborState[DataType.Direction.LEFT] = statesHolder[row + 1][col];
            neigborState[DataType.Direction.RIGHT] = statesHolder[row + 1][col + 2];
            neigborState[DataType.Direction.BOTTOM] = statesHolder[row + 2][col + 1];
            neigborState[DataType.Direction.TOP] = statesHolder[row][col + 1];
            return neigborState;
        }
    }

    private static class DataType {
        public enum State {
            BLOCK, OPEN
        }

        static class Direction {
            static final int NEIGHBOR_SIZE = 4;
            static final int LEFT = 0;
            static final int BOTTOM = 1;
            static final int RIGHT = 2;
            static final int TOP = 3;
        }
    }
}
