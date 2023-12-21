package com.adventofcode2023;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day21 {
    public final String FILE_DIR = "./src/main/resources/day21.txt";
    public File file = new File(FILE_DIR);
    public Scanner scanner;

    public Day21() {
        try {
            partOne();
        } catch (Exception e) {
            System.out.println("Error with Day 21: " + e.getMessage());
        }
    }

    public void partOne() throws FileNotFoundException {
        scanner = new Scanner(file);

        List<String> lines = new ArrayList<>();
        int ROWS = 0;
        int COLS = 0;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            lines.add(line);
            COLS = line.length();
            ROWS += 1;
        }

        Character[][] grid = new Character[ROWS][COLS];

        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            for (int col = 0; col < line.length(); col++) {
                grid[row][col] = line.charAt(col);
            }
        }


        // Determine starting position
        Point startingPoint = null;
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (grid[row][col] == 'S') {
                    startingPoint = new Point(row, col);
                    break;
                }
            }
        }

        // keep track of the number of unique points you can visit (value) after every step (key)
        Map<Integer, Set<Point>> stepsToVisitedMap = new LinkedHashMap<>();

        navigate(startingPoint.getX(), startingPoint.getY(), 64, grid, ROWS, COLS, stepsToVisitedMap);

        // Get the number of unique points you can visit with no (0) steps remaining
        Set<Point> visitedAtLastStep = stepsToVisitedMap.get(0);

        System.out.println("Day 21 Part 1: " + visitedAtLastStep.size());
    }

    private void navigate(int row, int col, int stepsRemaining, Character[][] grid, int ROWS, int COLS,Map<Integer, Set<Point>> stepsToVisitedMap) {
        // return if out of bounds
        if (row < 0 || row >= ROWS || col < 0 || col >= COLS)
            return;

        // return if it's a rock
        if (grid[row][col] == '#')
            return;

        Point currentPoint = new Point(row, col);

        Set<Point> visited = stepsToVisitedMap.getOrDefault(stepsRemaining, new HashSet<>());

        // if already visited, no need to navigate again, otherwise update the map with the updated set
        if (visited.contains(currentPoint))
            return;
        else {
            visited.add(currentPoint);
            stepsToVisitedMap.put(stepsRemaining, visited);
        }

        // return if no steps remaining
        if (stepsRemaining == 0) {
            return;
        }

        // keep navigating in 4 directions with 1 less step

        // UP
        navigate(row - 1, col, stepsRemaining - 1, grid, ROWS, COLS, stepsToVisitedMap);
        // LEFT
        navigate(row, col - 1, stepsRemaining - 1, grid, ROWS, COLS, stepsToVisitedMap);
        // RIGHT
        navigate(row, col + 1, stepsRemaining - 1, grid, ROWS, COLS, stepsToVisitedMap);
        // DOWN
        navigate(row + 1, col, stepsRemaining - 1, grid, ROWS, COLS, stepsToVisitedMap);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    private static class Point {
        int x;
        int y;

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Point other))
                return false;

            return x == other.x && y == other.y;
        }

        @Override
        public int hashCode() {
            // https://stackoverflow.com/a/50228389
            int ax = Math.abs(x);
            int ay = Math.abs(y);
            if (ax > ay && x > 0) return 4 * x * x - 3 * x + y + 1;
            if (ax > ay && x <= 0) return 4 * x * x - x - y + 1;
            if (ax <= ay && y > 0) return 4 * y * y - y - x + 1;
            return 4 * y * y - 3 * y + x + 1;
        }

        @Override
        public String toString() {
            return String.format("[%d][%d]", x, y);
        }
    }
}
