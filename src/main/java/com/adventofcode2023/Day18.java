package com.adventofcode2023;

import lombok.*;

import java.io.File;
import java.util.*;

public class Day18 {
    public final String FILE_DIR = "./src/main/resources/day18.txt";
    public File file = new File(FILE_DIR);
    public Scanner scanner;

    public Day18() {
        try {
            partOne();
            partTwo();
        } catch (Exception e) {
            System.out.println("Error with Day 18: " + e.getMessage());
        }
    }

    // https://www.youtube.com/watch?v=bGWK76_e-LM
    public void partOne() throws Exception {
        scanner = new Scanner(file);

        List<Direction> directions = readDirectionsPartOne(scanner);

        List<Point> points = new ArrayList<>();
        points.add(new Point(0, 0));

        long boundary = 0;
        for (Direction direction : directions) {
            Point prevPoint = points.getLast();

            long x = prevPoint.getX();
            long y = prevPoint.getY();

            switch (direction.getDirection()) {
                case 'L': {
                    x -= direction.getSteps();
                    break;
                }
                case 'R': {
                    x += direction.getSteps();
                    break;
                }
                case 'U': {
                    y += direction.getSteps();
                    break;
                }
                case 'D': {
                    y -= direction.getSteps();
                    break;
                }
                default:
                    throw new Exception("Unknown coordinate!");
            }

            Point currentPoint = new Point(x, y);
            points.add(currentPoint);

            boundary += direction.getSteps();
        }

        long internalArea = Math.abs(shoelaceFormula(points));
        long totalArea = internalArea - (boundary / 2) + 1;

        long answer = totalArea + boundary;
        System.out.println("Day 20 Part 1: " + answer);
    }

    // https://www.youtube.com/watch?v=bGWK76_e-LM
    public void partTwo() throws Exception {
        scanner = new Scanner(file);

        List<Direction> directions = readDirectionsPartTwo(scanner);

        List<Point> points = new ArrayList<>();
        points.add(new Point(0, 0));

        long boundary = 0;
        for (Direction direction : directions) {
            Point prevPoint = points.getLast();

            long x = prevPoint.getX();
            long y = prevPoint.getY();

            switch (direction.getDirection()) {
                case 'L': {
                    x -= direction.getSteps();
                    break;
                }
                case 'R': {
                    x += direction.getSteps();
                    break;
                }
                case 'U': {
                    y += direction.getSteps();
                    break;
                }
                case 'D': {
                    y -= direction.getSteps();
                    break;
                }
                default:
                    throw new Exception("Unknown coordinate!");
            }

            Point currentPoint = new Point(x, y);
            points.add(currentPoint);

            boundary += direction.getSteps();
        }

        long internalArea = Math.abs(shoelaceFormula(points));
        long totalArea = internalArea - (boundary / 2) + 1;

        long answer = totalArea + boundary;
        System.out.println("Day 20 Part 2: " + answer);
    }

    private List<Direction> readDirectionsPartOne(Scanner scanner) {
        List<Direction> directions = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] tokens = line.split(" ");

            char direction = tokens[0].charAt(0);
            long steps = Integer.parseInt(tokens[1]);

            directions.add(new Direction(direction, steps));
        }

        return directions;
    }

    private List<Direction> readDirectionsPartTwo(Scanner scanner) {
        List<Direction> directions = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] tokens = line.split(" ");

            String hex = tokens[2].substring(1, tokens[2].length() - 1);
            char direction = hex.charAt(hex.length() - 1);
            hex = hex.substring(0, hex.length() - 1);

            if (direction == '0')
                direction = 'R';
            else if (direction == '1')
                direction = 'D';
            else if (direction == '2')
                direction = 'L';
            else if (direction == '3')
                direction = 'U';

            long steps = Long.decode(hex);

            directions.add(new Direction(direction, steps));
        }

        return directions;
    }

    // https://en.m.wikipedia.org/wiki/Shoelace_formula
    private long shoelaceFormula(List<Point> visited) {
        long sum = 0;
        Point a;
        Point b;

        for (int i = 0; i < visited.size() - 1; i++) {
            a = visited.get(i);
            b = visited.get(i + 1);

            sum += (a.getX() * b.getY() - b.getX() * a.getY());
        }

        // Complete the shoelace using the first and last points
        a = visited.getLast();
        b = visited.getFirst();

        sum += (a.getX() * b.getY() - b.getX() * a.getY());

        return sum / 2;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    private class Direction {
        char direction;
        long steps;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    private class Point {
        long x;
        long y;

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Point other))
                return false;

            return x == other.x && y == other.y;
        }

        @Override
        public String toString() {
            return String.format("Point [%d][%d]", x, y);
        }
    }

}
