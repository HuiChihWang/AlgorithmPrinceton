import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final int experimentTimes;
    private final int testGridSize;
    private final double[] arrayPercolationThresh;
    private final double ONE_STDDEV = 1.96;


    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }

        experimentTimes = trials;
        testGridSize = n;
        arrayPercolationThresh = new double[experimentTimes];

        simulate();
    }

    private Percolation createPercolation(int gridSize) {
        return new Percolation(gridSize);
    }

    private void simulate() {
        for (int i = 0; i < experimentTimes; ++i) {
            arrayPercolationThresh[i] = simulateOneTime();
        }

    }

    private double simulateOneTime() {
        Percolation tester = createPercolation(testGridSize);
        while (!tester.percolates()) {
            randomOpenSite(tester);
        }

        return (double) tester.numberOfOpenSites() / (double) (testGridSize * testGridSize);
    }

    private void randomOpenSite(Percolation tester) {
        int[] blockPos = chooseRandomBlockSite(tester);
        tester.open(blockPos[0], blockPos[1]);
    }

    private int[] chooseRandomBlockSite(Percolation tester) {
        int row = StdRandom.uniform(testGridSize) + 1;
        int col = StdRandom.uniform(testGridSize) + 1;

        while (tester.isOpen(row, col)) {
            row = StdRandom.uniform(testGridSize);
            col = StdRandom.uniform(testGridSize);
        }
        return new int[]{row, col};
    }

    public double mean() {
        return StdStats.mean(arrayPercolationThresh);
    }

    public double stddev() {
        return StdStats.stddev(arrayPercolationThresh);
    }

    public double confidenceLo() {
        return mean() - ONE_STDDEV * stddev() / Math.sqrt((double) experimentTimes);
    }

    public double confidenceHi() {
        return mean() + ONE_STDDEV * stddev() / Math.sqrt((double) experimentTimes);
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats percolateState = new PercolationStats(n, trials);

        System.out.println(String.format("mean = %f", percolateState.mean()));
        System.out.println(String.format("stddev = %f", percolateState.stddev()));
        double confidenceLower = percolateState.confidenceLo();
        double confidenceHigh = percolateState.confidenceHi();
        System.out.println(String.format("stddev = [%f, %f]", confidenceLower, confidenceHigh));
    }
}
