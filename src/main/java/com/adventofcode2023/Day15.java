package com.adventofcode2023;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day15 {
    public final String FILE_DIR = "./src/main/resources/day15.txt";
    public File file = new File(FILE_DIR);
    public Scanner scanner;

    public Day15() {
        try {
            partOne();
        } catch (Exception e) {
            System.out.println("Error with Day 15: " + e.getMessage());
        }
    }

    public void partOne() throws FileNotFoundException {
        scanner = new Scanner(file);

        long sum = 0;
        while (scanner.hasNext()) {
            String[] tokens = scanner.nextLine().split(",");

            for (String token : tokens) {
                token = token.trim();
                if (token.isBlank())
                    continue;

                long hash = hash(token);
                sum += hash;
            }
        }

        System.out.println("Day 15 Part 1: " + sum);
    }

    private long hash(String string) {
        long value = 0;

        for (Character c : string.toCharArray()) {
            value += (int) c;
            value *= 17;
            value = value % 256;
        }

        return value;
    }

}
