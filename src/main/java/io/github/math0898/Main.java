package io.github.math0898;

import io.github.math0898.processing.Encounter;

import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private final static String TEST_FILE = "/WoWCombatLog-051425_164349.txt";

    private final static List<Encounter> encounters = new ArrayList<>();

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        processFile(TEST_FILE);
        long endTime = System.currentTimeMillis();
        System.out.println("Found " + encounters.size() + " encounters. Took: " + NumberFormat.getInstance().format((endTime - startTime)) + "ms");
//        for (int i = 0; i < encounters.size(); i++) {
//            encounters.get(i).process();
//            if (encounters.get(i).eventCount() < 50) continue;
//            encounters.get(i).summarize();
//            System.out.println("Wipe " + i);
//        }
        int testIndex = 3;
        encounters.get(testIndex).process();
        encounters.get(testIndex).graph();
        encounters.get(testIndex).summarize();
    }

    /**
     * Processes the given log file splitting it into its encounters.
     *
     * @param logFile The log file to investigate.
     */
    public static void processFile (String logFile) {
        try (InputStream inputStream = Main.class.getResourceAsStream(logFile)) { // todo: Consider non-class resources.
            if (inputStream == null) return;
            Scanner s = new Scanner(inputStream);
            StringBuilder builder = new StringBuilder(); // todo: First encounter over counts healing.
            boolean encounter_start = false;
            while (s.hasNextLine()) {
                String line = s.nextLine();
                if (line.contains("ENCOUNTER_END")) {
                    encounters.add(new Encounter(builder.toString()));
                    encounter_start = false;
                    builder = new StringBuilder();
                } else if (line.contains("ENCOUNTER_START"))
                    encounter_start = true;
                if (encounter_start) builder.append("\n").append(line);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}