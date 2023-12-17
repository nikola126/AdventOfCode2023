package com.adventofcode2023;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day17 {
    public final String FILE_DIR = "./src/main/resources/day17.txt";
    public File file = new File(FILE_DIR);
    public Scanner scanner;

    public Day17() {
        try {
            partOne();
            partTwo();
        } catch (Exception e) {
            System.out.println("Error with Day 17: " + e.getMessage());
        }
    }

    // Based on https://github.com/hyper-neutrino/advent-of-code
    public void partOne() throws FileNotFoundException {
        scanner = new Scanner(file);

        List<List<Integer>> grid = readGrid(scanner);

        int ROWS = grid.size();
        int COLS = grid.getFirst().size();


        PriorityQueue<CruciblePoint> priorityQueue = new PriorityQueue<>();

        // keeps track of the different ways the crucible enters a given point
        // row, col, directionRow, directionCol and sameDirectionSteps matter
        // heat is NOT included, because it's incremented after each step, and the loop will NOT be detected!
        Set<String> visited = new HashSet<>();

        // starting at top left, with zero heat, no previous movement and zero steps
        priorityQueue.add(new CruciblePoint(0, 0, 0, 0, 0, 0));

        while (!priorityQueue.isEmpty()) {
            CruciblePoint point = priorityQueue.poll();

            // End is reached, and solution is optimal (because of priority queue)
            if (point.getRow() == ROWS - 1 && point.getCol() == COLS - 1) {
                System.out.println("Day 17 Part 1: " + point.getHeat());
                return;
            }

            // Loop detected, this step can be skipped
            if (!visited.add(point.toStateString())) {
                continue;
            }

            // Keep moving in the same direction if and only if less than 3 steps have already been accumulated
            // and the crucible was previously moving (true every time except at the start)
            if (point.getSameDirectionSteps() < 3 && !(point.getDirectionRow() == 0 && point.getDirectionCol() == 0)) {
                int nextRow = point.getRow() + point.getDirectionRow();
                int nextCol = point.getCol() + point.getDirectionCol();

                // ignore ouf-of-bounds
                if ((0 <= nextRow && nextRow < ROWS) && (0 <= nextCol && nextCol < COLS)) {
                    // increment heat and same direction step counter
                    CruciblePoint nextPoint = new CruciblePoint(
                            nextRow, nextCol,
                            point.getHeat() + grid.get(nextRow).get(nextCol),
                            point.getDirectionRow(), point.getDirectionCol(),
                            point.getSameDirectionSteps() + 1);

                    priorityQueue.add(nextPoint);
                }
            }

            // Also evaluate all possible directions
            List<String> directions = new ArrayList<>();
            directions.add("-1 0"); // left
            directions.add("1 0"); // right
            directions.add("0 -1"); // up
            directions.add("0 1"); // down

            for (String direction : directions) {
                int directionRow = Integer.parseInt(direction.split(" ")[0]);
                int directionCol = Integer.parseInt(direction.split(" ")[1]);

                // same direction is already covered in the previous case
                boolean isInSameDirection = directionRow == point.getDirectionRow() && directionCol == point.getDirectionCol();

                // the crucible cannot turn!
                boolean isInReverseDirection = directionRow == -point.getDirectionRow() && directionCol == -point.getDirectionCol();

                if (!isInSameDirection && !isInReverseDirection) {
                    int nextRow = point.getRow() + directionRow;
                    int nextCol = point.getCol() + directionCol;

                    // ignore ouf-of-bounds
                    if ((0 <= nextRow && nextRow < ROWS) && (0 <= nextCol && nextCol < COLS)) {
                        // increment heat, reset steps counter
                        CruciblePoint nextPoint = new CruciblePoint(
                                nextRow, nextCol,
                                point.getHeat() + grid.get(nextRow).get(nextCol),
                                directionRow, directionCol,
                                1);

                        priorityQueue.add(nextPoint);
                    }
                }
            }
        }
    }

    // Based on https://github.com/hyper-neutrino/advent-of-code
    public void partTwo() throws FileNotFoundException {
        scanner = new Scanner(file);

        List<List<Integer>> grid = readGrid(scanner);

        int ROWS = grid.size();
        int COLS = grid.getFirst().size();


        PriorityQueue<CruciblePoint> priorityQueue = new PriorityQueue<>();

        // keeps track of the different ways the crucible enters a given point
        // row, col, directionRow, directionCol and sameDirectionSteps matter
        // heat is NOT included, because it's incremented after each step, and the loop will NOT be detected!
        Set<String> visited = new HashSet<>();

        // starting at top left, with zero heat, no previous movement and zero steps
        priorityQueue.add(new CruciblePoint(0, 0, 0, 0, 0, 0));

        while (!priorityQueue.isEmpty()) {
            CruciblePoint point = priorityQueue.poll();

            // End is reached, but the crucible must have at least 4 steps in the same direction
            if (point.getRow() == ROWS - 1 && point.getCol() == COLS - 1 && point.getSameDirectionSteps() >= 4) {
                System.out.println("Day 17 Part 2: " + point.getHeat());
                return;
            }

            // Loop detected, this step can be skipped
            if (!visited.add(point.toStateString())) {
                continue;
            }

            // Keep moving in the same direction if and only if less than 10 steps have already been accumulated
            // and the crucible was previously moving (true every time except at the start)
            if (point.getSameDirectionSteps() < 10 && !(point.getDirectionRow() == 0 && point.getDirectionCol() == 0)) {
                int nextRow = point.getRow() + point.getDirectionRow();
                int nextCol = point.getCol() + point.getDirectionCol();

                // ignore ouf-of-bounds
                if ((0 <= nextRow && nextRow < ROWS) && (0 <= nextCol && nextCol < COLS)) {
                    // increment heat and same direction step counter
                    CruciblePoint nextPoint = new CruciblePoint(
                            nextRow, nextCol,
                            point.getHeat() + grid.get(nextRow).get(nextCol),
                            point.getDirectionRow(), point.getDirectionCol(),
                            point.getSameDirectionSteps() + 1);

                    priorityQueue.add(nextPoint);
                }
            }

            // Try turning only if the crucible was already moving for 4 steps
            // Add exception for the start point when the crucible is not moving at all
            if (point.getSameDirectionSteps() >= 4 || (point.getDirectionRow() == 0 && point.getDirectionCol() == 0)) {
                List<String> directions = new ArrayList<>();
                directions.add("-1 0"); // left
                directions.add("1 0"); // right
                directions.add("0 -1"); // up
                directions.add("0 1"); // down

                for (String direction : directions) {
                    int directionRow = Integer.parseInt(direction.split(" ")[0]);
                    int directionCol = Integer.parseInt(direction.split(" ")[1]);

                    // same direction is already covered in the previous case
                    boolean isInSameDirection = directionRow == point.getDirectionRow() && directionCol == point.getDirectionCol();

                    // the crucible cannot turn!
                    boolean isInReverseDirection = directionRow == -point.getDirectionRow() && directionCol == -point.getDirectionCol();

                    if (!isInSameDirection && !isInReverseDirection) {
                        int nextRow = point.getRow() + directionRow;
                        int nextCol = point.getCol() + directionCol;

                        // ignore ouf-of-bounds
                        if ((0 <= nextRow && nextRow < ROWS) && (0 <= nextCol && nextCol < COLS)) {
                            // increment heat, reset steps counter
                            CruciblePoint nextPoint = new CruciblePoint(
                                    nextRow, nextCol,
                                    point.getHeat() + grid.get(nextRow).get(nextCol),
                                    directionRow, directionCol,
                                    1);

                            priorityQueue.add(nextPoint);
                        }
                    }
                }
            }
        }
    }

    private List<List<Integer>> readGrid(Scanner scanner) {
        List<List<Integer>> grid = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            List<Integer> gridLine = new ArrayList<>();
            for (Character c : line.toCharArray())
                gridLine.add((int) c - '0');
            grid.add(gridLine);
        }

        return grid;
    }

    private void printGrid(List<List<Integer>> grid) {
        int N = grid.getFirst().size();

        for (int i = 0; i < N; i++)
            System.out.print("-");
        System.out.println();

        for (List<Integer> line : grid) {
            for (Integer d : line)
                System.out.printf("%d", d);
            System.out.println();
        }

        for (int i = 0; i < N; i++)
            System.out.print("-");
        System.out.println();
    }

    // Class used in the priority queue
    @AllArgsConstructor
    @Getter
    @Setter
    private static class CruciblePoint implements Comparable<CruciblePoint> {
        int row;
        int col;
        int heat;
        int directionRow;
        int directionCol;
        int sameDirectionSteps;

        @Override
        public String toString() {
            String direction = "Not moving";
            if (directionRow == 0 && directionCol == 1)
                direction = "Moving left";
            else if (directionRow == 0 && directionCol == -1)
                direction = "Moving right";
            else if (directionRow == 1 && directionCol == 0)
                direction = "Moving down";
            else if (directionRow == -1 && directionCol == 0)
                direction = "Moving up";

            return String.format("Heat [%d] [%d][%d] [%s] Steps: [%d]",
                    heat, row, col, direction, sameDirectionSteps);
        }

        // the visited set uses this string as input, to determine loops in the pathfinding function
        // heat is not included, as it's always increasing
        public String toStateString() {
            return String.format("[%d][%d] [%d][%d] [%d]",
                    row, col, directionRow, directionCol, sameDirectionSteps);
        }

        // prioritize heat over everything else
        @Override
        public int compareTo(CruciblePoint that) {
            return Comparator.comparing(CruciblePoint::getHeat)
                    .thenComparing(CruciblePoint::getRow)
                    .thenComparing(CruciblePoint::getCol)
                    .thenComparing(CruciblePoint::getDirectionRow)
                    .thenComparing(CruciblePoint::getDirectionCol)
                    .thenComparing(CruciblePoint::getSameDirectionSteps)
                    .compare(this, that);
        }
    }

}
