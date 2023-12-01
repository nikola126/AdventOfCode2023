package com.adventofcode2023;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day1 {
    public final String FILE_DIR = "./src/main/resources/day1.txt";
    public File file = new File(FILE_DIR);
    public Scanner scanner;

    public Day1() {
        try {
            partOne();
            partTwo();
        } catch (Exception e) {
            System.out.println("Error with Day 1: " + e.getMessage());
        }
    }

    public void partOne() throws FileNotFoundException {
        scanner = new Scanner(file);
        int sum = 0;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            int firstNumber = -1;
            int lastNumber = -1;

            for (Character c : line.toCharArray()) {
                if (!Character.isDigit(c))
                    continue;

                if (firstNumber == -1)
                    firstNumber = Integer.parseInt(String.valueOf(c));
                else
                    lastNumber = Integer.parseInt(String.valueOf(c));
            }

            if (lastNumber == -1)
                lastNumber = firstNumber;

            sum += (firstNumber * 10) + lastNumber;
        }

        System.out.println("Day 1 Part 1: " + sum);
    }

    public void partTwo() throws FileNotFoundException {
        scanner = new Scanner(file);
        int sum = 0;

        Map<String, Integer> tokenToValueMap = new HashMap<>();
        tokenToValueMap.put("1", 1);
        tokenToValueMap.put("2", 2);
        tokenToValueMap.put("3", 3);
        tokenToValueMap.put("4", 4);
        tokenToValueMap.put("5", 5);
        tokenToValueMap.put("6", 6);
        tokenToValueMap.put("7", 7);
        tokenToValueMap.put("8", 8);
        tokenToValueMap.put("9", 9);
        tokenToValueMap.put("one", 1);
        tokenToValueMap.put("two", 2);
        tokenToValueMap.put("three", 3);
        tokenToValueMap.put("four", 4);
        tokenToValueMap.put("five", 5);
        tokenToValueMap.put("six", 6);
        tokenToValueMap.put("seven", 7);
        tokenToValueMap.put("eight", 8);
        tokenToValueMap.put("nine", 9);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Map<Integer, String> firstIndices = new TreeMap<>();
            Map<Integer, String> lastIndices = new TreeMap<>(Collections.reverseOrder());

            for (Map.Entry<String, Integer> token : tokenToValueMap.entrySet()) {
                int firstIndex = line.indexOf(token.getKey());
                int lastIndex = line.lastIndexOf(token.getKey());

                if (firstIndex == -1 || lastIndex == -1)
                    continue;

                firstIndices.put(firstIndex, token.getKey());
                lastIndices.put(lastIndex, token.getKey());
            }

            List<String> asList = firstIndices.values().stream().toList();
            List<String> lastAsList = lastIndices.values().stream().toList();

            int firstNumber = tokenToValueMap.get(asList.getFirst());
            int lastNumber = tokenToValueMap.get(lastAsList.getFirst());

            sum += (firstNumber * 10) + lastNumber;

        }

        System.out.println("Day 1 Part 2: " + sum);
    }
}
