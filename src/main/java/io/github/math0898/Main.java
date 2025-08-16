package io.github.math0898;

import io.github.math0898.game.UltimateHealerAssistantGame;
import io.github.math0898.game.UltimateHealerAssistantGameKeyListener;
import io.github.math0898.game.UltimateMouseListener;
import io.github.math0898.processing.LogManager;
import io.github.math0898.views.healgraph.GraphicsPanel;
import suga.engine.GameEngine;
import suga.engine.game.BasicGame;
import suga.engine.input.keyboard.GameKeyListener;
import suga.engine.input.mouse.BasicMouseListener;
import suga.engine.logger.Level;

import java.awt.*;
import java.text.NumberFormat;

public class Main {

//    public final static String TEST_FILE = "/home/sugaku/Development/Standalone/Java/UltimateHealerAssistant/testfiles/Archive-WoWCombatLog-071625_185305.txt";
//    public final static String TEST_FILE = "/home/sugaku/Development/Standalone/HolyC/WoWLogConverter/test/WoWCombatLog-062725_150059.txt";
//    public final static String TEST_FILE = "/home/sugaku/Development/Standalone/Java/UltimateHealerAssistant/testfiles/Archive-WoWCombatLog-071725_190112.txt";
//    public final static String TEST_FILE = "/home/sugaku/Development/Standalone/Java/UltimateHealerAssistant/testfiles/Archive-WoWCombatLog-081325_185940.txt";
    public final static String TEST_FILE = "/home/sugaku/Development/Standalone/Java/UltimateHealerAssistant/testfiles/Archive-WoWCombatLog-081425_185246.txt";

    public static void main(String[] args) {
        GameEngine.getLogger().setLevel(Level.DEBUG);
        long startTime = System.currentTimeMillis();
        LogManager logManager = LogManager.getInstance();
        logManager.processFile(TEST_FILE);
        long endTime = System.currentTimeMillis(); // todo: Mouseover Abilities, names.
        GameEngine.getLogger().log("Found " + logManager.getEncounterCount() + " encounters. Took: " + NumberFormat.getInstance().format((endTime - startTime)) + "ms");
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
}
