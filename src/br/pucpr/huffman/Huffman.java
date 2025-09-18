package br.pucpr.huffman;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class Huffman {
    private Node root;

    private char[] getChars(String text) {
        var letters = new char[text.length()];
        text.getChars(0, text.length(), letters, 0);
        return letters;
    }

    private PriorityQueue<Node> countFrequencies(char[] letters) {
        var count = new HashMap<Character, Node>();
        for (char ch : letters) {
            if (!count.containsKey(ch)) {
                count.put(ch, new Node(ch));
            }
            count.get(ch).add();
        }

        return new PriorityQueue<>(count.values());
    }

    private Node createTree(PriorityQueue<Node> nodes) {
        while (true) {
            //1. Take the two lowest frequent nodes
            var node1 = nodes.poll();
            var node2 = nodes.poll();

            //2. Group them
            var parent = new Node(node1, node2);

            //3. If the queue is empty, the group is the root
            if (nodes.isEmpty()) {
                return parent;
            }

            //4. Otherwise, insert back the group in the tree
            nodes.add(parent);
        }
    }

    private Map<Character, String> createCodeMap() {
        var result = new TreeMap<Character, String>();
        root.fillCodeMap(result, "");
        return result;
    }

    public String encode(String text) {
        var letters = getChars(text);
        root = createTree(countFrequencies(letters));
        var codemap = createCodeMap();

        var data = new StringBuilder();
        for (char ch : letters) {
            data.append(codemap.get(ch));
        }
        return data.toString();
    }

    public String decode(String data) {
        var current = root;

        var result = new StringBuilder();
        for (var ch : getChars(data)) {
            current = ch == '0' ? current.getLeft() : current.getRight();

            if (current.isLeaf()) {
                result.append(current.getSymbol());
                current = root;
            }
        }
        return result.toString();
    }
}
