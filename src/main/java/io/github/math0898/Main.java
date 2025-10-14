package io.github.math0898;

import io.github.math0898.game.UltimateHealerAssistantGame;
import io.github.math0898.game.UltimateHealerAssistantGameKeyListener;
import io.github.math0898.game.UltimateMouseListener;
import io.github.math0898.processing.LogManager;
import suga.engine.GameEngine;
import suga.engine.game.BasicGame;
import suga.engine.graphics.Graphics2d;
import suga.engine.input.keyboard.GameKeyListener;
import suga.engine.input.mouse.BasicMouseListener;
import suga.engine.logger.Level;

import java.awt.*;
import java.text.NumberFormat;

public class Main {

    public final static String TEST_FILE = "/home/sugaku/Development/Standalone/Java/UltimateHealerAssistant/testfiles/Archive-WoWCombatLog-100925_185651.txt";

    /**
     * The active file to load.
     */
    private static String loadFile = TEST_FILE;

    public static void main(String[] args) {
        GameEngine.getLogger().setLevel(Level.DEBUG);
        if (loadFile != null) analyzeFile();
        gui();
    }

    /**
     * Sets the active log file to load.
     *
     * @param file The file to load.
     */
    public static void setLogfile (String file) {
        loadFile = file;
    }

    /**
     * Opens the graphical interface.
     */
    public static void gui () {
        Graphics2d graphicsPanel = new Graphics2d();
        GameKeyListener gameKeyListener = new UltimateHealerAssistantGameKeyListener();
        BasicMouseListener gameMouseListener = new UltimateMouseListener();
        BasicGame game = new UltimateHealerAssistantGame(graphicsPanel, gameKeyListener, gameMouseListener);
        game.setPanel(graphicsPanel);
        GameEngine.launchGameWindow(1920, 1000, "Ultimate Healer Assistant", true, graphicsPanel,
                Color.getHSBColor(0, 0, 0.05f), 30, 30, gameKeyListener, gameMouseListener, game);
        game.loadScene("main");
    }

    /**
     * Computes data about the selected log file. This will freeze whatever thread it is run on.
     */
    public static void analyzeFile () {
        long startTime = System.currentTimeMillis();
        LogManager logManager = LogManager.getInstance();
        logManager.processFile(loadFile);
        long endTime = System.currentTimeMillis(); // todo: Mouseover Abilities, names.
        GameEngine.getLogger().log("Found " + logManager.getEncounterCount() + " encounters. Took: " + NumberFormat.getInstance().format((endTime - startTime)) + "ms");
    }
}
