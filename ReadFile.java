import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;
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

        System.out.println("Completed");
    }
}