package com.adventofcode2023;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.*;

public class Day19 {
    public final String FILE_DIR = "./src/main/resources/day19.txt";
    public File file = new File(FILE_DIR);
    public Scanner scanner;

    public Day19() {
        try {
            partOne();
        } catch (Exception e) {
            System.out.println("Error with Day 19: " + e.getMessage());
        }
    }

    public void partOne() throws Exception {
        scanner = new Scanner(file);

        List<Workflow> workflows = new ArrayList<>();
        List<Part> parts = new ArrayList<>();

        readInput(scanner, workflows, parts);

        Map<String, Workflow> workflowMap = new LinkedHashMap<>();
        for (Workflow workflow : workflows)
            workflowMap.put(workflow.getName(), workflow);

        List<Part> accepted = new ArrayList<>();

        for (Part part : parts) {
            if (evaluatePart(part, workflowMap))
                accepted.add(part);
        }

        long sum = 0;
        for (Part part : accepted) {
            sum += part.getRating();
        }

        System.out.println("Day 19 Part 1: " + sum);
    }

    private void readInput(Scanner scanner, List<Workflow> workflows, List<Part> parts) {
        boolean collectingWorkflows = true;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (line.isBlank()) {
                collectingWorkflows = false;
                continue;
            }

            if (collectingWorkflows) {
                String name = line.split("\\{")[0].trim();
                String text = line.split("\\{")[1].split("}")[0].trim();

                String[] tokens = text.split(",");
                List<String> rules = new ArrayList<>(Arrays.asList(tokens));

                workflows.add(new Workflow(name, rules));
            } else {
                String text = line.split("\\{")[1].split("}")[0].trim();
                String[] tokens = text.split(",");

                int x = Integer.parseInt(tokens[0].split("=")[1]);
                int m = Integer.parseInt(tokens[1].split("=")[1]);
                int a = Integer.parseInt(tokens[2].split("=")[1]);
                int s = Integer.parseInt(tokens[3].split("=")[1]);

                parts.add(new Part(x, m, a, s));
            }

        }

    }

    private String evaluateRule(Part part, String ruleText) {
        if (ruleText.contains("<")) {
            String s = ruleText.split("<")[0].trim();
            String val = ruleText.split("<")[1].split(":")[0].trim();
            String out = ruleText.split(":")[1].trim();

            int operand1 = switch (s) {
                case "x" -> part.getX();
                case "m" -> part.getM();
                case "a" -> part.getA();
                case "s" -> part.getS();
                default -> throw new IllegalArgumentException("Unknown part!");
            };

            int operand2 = Integer.parseInt(val);

            if (operand1 < operand2)
                return out;
            else
                return "";
        } else if (ruleText.contains(">")) {
            String s = ruleText.split(">")[0].trim();
            String val = ruleText.split(">")[1].split(":")[0].trim();
            String out = ruleText.split(":")[1].trim();

            int operand1 = switch (s) {
                case "x" -> part.getX();
                case "m" -> part.getM();
                case "a" -> part.getA();
                case "s" -> part.getS();
                default -> throw new IllegalArgumentException("Unknown part!");
            };

            int operand2 = Integer.parseInt(val);

            if (operand1 > operand2)
                return out;
            else
                return "";
        } else {
            // accepted, rejected or next workflow
            return ruleText;
        }
    }

    private boolean evaluatePart(Part part, Map<String, Workflow> workflowMap) {
        String output;
        Workflow workflow = workflowMap.get("in");
        int ruleIndex = 0;
        String rule = workflow.getRules().getFirst();

        while (true) {
            output = evaluateRule(part, rule);

            if (output.isBlank()) {
                rule = workflow.getRules().get(++ruleIndex);
            } else if (output.equals("A")) {
                return true;
            }
            else if (output.equals("R")) {
                return false;
            }
            else {
                workflow = workflowMap.get(output);
                ruleIndex = 0;
                rule = workflow.getRules().getFirst();
            }
        }
    }

    @AllArgsConstructor
    @Getter
    @Setter
    private class Part {
        int x;
        int m;
        int a;
        int s;

        @Override
        public String toString() {
            return String.format("X [%d] M [%d] A [%d] S [%d]", x, m, a, s);
        }

        public int getRating() {
            return x + m + a + s;
        }
    }

    @AllArgsConstructor
    @Getter
    @Setter
    private class Workflow {
        String name;
        List<String> rules;

        @Override
        public String toString() {
            return String.format("Workflow [%s] with [%d] rules", name, rules.size());
        }
    }


}
