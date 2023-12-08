package com.adventofcode2023;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day8 {
    public final String FILE_DIR = "./src/main/resources/day8.txt";
    public File file = new File(FILE_DIR);
    public Scanner scanner;

    public Day8() {
        try {
            partOne();
        } catch (Exception e) {
            System.out.println("Error with Day 8: " + e.getMessage());
        }
    }

    public void partOne() throws FileNotFoundException {
        scanner = new Scanner(file);

        List<Character> instructions = new ArrayList<>();
        Map<String, Node> map = new LinkedHashMap<>();

        boolean inMap = false;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (line.isEmpty()) {
                inMap = true;
                continue;
            }

            if (inMap) {
                String[] tokens = line.split(" = ");
                String node = tokens[0].trim();
                String[] directions = tokens[1].split(", ");

                String left = directions[0].substring(1).trim();
                String right = directions[1].substring(0, directions[1].length() - 1);

                map.put(node, new Node(node, left, right, 0));
            } else {
                for (Character c : line.toCharArray())
                    instructions.add(c);
            }
        }

        Node currentNode = map.entrySet().stream()
                .filter(node -> node.getValue().getText().equals("AAA"))
                .findFirst().get().getValue();

        currentNode.incrementVisited();
        int index = 0;
        int steps = 0;

        while (true) {
            if (index == instructions.size())
                index = 0;

            boolean goLeft = instructions.get(index++) == 'L';

            if (currentNode.getText().equals("ZZZ"))
                break;

            steps += 1;
            currentNode.incrementVisited();

            currentNode = map.get(goLeft ? currentNode.getLeft() : currentNode.getRight());
        }

        System.out.println("Day 8 Part 1: " + steps);
    }

    @AllArgsConstructor
    @Getter
    @Setter
    private static class Node {
        private String text;
        private String left;
        private String right;
        private int countVisited;

        @Override
        public String toString() {
            return String.format("[%s] (%s) (%s) [%d]", text, left, right, countVisited);
        }

        public void incrementVisited() {
            this.countVisited += 1;
        }
    }

}
