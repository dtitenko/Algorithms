import java.util.Collection;
import java.util.HashSet;

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
        WordSearcher searcher = new WordSearcher(board);
        return searcher.getWords();
    }

    private class WordSearcher {
        private BoggleBoard _board;
        private HashSet<String> queue;

        public WordSearcher(BoggleBoard board) {
            _board = board;
            queue = new HashSet<String>();
        }

        public Iterable<String> getWords() {
            for (int i = 0; i < _board.cols(); i++) {
                for (int j = 0; j < _board.rows(); j++) {
                    searchWords(i, j, "", new boolean[_board.rows()][_board.cols()]);
                }
            }
            return queue;
        }

        private void searchWords(int i, int j, String prefix, boolean[][] _visited) {
            if (i < 0 || j < 0 || i >= _board.cols() || j >= _board.rows() || _visited[j][i]) {
                return;
            }
            char letter = _board.getLetter(i, j);
            String word = prefix + letter;
            if (letter == 'Q') {
            	word += 'U';
            }
            Integer key = _dictionary.get(word);
            if (key != null && word.length() > 2) {
                queue.add(word);
            }
            int futureCount = size(_dictionary.keysWithPrefix(word));
            if (futureCount == 1 && key != null || futureCount == 0) {
                return;
            }
            boolean[][] newVisited = cloneArray(_visited);
            newVisited[j][i] = true;
            searchWords(i - 1, j - 1, word, newVisited);
            searchWords(i - 1, j, word, newVisited);
            searchWords(i - 1, j + 1, word, newVisited);
            searchWords(i, j - 1, word, newVisited);
            searchWords(i, j + 1, word, newVisited);
            searchWords(i + 1, j - 1, word, newVisited);
            searchWords(i + 1, j, word, newVisited);
            searchWords(i + 1, j + 1, word, newVisited);
        }
        
        public boolean[][] cloneArray(boolean[][] src) {
            int length = src.length;
            boolean[][] target = new boolean[length][src[0].length];
            for (int i = 0; i < length; i++) {
                System.arraycopy(src[i], 0, target[i], 0, src[i].length);
            }
            return target;
        }

        private int size(Iterable<?> it) {
            if (it instanceof Collection)
              return ((Collection<?>)it).size();

            // else iterate

            int i = 0;
            for (Object obj : it) i++;
            return i;
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