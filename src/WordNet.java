package src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdOut;

public class WordNet {

    private Map<String, List<Integer>> _dict = new HashMap<String, List<Integer>>();
    private Map<Integer, String> _reverseDict = new HashMap<Integer, String>();
    private SAP _sap;
    private int _v;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) throws NullPointerException, IllegalArgumentException, IOException {
        if (synsets == null || hypernyms == null) {
            throw new java.lang.NullPointerException("inputs cannot be null!");
        }
        this.readSynsets(synsets);
        Digraph graph = this.readHypernyms(hypernyms);
        if (!isDAG(graph))
            throw new IllegalArgumentException();
        _sap = new SAP(graph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return _dict.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return _dict.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();
        return _sap.length(_dict.get(nounA), _dict.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();
        int id = _sap.ancestor(_dict.get(nounA), _dict.get(nounB));
        return _reverseDict.get(id);
    }

    private void readSynsets(String synsets) {
        BufferedReader in = new BufferedReader(new FileReader(synsets));
        String line = null;
        _v = 0;
        while ((line = in.readLine()) != null) {
            String items[] = line.split(",");
            String nouns[] = items[1].split(" ");
            List<Integer> nounList = null;
            for (int i = 0; i < nouns.length; i++) {
                if (!_dict.containsKey(nouns[i]))
                    nounList = new LinkedList<Integer>();
                else
                    nounList = _dict.get(nouns[i]);
                nounList.add(Integer.parseInt(items[0]));
                _dict.put(nouns[i], nounList);
            }
            _reverseDict.put(Integer.parseInt(items[0]), items[1]);
            _v = Math.max(_v, Integer.parseInt(items[0]));
        }
        in.close();
    }

    private Digraph readHypernyms(String hypernyms) {
        Digraph graph = new Digraph(_v + 1);
        BufferedReader in = new BufferedReader(new FileReader(hypernyms));
        String line = null;
        while ((line = in.readLine()) != null) {
            String items[] = line.split(",");
            int v = Integer.parseInt(items[0]);
            for (int i = 1; i < items.length; i++)
                graph.addEdge(v, Integer.parseInt(items[i]));
        }
        in.close();
    }

    private boolean dfs(int v, Digraph G, Map<Integer, Boolean> visit, Stack<Integer> S) {
        visit.put(v, true);
        S.push(v);
        for (int w : G.adj(v)) {
            if (S.contains(w)) {
                return false;
            }

            if (!visit.containsKey(w)) {
                if (!dfs(w, G, visit, S)) {
                    return false;
                }
            }
        }

        S.pop();

        return true;
    }

    private boolean isDAG(Digraph G) {
        Map<Integer, Boolean> visit = new HashMap<Integer, Boolean>();
        Stack<Integer> S = new Stack<Integer>();
        for (int i = 0; i < G.V(); i++) {
            if (!visit.containsKey(i))
                if (!dfs(i, G, visit, S))
                    return false;
        }
        // check multiple roots
        int cnt = 0;
        for (int i = 0; i < G.V(); i++) {
            int n = 0;
            Iterator<Integer> it = G.adj(i).iterator();
            while (it.hasNext()) {
                n += 1;
                it.next();
            }
            if (n == 0) {
                cnt += 1;
            }
        }
        if (cnt != 1) {
            return false;
        }

        return true;
    }

    // for unit testing of this class
    public static void main(String[] args) throws IllegalArgumentException, IOException {
        WordNet wordnet = new WordNet(args[0], args[1]);
        int res = wordnet.distance("wolf", "fish");
        StdOut.println("distance - " + res);
    }

}