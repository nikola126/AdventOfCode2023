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
            partTwo();
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

                map.put(node, new Node(node, left, right));
            } else {
                for (Character c : line.toCharArray())
                    instructions.add(c);
            }
        }

        Node currentNode = map.entrySet().stream()
                .filter(node -> node.getValue().getText().equals("AAA"))
                .findFirst().get().getValue();

        int index = 0;
        int steps = 0;

        while (true) {
            if (index == instructions.size())
                index = 0;

            boolean goLeft = instructions.get(index++) == 'L';

            if (currentNode.getText().equals("ZZZ"))
                break;

            steps += 1;

            currentNode = map.get(goLeft ? currentNode.getLeft() : currentNode.getRight());
        }

        System.out.println("Day 8 Part 1: " + steps);
    }

    public void partTwo() throws FileNotFoundException {
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

                map.put(node, new Node(node, left, right));
            } else {
                for (Character c : line.toCharArray())
                    instructions.add(c);
            }
        }

        List<Node> startingNodes = map.values().stream()
                .filter(node -> node.getText().endsWith("A"))
                .toList();

        List<Long> stepsUntilLoop = new ArrayList<>();
        for (Node node : startingNodes) {
            int index = 0;
            long steps = 0;

            Node currentNode = node;

            while (true) {
                if (index == instructions.size())
                    index = 0;

                boolean goLeft = instructions.get(index) == 'L';

                currentNode = map.get(goLeft ? currentNode.getLeft() : currentNode.getRight());
                steps++;
                index++;

                if (currentNode.getText().endsWith("Z")) {
                    stepsUntilLoop.add(steps);
                    break;
                }
            }
        }

        System.out.println("Day 8 Part 2: " + LCM(stepsUntilLoop));
    }

    // https://stackoverflow.com/a/4202114
    public static long GCD(long a, long b) {
        while (b > 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }

        return a;
    }

    // https://stackoverflow.com/a/4202114
    public static long GCD(List<Long> inputs) {
        long result = inputs.get(0);

        for (int i = 1; i < inputs.size(); i++)
            result = GCD(result, inputs.get(i));

        return result;
    }

    // https://stackoverflow.com/a/4202114
    public static long LCM(long a, long b) {
        return a * (b / GCD(a, b));
    }

    // https://stackoverflow.com/a/4202114
    public static long LCM(List<Long> inputs) {
        long result = inputs.get(0);

        for (int i = 1; i < inputs.size(); i++)
            result = LCM(result, inputs.get(i));

        return result;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    private static class Node {
        private String text;
        private String left;
        private String right;

        @Override
        public String toString() {
            return String.format("[%s] (%s) (%s)", text, left, right);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return Objects.equals(text, node.text);
        }

        @Override
        public int hashCode() {
            return Objects.hash(text);
        }
    }

}
