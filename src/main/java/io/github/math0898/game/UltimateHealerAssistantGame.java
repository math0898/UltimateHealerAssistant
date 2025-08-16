package io.github.math0898.game;

import io.github.math0898.views.encountersummary.EncounterSummaryScene;
import io.github.math0898.views.healgraph.MainGraphScene;
import io.github.math0898.views.nightsummary.ProgOffencesScene;
import suga.engine.GameEngine;
import suga.engine.game.BasicGame;
import suga.engine.graphics.GraphicsPanel;
import suga.engine.input.keyboard.GameKeyListener;
import suga.engine.input.keyboard.KeyValue;
import suga.engine.input.mouse.BasicMouseListener;
import suga.engine.logger.Level;

import java.awt.event.MouseEvent;
import java.util.Stack;

/**
 * The game handles the whole graphics, ui, and input portions of the program.
 *
 * @author Sugaku
 */
public class UltimateHealerAssistantGame extends BasicGame {

    /**
     * The active UltimateHealerAssistantGame instance that is running.
     */
    private static UltimateHealerAssistantGame instance;

    public UltimateHealerAssistantGame (GraphicsPanel panel, GameKeyListener listener, BasicMouseListener mouseListener) {
        super(panel, listener, mouseListener);
        scenes.put("main", new MainGraphScene());
        scenes.put("prog", new ProgOffencesScene());
        scenes.put("encounter", new EncounterSummaryScene());
        instance = this;
    }

    /**
     * Accessor method for the active game instance.
     *
     * @return The active game instance.
     */
    public static UltimateHealerAssistantGame getInstance () {
        return  instance;
    }

    /**
     * Processes inputs given by players. Is run during pause.
     */
    @Override
    public void processInput () {
        if (loadedScene == null) {
            GameEngine.getLogger().log("BasicGame: No loaded scene. Cannot process inputs.", Level.WARNING);
            return;
        }
        Stack<MouseEvent> mice = mouseListener.getEvents();
        while (!mice.isEmpty()) {
            MouseEvent e = mice.pop();
            loadedScene.mouseInput(e.getPoint(), e.paramString().startsWith("MOUSE_PRESSED"));
        }
        Stack<KeyValue> keys = keyListener.getKeyPresses();
        while (!keys.isEmpty())
            loadedScene.keyboardInput(keys.pop(), true);
        keys = keyListener.getKeyReleases();
        while (!keys.isEmpty())
            loadedScene.keyboardInput(keys.pop(), false);
    }
}
