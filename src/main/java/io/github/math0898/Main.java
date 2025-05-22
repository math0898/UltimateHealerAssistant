package io.github.math0898;

import io.github.math0898.gui.GraphicsPanel;
import io.github.math0898.processing.Encounter;
import suga.engine.GameEngine;
import suga.engine.game.BasicGame;
import io.github.math0898.gui.*;
import suga.engine.input.keyboard.GameKeyListener;
import suga.engine.input.mouse.BasicMouseListener;

import java.awt.*;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private final static String TEST_FILE = "/Archive-WoWCombatLog-051525_185636.txt";

    public final static List<Encounter> encounters = new ArrayList<>();

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
        int testIndex = 5;
        encounters.get(testIndex).process();
        encounters.get(testIndex).summarize();
        System.out.println(encounters.get(testIndex).queryHealing("Halo", 0, 1000000000));
        gui();
    }

    /**
     * Opens the graphical interface.
     */
    public static void gui () {
        GraphicsPanel graphicsPanel = new GraphicsPanel();
        GameKeyListener gameKeyListener = new UltimateHealerAssistantGameKeyListener();
        BasicGame game = new UltimateHealerAssistantGame(graphicsPanel, gameKeyListener, new BasicMouseListener());
        game.setPanel(graphicsPanel);
        GameEngine.launchGameWindow(960, 540, "Ultimate Healer Assistant", true, graphicsPanel,
                Color.getHSBColor(0, 0, 0.05f), 30, 30, gameKeyListener, new BasicMouseListener(), game);
        game.loadScene("main");
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
                    Encounter encounter = new Encounter(builder.toString());
                    encounter.process();
                    if (encounter.eventCount() > 50 && encounter.encounterLengthMillis() > 3000)
                        encounters.add(encounter);
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