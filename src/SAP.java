import java.util.HashMap;
import java.util.Map;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.In;

public class SAP {

    private Digraph _graph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this._graph = new Digraph(G);
    }

    private Map<Integer, Integer> getAncestors(int v) {
        Queue<Integer> vQ = new Queue<Integer>();
        Map<Integer, Integer> vM = new HashMap<Integer, Integer>();
        vQ.enqueue(v);
        vM.put(v, 0);
        while (!vQ.isEmpty()) {
            int head = vQ.dequeue();
            int currentDist = vM.get(head);
            for (Integer w : _graph.adj(head)) {
                if (!vM.containsKey(w) || vM.get(w) > currentDist + 1) {
                    vQ.enqueue(w);
                    vM.put(w, currentDist + 1);
                }
            }
        }
        return vM;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || v >= _graph.V())
            throw new IndexOutOfBoundsException();
        if (w < 0 || w >= _graph.V())
            throw new IndexOutOfBoundsException();
        Map<Integer, Integer> ancestorV = getAncestors(v);
        Map<Integer, Integer> ancestorW = getAncestors(w);
        int dist = -1;
        for (Integer key : ancestorV.keySet()) {
            if (ancestorW.containsKey(key)) {
                int currentDist = ancestorW.get(key) + ancestorV.get(key);
                if (dist < 0 || currentDist < dist)
                    dist = currentDist;
            }
        }
        return dist;
    }

    // a common ancestor of v and w that participates in a shortest ancestral
    // path; -1 if no such path
    public int ancestor(int v, int w) {
        Map<Integer, Integer> ancestorV = getAncestors(v);
        Map<Integer, Integer> ancestorW = getAncestors(w);
        if (v < 0 || v >= _graph.V())
            throw new IndexOutOfBoundsException();
        if (w < 0 || w >= _graph.V())
            throw new IndexOutOfBoundsException();
        int dist = -1, anc = -1;
        for (Integer key : ancestorV.keySet()) {
            if (ancestorW.containsKey(key)) {
                int currentDist = ancestorW.get(key) + ancestorV.get(key);
                if (dist < 0 || currentDist < dist) {
                    dist = currentDist;
                    anc = key;
                }
            }
        }
        return anc;
    }

    // length of shortest ancestral path between any vertex in v and any vertex
    // in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        int dist = -1;
        for (Integer eV : v) {
            for (Integer eW : w) {
                int currentDist = length(eV, eW);
                if (currentDist > 0 && (dist < 0 || currentDist < dist))
                    dist = currentDist;
            }
        }
        return dist;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no
    // such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        int dist = -1, anc = -1;
        for (Integer eV : v) {
            for (Integer eW : w) {
                int currentDist = length(eV, eW);
                if (currentDist > 0 && (dist < 0 || currentDist < dist)) {
                    dist = currentDist;
                    anc = ancestor(eV, eW);
                }
            }
        }
        return anc;
    }

    // for unit testing of this class (such as the one below)
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph graph = new Digraph(in);
        SAP sap = new SAP(graph);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}