import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import java.util.HashMap;

public class Percolation {
    private final WeightedQuickUnionUF UnionOfTopVirtualSite;
    private final WeightedQuickUnionUF UnionOfBottomVirtualSite;
    private final PercolationBoard StatesHolder;
    private final int GridSize;
    private final int VirtualSiteIndex;
    private boolean isPercolate;
    private int NumOfOpenSites;

    public Percolation(int N) {
        if (N <= 0)
            throw  new IllegalArgumentException();

        GridSize = N;
        NumOfOpenSites = 0;
        VirtualSiteIndex = GridSize * GridSize;
        StatesHolder = new PercolationBoard(GridSize);
        UnionOfTopVirtualSite = new WeightedQuickUnionUF(GridSize*GridSize + 1);
        UnionOfBottomVirtualSite = new WeightedQuickUnionUF(GridSize*GridSize +1);
    }

    public void open(int row, int col){
        checkValidIndex(row, col);
        if(StatesHolder.GetState(row, col) == DataType.State.OPEN)
            return;

        ConnectedToSource(row, col);
        ConnectedToTarget(row, col);

        StatesHolder.SetState(DataType.State.OPEN, row, col);
        NumOfOpenSites += 1;

        ConnectToNeigbor(row, col);
        CheckIsPercolate(row, col);
    }

    public boolean isOpen(int row, int col){
        checkValidIndex(row, col);
        return StatesHolder.GetState(row, col) == DataType.State.OPEN;
    }

    public boolean isFull(int row, int col){
        checkValidIndex(row, col);
        return UnionOfTopVirtualSite.connected(VirtualSiteIndex, transformTo1DIndex(row, col));
    }

    public int numberOfOpenSites(){
        return NumOfOpenSites;
    }

    public boolean percolates(){
        return isPercolate;
    }

    private void checkValidIndex(int row, int col){
        if(row >= GridSize || row < 0 || col >= GridSize || col < 0)
            throw new IndexOutOfBoundsException();
    }

    private int transformTo1DIndex(int row, int col){
        return row * GridSize + col;
    }

    private int[] getTranslationByDirection(DataType.Direction dir, int row, int col){
        if(dir == DataType.Direction.LEFT)
            return new int[]{row, col - 1};
        else if(dir == DataType.Direction.RIGHT)
            return new int[]{row, col + 1};
        else if(dir == DataType.Direction.TOP)
            return new int[]{row - 1, col};
        else if(dir == DataType.Direction.BOTTOM)
            return new int[]{row + 1, col};
        else
            throw new IllegalArgumentException();
    }

    private void ConnectToNeigbor(int row, int col){
        int current_index = transformTo1DIndex(row, col);
        HashMap<DataType.Direction, DataType.State> neighbors = StatesHolder.GetNeighborStates(row, col);
        for(DataType.Direction dir: neighbors.keySet()){
            if(neighbors.get(dir) == DataType.State.OPEN){
                int[] neigbor_pos = getTranslationByDirection(dir, row, col);
                int neigbor_index = transformTo1DIndex(neigbor_pos[0], neigbor_pos[1]);
                UnionOfTopVirtualSite.union(neigbor_index, current_index);
                UnionOfBottomVirtualSite.union(neigbor_index,current_index);
            }
        }
    }

    private void ConnectedToSource(int row, int col){
        if(row == 0)
            UnionOfTopVirtualSite.union(VirtualSiteIndex, transformTo1DIndex(row, col));
    }

    private void ConnectedToTarget(int row, int col){
        if(row == GridSize-1)
            UnionOfBottomVirtualSite.union(VirtualSiteIndex, transformTo1DIndex(row, col));
    }

    private void CheckIsPercolate(int row, int col){
        int current_idx = transformTo1DIndex(row, col);
        boolean isConnectToBottom = UnionOfBottomVirtualSite.connected(VirtualSiteIndex, current_idx);
        boolean isConnectToTop = UnionOfTopVirtualSite.connected(VirtualSiteIndex, current_idx);
        isPercolate = isConnectToBottom && isConnectToTop;
    }

    private class PercolationBoard {
        private DataType.State[][] StatesHolder;
    
        public PercolationBoard(int N){
            StatesHolder =  new DataType.State[N + 2][N + 2];
            
            for(int i = 0; i < N + 2; ++i)
                for(int j = 0; j < N + 2; ++j)
                    StatesHolder[i][j] = DataType.State.BLOCK;
        }
    
        public void SetState(DataType.State state, int row, int col){
            StatesHolder[row + 1][col + 1] = state;
        }
    
        public DataType.State GetState(int row, int col){
            return StatesHolder[row + 1][col + 1];
        }
    
        public HashMap<DataType.Direction, DataType.State> GetNeighborStates(int row, int col) {
            HashMap<DataType.Direction, DataType.State> neigborState = new HashMap<>();
            neigborState.put(DataType.Direction.LEFT, StatesHolder[row+1][col]);
            neigborState.put(DataType.Direction.RIGHT, StatesHolder[row+1][col+2]);
            neigborState.put(DataType.Direction.TOP, StatesHolder[row][col+1]);
            neigborState.put(DataType.Direction.BOTTOM, StatesHolder[row+2][col+1]);
            return neigborState;
        }
    }

    private static class DataType{

        public enum State {
            BLOCK, OPEN
        }

        public enum Direction {
            LEFT, BOTTOM, RIGHT, TOP
        }
    }
}
