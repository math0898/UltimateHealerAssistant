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
        encounters.get(3).process();
        encounters.get(3).summarize();
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
            StringBuilder builder = new StringBuilder();
            while (s.hasNextLine()) {
                String line = s.nextLine();
                if (line.contains("ENCOUNTER_END")) {
                    encounters.add(new Encounter(builder.toString()));
                    builder = new StringBuilder();
                }
                builder.append("\n").append(line);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}