package com.adventofcode2023;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day3 {
    public final String FILE_DIR = "./src/main/resources/day3.txt";
    public File file = new File(FILE_DIR);
    public Scanner scanner;

    public Day3() {
        try {
            partOne();
            partTwo();
        } catch (Exception e) {
            System.out.println("Error with Day 3: " + e.getMessage());
        }
    }

    public void partOne() throws FileNotFoundException {
        scanner = new Scanner(file);

        List<List<Character>> grid = new ArrayList<>();
        int sum = 0;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            List<Character> gridLine = new ArrayList<>();
            for (Character c : line.toCharArray()) {
                gridLine.add(c);
            }

            grid.add(gridLine);
        }

        Set<Point> visited = new HashSet<>();

        for (int row = 0; row < grid.size(); row++) {
            for (int col = 0; col <grid.getFirst().size(); col++) {
                Character c = grid.get(row).get(col);
                Point currentPoint = new Point(row, col, c);

                if (Character.isDigit(c) && !visited.contains(currentPoint)) {
                    List<Point> islandPoints = findIslandPoints(row, col, grid, visited);
                    visited.add(currentPoint);

                    boolean isAnyPointAdjacentToSymbol = islandPoints.stream()
                            .anyMatch(point -> isAdjacentToSymbol(point.getX(), point.getY(), grid));

                    if (isAnyPointAdjacentToSymbol)
                        sum += calculateIslandValue(islandPoints);

                }
            }

        }

        System.out.println("Day 3 Part 1: " + sum);
    }

    public void partTwo() throws FileNotFoundException {
        scanner = new Scanner(file);

        List<List<Character>> grid = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            List<Character> gridLine = new ArrayList<>();
            for (Character c : line.toCharArray()) {
                gridLine.add(c);
            }

            grid.add(gridLine);
        }

        List<List<Point>> islands = new ArrayList<>();
        Set<Point> visited = new HashSet<>();

        for (int row = 0; row < grid.size(); row++) {
            for (int col = 0; col <grid.getFirst().size(); col++) {
                Character c = grid.get(row).get(col);
                Point currentPoint = new Point(row, col, c);

                if (Character.isDigit(c) && !visited.contains(currentPoint)) {
                    List<Point> islandPoints = findIslandPoints(row, col, grid, visited);
                    visited.add(currentPoint);

                    islands.add(islandPoints);

                }
            }
        }

        int gearRatio = 0;

        for (int row = 0; row < grid.size(); row++) {
            for (int col = 0; col <grid.getFirst().size(); col++) {
                Character c = grid.get(row).get(col);

                if (c == '*') {
                    gearRatio += calculateGearRatio(row, col, grid, islands);
                }
            }
        }

        System.out.println("Day 3 Part 2: " + gearRatio);
    }

    private List<Point> findIslandPoints(int row, int col, List<List<Character>> grid, Set<Point> visited) {
        List<Point> islandPoints = new ArrayList<>();
        islandPoints.add(new Point(row, col, grid.get(row).get(col)));

        int nextCol = col + 1;
        while (isInBounds(row, nextCol, grid) && Character.isDigit(grid.get(row).get(nextCol))) {
            Point point = new Point(row, nextCol, grid.get(row).get(nextCol));
            islandPoints.add(point);
            visited.add(point);
            nextCol += 1;
        }


        return islandPoints;
    }

    private boolean isAdjacentToSymbol(int row, int col, List<List<Character>> grid) {
        int rowDest, colDest;

        // up left
        rowDest = row - 1;
        colDest = col - 1;
        if (isInBounds(rowDest, colDest, grid)) {
            Character c = grid.get(rowDest).get(colDest);
            if (!Character.isDigit(c) && c != '.') {
                return true;
            }
        }

        // up
        rowDest = row - 1;
        colDest = col;
        if (isInBounds(rowDest, colDest, grid)) {
            Character c = grid.get(rowDest).get(colDest);
            if (!Character.isDigit(c) && c != '.') {
                return true;
            }
        }

        // up right
        rowDest = row - 1;
        colDest = col + 1;
        if (isInBounds(rowDest, colDest, grid)) {
            Character c = grid.get(rowDest).get(colDest);
            if (!Character.isDigit(c) && c != '.') {
                return true;
            }
        }

        // left
        rowDest = row;
        colDest = col - 1;
        if (isInBounds(rowDest, colDest, grid)) {
            Character c = grid.get(rowDest).get(colDest);
            if (!Character.isDigit(c) && c != '.') {
                return true;
            }
        }

        // right
        rowDest = row;
        colDest = col + 1;
        if (isInBounds(rowDest, colDest, grid)) {
            Character c = grid.get(rowDest).get(colDest);
            if (!Character.isDigit(c) && c != '.') {
                return true;
            }
        }

        // down left
        rowDest = row + 1;
        colDest = col - 1;
        if (isInBounds(rowDest, colDest, grid)) {
            Character c = grid.get(rowDest).get(colDest);
            if (!Character.isDigit(c) && c != '.') {
                return true;
            }
        }

        // down
        rowDest = row + 1;
        colDest = col;
        if (isInBounds(rowDest, colDest, grid)) {
            Character c = grid.get(rowDest).get(colDest);
            if (!Character.isDigit(c) && c != '.') {
                return true;
            }
        }

        // down right
        rowDest = row + 1;
        colDest = col + 1;
        if (isInBounds(rowDest, colDest, grid)) {
            Character c = grid.get(rowDest).get(colDest);
            if (!Character.isDigit(c) && c != '.') {
                return true;
            }
        }

        return false;
    }

    private int calculateGearRatio(int row, int col, List<List<Character>> grid, List<List<Point>> islands) {
        int rowDest, colDest;
        Set<List<Point>> adjacentIslands = new HashSet<>();

        // up left
        rowDest = row - 1;
        colDest = col - 1;
        if (isInBounds(rowDest, colDest, grid) && Character.isDigit(grid.get(rowDest).get(colDest))) {
            Point point = new Point(rowDest, colDest, grid.get(rowDest).get(colDest));
            findAdjacentIslands(islands, point, adjacentIslands);

            if (adjacentIslands.size() > 2)
                return 0;
        }

        // up
        rowDest = row - 1;
        colDest = col;
        if (isInBounds(rowDest, colDest, grid)) {
            Point point = new Point(rowDest, colDest, grid.get(rowDest).get(colDest));

            findAdjacentIslands(islands, point, adjacentIslands);

            if (adjacentIslands.size() > 2)
                return 0;
        }

        // up right
        rowDest = row - 1;
        colDest = col + 1;
        if (isInBounds(rowDest, colDest, grid)) {
            Point point = new Point(rowDest, colDest, grid.get(rowDest).get(colDest));

            findAdjacentIslands(islands, point, adjacentIslands);

            if (adjacentIslands.size() > 2)
                return 0;
        }

        // left
        rowDest = row;
        colDest = col - 1;
        if (isInBounds(rowDest, colDest, grid)) {
            Point point = new Point(rowDest, colDest, grid.get(rowDest).get(colDest));
            findAdjacentIslands(islands, point, adjacentIslands);

            if (adjacentIslands.size() > 2)
                return 0;
        }

        // right
        rowDest = row;
        colDest = col + 1;
        if (isInBounds(rowDest, colDest, grid)) {
            Point point = new Point(rowDest, colDest, grid.get(rowDest).get(colDest));
            findAdjacentIslands(islands, point, adjacentIslands);

            if (adjacentIslands.size() > 2)
                return 0;
        }

        // down left
        rowDest = row + 1;
        colDest = col - 1;
        if (isInBounds(rowDest, colDest, grid)) {
            Point point = new Point(rowDest, colDest, grid.get(rowDest).get(colDest));
            findAdjacentIslands(islands, point, adjacentIslands);

            if (adjacentIslands.size() > 2)
                return 0;
        }

        // down
        rowDest = row + 1;
        colDest = col;
        if (isInBounds(rowDest, colDest, grid)) {
            Point point = new Point(rowDest, colDest, grid.get(rowDest).get(colDest));
            findAdjacentIslands(islands, point, adjacentIslands);

            if (adjacentIslands.size() > 2)
                return 0;
        }

        // down right
        rowDest = row + 1;
        colDest = col + 1;
        if (isInBounds(rowDest, colDest, grid)) {
            Point point = new Point(rowDest, colDest, grid.get(rowDest).get(colDest));
            findAdjacentIslands(islands, point, adjacentIslands);

            if (adjacentIslands.size() > 2)
                return 0;
        }

        if (adjacentIslands.size() != 2)
            return 0;

        List<List<Point>> asList = new ArrayList<>(adjacentIslands);
        return calculateIslandValue(asList.getFirst()) * calculateIslandValue(asList.getLast());
    }

    private static void findAdjacentIslands(List<List<Point>> islands, Point point, Set<List<Point>> adjacentIslands) {
        // find its adjacent island and check if its already in the adjacent islands list
        for (List<Point> island : islands) {
            if (island.contains(point)) {
                adjacentIslands.add(island);
            }
        }
    }

    private int calculateIslandValue(List<Point> points) {
        StringBuilder sb = new StringBuilder();
        for (Point point : points) {
            sb.append(point.getCharacter());
        }

        return Integer.parseInt(sb.toString());
    }

    private boolean isInBounds(int row, int col, List<List<Character>> grid) {
        if (row < 0 || row == grid.size())
            return false;
        if (col < 0 || col == grid.get(0).size())
            return false;

        return true;
    }

    private void printGrid(List<List<Character>> grid) {

        int N = grid.getFirst().size();

        for (int i = 0; i < N; i++)
            System.out.print("-");
        System.out.println();

        for (List<Character> line : grid) {
            for (Character c : line)
                System.out.printf("%c", c);
            System.out.println();
        }

        for (int i = 0; i < N; i++)
            System.out.print("-");
        System.out.println();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    private static class Point {
        int x;
        int y;
        Character character;

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
            return "[" + x + "][" + y + "]";
        }
    }

}
