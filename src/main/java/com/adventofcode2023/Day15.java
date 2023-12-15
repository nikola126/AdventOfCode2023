package com.adventofcode2023;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day15 {
    public final String FILE_DIR = "./src/main/resources/day15.txt";
    public File file = new File(FILE_DIR);
    public Scanner scanner;

    public Day15() {
        try {
            partOne();
            partTwo();
        } catch (Exception e) {
            System.out.println("Error with Day 15: " + e.getMessage());
        }
    }

    public void partOne() throws FileNotFoundException {
        scanner = new Scanner(file);

        long sum = 0;
        while (scanner.hasNext()) {
            String[] tokens = scanner.nextLine().split(",");

            for (String token : tokens) {
                token = token.trim();
                if (token.isBlank())
                    continue;

                long hash = hash(token);
                sum += hash;
            }
        }

        System.out.println("Day 15 Part 1: " + sum);
    }

    public void partTwo() throws FileNotFoundException {
        scanner = new Scanner(file);

        List<Box> boxes = new ArrayList<>(256);
        for (int i = 0; i < 256; i++) {
            boxes.add(new Box(i, new ArrayList<>()));
        }

        while (scanner.hasNext()) {
            String[] tokens = scanner.nextLine().split(",");

            for (String token : tokens) {
                token = token.trim();
                if (token.isBlank())
                    continue;

                if (token.contains("=")) {
                    String lensType = token.split("=")[0].trim();
                    int boxIndex = hash(token.split("=")[0].trim());

                    boolean isSameTypeLensAlreadyInBox = boxes.get(boxIndex).getLenses()
                            .stream()
                            .anyMatch(l -> l.startsWith(lensType));

                    if (!isSameTypeLensAlreadyInBox)
                        boxes.get(boxIndex).getLenses().add(token);
                    else {
                        for (int i = 0; i < boxes.get(boxIndex).getLenses().size(); i++) {
                            String lensInside = boxes.get(boxIndex).getLenses().get(i);

                            if (lensInside.startsWith(lensType)) {
                                boxes.get(boxIndex).getLenses().set(i, token);
                                break;
                            }
                        }
                    }

                } else {
                    String lens = token.split("-")[0].trim();
                    int boxIndex = hash(lens);

                    List<String> shifted = new ArrayList<>();
                    for (String l : boxes.get(boxIndex).getLenses()) {
                        if (!l.startsWith(lens))
                            shifted.add(l);
                    }

                    boxes.get(boxIndex).setLenses(shifted);
                }
            }
        }

        int power = calculateFocusingPower(boxes);
        System.out.println("Day 15 Part 2: " + power);
    }

    private int calculateFocusingPower(List<Box> boxes) {
        int total = 0;

        for (Box box : boxes) {
            for (int i = 0; i < box.getLenses().size(); i++) {
                String lens = box.getLenses().get(i);
                int focalLength = Integer.parseInt(lens.split("=")[1]);

                int power = (box.getNumber() + 1) * (i + 1) * focalLength;
                total += power;
            }
        }

        return total;
    }

    private void printBoxes(List<Box> boxes) {
        for (Box box : boxes) {
            if (box.getLenses().isEmpty())
                continue;
            System.out.println("\t" + box);
        }
    }

    private int hash(String string) {
        int value = 0;

        for (Character c : string.toCharArray()) {
            value += (int) c;
            value *= 17;
            value = value % 256;
        }

        return value;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    private static class Box {
        int number;
        List<String> lenses;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("Box %d: [", number));

            for (String lens : lenses) {
                sb.append(lens);
                sb.append(", ");
            }

            sb.append("]");

            return sb.toString();
        }
    }

}
