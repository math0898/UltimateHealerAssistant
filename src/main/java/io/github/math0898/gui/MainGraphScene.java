package io.github.math0898.gui;

import io.github.math0898.Main;
import suga.engine.GameEngine;
import suga.engine.game.BasicScene;
import suga.engine.game.Game;
import suga.engine.input.keyboard.KeyValue;
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
        if (pressed) logger.log("You pressed: " + key + "!");
        else logger.log("You released: " + key + "!");
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
     * Passes a mouse input into the scene.
     *
     * @param pos     The position of the mouse when it was clicked.
     * @param pressed True if the button was pressed, false if it was released.
     */
    @Override
    public void mouseInput (Point pos, boolean pressed) {
        Logger logger = GameEngine.getLogger();
        if (pressed) logger.log("Clicked at " + pos + "!");
        else logger.log("Click released at " + pos + "!");
    }
}
