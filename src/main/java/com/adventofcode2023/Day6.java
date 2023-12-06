package com.adventofcode2023;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day6 {
    public final String FILE_DIR = "./src/main/resources/day6.txt";
    public File file = new File(FILE_DIR);
    public Scanner scanner;

    public Day6() {
        try {
            partOne();
            partTwo();
        } catch (Exception e) {
            System.out.println("Error with Day 6: " + e.getMessage());
        }
    }

    public void partOne() throws FileNotFoundException {
        scanner = new Scanner(file);

        List<Integer> times = new ArrayList<>();
        List<Integer> distances = new ArrayList<>();
        int marginOfError = 1;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (line.toLowerCase().contains("time")) {
                String content = line.split(":")[1];

                StringBuilder sb = new StringBuilder();
                for (Character c : content.toCharArray()) {
                    if (Character.isDigit(c))
                        sb.append(c);
                    else if (!sb.isEmpty()) {
                        times.add(Integer.parseInt(sb.toString()));
                        sb.setLength(0);
                    }
                }

                if (!sb.isEmpty())
                    times.add(Integer.parseInt(sb.toString()));
            } else if (line.toLowerCase().contains("distance")) {
                String content = line.split(":")[1];

                StringBuilder sb = new StringBuilder();
                for (Character c : content.toCharArray()) {
                    if (Character.isDigit(c))
                        sb.append(c);
                    else if (!sb.isEmpty()) {
                        distances.add(Integer.parseInt(sb.toString()));
                        sb.setLength(0);
                    }
                }

                if (!sb.isEmpty())
                    distances.add(Integer.parseInt(sb.toString()));
            }
        }

        for (int i = 0; i < times.size(); i++) {
            int count = 0;
            int bestDistance = distances.get(i);
            int time = times.get(i);

            for (int press = 1; press < time; press++) {
                int distance = press * (time - press);

                if (distance > bestDistance)
                    count += 1;
            }

            marginOfError *= count;
        }

        System.out.println("Day 6 Part 1: " + marginOfError);
    }

    public void partTwo() throws FileNotFoundException {
        scanner = new Scanner(file);

        long time = -1L;
        long distance = -1L;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (line.toLowerCase().contains("time")) {
                String content = line.split(":")[1];

                StringBuilder sb = new StringBuilder();
                for (Character c : content.toCharArray()) {
                    if (Character.isDigit(c))
                        sb.append(c);
                }

                if (!sb.isEmpty())
                    time = Long.parseLong(sb.toString());
            } else if (line.toLowerCase().contains("distance")) {
                String content = line.split(":")[1];

                StringBuilder sb = new StringBuilder();
                for (Character c : content.toCharArray()) {
                    if (Character.isDigit(c))
                        sb.append(c);
                }

                if (!sb.isEmpty())
                    distance = Long.parseLong(sb.toString());
            }
        }

        // Binary search for latest win
        long left = 2;
        long right = time - 1;

        while (left <= right) {
            long mid = left + (right - left) / 2;
            long currentDistance = mid * (time - mid);

            if (currentDistance <= distance) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        long rightMost = right;

        // Binary search for earliest win
        left = 2;
        right = time - 1;

        while (left <= right) {
            long mid = left + (right - left) / 2;
            long currentDistance = mid * (time - mid);

            if (currentDistance > distance) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        long leftMost = left;

        System.out.println("Day 6 Part 2: " + (rightMost - leftMost + 1));
    }

}
