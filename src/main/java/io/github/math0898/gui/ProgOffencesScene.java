package io.github.math0898.gui;

import suga.engine.game.BasicScene;
import suga.engine.game.Game;
import suga.engine.game.objects.BasicGameObject;
import suga.engine.game.objects.GameObject;
import suga.engine.input.keyboard.KeyValue;

import java.awt.*;

/**
 * The ProgOffencesScene is used to generate nice looking graphics to track how many times players mess up during prog.
 * Our guild is using this to help track down and monitor players who regularly cause raid wide wipes.
 *
 * @author Sugaku
 */
public class ProgOffencesScene extends BasicScene {

    /**
     * Loads this scene into the given game.
     *
     * @param game The game to load this scene into.
     * @return True if loading was successful. Otherwise, false.
     */
    @Override
    public boolean load (Game game) {
        game.clear();
        game.addGameObject("Seranite", new PlayerIcon("malganis", "seranite", 200, 200, 1920 / 4, 1080 / 4));
        game.addGameObject("Nillath", new PlayerIcon("stormrage", "nillath", 200, 200, (1920 / 4) * 3, 1080 / 4));
        game.addGameObject("Skullzdrood", new PlayerIcon("stormrage", "skullzdrood", 200, 200, 1920 / 4, (1080 / 4) * 2));
        game.addGameObject("Syudou", new PlayerIcon("stormrage", "syudou", 200, 200, (1920 / 4) * 3, (1080 / 4) * 2));
        return super.load(game);
    }

    /**
     * Passes a keyboard input into the scene.
     *
     * @param key     The value of the key pressed.
     * @param pressed True if the key was pressed, false if it was released.
     */
    @Override
    public void keyboardInput (KeyValue key, boolean pressed) {
        switch (key) {
            case NUM_1 -> game.loadScene("main");
        }
    }

    /**
     * Passes a mouse input into the scene.
     *
     * @param pos     The position of the mouse when it was clicked.
     * @param pressed True if the button was pressed, false if it was released.
     */
    @Override
    public void mouseInput (Point pos, boolean pressed) {

    }
}
