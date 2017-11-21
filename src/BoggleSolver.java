import java.util.HashSet;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {
    private BoggleTrie _dictionary = new BoggleTrie();

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        for (int i = 0; i < dictionary.length; i++) {
            String key = dictionary[i];
            _dictionary.put(key);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        WordSearcher searcher = new WordSearcher(board);
        return searcher.getWords();
    }

    private class WordSearcher {
        private BoggleBoard _board;
        private HashSet<String> _words;
        private boolean[][] _visited;

        public WordSearcher(BoggleBoard board) {
            _board = board;
            _words = new HashSet<String>();
            _visited = new boolean[_board.rows()][_board.cols()];
        }

        public Iterable<String> getWords() {
            for (int i = 0; i < _board.cols(); i++) {
                for (int j = 0; j < _board.rows(); j++) {
                    searchWords(i, j, "");
                }
            }
            return _words;
        }

        private void searchWords(int i, int j, String prefix) {
            if (i < 0 || j < 0 || i >= _board.cols() || j >= _board.rows() || _visited[j][i]) {
                return;
            }
            char letter = _board.getLetter(j, i);
            String word;
            if (letter == 'Q') {
                word = prefix + "QU";
            } else {
                word = prefix + letter;
            }
            boolean contains = _dictionary.contains(word);
            if (contains && word.length() > 2) {
                _words.add(word);
            }
            if (!_dictionary.hasWordsWithPrefix(word)) {
                return;
            }

            _visited[j][i] = true;
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    searchWords(i + x, j + y, word);
                }
            }
            _visited[j][i] = false;
        }
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