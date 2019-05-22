import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final int ExperimentTimes;
    private final int TestGridSize;
    private final double[] arrayPercolationThresh;


    public PercolationStats(int n, int trials){
        if(n <= 0 || trials <= 0)
            throw new IllegalArgumentException();

        ExperimentTimes = trials;
        TestGridSize = n;
        arrayPercolationThresh = new double[ExperimentTimes];

        Simulate();
    }

    private Percolation createPercolation(int N){
        return new Percolation(N);
    }

    private void Simulate(){
        for(int i=0; i<ExperimentTimes; ++i)
            arrayPercolationThresh[i] = SimulateOneTime();

    }

    private double SimulateOneTime(){
        Percolation pTester = createPercolation(TestGridSize);
        while(!pTester.percolates())
            RandomOpenSite(pTester);

        return (double)pTester.numberOfOpenSites() / (double)(TestGridSize*TestGridSize);
    }

    private void RandomOpenSite(Percolation pTester){
        int[] blockPos = ChooseRandomBlockSite(pTester);
        pTester.open(blockPos[0], blockPos[1]);
    }

    private int[] ChooseRandomBlockSite(Percolation pTester){
        int row = StdRandom.uniform(TestGridSize);
        int col = StdRandom.uniform(TestGridSize);

        while(pTester.isOpen(row, col)){
            row = StdRandom.uniform(TestGridSize);
            col = StdRandom.uniform(TestGridSize);
        }
        return new int[]{row, col};
    }

    public double mean(){
        return StdStats.mean(arrayPercolationThresh);
    }

    public double stddev(){
        return StdStats.stddev(arrayPercolationThresh);
    }

    public double confidenceLo(){
        return mean() - 1.96*stddev()/Math.sqrt((double)ExperimentTimes);
    }

    public double confidenceHi(){
        return mean() + 1.96*stddev()/Math.sqrt((double)ExperimentTimes);
    }

    public static void main(String[] args){
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats percolateState = new PercolationStats(n, trials);

        System.out.println(String.format("mean = %f", percolateState.mean()));
        System.out.println(String.format("stddev = %f", percolateState.stddev()));
        System.out.println(String.format("stddev = [%f, %f]", percolateState.confidenceLo(), percolateState.confidenceHi()));
    }
}
