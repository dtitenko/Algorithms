import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.Stack;
import java.awt.Color;

public class SeamCarver {
    private Picture _picture;
    private double[][] _energy;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        _picture = picture;
        _energy = new double[_picture.height()][_picture.width()];
        for (int j = 0; j < _picture.height(); j++) {
            for (int i = 0; i < _picture.width(); i++) {
                _energy[j][i] = _getEnergy(i, j);
            }
        }
    }

    // current picture
    public Picture picture() {
        return _picture;
    }

    // width of current picture
    public int width() {
        return _picture.width();
    }

    // height of current picture
    public int height() {
        return _picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= _picture.width() || y < 0 || y >= _picture.height()) {
            throw new java.lang.IllegalArgumentException();
        }

        if (x == 0 || y == 0 || x == _picture.width() - 1 || y == _picture.height() - 1) {
            return 1000;
        }

        return _energy[y][x];
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        throw new UnsupportedOperationException();
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int vertexCount = this.height() * this.width() + 2;
        Bag<DirectedEdge>[] adj = (Bag<DirectedEdge>[]) new Bag[vertexCount];
        adj[0] = new Bag<DirectedEdge>();
        adj[vertexCount - 1] = new Bag<DirectedEdge>();
        // adj[vertexCount - 1] = new Bag<DirectedEdge>();
        for (int x = 0; x < this.width(); x++) {
            adj[0].add(new DirectedEdge(0, x + 1, 1000.0 + x));
            int bottomRow = (this.height() - 1) * this.width() + 1;
            adj[bottomRow + x] = new Bag<DirectedEdge>();
            adj[bottomRow + x].add(new DirectedEdge(bottomRow + x, vertexCount - 1, 1000.0 + x));
        }

        for (int y = 0; y < this.height() - 1; y++) {
            for (int x = 0; x < this.width(); x++) {
                int vertexIndex = y * this.width() + x + 1;
                int baseTo = (y + 1) * this.width() + 1;
                adj[vertexIndex] = new Bag<DirectedEdge>();
                if (x > 0) {
                    adj[vertexIndex].add(new DirectedEdge(vertexIndex, baseTo + x - 1, energy(x - 1, y + 1) + x));
                }
                adj[vertexIndex].add(new DirectedEdge(vertexIndex, baseTo + x, energy(x, y + 1) + x));
                if (x < this.width() - 1) {
                    adj[vertexIndex].add(new DirectedEdge(vertexIndex, baseTo + x + 1, energy(x + 1, y + 1) + x));
                }
            }
        }

        Stack<DirectedEdge> path = dijkstra(adj);
        int[] intPath = new int[path.size() - 1];
        for (int p = 0; p < intPath.length; p++) {
            // reverse the pseudo index
            // int vertexIndex = y * this.width() + x + 1;
            int vertexIndex = path.pop().to() - 1;
            int y = vertexIndex / this.width();
            int x = vertexIndex - y * this.width();
            intPath[p] = x;
        }
        return intPath;
    }

    private final Stack<DirectedEdge> dijkstra(Bag<DirectedEdge>[] adj) {
        double[] distTo = new double[adj.length];
        for (int v = 0; v < adj.length; v++) {
            distTo[v] = Double.POSITIVE_INFINITY;
        }
        distTo[0] = 0.0;
        DirectedEdge[] edgeTo = new DirectedEdge[adj.length];
        IndexMinPQ<Double> pq = new IndexMinPQ<Double>(adj.length);
        pq.insert(0, 0.0);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            for (DirectedEdge e : adj[v]) {
                relax(e, distTo, edgeTo, pq);
            }
        }

        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
        for (DirectedEdge e = edgeTo[adj.length - 1]; e != null; e = edgeTo[e.from()]) {
            path.push(e);
        }
        return path;
    }

    private final void relax(DirectedEdge e, double[] distTo, DirectedEdge[] edgeTo, IndexMinPQ<Double> pq) {
        int v = e.from(), w = e.to();
        if (distTo[w] > distTo[v] + e.weight()) {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = e;
            if (pq.contains(w)) {
                pq.decreaseKey(w, distTo[w]);
            } else {
                pq.insert(w, distTo[w]);
            }
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam.length != _picture.width()) {
            throw new IllegalArgumentException();
        }

        Picture cPic = new Picture(_picture.width(), _picture.height() - 1);
        for (int i = 0; i < _picture.width(); i++) {
            for (int j = 0; j < _picture.height(); j++) {
                if (j == seam[i]) {
                    continue;
                }
                int pt = j;
                if (pt > seam[i]) {
                    pt--;
                }
                cPic.set(i, pt, this._picture.get(i, j));
            }
        }

        this._picture = cPic;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam.length != _picture.height()) {
            throw new IllegalArgumentException();
        }

        Picture newPicture = new Picture(_picture.width() - 1, _picture.height());
        for (int j = 0; j < _picture.height(); j++) {
            for (int i = 0; i < _picture.width(); i++) {
                if (i == seam[j]) {
                    continue;
                }
                int pt = i;
                if (pt > seam[j]) {
                    pt--;
                }
                newPicture.set(pt, j, _picture.get(i, j));
            }
        }

        this._picture = newPicture;
    }

    private double _getEnergy(int x, int y) {
        if (x < 0 || x >= _picture.width() || y < 0 || y >= _picture.height()) {
            throw new java.lang.IllegalArgumentException();
        }

        if (x == 0 || y == 0 || x == _picture.width() - 1 || y == _picture.height() - 1) {
            return 1000;
        }

        Color colorX1 = _picture.get(x + 1, y);
        Color colorX2 = _picture.get(x - 1, y);
        Color colorY1 = _picture.get(x, y + 1);
        Color colorY2 = _picture.get(x, y - 1);

        return Math.sqrt(Math.pow(colorX1.getRed() - colorX2.getRed(), 2)
                + Math.pow(colorX1.getGreen() - colorX2.getGreen(), 2)
                + Math.pow(colorX1.getBlue() - colorX2.getBlue(), 2) + Math.pow(colorY1.getRed() - colorY2.getRed(), 2)
                + Math.pow(colorY1.getGreen() - colorY2.getGreen(), 2)
                + Math.pow(colorY1.getBlue() - colorY2.getBlue(), 2));
    }

    private static final boolean HORIZONTAL = true;
    private static final boolean VERTICAL = false;

    private static void printSeam(SeamCarver carver, int[] seam, boolean direction) {
        double totalSeamEnergy = 0.0;

        for (int row = 0; row < carver.height(); row++) {
            for (int col = 0; col < carver.width(); col++) {
                double energy = carver.energy(col, row);
                String marker = " ";
                if ((direction == HORIZONTAL && row == seam[col]) || (direction == VERTICAL && col == seam[row])) {
                    marker = "*";
                    totalSeamEnergy += energy;
                }
                StdOut.printf("%7.2f%s ", energy, marker);
            }
            StdOut.println();
        }
        // StdOut.println();
        StdOut.printf("Total energy = %f\n", totalSeamEnergy);
        StdOut.println();
        StdOut.println();
    }

    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        StdOut.printf("%s (%d-by-%d image)\n", args[0], picture.width(), picture.height());
        StdOut.println();
        StdOut.println("The table gives the dual-gradient energies of each pixel.");
        StdOut.println("The asterisks denote a minimum energy vertical or horizontal seam.");
        StdOut.println();

        SeamCarver carver = new SeamCarver(picture);

        StdOut.printf("Vertical seam: { ");
        int[] verticalSeam = carver.findVerticalSeam();
        for (int x : verticalSeam)
            StdOut.print(x + " ");
        StdOut.println("}");
        printSeam(carver, verticalSeam, VERTICAL);

        // StdOut.printf("Horizontal seam: { ");
        // int[] horizontalSeam = carver.findHorizontalSeam();
        // for (int y : horizontalSeam)
        //     StdOut.print(y + " ");
        // StdOut.println("}");
        // printSeam(carver, horizontalSeam, HORIZONTAL);

    }
}