package io.github.math0898;

import io.github.math0898.gui.GraphicsPanel;
import io.github.math0898.processing.Encounter;
import suga.engine.GameEngine;
import suga.engine.game.BasicGame;
import io.github.math0898.gui.*;
import suga.engine.input.keyboard.GameKeyListener;
import suga.engine.input.mouse.BasicMouseListener;
import suga.engine.logger.Level;

import java.awt.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Main {

//    public final static String TEST_FILE = "/home/sugaku/Development/Standalone/Java/UltimateHealerAssistant/testfiles/Archive-WoWCombatLog-071625_185305.txt";
    public final static String TEST_FILE = "/home/sugaku/Development/Standalone/Java/UltimateHealerAssistant/testfiles/Archive-WoWCombatLog-071725_190112.txt";

    public final static List<Encounter> encounters = new ArrayList<>();

    public static void main(String[] args) {
        GameEngine.getLogger().setLevel(Level.VERBOSE);
        long startTime = System.currentTimeMillis();
        new Main().processFile(TEST_FILE);
        long endTime = System.currentTimeMillis();
        GameEngine.getLogger().log("Found " + encounters.size() + " encounters. Took: " + NumberFormat.getInstance().format((endTime - startTime)) + "ms");
        gui();
    }

    /**
     * Opens the graphical interface.
     */
    public static void gui () {
        GraphicsPanel graphicsPanel = new GraphicsPanel();
        GameKeyListener gameKeyListener = new UltimateHealerAssistantGameKeyListener();
        BasicMouseListener gameMouseListener = new UltimateMouseListener();
        BasicGame game = new UltimateHealerAssistantGame(graphicsPanel, gameKeyListener, gameMouseListener);
        game.setPanel(graphicsPanel);
        GameEngine.launchGameWindow(1920, 1000, "Ultimate Healer Assistant", true, graphicsPanel,
                Color.getHSBColor(0, 0, 0.05f), 30, 30, gameKeyListener, gameMouseListener, game);
        game.loadScene("main");
    }

    /**
     * Processes the given log file splitting it into its encounters.
     *
     * @param logFile The log file to investigate.
     */
    public void processFile (String logFile) {
        try (InputStream inputStream = new FileInputStream(logFile)) {
            StringBuilder builder = new StringBuilder();
            boolean encounter_start = false;
            String data = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8).lines().collect(Collectors.joining("\n"));
            String[] lines = data.split("\n");
            try (ExecutorService executor = new ThreadPoolExecutor(4, 8, 5, TimeUnit.SECONDS, new LinkedBlockingQueue<>())) {
                List<Future<?>> futures = new ArrayList<>();
                for (int i = 0; i < lines.length; i++) {
                    String line = lines[i];
                    if (line.contains("ENCOUNTER_END")) {
                        builder.append("\n").append(line);
                        String finalDataString = builder.toString();
                        Runnable thread = () -> {
                            Encounter encounter = new Encounter(finalDataString);
                            encounter.process();
                            if (encounter.eventCount() > 50 && encounter.encounterLengthMillis() > 3000)
                                encounters.add(encounter);
                        };
                        futures.add(executor.submit(thread));
                        encounter_start = false;
                        builder = new StringBuilder();
                    } else if (line.contains("ENCOUNTER_START")) encounter_start = true;
                    if (encounter_start) builder.append("\n").append(line);
                }
                for (Future<?> f : futures) f.get(); // Wait for all to finish.
                encounters.sort((e1, e2) -> Math.toIntExact(e1.getEncounterStartMillis() - e2.getEncounterStartMillis()));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}