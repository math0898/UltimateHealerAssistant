package io.github.math0898.gui;

import io.github.math0898.Main;
import suga.engine.GameEngine;
import suga.engine.game.BasicScene;
import suga.engine.game.Game;
import suga.engine.input.keyboard.KeyValue;
import suga.engine.logger.Level;
import suga.engine.logger.Logger;

import java.awt.*;

public class MainGraphScene extends BasicScene {

    /**
     * The index of the encounter that is currently being graphed.
     */
    private int graphedEncounterIndex = 39; // todo: Set to 0

    /**
     * The GraphGameObject that actually draws the graph.
     */
    private GraphGameObject graphGameObject;

    /**
     * Loads this scene into the given game.
     *
     * @param game The game to load this scene into.
     * @return True if loading was successful. Otherwise, false.
     */
    @Override
    public boolean load (Game game) {
        graphGameObject = new GraphGameObject();
        game.addGameObject("Main Graph", graphGameObject);
        game.addGameObject("Encounter Indicator", new EncounterIndicator(this));
        game.addGameObject("Cast Indicator", new CastIndicator(this)); // todo: Probably make separate instances per spell query.
        game.addGameObject("Pres Icon", new SpecIcon(0, "/home/sugaku/Development/Standalone/Java/UltimateHealerAssistant/icons/classicon_evoker_preservation.jpg"));
        game.addGameObject("Holy Icon", new SpecIcon(70, "/home/sugaku/Development/Standalone/Java/UltimateHealerAssistant/icons/spell_holy_guardianspirit.jpg"));
        game.addGameObject("Disc Icon", new SpecIcon(140, "/home/sugaku/Development/Standalone/Java/UltimateHealerAssistant/icons/spell_holy_powerwordshield.jpg"));
        game.addGameObject("Resto Icon", new SpecIcon(210, "/home/sugaku/Development/Standalone/Java/UltimateHealerAssistant/icons/inv_1115_shaman_chainheal.jpg"));
        return true;
    }

    /**
     * Passes a keyboard input into the scene.
     *
     * @param key     The value of the key pressed.
     * @param pressed True if the key was pressed, false if it was released.
     */
    @Override
    public void keyboardInput (KeyValue key, boolean pressed) {
        Logger logger = GameEngine.getLogger();
        if (pressed) logger.log("You pressed: " + key + "!", Level.VERBOSE);
        else logger.log("You released: " + key + "!", Level.VERBOSE);
        if (!pressed) {
            switch (key) {
                case ARROW_UP -> {
                    graphedEncounterIndex = Math.min(graphedEncounterIndex + 1, Main.encounters.size() - 1);
                    updateGraph();
                }
                case ARROW_DOWN -> {
                    graphedEncounterIndex = Math.max(graphedEncounterIndex - 1, 0);
                    updateGraph();
                }
            }
        }
    }

    /**
     * Forces the graph to update.
     */
    private void updateGraph () {
        graphGameObject.setEncounter(Main.encounters.get(graphedEncounterIndex));
    }

    /**
     * Accessor method for the encounter number this Scene is currently on.
     *
     * @return The index of the currently graphed encounter.
     */
    public int getGraphedEncounterIndex () {
        return graphedEncounterIndex;
    }

    /**
     * Passes a mouse input into the scene.
     *
     * @param pos     The position of the mouse when it was clicked.
     * @param pressed True if the button was pressed, false if it was released.
     */
    @Override
    public void mouseInput (Point pos, boolean pressed) {
        Logger logger = GameEngine.getLogger();
        if (pressed) logger.log("Clicked at " + pos + "!", Level.VERBOSE);
        else logger.log("Click released at " + pos + "!", Level.VERBOSE);
        if (pressed) {
            if (pos.x < (1920 / 16) + 50 && pos.x > (1920 / 16)) {
                if (pos.y > 1080 / 8 + 50 && pos.y < 1080 / 8 + 100) {
                    graphGameObject.toggleSpec("pres");
                }
                if (pos.y > 1080 / 8 + 120 && pos.y < 1080 / 8 + 170) {
                    graphGameObject.toggleSpec("holy");
                }
                if (pos.y > 1080 / 8 + 190 && pos.y < 1080 / 8 + 240) {
                    graphGameObject.toggleSpec("disc");
                }
                if (pos.y > 1080 / 8 + 260 && pos.y < 1080 / 8 + 310) {
                    graphGameObject.toggleSpec("resto");
                }
            }
        }
    }
}
