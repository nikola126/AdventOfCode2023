package com.adventofcode2023;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class Day9 {
    public final String FILE_DIR = "./src/main/resources/day9.txt";
    public File file = new File(FILE_DIR);
    public Scanner scanner;

    public Day9() {
        try {
            partOne();
            partTwo();
        } catch (Exception e) {
            System.out.println("Error with Day 9: " + e.getMessage());
        }
    }

    public void partOne() throws FileNotFoundException {
        scanner = new Scanner(file);

        int answer = 0;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            List<Integer> history = new ArrayList<>();

            String[] tokens = line.split(" ");
            for (String token : tokens)
                history.add(Integer.parseInt(token.trim()));

            Stack<List<Integer>> stack = new Stack<>();
            stack.push(history);

            while (true) {
                List<Integer> current = stack.peek();
                List<Integer> next = new ArrayList<>();

                boolean nonZeroWasAdded = false;

                for (int i = 0; i < current.size() - 1; i++) {
                    next.add(current.get(i + 1) - current.get(i));
                    nonZeroWasAdded = next.getLast() != 0;
                }

                stack.push(next);

                if (!nonZeroWasAdded)
                    break;
            }

            stack.pop();
            int lastOnTheRight = 0;
            while (!stack.isEmpty()) {
                List<Integer> list = stack.pop();
                lastOnTheRight = list.getLast() + lastOnTheRight;
            }

            answer += lastOnTheRight;
        }

        System.out.println("Day 9 Part 1: " + answer);
    }

    public void partTwo() throws FileNotFoundException {
        scanner = new Scanner(file);

        int answer = 0;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            List<Integer> history = new ArrayList<>();

            String[] tokens = line.split(" ");
            for (String token : tokens)
                history.add(Integer.parseInt(token.trim()));

            Stack<List<Integer>> stack = new Stack<>();
            stack.push(history);

            while (true) {
                List<Integer> current = stack.peek();
                List<Integer> next = new ArrayList<>();

                boolean nonZeroWasAdded = false;

                for (int i = 0; i < current.size() - 1; i++) {
                    next.add(current.get(i + 1) - current.get(i));
                    nonZeroWasAdded = next.getLast() != 0;
                }

                stack.push(next);

                if (!nonZeroWasAdded)
                    break;
            }

            stack.pop();
            int lastOnTheLeft = 0;
            while (!stack.isEmpty()) {
                List<Integer> list = stack.pop();
                lastOnTheLeft = list.getFirst() - lastOnTheLeft;
            }

            answer += lastOnTheLeft;
        }

        System.out.println("Day 9 Part 2: " + answer);
    }

}
