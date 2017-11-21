public class BoggleTrie {
    private static final int R = 26;

    private Node root; // root of trie

    public class Node {
        public boolean val;
        public Node[] next = new Node[R];
        public boolean hasChilds;
        public Node next(char c) {
            return next[index(c)];
        }
    }

    public Node root() {
        return root;
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
        x.hasChilds = true;
        return x;
    }

    private int index(char c) {
        return c - (char) 65;
    }
}