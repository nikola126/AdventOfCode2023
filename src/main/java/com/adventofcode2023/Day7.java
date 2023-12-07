package com.adventofcode2023;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day7 {
    public final String FILE_DIR = "./src/main/resources/day7.txt";
    public File file = new File(FILE_DIR);
    public Scanner scanner;

    public Day7() {
        try {
            partOne();
        } catch (Exception e) {
            System.out.println("Error with Day 7: " + e.getMessage());
        }
    }

    public void partOne() throws FileNotFoundException {
        scanner = new Scanner(file);

        List<Hand> hands = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] tokens = line.split(" ");

            List<CARD> cards = new ArrayList<>();

            for (Character c : tokens[0].toCharArray()) {
                switch (c) {
                    case '2': {
                        cards.add(CARD.TWO);
                        break;
                    }
                    case '3': {
                        cards.add(CARD.THREE);
                        break;
                    }
                    case '4': {
                        cards.add(CARD.FOUR);
                        break;
                    }
                    case '5': {
                        cards.add(CARD.FIVE);
                        break;
                    }
                    case '6': {
                        cards.add(CARD.SIX);
                        break;
                    }
                    case '7': {
                        cards.add(CARD.SEVEN);
                        break;
                    }
                    case '8': {
                        cards.add(CARD.EIGHT);
                        break;
                    }
                    case '9': {
                        cards.add(CARD.NINE);
                        break;
                    }
                    case 'T': {
                        cards.add(CARD.T);
                        break;
                    }
                    case 'J': {
                        cards.add(CARD.J);
                        break;
                    }
                    case 'Q': {
                        cards.add(CARD.Q);
                        break;
                    }
                    case 'K': {
                        cards.add(CARD.K);
                        break;
                    }
                    case 'A': {
                        cards.add(CARD.A);
                        break;
                    }
                }
            }

            int bid = Integer.parseInt(tokens[1].trim());
            TYPE type = determineHandType(cards);

            Hand hand = new Hand(line, cards, type, bid);
            hands.add(hand);
        }

        hands.sort(Collections.reverseOrder());

        int winnings = 0;
        int index = 1;

        for (Hand hand : hands) {
            winnings += (index * hand.getBid());
            index += 1;
        }

        System.out.println("Day 7 Part 1: " + winnings);
    }
    private TYPE determineHandType(List<CARD> cards) {
        Map<CARD, Integer> occurrences = new LinkedHashMap<>();
        for (CARD c : cards) {
            occurrences.put(c, occurrences.getOrDefault(c, 0) + 1);
        }

        if (occurrences.entrySet().stream().anyMatch(entry -> entry.getValue().equals(5)))
            return TYPE.FIVE_OF_A_KIND;
        else if (occurrences.entrySet().stream().anyMatch(entry -> entry.getValue().equals(4)))
            return TYPE.FOUR_OF_A_KIND;
        else if (occurrences.entrySet().stream().anyMatch(entry -> entry.getValue().equals(3)) &&
                 occurrences.entrySet().stream().anyMatch(entry -> entry.getValue().equals(2)))
            return TYPE.FULL_HOUSE;
        else if (occurrences.entrySet().stream().anyMatch(entry -> entry.getValue().equals(3)) &&
                occurrences.entrySet().stream().noneMatch(entry -> entry.getValue().equals(2)))
            return TYPE.THREE_OF_A_KIND;
        else if (occurrences.entrySet().stream().filter(entry -> entry.getValue().equals(2)).count() == 2L)
            return TYPE.TWO_PAIR;
        else if (occurrences.entrySet().stream().filter(entry -> entry.getValue().equals(2)).count() == 1L)
            return TYPE.ONE_PAIR;
        else if (occurrences.entrySet().stream().filter(entry -> entry.getValue().equals(1)).count() == 5L)
            return TYPE.HIGH_CARD;

        throw new IllegalArgumentException("Could not determine hand type!");
    }

    @AllArgsConstructor
    @Getter
    @Setter
    private static class Hand implements Comparable<Hand> {
        private String line;
        private List<CARD> cards;
        private TYPE type;
        private int bid;

        @Override
        public String toString() {
            return String.format("HAND [%s] [%s] [%d]", line, type, bid);
        }

        @Override
        public int compareTo(Hand o) {
            if (this.type != o.type)
                return this.type.compareTo(o.type);
            else {
                for (int i = 0; i < this.cards.size(); i++) {
                    CARD thisCard = this.cards.get(i);
                    CARD thatCard = o.cards.get(i);

                    if (thisCard.compareTo(thatCard) != 0) {
                        return -thisCard.compareTo(thatCard);
                    }
                }

                return 0;
            }
        }
    }

    private enum CARD {
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5),
        SIX(6),
        SEVEN(7),
        EIGHT(8),
        NINE(9),
        T(10),
        J(11),
        Q(12),
        K(13),
        A(14);

        public final int value;

        CARD(int value) {
            this.value = value;
        }
    }

    private enum TYPE {
        FIVE_OF_A_KIND,
        FOUR_OF_A_KIND,
        FULL_HOUSE,
        THREE_OF_A_KIND,
        TWO_PAIR,
        ONE_PAIR,
        HIGH_CARD
    }

}
