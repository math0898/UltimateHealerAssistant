package io.github.math0898.gui;

import suga.engine.game.BasicGame;
import suga.engine.graphics.GraphicsPanel;
import suga.engine.input.keyboard.GameKeyListener;
import suga.engine.input.mouse.BasicMouseListener;

/**
 *
 */
public class UltimateHealerAssistantGame extends BasicGame {

    public UltimateHealerAssistantGame (GraphicsPanel panel, GameKeyListener listener, BasicMouseListener mouseListener) {
        super(panel, listener, mouseListener);
        scenes.put("main", new MainGraphScene());
    }

    public UltimateHealerAssistantGame () {
        scenes.put("main", new MainGraphScene());
    }
}
