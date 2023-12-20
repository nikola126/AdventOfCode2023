package com.adventofcode2023;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day20 {
    public final String FILE_DIR = "./src/main/resources/day20.txt";
    public File file = new File(FILE_DIR);
    public Scanner scanner;

    public Day20() {
        try {
            partOne();
            partTwo();
        } catch (Exception e) {
            System.out.println("Error with Day 20: " + e.getMessage());
        }
    }

    public void partOne() throws FileNotFoundException {
        scanner = new Scanner(file);

        Map<String, Component> componentMap = new LinkedHashMap<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] tokens = line.split("->");
            String[] outputs = tokens[1].trim().split(",");

            Character type = tokens[0].charAt(0);
            String name = tokens[0].substring(1).trim();

            if (name.equals("roadcaster"))
                name = "broadcaster";

            List<String> outputList = new ArrayList<>();
            for (String output : outputs)
                outputList.add(output.trim());

            componentMap.put(name, new Component(type, name, outputList, new LinkedHashMap<>(), false));
        }

        // fill default memory
        for (Component conjunction : componentMap.values()) {
            if (conjunction.getType() == '&') {
                List<Component> inputs = componentMap.values().stream()
                        .filter(c -> c.getOutputs().contains(conjunction.getName()))
                        .toList();

                for (Component input : inputs)
                    conjunction.getMemory().putIfAbsent(input.getName(), false);
            }
        }

        Deque<Event> deque = new LinkedList<>();
        int buttonPresses = 1000;
        long countLow = 0;
        long countHigh = 0;

        for (int i = 0; i < buttonPresses; i++) {
            Event buttonPress = new Event("button", false, "broadcaster");
            deque.push(buttonPress);

            while (!deque.isEmpty()) {
                Event event = deque.pollLast();

                if (event.isHigh()) {
                    countHigh += 1;
                } else {
                    countLow += 1;
                }

                Component component = componentMap.getOrDefault(event.getDestination(), null);

                if (component == null)
                    continue; // it's the output

                if (component.getType() == '%') {
                    if (!event.isHigh()) {
                        // flip state and send corresponding impulse
                        if (!component.isHigh()) {
                            component.setHigh(true);
                            for (String output : component.getOutputs())
                                deque.push(new Event(component.getName(), true, output));
                        } else {
                            component.setHigh(false);
                            for (String output : component.getOutputs())
                                deque.push(new Event(component.getName(), false, output));
                        }
                    }
                } else if (component.getType() == '&') {
                    // update memory for that input
                    component.getMemory().put(event.getSource(), event.isHigh());

                    boolean allHigh = component.getMemory().values().stream().allMatch(s -> s);

                    for (String output : component.getOutputs()) {
                        deque.push(new Event(component.getName(), !allHigh, output));
                    }
                } else if (component.getType() == 'b') {
                    // send same input for all outputs
                    for (String output : component.getOutputs())
                        deque.addFirst(new Event(component.getName(), event.isHigh(), output));
                } else {
                    throw new IllegalArgumentException("Unknown component " + component);
                }
            }
        }

        long multiplied = countLow * countHigh;

        System.out.println("Day 20 Part 1: " + multiplied);
    }

    public void partTwo() throws FileNotFoundException {
        scanner = new Scanner(file);

        Map<String, Component> componentMap = new LinkedHashMap<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] tokens = line.split("->");
            String[] outputs = tokens[1].trim().split(",");

            Character type = tokens[0].charAt(0);
            String name = tokens[0].substring(1).trim();

            if (name.equals("roadcaster"))
                name = "broadcaster";

            List<String> outputList = new ArrayList<>();
            for (String output : outputs)
                outputList.add(output.trim());

            componentMap.put(name, new Component(type, name, outputList, new LinkedHashMap<>(), false));
        }

        // fill default memory
        for (Component conjunction : componentMap.values()) {
            if (conjunction.getType() == '&') {
                List<Component> inputs = componentMap.values().stream()
                        .filter(c -> c.getOutputs().contains(conjunction.getName()))
                        .toList();

                for (Component input : inputs)
                    conjunction.getMemory().putIfAbsent(input.getName(), false);
            }
        }

        Deque<Event> deque = new LinkedList<>();
        long countButtonPresses = 0;

        // Inspect output and determine which components will cause a low signal to be sent
        // to the component just before rx;
        final String COMPONENT_JUST_BEFORE_RX = "vf";
        Map<String, Long> expectedInputs = new HashMap<>();
        expectedInputs.put("pm", null);
        expectedInputs.put("mk", null);
        expectedInputs.put("pk", null);
        expectedInputs.put("hf", null);

        while (true) {
            Event buttonPress = new Event("button", false, "broadcaster");
            deque.push(buttonPress);
            countButtonPresses += 1;

            while (!deque.isEmpty()) {
                Event event = deque.pollLast();

                // this will take some time
                if (event.getDestination().equals("rx") && !event.isHigh()) {
                    System.out.println("Day 20 Part 2: " + countButtonPresses);
                    return;
                }

                // remember the number of button presses necessary to send a low signal
                if (event.getDestination().equals(COMPONENT_JUST_BEFORE_RX) && event.isHigh()) {
                    expectedInputs.put(event.getSource(), countButtonPresses);

                    // when all are encountered, return their LCM
                    if (expectedInputs.values().stream().noneMatch(Objects::isNull)) {
                        List<Long> countPresses = new ArrayList<>(expectedInputs.values());

                        System.out.println("Day 20 Part 2: " + Day8.LCM(countPresses));
                        return;
                    }
                }

                Component component = componentMap.getOrDefault(event.getDestination(), null);

                if (component == null)
                    continue; // it's the output

                if (component.getType() == '%') {
                    if (!event.isHigh()) {
                        // flip state and send corresponding impulse
                        if (!component.isHigh()) {
                            component.setHigh(true);
                            for (String output : component.getOutputs())
                                deque.push(new Event(component.getName(), true, output));
                        } else {
                            component.setHigh(false);
                            for (String output : component.getOutputs())
                                deque.push(new Event(component.getName(), false, output));
                        }
                    }
                } else if (component.getType() == '&') {
                    // update memory for that input
                    component.getMemory().put(event.getSource(), event.isHigh());

                    boolean allHigh = component.getMemory().values().stream().allMatch(s -> s);

                    for (String output : component.getOutputs()) {
                        deque.push(new Event(component.getName(), !allHigh, output));
                    }
                } else if (component.getType() == 'b') {
                    // send same input for all outputs
                    for (String output : component.getOutputs())
                        deque.addFirst(new Event(component.getName(), event.isHigh(), output));
                } else {
                    throw new IllegalArgumentException("Unknown component " + component);
                }
            }
        }
    }

    @AllArgsConstructor
    @Getter
    @Setter
    private class Component {
        private Character type;
        private String name;
        private List<String> outputs;
        private Map<String, Boolean> memory;
        private boolean high;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (String output : outputs) {
                sb.append(output);
                sb.append(" ");
            }
            sb.setLength(sb.length() - 1);

            StringBuilder mem = new StringBuilder();
            for (Map.Entry<String, Boolean> entry : memory.entrySet()) {
                mem.append(String.format("[%s]:[%b]", entry.getKey(), entry.getValue()));
            }

            return String.format("[%c] [%s] [%s] MEMORY: [%s]", type, name, sb, mem);
        }
    }

    @AllArgsConstructor
    @Getter
    @Setter
    private class Event {
        private String source;
        private boolean high;
        private String destination;

        @Override
        public String toString() {
            return String.format("[%s]--[%s]-->[%s]", source, high ? "HIGH" : "LOW", destination);
        }
    }

}
