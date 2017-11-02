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
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
    }
}