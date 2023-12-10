package com.adventofcode2023;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day10 {
    public final String FILE_DIR = "./src/main/resources/day10.txt";
    public File file = new File(FILE_DIR);
    public Scanner scanner;

    private int loopLength = 0;

    public Day10() {
        try {
            partOne();
        } catch (Exception e) {
            System.out.println("Error with Day 10: " + e.getMessage());
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

        Set<Point> visitedPoints = new HashSet<>();

        Point startingPoint = null;
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (grid[row][col] == 'S') {
                    startingPoint = new Point(row, col, 'S');
                    break;
                }
            }
        }

        List<Point> accessiblePoints = determineAccessiblePoints(grid, ROWS, COLS, startingPoint);

        visitedPoints.add(startingPoint);

        Point nextPoint = accessiblePoints.stream().filter(p -> p.getC() != '.').findFirst().orElse(null);
        visitedPoints.add(nextPoint);

        while (nextPoint != null) {
            nextPoint = navigateThruPipe(grid, ROWS, COLS, nextPoint, startingPoint, visitedPoints);
        }

        System.out.println("Day 10 Part 1: " + ((loopLength / 2) + 1));
    }

    private Point navigateThruPipe(Character[][] grid, int ROWS, int COLS, Point currentPoint, Point prevPoint, Set<Point> visited) {
        if (currentPoint == null)
            return null;

        loopLength += 1;

        if (currentPoint.getC() == 'S')
            return null;

        if (currentPoint.getC() == '.')
            return null;

        List<Point> accessiblePoints = determineAccessiblePoints(grid, ROWS, COLS, currentPoint)
                .stream().filter(point -> !visited.contains(point)).toList();

        Point nextPoint = accessiblePoints.stream().filter(point -> !point.equals(prevPoint)).findFirst().orElse(null);

        if (visited.contains(nextPoint))
            return null;

        visited.add(nextPoint);
        return nextPoint;
    }

    private static void printGrid(Character[][] grid, int ROWS, int COLS) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                System.out.printf("%c", grid[row][col]);
            }
            System.out.println();
        }
    }

    private List<Point> determineAccessiblePoints(Character[][] grid, int ROWS, int COLS, Point currentPoint) {
        Character pipe = grid[currentPoint.getX()][currentPoint.getY()];
        List<Point> accessiblePoints = new ArrayList<>();

        int currentRow = currentPoint.getX();
        int currentCol = currentPoint.getY();

        switch (pipe) {
            case 'S': {
                // TOP
                if (currentRow != 0) {
                    Point point = new Point(currentRow - 1, currentCol, grid[currentRow - 1][currentCol]);
                    if (point.getC() == '|' || point.getC() == '7' || point.getC() == 'F')
                        accessiblePoints.add(point);
                }
                // LEFT
                if (currentCol != 0) {
                    Point point = new Point(currentRow, currentCol - 1, grid[currentRow][currentCol - 1]);
                    if (point.getC() == '-' || point.getC() == 'L' || point.getC() == 'F')
                        accessiblePoints.add(point);
                }
                // RIGHT
                if (currentCol != COLS) {
                    Point point = new Point(currentRow, currentCol + 1, grid[currentRow][currentCol + 1]);
                    if (point.getC() == '-' || point.getC() == 'J' || point.getC() == '7')
                        accessiblePoints.add(point);
                }
                // BOTTOM
                if (currentRow != ROWS) {
                    Point point = new Point(currentRow + 1, currentCol, grid[currentRow + 1][currentCol]);
                    if (point.getC() == '|' || point.getC() == 'L' || point.getC() == 'J')
                        accessiblePoints.add(point);
                }
                break;
            }
            case '.': {
                break;
            }
            case '|': {
                // TOP
                if (currentRow != 0) {
                    accessiblePoints.add(new Point(currentRow - 1, currentCol, grid[currentRow - 1][currentCol]));
                }
                // BOTTOM
                if (currentRow != ROWS) {
                    accessiblePoints.add(new Point(currentRow + 1, currentCol, grid[currentRow + 1][currentCol]));
                }
                break;
            }
            case '-': {
                // LEFT
                if (currentCol != 0) {
                    accessiblePoints.add(new Point(currentRow, currentCol - 1, grid[currentRow][currentCol - 1]));
                }
                // RIGHT
                if (currentCol != COLS) {
                    accessiblePoints.add(new Point(currentRow, currentCol + 1, grid[currentRow][currentCol + 1]));
                }
                break;
            }
            case 'L': {
                // TOP
                if (currentRow != 0) {
                    accessiblePoints.add(new Point(currentRow - 1, currentCol, grid[currentRow - 1][currentCol]));
                }
                // RIGHT
                if (currentCol != COLS) {
                    accessiblePoints.add(new Point(currentRow, currentCol + 1, grid[currentRow][currentCol + 1]));
                }
                break;
            }
            case 'J': {
                // TOP
                if (currentRow != 0) {
                    accessiblePoints.add(new Point(currentRow - 1, currentCol, grid[currentRow - 1][currentCol]));
                }
                // LEFT
                if (currentCol != 0) {
                    accessiblePoints.add(new Point(currentRow, currentCol - 1, grid[currentRow][currentCol - 1]));
                }
                break;
            }
            case '7': {
                // LEFT
                if (currentCol != 0) {
                    accessiblePoints.add(new Point(currentRow, currentCol - 1, grid[currentRow][currentCol - 1]));
                }
                // BOTTOM
                if (currentRow != ROWS) {
                    accessiblePoints.add(new Point(currentRow + 1, currentCol, grid[currentRow + 1][currentCol]));
                }
                break;
            }
            case 'F': {
                // RIGHT
                if (currentCol != COLS) {
                    accessiblePoints.add(new Point(currentRow, currentCol + 1, grid[currentRow][currentCol + 1]));
                }
                // BOTTOM
                if (currentRow != ROWS) {
                    accessiblePoints.add(new Point(currentRow + 1, currentCol, grid[currentRow + 1][currentCol]));
                }
                break;
            }
        }

        return accessiblePoints;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    private static class Point {
        int x;
        int y;
        Character c;

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
            return String.format("[%c] [%d][%d]", c, x, y);
        }
    }

}
