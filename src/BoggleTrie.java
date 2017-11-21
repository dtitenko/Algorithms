public class BoggleTrie {
    private static final int R = 26;

    private Node root; // root of trie

    private static class Node {
        private boolean val;
        private Node[] next = new Node[R];
    }

    public boolean contains(String key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to get() is null");
        }
        Node x = get(root, key, 0);
        return x != null && x.val;
    }

    private Node get(Node x, String key, int d) {
        if (x == null)
            return null;
        if (d == key.length())
            return x;
        char c = key.charAt(d);
        return get(x.next[index(c)], key, d + 1);
    }

    public void put(String key) {
        if (key == null) {
            throw new IllegalArgumentException("first argument to put() is null");
        } else {
            root = put(root, key, 0);
        }
    }

    private Node put(Node x, String key, int d) {
        if (x == null)
            x = new Node();
        if (d == key.length()) {
            x.val = true;
            return x;
        }
        char c = key.charAt(d);
        x.next[index(c)] = put(x.next[index(c)], key, d + 1);
        return x;
    }

    public boolean hasWordsWithPrefix(String prefix) {
        Node x = get(root, prefix, 0);
        if (x == null) {
            return false;
        }
        for (char c = 0; c < R; c++) {
            if (x.next[c] != null) {
                return true;
            }
        }
        return false;
    }

    private int index(char c) {
        return c - (char) 65;
    }
}