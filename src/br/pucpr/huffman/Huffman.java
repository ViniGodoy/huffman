package br.pucpr.huffman;

import java.util.*;

public class Huffman {
    private Node root;

    private char[] getChars(String text) {
        char[] letters = new char[text.length()];
        text.getChars(0, text.length(), letters, 0);
        return letters;
    }

    private PriorityQueue<Node> countFrequencies(char[] letters) {
        Map<Character, Node> count = new HashMap<>();
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
            Node node1 = nodes.poll();
            Node node2 = nodes.poll();

            //2. Group them
            Node parent = new Node(node1, node2);

            //3. If the queue is empty, the group is the root
            if (nodes.isEmpty()) {
                return parent;
            }

            //4. Otherwise, insert back the group in the tree
            nodes.add(parent);
        }
    }

    private Map<Character, String> createCodeMap() {
        Map<Character, String> result = new TreeMap<>();
        root.fillCodeMap(result, "");
        return result;
    }

    private String encode(String text) {
        char[] letters = getChars(text);
        root = createTree(countFrequencies(letters));
        Map<Character, String> codemap = createCodeMap();

        StringBuilder data = new StringBuilder();
        for (char ch : letters) {
            data.append(codemap.get(ch));
        }
        return data.toString();
    }

    private String decode(String data) {
        Node current = root;

        StringBuilder result = new StringBuilder();
        for (char ch : getChars(data)) {
            if (ch == '0') {
                current = current.getLeft();
            } else {
                current = current.getRight();
            }

            if (current.isLeaf()) {
                result.append(current.getSymbol());
                current = root;
            }
        }
        return result.toString();
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.println("--------------------------------------------");
        System.out.println("Huffman coding - Developed by Vinicius Godoy");
        System.out.println("--------------------------------------------");

        while (true) {
            System.out.println("Type the text to be encoded");
            System.out.println("Use Huffman for a default text, or leave blank to stop.");
            String text = in.nextLine().trim();

            if (text.equalsIgnoreCase("Huffman")) {
                text =
                        "In computer science and information theory, a Huffman code is a particular type\n" +
                                "of optimal prefix code that is commonly used for lossless data compression. The\n" +
                                "process of finding and/or using such a code proceeds by means of Huffman coding,\n" +
                                "an algorithm developed by David A. Huffman while he was a Ph.D. student at MIT,\n" +
                                "and published in the 1952 paper \"A Method for the Construction of\n" +
                                "Minimum-Redundancy Codes\".";
            }

            System.out.println();
            Huffman huff = new Huffman();
            String data = huff.encode(text);

            int normalSize = text.length() * 8;
            int compressedSize = data.length();
            double rate = 100.0 - (compressedSize * 100.0 / normalSize);
            System.out.println("Normal size: " + normalSize);
            System.out.println("Compressed size: " + compressedSize);
            System.out.printf("Compressed is %.2f%% smaller than the original. %n", rate);
            System.out.println();
            System.out.println("Encoded data:");
            System.out.println(data);
            System.out.println();
            System.out.println("Decoded text:");
            System.out.println(huff.decode(data));
            System.out.println();
            System.out.println();
        }
    }
}
