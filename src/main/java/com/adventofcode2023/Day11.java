package com.adventofcode2023;

import com.adventofcode2023.astar.AStar;
import com.adventofcode2023.astar.Node;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day11 {
    public final String FILE_DIR = "./src/main/resources/day11.txt";
    public File file = new File(FILE_DIR);
    public Scanner scanner;

    public Day11() {
        try {
            partOne();
            parTwo();
        } catch (Exception e) {
            System.out.println("Error with Day 11: " + e.getMessage());
        }
    }

    public void partOne() throws FileNotFoundException {
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

        int ROWS  = grid.size();
        int COLS = grid.getFirst().size();

        List<Point> galaxies = new ArrayList<>();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (grid.get(row).get(col) == '#')
                    galaxies.add(new Point(row, col, '#'));
            }
        }

        List<List<Point>> pairs = new ArrayList<>();
        for (int i = 0; i < galaxies.size(); i++) {
            for (int j = i + 1; j < galaxies.size(); j++) {
                List<Point> pair = new ArrayList<>();
                pair.add(galaxies.get(i));
                pair.add(galaxies.get(j));
                pairs.add(pair);
            }
        }

        List<Integer> emptyRowIndices = new ArrayList<>();
        List<Integer> emptyColIndices = new ArrayList<>();

        determineExpandedRowsAndCols(grid, emptyRowIndices, emptyColIndices);

        long sum = 0;
        long expansionFactor = 1;
        for (List<Point> pair : pairs) {
            Set<Integer> passedRowExpansions = new HashSet<>();
            Set<Integer> passedColumnExpansions = new HashSet<>();
            Node initialNode = new Node(pair.getFirst().getX(), pair.getFirst().getY());
            Node finalNode = new Node(pair.getLast().getX(), pair.getLast().getY());

            AStar aStar = new AStar(ROWS, COLS, initialNode, finalNode);
            List<Node> path = aStar.findPath();

            long pathLength = 0;

            for (Node node : path) {
                if (emptyRowIndices.contains(node.getRow()) && !passedRowExpansions.contains(node.getRow())) {
                    pathLength += expansionFactor;
                    passedRowExpansions.add(node.getRow());
                }
                else if (emptyColIndices.contains(node.getCol()) && !passedColumnExpansions.contains(node.getCol())) {
                    pathLength += expansionFactor;
                    passedColumnExpansions.add(node.getCol());
                }
            }

            pathLength += path.size() - 1;
            sum += pathLength;
        }

        System.out.println("Day 11 Part 1: " + sum);
    }

    public void parTwo() throws FileNotFoundException {
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

        int ROWS  = grid.size();
        int COLS = grid.getFirst().size();

        List<Point> galaxies = new ArrayList<>();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (grid.get(row).get(col) == '#')
                    galaxies.add(new Point(row, col, '#'));
            }
        }


        List<List<Point>> pairs = new ArrayList<>();
        for (int i = 0; i < galaxies.size(); i++) {
            for (int j = i + 1; j < galaxies.size(); j++) {
                List<Point> pair = new ArrayList<>();
                pair.add(galaxies.get(i));
                pair.add(galaxies.get(j));
                pairs.add(pair);
            }
        }

        List<Integer> emptyRowIndices = new ArrayList<>();
        List<Integer> emptyColIndices = new ArrayList<>();

        determineExpandedRowsAndCols(grid, emptyRowIndices, emptyColIndices);

        long sum = 0;
        long expansionFactor = 1000000 - 1;
        for (List<Point> pair : pairs) {
            Set<Integer> passedRowExpansions = new HashSet<>();
            Set<Integer> passedColumnExpansions = new HashSet<>();
            Node initialNode = new Node(pair.getFirst().getX(), pair.getFirst().getY());
            Node finalNode = new Node(pair.getLast().getX(), pair.getLast().getY());

            AStar aStar = new AStar(ROWS, COLS, initialNode, finalNode);
            List<Node> path = aStar.findPath();

            long pathLength = 0;

            for (Node node : path) {
                if (emptyRowIndices.contains(node.getRow()) && !passedRowExpansions.contains(node.getRow())) {
                    pathLength += expansionFactor;
                    passedRowExpansions.add(node.getRow());
                }
                else if (emptyColIndices.contains(node.getCol()) && !passedColumnExpansions.contains(node.getCol())) {
                    pathLength += expansionFactor;
                    passedColumnExpansions.add(node.getCol());
                }
            }

            pathLength += path.size() - 1;
            sum += pathLength;
        }

        System.out.println("Day 11 Part 2: " + sum);
    }

    private List<List<Character>> expandGrid(List<List<Character>> original) {
        List<List<Character>> expandedRows = new ArrayList<>();
        for (List<Character> gridLine : original) {
            if (gridLine.stream().noneMatch(c -> c == '#')) {
                expandedRows.add(gridLine);
            }
            expandedRows.add(gridLine);
        }

        List<Integer> emptyColumnIndices = new ArrayList<>();

        for (int col = 0; col < expandedRows.getFirst().size(); col++) {
            boolean isColumnEmpty = true;
            for (List<Character> expandedRow : expandedRows) {
                if (expandedRow.get(col) == '#') {
                    isColumnEmpty = false;
                    break;
                }
            }

            if (isColumnEmpty) {
                emptyColumnIndices.add(col);
            }
        }

        List<List<Character>> expandedCols = new ArrayList<>();

        for (List<Character> gridLine : expandedRows) {
            List<Character> expandedCol = new ArrayList<>();
            for (int i = 0; i < gridLine.size(); i++) {
                if (emptyColumnIndices.contains(i))
                    expandedCol.add('.');
                expandedCol.add(gridLine.get(i));
            }
            expandedCols.add(expandedCol);
        }

        return expandedCols;
    }

    private void determineExpandedRowsAndCols(List<List<Character>> original, List<Integer> expandedRowIndices, List<Integer> expandedColIndices) {
        int rowIndex = 0;

        for (List<Character> gridLine : original) {
            if (gridLine.stream().noneMatch(c -> c == '#')) {
                expandedRowIndices.add(rowIndex);
            }
            rowIndex += 1;
        }

        for (int col = 0; col < original.getFirst().size(); col++) {
            boolean isColumnEmpty = true;
            for (List<Character> expandedRow : original) {
                if (expandedRow.get(col) == '#') {
                    isColumnEmpty = false;
                    break;
                }
            }

            if (isColumnEmpty) {
                expandedColIndices.add(col);
            }
        }
    }

    private void printGrid(List<List<Character>> grid) {
        for (List<Character> gridLine : grid) {
            for (Character c : gridLine) {
                System.out.printf("%c", c);
            }
            System.out.println();
        }
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
