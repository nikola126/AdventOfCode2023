package com.adventofcode2023;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day9 {
    public final String FILE_DIR = "./src/main/resources/day9.txt";
    public File file = new File(FILE_DIR);
    public Scanner scanner;

    public Day9() {
        try {
            partOne();
        } catch (Exception e) {
            System.out.println("Error with Day 9: " + e.getMessage());
        }
    }

    public void partOne() throws FileNotFoundException {
        scanner = new Scanner(file);

        List<List<Integer>> histories = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            List<Integer> history = new ArrayList<>();

            String[] tokens = line.split(" ");
            for (String token : tokens)
                history.add(Integer.parseInt(token.trim()));

            histories.add(history);

        }

        for (List<Integer> history : histories) {
            Stack<List<Integer>> stack = new Stack<>();
            stack.push(history);
            boolean allZeroes = false;

            while (!allZeroes) {
                List<Integer> current = stack.peek();
                List<Integer> next = new ArrayList<>();

                for (int i = 0; i < current.size() - 1; i++)
                    next.add(current.get(i + 1) - current.get(i));

                allZeroes = next.stream().allMatch(number -> number == 0);
                stack.push(next);
            }

            for (List<Integer> list : stack) {
                for (Integer number : list) System.out.printf("%d ", number);
                System.out.println();
            }

            System.out.println("--- --- --- --- ---");

        }
    }

}
