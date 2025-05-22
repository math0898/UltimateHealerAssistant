package io.github.math0898.gui;

import io.github.math0898.Main;
import io.github.math0898.processing.Encounter;
import io.github.math0898.processing.Graph;
import suga.engine.game.BasicGame;
import suga.engine.game.BasicScene;
import suga.engine.graphics.DrawListener;
import suga.engine.graphics.GraphicsPanel;
import suga.engine.input.keyboard.GameKeyListener;
import suga.engine.input.keyboard.KeyValue;
import suga.engine.input.mouse.BasicMouseListener;

import java.awt.*;
import java.util.Arrays;

public class UltimateHealerAssistantGame extends BasicGame {

    public UltimateHealerAssistantGame (GraphicsPanel panel, GameKeyListener listener, BasicMouseListener mouseListener) {
        super(panel, listener, mouseListener);
        scenes.put("main", new MainGraphScene());
    }

    public UltimateHealerAssistantGame () {
        scenes.put("main", new MainGraphScene());
    }
}
