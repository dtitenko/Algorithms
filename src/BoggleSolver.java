import edu.princeton.cs.algs4.TrieST;

public class BoggleSolver {
    private TrieST<Integer> _dictionary = new TrieST<Integer>();

	// Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
    	for (int i = 0; i < dictionary.length; i++) {
            String key = dictionary[i];
            _dictionary.put(key, i);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        throw new UnsupportedOperationException();
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word == null || word.length() < 3 || !_dictionary.contains(word))
            return 0;
        if (word.length() < 5)
            return 1;
        if (word.length() == 5)
            return 2;
        if (word.length() == 6)
            return 3;
        if (word.length() == 7)
            return 5;
        return 11;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}