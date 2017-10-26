import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.StdOut;

public class ReadFile {
    public static void main(String[] args) {
        In in = new In(args[0]);
        Graph G = new Graph(in);

        for (int v = 0; v < G.V(); v++) {
            for (int w : G.adj(v)) {
                StdOut.println(v + " - " + w);
            }
        }

        StdOut.println("Max degree: " + GraphExt.maxDegree(G));
        StdOut.println("Average degree: " + GraphExt.averageDegree(G));
        StdOut.println("Number of self loops: " + GraphExt.numberOfSelfLoops(G));

        StdOut.println("Completed");
    }
}