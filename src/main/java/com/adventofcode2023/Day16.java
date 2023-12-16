package com.adventofcode2023;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day16 {
    public final String FILE_DIR = "./src/main/resources/day16.txt";
    public File file = new File(FILE_DIR);
    public Scanner scanner;

    public Day16() {
        try {
            partOne();
            partTwo();
        } catch (Exception e) {
            System.out.println("Error with Day 16: " + e.getMessage());
        }
    }

    int ROWS = -1;
    int COLS = -1;
    Set<String> rayStates = new HashSet<>();
    Set<String> energizedCells = new HashSet<>();

    public void partOne() throws FileNotFoundException {
        scanner = new Scanner(file);

        List<List<String>> grid = readGrid(scanner);

        navigate(grid, 0, 0, true, null);

        System.out.println("Day 16 Part 1: " + energizedCells.size());
    }

    public void partTwo() throws FileNotFoundException {
        scanner = new Scanner(file);

        List<List<String>> grid = readGrid(scanner);

        int maxEnergy = -1;

        // Top edge
        for (int i = 0; i < COLS; i++) {
            rayStates.clear();
            energizedCells.clear();
            navigate(grid, 0, i, null, true);
            maxEnergy = Math.max(maxEnergy, energizedCells.size());
        }

        // Left edge
        for (int i = 0; i < ROWS; i++) {
            rayStates.clear();
            energizedCells.clear();
            navigate(grid, i, 0, true, null);
            maxEnergy = Math.max(maxEnergy, energizedCells.size());
        }

        // Right edge
        for (int i = 0; i < ROWS; i++) {
            rayStates.clear();
            energizedCells.clear();
            navigate(grid, i, COLS - 1, false, null);
            maxEnergy = Math.max(maxEnergy, energizedCells.size());
        }

        // Bottom edge
        for (int i = 0; i < COLS; i++) {
            rayStates.clear();
            energizedCells.clear();
            navigate(grid, ROWS - 1, i, null, false);
            maxEnergy = Math.max(maxEnergy, energizedCells.size());
        }

        System.out.println("Day 16 Part 2: " + maxEnergy);
    }

    private List<List<String>> readGrid(Scanner scanner) {
        List<List<String>> grid = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            List<String> gridLine = new ArrayList<>();
            for (Character c : line.toCharArray()) {
                gridLine.add(String.valueOf(c));
            }

            grid.add(gridLine);
        }

        ROWS = grid.size();
        COLS = grid.getFirst().size();

        return grid;
    }

    private void navigate(List<List<String>> grid, int currentRow, int currentCol, Boolean leftToRight, Boolean topToBottom) {
        if (currentRow < 0 || currentRow >= ROWS)
            return;

        if (currentCol < 0 || currentCol >= COLS)
            return;

        String c = grid.get(currentRow).get(currentCol);

        energizedCells.add(String.format("[%d][%d]", currentRow, currentCol));

        if (!rayStates.add(rayStateToString(currentRow, currentCol, leftToRight, topToBottom))) {
            return;
        }

        switch (c) {
            // Energize, and keep moving in the same direction
            case "/" -> {
                if (leftToRight != null) {
                    if (leftToRight) {
                        navigate(grid, currentRow - 1, currentCol, null, false);
                    } else {
                        navigate(grid, currentRow + 1, currentCol, null, true);
                    }
                } else if (topToBottom != null) {
                    if (topToBottom) {
                        navigate(grid, currentRow, currentCol - 1, false, null);
                    } else {
                        navigate(grid, currentRow, currentCol + 1, true, null);
                    }
                }
            }
            case "\\" -> {
                if (leftToRight != null) {
                    if (leftToRight) {
                        navigate(grid, currentRow + 1, currentCol, null, true);
                    } else {
                        navigate(grid, currentRow - 1, currentCol, null, false);
                    }
                } else if (topToBottom != null) {
                    if (topToBottom) {
                        navigate(grid, currentRow, currentCol + 1, true, null);
                    } else {
                        navigate(grid, currentRow, currentCol - 1, false, null);
                    }
                }
            }
            case "|" -> {
                if (leftToRight != null) {
                    navigate(grid, currentRow - 1, currentCol, null, false);
                    navigate(grid, currentRow + 1, currentCol, null, true);
                } else if (topToBottom != null) {
                    if (topToBottom) {
                        navigate(grid, currentRow + 1, currentCol, null, true);
                    } else {
                        navigate(grid, currentRow - 1, currentCol, null, false);
                    }
                }
            }
            case "-" -> {
                if (leftToRight != null) {
                    if (leftToRight) {
                        navigate(grid, currentRow, currentCol + 1, true, null);
                    } else {
                        navigate(grid, currentRow, currentCol - 1, false, null);
                    }
                } else if (topToBottom != null) {
                    navigate(grid, currentRow, currentCol - 1, false, null);
                    navigate(grid, currentRow, currentCol + 1, true, null);
                }
            }
            default -> {
                // Already on a ray
                // Energize and keep moving in the same direction
                grid.get(currentRow).set(currentCol, "#");
                if (leftToRight != null) {
                    if (leftToRight) {
                        navigate(grid, currentRow, currentCol + 1, true, null);
                    } else {
                        navigate(grid, currentRow, currentCol - 1, false, null);
                    }
                } else if (topToBottom != null) {
                    if (topToBottom) {
                        navigate(grid, currentRow + 1, currentCol, null, true);
                    } else {
                        navigate(grid, currentRow - 1, currentCol, null, false);
                    }
                }
            }
        }
    }

    private void printGrid(List<List<String>> grid) {
        StringBuilder sb = new StringBuilder();
        sb.append("_ ".repeat(Math.max(0, COLS + 1)));
        System.out.println(sb);

        sb.setLength(0);
        for (int i = 0; i < COLS; i++)
            sb.append(" " + i);
        System.out.println(" " + sb);

        int rowIndex = 0;
        for (List<String> gridLine : grid) {
            System.out.printf("%d ", rowIndex++);
            for (String s : gridLine) {
                System.out.printf("%s ", s);
            }
            System.out.println();
        }
    }

    private String rayStateToString(int currentRow, int currentCol, Boolean leftToRight, Boolean topToBottom) {
        String direction;
        if (leftToRight == null)
            direction = topToBottom ? "top to bottom" : "bottom to top";
        else
            direction = leftToRight ? "left to right" : "right to left";

        return String.format("Currently at [%d][%d], moving [%s]\n", currentRow, currentCol, direction);
    }

}
