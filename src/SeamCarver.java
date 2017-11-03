import java.awt.Color;

public class SeamCarver {
    private Picture _picture;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this._picture = picture;
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

        Color colorX1 = _picture.get(x + 1, y);
        Color colorX2 = _picture.get(x - 1, y);
        Color colorY1 = _picture.get(x, y + 1);
        Color colorY2 = _picture.get(x, y - 1);

        return Math.sqrt(
            Math.pow(colorX1.getRed() - colorX2.getRed(), 2) +
            Math.pow(colorX1.getGreen() - colorX2.getGreen(), 2) +
            Math.pow(colorX1.getBlue() - colorX2.getBlue(), 2) +
            Math.pow(colorY1.getRed() - colorY2.getRed(), 2) +
            Math.pow(colorY1.getGreen() - colorY2.getGreen(), 2) +
            Math.pow(colorY1.getBlue() - colorY2.getBlue(), 2)
        );
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        throw new UnsupportedOperationException();
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        throw new UnsupportedOperationException();
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
    }

    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        StdOut.printf("image is %d pixels wide by %d pixels high.\n", picture.width(), picture.height());

        SeamCarver sc = new SeamCarver(picture);

        StdOut.printf("Printing energy calculated for each pixel.\n");

        for (int row = 0; row < sc.height(); row++) {
            for (int col = 0; col < sc.width(); col++)
                StdOut.printf("%7.2f ", sc.energy(col, row));
            StdOut.println();
        }
    }
}