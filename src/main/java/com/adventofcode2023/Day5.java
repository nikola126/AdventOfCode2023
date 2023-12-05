package com.adventofcode2023;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day5 {
    public final String FILE_DIR = "./src/main/resources/day5.txt";
    public File file = new File(FILE_DIR);
    public Scanner scanner;

    public Day5() {
        try {
            partOne();
            partTwo();
        } catch (Exception e) {
            System.out.println("Error with Day 5: " + e.getMessage());
        }
    }

    public void partOne() throws FileNotFoundException {
        scanner = new Scanner(file);

        List<List<Range>> almanac = new ArrayList<>();
        List<Long> seeds = new ArrayList<>();

        List<Range> currentList = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (line.startsWith("seeds:")) {
                String[] tokens = line.split(" ");
                for (String token : tokens) {
                    if (token.contains("seeds"))
                        continue;

                    token = token.trim();

                    if (token.isBlank())
                        continue;

                    seeds.add(Long.parseLong(token));
                }
            } else if (line.contains("map")) {
                if (!currentList.isEmpty()) {
                    almanac.add(new ArrayList<>(currentList));
                    currentList.clear();
                }
            } else if (!line.isBlank()) {
                String[] tokens = line.split(" ");
                Range range = new Range(Long.parseLong(tokens[0]), Long.parseLong(tokens[1]), Long.parseLong(tokens[2]));
                currentList.add(range);
            }
        }
        almanac.add(new ArrayList<>(currentList));

        // Sorts all ranges in reverse destination range
        for (List<Range> rangeList : almanac) {
            rangeList.sort(Range::compareTo);
        }

        for (List<Range> rangeList : almanac) {
            for (int i = 0; i < seeds.size(); i++) {
                Long index = determineSeedLocation(rangeList, seeds.get(i));
                seeds.set(i, index);
            }
        }

        seeds.sort(Long::compareTo);

        System.out.println("Day 5 Part 1: " + seeds.getFirst());
    }

    public void partTwo() throws FileNotFoundException {
        scanner = new Scanner(file);

        List<List<Range>> almanac = new ArrayList<>();
        String seedsLine = "";

        List<Range> currentList = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (line.startsWith("seeds:")) {
                seedsLine = line;
            } else if (line.contains("map")) {
                if (!currentList.isEmpty()) {
                    almanac.add(new ArrayList<>(currentList));
                    currentList.clear();
                }
            } else if (!line.isBlank()) {
                String[] tokens = line.split(" ");
                Range range = new Range(Long.parseLong(tokens[0]), Long.parseLong(tokens[1]), Long.parseLong(tokens[2]));
                currentList.add(range);
            }
        }

        almanac.add(new ArrayList<>(currentList));

        for (List<Range> rangeList : almanac) {
            rangeList.sort(Range::compareTo);
        }

        // Go seed by seed
        String[] tokens = seedsLine.split(" ");
        long lowestSeed = Long.MAX_VALUE;
        long rangeStart = -1L;

        for (int i = 1; i < tokens.length; i++) {
            String token = tokens[i].trim();
            if (i % 2 == 0) {
                for (long seedIndex = rangeStart; seedIndex < (rangeStart + Long.parseLong(token)); seedIndex++) {
                    Long seed = seedIndex;

                    for (List<Range> rangeList : almanac) {
                        seed = determineSeedLocation(rangeList, seed);
                    }

                    lowestSeed = Math.min(lowestSeed, seed);
                }
            } else {
                rangeStart = Long.parseLong(token);
            }
        }

        System.out.println("Day 5 Part 2: " + lowestSeed);
    }

    private Long determineSeedLocation(List<Range> rangeList, Long seed) {
        Long index = -1L;

        for (Range range : rangeList) {
            if (seed > range.getDestinationRange() && (seed <= range.getDestinationRange() + range.getRangeLength())) {
                index = seed + (range.getSourceRange() - range.getDestinationRange());
                break;
            } else if (seed.equals(range.getDestinationRange())) {
                index = range.getSourceRange();
                break;
            }
        }

        if (index == -1L)
            index = seed;

        return index;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    private static class Range implements Comparable<Range> {
        private Long sourceRange;
        private Long destinationRange;
        private Long rangeLength;

        @Override
        public String toString() {
            return String.format("RANGE S: [%d] D: [%d] L: [%d]", sourceRange, destinationRange, rangeLength);
        }

        @Override
        public int compareTo(Range o) {
            // Reversed order!
            return Long.compare(o.getDestinationRange(), this.destinationRange);
        }
    }

}
