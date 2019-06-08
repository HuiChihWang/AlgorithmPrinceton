import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private WordNet wordNet;

    public Outcast(WordNet wordNet) {
        this.wordNet = wordNet;
    }

    public String outcast(String[] nouns) {

        int outCastIdx = 0;
        int maxDist = 0;
        for (int nounIdx = 0; nounIdx < nouns.length; ++nounIdx) {
            int dist = distance(nouns, nounIdx);
            if (dist > maxDist) {
                outCastIdx = nounIdx;
                maxDist = dist;
            }
        }

        return nouns[outCastIdx];
    }

    private int distance(String[] nouns, int nounIdx) {
        int sum = 0;
        for (int i = 0; i < nouns.length; ++i) {
            if (i != nounIdx) {
                sum += wordNet.distance(nouns[nounIdx], nouns[i]);
            }
        }

        return sum;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
