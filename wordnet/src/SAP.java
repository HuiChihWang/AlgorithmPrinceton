import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;


import java.util.HashSet;

public class SAP {
    private Digraph graph;

    public SAP(Digraph G) {
        checkArgumentNull(G);
        graph = new Digraph(G);
    }

    public int length(int v, int w) {
        HashSet<Integer> vSet = new HashSet<>();
        HashSet<Integer> wSet = new HashSet<>();

        vSet.add(v);
        wSet.add(w);

        return length(vSet, wSet);
    }


    public int ancestor(int v, int w) {
        HashSet<Integer> vSet = new HashSet<>();
        HashSet<Integer> wSet = new HashSet<>();

        vSet.add(v);
        wSet.add(w);

        return ancestor(vSet, wSet);
    }


    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return findCommonAncestor(v, w)[0];
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return findCommonAncestor(v, w)[1];
    }

    private int[] findCommonAncestor(Iterable<Integer> v, Iterable<Integer> w) {
        checkNullInIterableItem(v);
        checkNullInIterableItem(w);

        BreadthFirstDirectedPaths bfsSolverFromV = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfsSolverFromW = new BreadthFirstDirectedPaths(graph, w);

        int minLength = Integer.MAX_VALUE;
        int nodeWithMinLength = -1;
        for (int nodeIdx = 0; nodeIdx < graph.V(); ++nodeIdx) {
            if (hasCommonAncester(bfsSolverFromV, bfsSolverFromW, nodeIdx)) {
                int lengthToCommomNode = bfsSolverFromV.distTo(nodeIdx) + bfsSolverFromW.distTo(nodeIdx);
                if (lengthToCommomNode < minLength) {
                    minLength = lengthToCommomNode;
                    nodeWithMinLength = nodeIdx;
                }
            }
        }

        if (nodeWithMinLength == -1) {
            minLength = -1;
        }

        return new int[]{minLength, nodeWithMinLength};
    }

    private boolean hasCommonAncester(BreadthFirstDirectedPaths bfsSolverFromV, BreadthFirstDirectedPaths bfsSolverFromW, int nodeIdx) {
        return bfsSolverFromV.hasPathTo(nodeIdx) && bfsSolverFromW.hasPathTo(nodeIdx);
    }

    private void checkArgumentNull(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException();
        }
    }

    private void checkNullInIterableItem(Iterable<Integer> iterItem) {
        checkArgumentNull(iterItem);
        for (Integer item: iterItem) {
            checkArgumentNull(item);
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
