package io.github.math0898.game;

import io.github.math0898.views.encountersummary.EncounterSummaryScene;
import io.github.math0898.views.fileselect.FileSelectionScene;
import io.github.math0898.views.general.MouseIndicator;
import io.github.math0898.views.healgraph.MainGraphScene;
import io.github.math0898.views.nightsummary.ProgOffencesScene;
import lombok.Getter;
import suga.engine.GameEngine;
import suga.engine.game.BasicGame;
import suga.engine.graphics.GraphicsPanel;
import suga.engine.input.keyboard.GameKeyListener;
import suga.engine.input.keyboard.KeyValue;
import suga.engine.input.mouse.BasicMouseListener;
import suga.engine.logger.Level;
import suga.engine.logger.Logger;

import java.awt.event.MouseEvent;
import java.util.Stack;

/**
 * The game handles the whole graphics, ui, and input portions of the program.
 *
 * @author Sugaku
 */
public class UltimateHealerAssistantGame extends BasicGame {

    /**
     * Whether debugging is enabled for the game currently.
     */
    private boolean debugging = false;

    /**
     * The active UltimateHealerAssistantGame instance that is running.
     * -- GETTER --
     *  Accessor method for the active game instance.
     *
     * @return The active game instance.
     */
    @Getter
    private static UltimateHealerAssistantGame instance;

    public UltimateHealerAssistantGame (GraphicsPanel panel, GameKeyListener listener, BasicMouseListener mouseListener) {
        super(panel, listener, mouseListener);
        scenes.put("main", new MainGraphScene());
        scenes.put("prog", new ProgOffencesScene());
        scenes.put("encounter", new EncounterSummaryScene());
        scenes.put("file", new FileSelectionScene());
        instance = this;
    }

    /**
     * Toggles debugging mode for UltimateHealerAssistant.
     */
    public void toggleDebug () {
        Logger logger = GameEngine.getLogger();
        if (debugging) { // Then disable
            logger.setLevel(Level.INFO);
            logger.log(this.getClass().getSimpleName() + ": Disabled debugging.", Level.INFO);
            MouseIndicator indicator = (MouseIndicator) objects.get("Mouse Indicator"); // Unsafe but should still work.
            if (indicator != null) indicator.degenerate();
            objects.remove("Mouse Indicator");
            debugging = false;
        } else { // Then enable.
            logger.setLevel(Level.DEBUG);
            logger.log(this.getClass().getSimpleName() + ": Enabled debugging.", Level.INFO);
            addGameObject("Mouse Indicator", new MouseIndicator());
            debugging = true;
        }
    }

    /**
     * Processes inputs given by players. Is run during pause.
     */
    @Override
    public void processInput () {
        if (loadedScene == null) {
            GameEngine.getLogger().log("BasicGame: No loaded scene. Will not process inputs.", Level.WARNING);
            return;
        }
        Stack<MouseEvent> mice = mouseListener.getEvents();
        while (!mice.isEmpty()) {
            MouseEvent e = mice.pop();
            loadedScene.mouseInput(e.getPoint(), e.paramString().startsWith("MOUSE_PRESSED"));
        }
        Stack<KeyValue> keys = keyListener.getKeyPresses();
        while (!keys.isEmpty()) {
            KeyValue val = keys.pop();
            if (val == null) return;
            switch (val) {
                case L -> UltimateHealerAssistantGame.getInstance().toggleDebug();
                default -> loadedScene.keyboardInput(val, true);
            }
        }
        keys = keyListener.getKeyReleases();
        while (!keys.isEmpty()) {
            KeyValue val = keys.pop();
            if (val == null) return;
            loadedScene.keyboardInput(val, false);
        }
    }
}
