package com.adventofcode2023;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day4 {
    public final String FILE_DIR = "./src/main/resources/day4.txt";
    public File file = new File(FILE_DIR);
    public Scanner scanner;

    public Day4() {
        try {
            partOne();
            partTwo();
        } catch (Exception e) {
            System.out.println("Error with Day 4: " + e.getMessage());
        }
    }

    public void partOne() throws FileNotFoundException {
        scanner = new Scanner(file);

        int totalScore = 0;
        int index = 0;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (line.isBlank())
                continue;

            totalScore += new Card(index++, line).getPoints();
        }

        System.out.println("Day 4 Part 1: " + totalScore);
    }

    public void partTwo() throws FileNotFoundException {
        scanner = new Scanner(file);

        List<Card> cards = new ArrayList<>();
        int index = 0;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (line.isBlank())
                continue;

            cards.add(new Card(index++, line));
        }

        Queue<Card> game = new LinkedList<>(cards);

        int countPlayed = 0;

        while (!game.isEmpty()) {
            Card card = game.poll();
            countPlayed += 1;

            for (int i = card.getIndex() + 1; i <= card.getIndex() + card.getMatches(); i++) {
                game.add(cards.get(i));
            }
        }

        System.out.println("Day 4 Part 2: " + countPlayed);
    }

    @Getter
    @Setter
    private static class Card {
        private int index;
        private String line;
        private Set<Integer> winning = new HashSet<>();
        private Set<Integer> current = new HashSet<>();
        private int points;
        private int matches;

        public Card(int index, String line) {
            this.index = index;
            this.line = line;

            String winningText = line.split("\\|")[0].split(":")[1].trim();
            String currentText = line.split("\\|")[1].trim();

            for (String asText : winningText.split(" ")) {
                asText = asText.trim();
                if (asText.isBlank())
                    continue;

                Integer number = Integer.parseInt(asText.trim());
                winning.add(number);
            }

            for (String asText : currentText.split(" ")) {
                asText = asText.trim();
                if (asText.isBlank())
                    continue;

                Integer number = Integer.parseInt(asText.trim());
                current.add(number);
            }

            current.retainAll(winning);

            matches = current.size();

            if (!current.isEmpty())
                points = (int) Math.pow(2, current.size() - 1);
        }

        @Override
        public String toString() {
            return String.format("[%d] [%s] POINTS:[%d] MATCHES:[%d]", index + 1, line, points, matches);
        }
    }

}
