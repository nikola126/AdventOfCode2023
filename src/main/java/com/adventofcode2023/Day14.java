package com.adventofcode2023;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day14 {
    public final String FILE_DIR = "./src/main/resources/day14.txt";
    public File file = new File(FILE_DIR);
    public Scanner scanner;

    int ROWS = 0;
    int COLS = 0;

    public Day14() {
        try {
            partOne();
            partTwo();
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

    public void partTwo() throws FileNotFoundException {
        scanner = new Scanner(file);

        int BILLION = 1000000000;
        Map<String, Character> grid = readGrid(scanner);
        Map<Integer, String> previouslySeenMapConfigurations = new HashMap<>();

        for (int i = 0; i < BILLION; i++) {
            cycle(grid);
            String mapState = convertToString(grid);

            if (previouslySeenMapConfigurations.containsValue(mapState)) {
                // If the current grid state has already been seen, a cycle has been found
                int start = previouslySeenMapConfigurations.entrySet().stream()
                        .filter(entry -> entry.getValue().equals(mapState))
                        .findFirst().get().getKey();

                // "Advance" to that position as many cycles forward as possible and finish the simulation
                int remaining = (BILLION - 1 - i) % (i - start);

                for (int j = 0; j < remaining; j++) {
                    cycle(grid);
                }
                System.out.println("Day 14 Part 2: " + calculateWeightOnNorthColumns(grid));
                break;
            } else {
                // Keep the current grid state for later
                previouslySeenMapConfigurations.put(i, mapState);
            }
        }
    }

    private Map<String, Character> readGrid(Scanner scanner) {
        Map<String, Character> grid = new LinkedHashMap<>();

        int row = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            COLS = line.length();
            for (int col = 0; col < line.length(); col++) {
                Character c = line.charAt(col);

                String coord = coordinatesToString(row, col);
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
                String coord = coordinatesToString(row, col);
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
                String coord = coordinatesToString(row, col);
                Character c = grid.get(coord);
                sb.append(c);
            }
            sb.append("|");
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
                        if (northC == '#')
                            break;
                        if (northC == '.')
                            northMostCoord = northCoord;
                    }

                    if (!northMostCoord.isBlank()) {
                        grid.put(coord, '.');
                        grid.put(northMostCoord, 'O');
                    }
                }
            }
        }
    }

    private void shiftWest(Map<String, Character> grid) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                String coord = coordinatesToString(row, col);
                Character c = grid.get(coord);

                if (c == '.' || c == '#')
                    continue;

                // Determine settle spot
                if (col > 0) {
                    String westMostCoord = "";
                    for (int westCol = col - 1; westCol >= 0; westCol--) {
                        String westCoord = coordinatesToString(row, westCol);

                        Character westC = grid.get(westCoord);
                        if (westC == '#' || westC == 'O')
                            break;
                        if (westC == '.')
                            westMostCoord = westCoord;
                    }

                    if (!westMostCoord.isBlank()) {
                        grid.put(coord, '.');
                        grid.put(westMostCoord, 'O');
                    }
                }
            }
        }
    }

    private void shiftSouth(Map<String, Character> grid) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                String coord = coordinatesToString(row, col);
                Character c = grid.get(coord);

                if (c == '.' || c == '#')
                    continue;

                // Determine settle spot
                if (row < ROWS) {
                    String southMostCoord = "";
                    for (int southRow = row + 1; southRow < ROWS; southRow++) {
                        String southCoord = coordinatesToString(southRow, col);

                        Character southC = grid.get(southCoord);
                        if (southC == '#')
                            break;
                        if (southC == '.')
                            southMostCoord = southCoord;
                    }

                    if (!southMostCoord.isBlank()) {
                        grid.put(coord, '.');
                        grid.put(southMostCoord, 'O');
                    }
                }
            }
        }
    }

    private void shiftEast(Map<String, Character> grid) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                String coord = coordinatesToString(row, col);
                Character c = grid.get(coord);

                if (c == '.' || c == '#')
                    continue;

                // Determine settle spot
                if (col < COLS) {
                    String eastMostCoord = "";
                    for (int eastCol = col + 1; eastCol < COLS; eastCol++) {
                        String eastCoord = coordinatesToString(row, eastCol);

                        Character eastC = grid.get(eastCoord);
                        if (eastC == '#')
                            break;
                        if (eastC == '.')
                            eastMostCoord = eastCoord;
                    }

                    if (!eastMostCoord.isBlank()) {
                        grid.put(coord, '.');
                        grid.put(eastMostCoord, 'O');
                    }
                }
            }
        }
    }

    private void cycle(Map<String, Character> grid) {
        shiftNorth(grid);
        shiftWest(grid);
        shiftSouth(grid);
        shiftEast(grid);
    }

    private long calculateWeightOnNorthColumns(Map<String, Character> grid) {
        long total = 0;

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                String coord = coordinatesToString(row, col);
                Character c = grid.get(coord);

                if (c == 'O') {
                    total += (ROWS - row);
                }
            }
        }

        return total;
    }

    private String coordinatesToString(int row, int col) {
        return "" + row + "_" + col;
    }

}
