import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.HashSet;


public class WordNet {

    private HashMap<String, HashSet<Integer>> mapWordIndex;
    private HashMap<Integer, HashSet<String>> mapIndexSynset;
    private Digraph wordGraph;
    private SAP sapSolver;
    private int setNum;

    public WordNet(String synsets, String hypernyms){
        makeNodeIndexMap(synsets);
        makeWordGraph(hypernyms);

        sapSolver = new SAP(wordGraph);
    }

    public Iterable<String> nouns(){
        return mapWordIndex.keySet();
    }

    public boolean isNoun(String word){
        return mapWordIndex.containsKey(word);
    }

    public int distance(String nounA, String nounB){
        return sapSolver.length(mapWordIndex.get(nounA), mapWordIndex.get(nounB));
    }

    public String sap(String nounA, String nounB){
        int ancestor = sapSolver.ancestor(mapWordIndex.get(nounA), mapWordIndex.get(nounB));
        return mapIndexSynset.get(ancestor).toString();
    }


    private void makeNodeIndexMap(String synsets){
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


    public static void main(String[] args){
        WordNet test = new WordNet(args[0], args[1]);
    }
}
