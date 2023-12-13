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
            partTwo();
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

    private boolean smudgeFixed = false;

    public void partTwo() throws FileNotFoundException {
        scanner = new Scanner(file);

        List<List<String>> grids = readGrids(scanner);

        int answer = 0;
        for (List<String> grid : grids) {
            smudgeFixed = false;
            int rowOfReflection = 0;
            int colOfReflection = 0;

            rowOfReflection = determineRowOfReflectionWithSmudges(grid);

            if (!smudgeFixed || rowOfReflection == 0)
                colOfReflection = determineRowOfReflectionWithSmudges(transpose(grid));

            int sum = colOfReflection + 100 * rowOfReflection;
            answer += sum;
        }

        System.out.println("Day 13 Part 2: " + answer);
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
                int maxReflectionLength = Math.min(row, ROWS - 1 - (row + 1));

                boolean maxReflectionReached = true;
                for (int ref = 0; ref < maxReflectionLength; ref++) {
                    String lineOver = grid.get(row - ref - 1);
                    String lineBelow = grid.get(row + ref + 2);

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

    private int determineRowOfReflectionWithSmudges(List<String> grid) {
        int ROWS = grid.size();

        int smudgeReplacementIndex = 0;
        String smudgeReplacementLine = "";

        for (int row = 0; row < ROWS - 1; row++) {
            String line = grid.get(row);
            String nextLine = grid.get(row + 1);

            int differences = countDifferences(line, nextLine);

            if (differences == 1 && smudgeFixed)
                return 0; // cannot fix another smudge and lines are not equal, so no reflection

            if (line.equals(nextLine) || differences == 1) {
                if (differences == 1) {
                    // remember where the candidate smudge is and keep looking forward
                    smudgeReplacementIndex = row;
                    smudgeReplacementLine = nextLine;
                    smudgeFixed = true;
                }

                int maxReflectionLength = Math.min(row, ROWS - 1 - (row + 1));

                boolean maxReflectionReached = true;

                for (int ref = 0; ref < maxReflectionLength; ref++) {
                    String lineOver = grid.get(row - ref - 1);
                    String lineBelow = grid.get(row + ref + 2);

                    if (lineOver.equals(lineBelow))
                        continue;

                    if (countDifferences(lineOver, lineBelow) == 1) {
                        if (smudgeFixed) {
                            // cannot fix another smudge, lines are not equal and edge was also not reached
                            maxReflectionReached = false;
                            break;
                        } else {
                            // remember where the candidate smudge is and keep looking forward
                            smudgeReplacementIndex = row - ref - 1;
                            smudgeReplacementLine = lineBelow;

                            smudgeFixed = true;
                            continue;
                        }
                    }

                    if (countDifferences(lineOver, lineBelow) != 1) {
                        // more than one difference, it's not a smudge, so it's not a reflection
                        maxReflectionReached = false;
                        break;
                    }
                }

                if (!smudgeFixed) {
                    // maybe the reflection is in another row, keep looking
                }
                else if (maxReflectionReached) {
                    // smudge was fixed, CHANGE THE GRID!
                    // and return (add one because the row itself contributes to the length of the reflection)
                    grid.set(smudgeReplacementIndex, smudgeReplacementLine);
                    return row + 1;
                }
                else {
                    // the smudge was fixed, but the edge was not reached
                    // ignore the last candidate smudge and start over
                    smudgeFixed = false;
                }
            }
        }
        return 0;
    }

    private int countDifferences(String a, String b) {
        int diffs = 0;
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt(i))
                diffs += 1;
        }

        return diffs;
    }

}
