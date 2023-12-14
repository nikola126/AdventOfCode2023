package com.adventofcode2023;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Day14 {
    public final String FILE_DIR = "./src/main/resources/day14.txt";
    public File file = new File(FILE_DIR);
    public Scanner scanner;

    int ROWS = 0;
    int COLS = 0;

    public Day14() {
        try {
            partOne();
        } catch (Exception e) {
            System.out.println("Error with Day 14: " + e.getMessage());
        }
    }

    public void partOne() throws FileNotFoundException {
        scanner = new Scanner(file);

        Map<String, Character> grid = readGrid(scanner);
        shiftNorth(grid);

        System.out.println("Day 14 Part 1: " + calculateWeightOnNorthColumns(grid));
    }

    private Map<String, Character> readGrid(Scanner scanner) {
        Map<String, Character> grid = new LinkedHashMap<>();

        int row = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            COLS = line.length();
            for (int col = 0; col < line.length(); col++) {
                Character c = line.charAt(col);

                String coord = "" + row + "_" + col;
                grid.put(coord, c);
            }
            row += 1;
        }

        ROWS = row;

        return grid;
    }

    private void printGrid(Map<String, Character> grid) {
        for (int row = 0; row < ROWS; row++) {
            System.out.print("|");
            for (int col = 0; col < COLS; col++) {
                String coord = "" + row + "_" + col;
                System.out.printf("%s", grid.get(coord));
            }
            System.out.println("|");
        }
        System.out.println();
    }

    private String convertToString(Map<String, Character> grid) {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                String coord = "" + row + "_" + col;
                Character c = grid.get(coord);
                sb.append(c);
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    private void shiftNorth(Map<String, Character> grid) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                String coord = coordinatesToString(row, col);
                Character c = grid.get(coord);

                if (c == '.' || c == '#')
                    continue;

                // Determine settle spot
                if (row > 0) {
                    String northMostCoord = "";
                    for (int northRow = row - 1; northRow >= 0; northRow--) {
                        String northCoord = coordinatesToString(northRow, col);

                        Character northC = grid.get(northCoord);
                        if (northC == '#' || northC == 'O')
                            break;
                        if (northC == '.')
                            northMostCoord = northCoord;
                    }

                    if (!northMostCoord.isBlank()) {
                        // System.out.printf("Move [%s] from [%s] to [%s]\n", c, coord, northMostCoord);
                        grid.put(coord, '.');
                        grid.put(northMostCoord, 'O');
                        // printGrid(grid);
                    }
                }
            }
        }
    }

    private long calculateWeightOnNorthColumns(Map<String, Character> grid) {
        long total = 0;

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                String coord = "" + row + "_" + col;
                Character c = grid.get(coord);

                if (c == 'O') {
                    long weight = ROWS - row;
                    // System.out.printf("Weight of O at [%s] is: [%d]\n", coord, weight);
                    total += weight;
                }
            }
        }

        return total;
    }

    private String coordinatesToString(int row, int col) {
        return "" + row + "_" + col;
    }

}
