package com.adventofcode2023;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day13 {
    public final String FILE_DIR = "./src/main/resources/day13.txt";
    public File file = new File(FILE_DIR);
    public Scanner scanner;

    public Day13() {
        try {
            partOne();
        } catch (Exception e) {
            System.out.println("Error with Day 13: " + e.getMessage());
        }
    }

    public void partOne() throws FileNotFoundException {
        scanner = new Scanner(file);

        List<List<String>> grids = readGrids(scanner);

        int answer = 0;
        for (List<String> grid : grids) {
            int rowOfReflection = determineRowOfReflection(grid);
            int colOfReflection = determineRowOfReflection(transpose(grid));
            int sum = colOfReflection + 100 * rowOfReflection;
            answer += sum;
        }

        System.out.println("Day 13 Part 1: " + answer);
    }

    private void printGrid(List<String> grid) {
        System.out.println("----- ----- ----- ----- -----");
        for (String gridLine : grid) {
            for (Character c : gridLine.toCharArray())
                System.out.printf("%c", c);
            System.out.println();
        }
        System.out.println("----- ----- ----- ----- -----");
    }

    private List<List<String>> readGrids(Scanner scanner) {
        List<List<String>> grids = new ArrayList<>();

        List<String> currentGrid = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (line.isBlank()) {
                grids.add(currentGrid);
                currentGrid = new ArrayList<>();
                continue;
            }
            currentGrid.add(line);
        }
        grids.add(currentGrid);

        return grids;
    }

    private List<String> transpose(List<String> grid) {
        List<String> transposed = new ArrayList<>();

        int ROWS = grid.size();
        int COLS = grid.getFirst().length();

        StringBuilder sb = new StringBuilder();
        for (int col = 0; col < COLS; col++) {
            sb.setLength(0);
            for (int row = 0; row < ROWS; row++) {
                sb.append(grid.get(row).charAt(col));
            }
            transposed.add(sb.reverse().toString());
        }

        return transposed;
    }

    private int determineRowOfReflection(List<String> grid) {
        int ROWS = grid.size();

        for (int row = 0; row < ROWS - 1; row++) {
            String line = grid.get(row);
            String nextLine = grid.get(row + 1);

            if (line.equals(nextLine)) {
                int maxReflectionLength = Math.min(row, ROWS - (row + 1) - 1);

                boolean maxReflectionReached = true;
                for (int refl = 0; refl < maxReflectionLength; refl++) {
                    String lineOver = grid.get(row - refl - 1);
                    String lineBelow = grid.get(row + refl + 2);

                    if (!lineOver.equals(lineBelow)) {
                        maxReflectionReached = false;
                        break;
                    }
                }

                if (maxReflectionReached) {
                    return row + 1;
                }

            }
        }
        return 0;
    }

}
