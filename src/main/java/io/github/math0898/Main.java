package io.github.math0898;

import java.io.InputStream;
import java.text.NumberFormat;
import java.util.Scanner;

public class Main {

    private final static String TEST_FILE = "/WoWCombatLog-051425_164349.txt";

    public static void main(String[] args) {
        try (InputStream inputStream = Main.class.getResourceAsStream(TEST_FILE)) {
            if (inputStream == null) return;
            Scanner s = new Scanner(inputStream);
            long startTime = System.currentTimeMillis();
            int i = 0;
            while (s.hasNextLine()) {
                String line = s.nextLine();
                i++;
                if (line.contains("ENCOUNTER_END"))
                    System.out.println(line);
            }
            long endTime = System.currentTimeMillis();
            System.out.println("Read " + NumberFormat.getInstance().format(i) + " lines. Took: " + NumberFormat.getInstance().format((endTime - startTime)) + "ms");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}