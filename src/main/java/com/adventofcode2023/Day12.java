package com.adventofcode2023;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day12 {
    public final String FILE_DIR = "./src/main/resources/day12.txt";
    public File file = new File(FILE_DIR);
    public Scanner scanner;

    public Day12() {
        try {
            partOne();
            partTwo();
        } catch (Exception e) {
            System.out.println("Error with Day 12: " + e.getMessage());
        }
    }

    public void partOne() throws FileNotFoundException {
        scanner = new Scanner(file);

        List<Spring> springs = readSprings(scanner);

        long sum = 0;

        for (Spring spring : springs) {
            map = new HashMap<>();
            long permutations = countPermutations(spring.getRecord(), spring.getGroups());
            sum += permutations;
        }

        System.out.println("Day 12 Part 1: " + sum);

    }

    public void partTwo() throws FileNotFoundException {
        scanner = new Scanner(file);

        List<Spring> springs = readSprings(scanner);

        List<Spring> unfoldedSprings = springs.stream().map(this::unfoldSpring).toList();

        long sum = 0;

        for (Spring spring : unfoldedSprings) {
            map = new HashMap<>();
            long permutations = countPermutations(spring.getRecord(), spring.getGroups());
            sum += permutations;
        }

        System.out.println("Day 12 Part 2: " + sum);
    }

    private List<Spring> readSprings(Scanner scanner) {
        List<Spring> springs = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (line.isBlank())
                continue;

            String[] tokens = line.split(" ");
            List<Integer> groups = new ArrayList<>();
            for (String token : tokens[1].split(",")) {
                token = token.trim();
                if (token.isBlank())
                    continue;
                groups.add(Integer.parseInt(token));
            }

            springs.add(new Spring(tokens[0].trim(), groups));
        }

        return springs;
    }

    private Spring unfoldSpring(Spring spring) {
        StringBuilder sb = new StringBuilder();
        List<Integer> unfoldedGroups = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            sb.append(spring.getRecord());
            sb.append("?");

            unfoldedGroups.addAll(spring.getGroups());
        }

        sb.setLength(sb.length() - 1);

        return new Spring(sb.toString(), unfoldedGroups);
    }

    private Map<String, Long> map = new HashMap<>();

    // Based on https://github.com/ash42/adventofcode/tree/main/adventofcode2023/src/nl/michielgraat/adventofcode2023/day12
    private long countPermutations(String record, List<Integer> groups) {
        Spring spring = new Spring(record, groups);

        // Skip computation if this spring was previously encountered
        if (map.containsKey(spring.toString()))
            return map.get(spring.toString());

        // nowhere to put springs, return a VALID permutation if there are no groups left
        if (record.isBlank())
            return groups.isEmpty() ? 1 : 0;

        char firstCharacter = record.charAt(0);
        long permutations = 0;

        if (firstCharacter == '.') {
            // Working spring, look forward with the same groups
            permutations = countPermutations(record.substring(1), groups);
        } else if (firstCharacter == '?') {
            // Unknown spring, look forward but count permutations with the first character both working and broken
            permutations =
                    countPermutations("." + record.substring(1), groups) +
                    countPermutations("#" + record.substring(1), groups);
        } else {
            // First character is #
            if (groups.isEmpty()) {
                // no more groups, but starts with broken, which is invalid (WHAT!?)
                permutations = 0;
            } else {
                int numberOfDamagedSprings = groups.getFirst();
                // check if the next group can actually fit in the record or is all broken
                if (numberOfDamagedSprings <= record.length() && record.chars().limit(numberOfDamagedSprings).allMatch(c -> c == '#' || c == '?')) {
                    List<Integer> nextGroups = groups.subList(1, groups.size());

                    if (numberOfDamagedSprings == record.length()) {
                        // fits exactly, return a valid permutation if there are no remaining groups
                        permutations = nextGroups.isEmpty() ? 1 : 0;
                    } else if (record.charAt(numberOfDamagedSprings) == '.') {
                        // a group of broken springs was added, so the next one can be anything
                        // skip forward one and look for permutations
                        permutations = countPermutations(record.substring(numberOfDamagedSprings + 1), nextGroups);
                    } else if (record.charAt(numberOfDamagedSprings) == '?') {
                        // a group of broken springs was added, so the next one can only be working (spacing between groups)
                        // add the working one and look for permutations
                        permutations = countPermutations("." + record.substring(numberOfDamagedSprings + 1), nextGroups);
                    } else {
                        // a group of broken springs was added, but the next one is again broken
                        // this is invalid
                        permutations = 0;
                    }
                } else {
                    // next group does not fit or all is working
                    permutations = 0;
                }
            }
        }

        // Save this computation for next time
        map.put(spring.toString(), permutations);

        return permutations;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    private static class Spring {
        private String record;
        private List<Integer> groups;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("[%s]", record));

            sb.append("[");
            for (Integer group : groups) {
                sb.append(group);
                sb.append(" ");
            }
            sb.setLength(sb.length() - 1);
            sb.append("]");

            return sb.toString();
        }
    }

}
