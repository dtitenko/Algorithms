package src;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private WordNet _wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this._wordnet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        String bestNouns = "";
        int bestDist = 0;
        for (int i = 0; i < nouns.length; i++) {
            int currentDist = 0;
            for (int j = 0; j < nouns.length; j++) {
                currentDist += _wordnet.distance(nouns[i], nouns[j]);
            }

            if (bestDist < currentDist) {
                bestDist = currentDist;
                bestNouns = nouns[i];
            }
        }
        return bestNouns;
    }

    // see test client below
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