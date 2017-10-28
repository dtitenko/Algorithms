package week1;

import edu.princeton.cs.algs4.In;

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.BreadthFirstPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Queue;

public class SAP {

    private Digraph _graph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        _graph = G;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        int[] res = this.findAncestorAndDist(v, w, _graph);
        return res[1];
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        int[] res = this.findAncestorAndDist(v, w, _graph);
        return res[0];
    }

    private int[] findAncestorAndDist(int v, int w, Digraph graph) {
        BreadthFirstDirectedPaths wPath = new BreadthFirstDirectedPaths(graph, w);
        int ancestor = -1;
        int dist = -1;
        boolean[] marked = new boolean[graph.V()];
        int[] distTo = new int[graph.V()];
        Queue<Integer> q = new Queue<Integer>();
        marked[v] = true;
        distTo[v] = 0;
        q.enqueue(v);
        while (!q.isEmpty()) {
            int vi = q.dequeue();
            for (int wi : graph.adj(vi)) {
                if (!marked[wi]) {
                    if (wPath.hasPathTo(wi)) {
                        ancestor = wi;
                        dist = distTo[vi] + 1 + wPath.distTo(wi);
                        return new int[] { ancestor, dist };
                    }
                    distTo[wi] = distTo[vi] + 1;
                    marked[wi] = true;
                    q.enqueue(wi);
                }
            }
        }

        return new int[] { ancestor, dist };
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) throws IndexOutOfBoundsException {
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
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) throws IndexOutOfBoundsException {
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

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        int v = Integer.parseInt(args[1]);
        int w = Integer.parseInt(args[2]);
        int length = sap.length(v, w);
        int ancestor = sap.ancestor(v, w);

        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    }

    // do unit testing of this class
    // public static void main(String[] args) {
    //     In in = new In(args[0]);
    //     Digraph G = new Digraph(in);
    //     SAP sap = new SAP(G);
    //     while (!StdIn.isEmpty()) {
    //         int v = StdIn.readInt();
    //         int w = StdIn.readInt();
    //         int length   = sap.length(v, w);
    //         int ancestor = sap.ancestor(v, w);

    //         StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    //     }
    // }
}