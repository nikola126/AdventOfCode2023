package com.adventofcode2023;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day2 {
    public final String FILE_DIR = "./src/main/resources/day2.txt";
    public File file = new File(FILE_DIR);
    public Scanner scanner;

    public Day2() {
        try {
            partOne();
            partTwo();
        } catch (Exception e) {
            System.out.println("Error with Day 2: " + e.getMessage());
        }
    }

    public void partOne() throws FileNotFoundException {
        scanner = new Scanner(file);

        int sum = 0;

        int red = 12;
        int green = 13;
        int blue = 14;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            String gameNumberString = line.split(":", 2)[0];
            int gameNumber = Integer.parseInt(gameNumberString.split(" ")[1]);

            String gameString = line.split(":", 2)[1];
            String[] games = gameString.split(";");

            boolean isGamePossible = true;

            for (String game : games) {
                String[] grabs = game.split(",");

                for (String grab : grabs) {
                    grab = grab.trim();

                    int number = Integer.parseInt(grab.split(" ")[0]);
                    String color = grab.split(" ")[1];

                    if (color.equalsIgnoreCase("red") && number > red) {
                        isGamePossible = false;
                        break;
                    }
                    if (color.equalsIgnoreCase("green") && number > green) {
                        isGamePossible = false;
                        break;
                    }
                    if (color.equalsIgnoreCase("blue") && number > blue) {
                        isGamePossible = false;
                        break;
                    }
                }
            }

            if (isGamePossible)
                sum += gameNumber;
        }

        System.out.println("Day 2 Part 1: " + sum);
    }

    public void partTwo() throws FileNotFoundException {
        scanner = new Scanner(file);

        int sum = 0;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            String gameString = line.split(":", 2)[1];
            String[] games = gameString.split(";");

            int minRed = 0;
            int minGreen = 0;
            int minBlue = 0;

            for (String game : games) {
                String[] grabs = game.split(",");

                for (String grab : grabs) {
                    grab = grab.trim();

                    int number = Integer.parseInt(grab.split(" ")[0]);
                    String color = grab.split(" ")[1];

                    if (color.equalsIgnoreCase("red")) {
                        minRed = Math.max(minRed, number);
                    }
                    if (color.equalsIgnoreCase("green")) {
                        minGreen = Math.max(minGreen, number);
                    }
                    if (color.equalsIgnoreCase("blue")) {
                        minBlue = Math.max(minBlue, number);
                    }
                }
            }

            int power = minRed * minGreen * minBlue;
            sum += power;
        }

        System.out.println("Day 2 Part 2: " + sum);
    }
}
