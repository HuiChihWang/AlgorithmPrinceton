import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Topological;

import java.util.HashMap;
import java.util.HashSet;

public class WordNet {

    private HashMap<String, HashSet<Integer>> mapWordIndex;
    private HashMap<Integer, HashSet<String>> mapIndexSynset;
    private Digraph wordGraph;
    private SAP sapSolver;
    private int setNum;

    public WordNet(String synsets, String hypernyms) {
        checkArgumentNull(synsets);
        checkArgumentNull(hypernyms);
        makeNodeIndexMap(synsets);
        makeWordGraph(hypernyms);
        checkRootedDAG(wordGraph);
        sapSolver = new SAP(wordGraph);
    }

    public Iterable<String> nouns() {
        return mapWordIndex.keySet();
    }

    public boolean isNoun(String word) {
        checkArgumentNull(word);
        return mapWordIndex.containsKey(word);
    }

    public int distance(String nounA, String nounB) {
        checkValidNoun(nounA);
        checkValidNoun(nounB);

        return sapSolver.length(mapWordIndex.get(nounA), mapWordIndex.get(nounB));
    }

    public String sap(String nounA, String nounB) {
        checkValidNoun(nounA);
        checkValidNoun(nounB);

        int ancestorIdx = sapSolver.ancestor(mapWordIndex.get(nounA), mapWordIndex.get(nounB));
        HashSet<String> ancestorSet = mapIndexSynset.get(ancestorIdx);

        return convertSetToString(ancestorSet);
    }

    private void makeNodeIndexMap(String synsets) {
        mapWordIndex = new HashMap<>();
        mapIndexSynset = new HashMap<>();
        In input = new In(synsets);

        setNum = 0;
        while (input.hasNextLine()) {
            String line = input.readLine();
            String[] sysSetSplit = line.split(",");
            int index = Integer.parseInt(sysSetSplit[0]);
            String nounSet = sysSetSplit[1];
            mapIndexSynset.put(setNum, new HashSet<>());
            for (String noun: nounSet.split(" ")) {
                if (!mapWordIndex.containsKey(noun)) {
                    mapWordIndex.put(noun, new HashSet<>());
                }
                mapWordIndex.get(noun).add(index);
                mapIndexSynset.get(setNum).add(noun);
            }

            setNum += 1;
        }
    }

    private void makeWordGraph(String hypernyms) {
        wordGraph = new Digraph(setNum);
        In input = new In(hypernyms);

        while (input.hasNextLine()) {
            String line = input.readLine();
            String[] hypernymSplit = line.split(",");

            int nodeIdx = Integer.parseInt(hypernymSplit[0]);
            for (int i = 1; i < hypernymSplit.length; ++i) {
                wordGraph.addEdge(nodeIdx, Integer.parseInt(hypernymSplit[i]));
            }
        }
    }

    private String convertSetToString(HashSet<String> ancestorSet) {
        StringBuilder ancestorBuilder = new StringBuilder();
        for (String ancestor: ancestorSet) {
            ancestorBuilder.append(ancestor);
            ancestorBuilder.append(" ");
        }
        ancestorBuilder.deleteCharAt(ancestorBuilder.length()-1);

        return ancestorBuilder.toString();
    }

    private void checkArgumentNull(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException();
        }
    }

    private void checkValidNoun(String noun) {
        checkArgumentNull(noun);
        if (!isNoun(noun)) {
            throw new IllegalArgumentException();
        }
    }

    private void checkRootedDAG(Digraph G) {
        Topological topologicalSolver = new Topological(G);

        if (!topologicalSolver.hasOrder()) {
            throw  new IllegalArgumentException();
        }

        int rootCount = 0;
        for (int vertex: topologicalSolver.order()) {
            if (G.outdegree(vertex) == 0) {
                rootCount += 1;
            }

            if (rootCount > 1) {
                throw new IllegalArgumentException();
            }
        }
    }

    public static void main(String[] args) {
        WordNet wordNet = new WordNet(args[0], args[1]);
    }
}
