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

    private class EssentialScene extends BasicScene {

        public EssentialScene () {
            addDrawingListener(new DrawListener() {
                @Override
                public void applyChanges (int width, int height, GraphicsPanel panel) {
                    Encounter encounter = Main.encounters.get(39);
                    final int timeStepCount = ((panel.getWidth() * 3) / 4) / 10;
                    final int startX = panel.getWidth() / 8;
                    final int startY = (panel.getHeight() * 7) / 8;
                    final int scale = 1000000;
                    Graph graph = encounter.graph(encounter.encounterLengthMillis() / timeStepCount, 1000000); // todo: This can be hard to dynamically calculate.
                    graph.smooth(1);
                    for (long i = graph.max; i >= 0; i--)
                        for (int j = 0; j < graph.overheal.size(); j++) {
                            if (graph.overheal.get(j) >= i) {
                                long consumeFlame = encounter.queryHealing("Consume Flame",
                                        (encounter.encounterLengthMillis() / timeStepCount) * j,
                                        (encounter.encounterLengthMillis() / timeStepCount)) / scale;
                                long rewind = encounter.queryHealing(Arrays.asList("Atonement", "Premonition of Piety", "Dark Reprimand", "Penance"),
                                (encounter.encounterLengthMillis() / timeStepCount) * j,
                                        (encounter.encounterLengthMillis() / timeStepCount)) / scale;
                                if (rewind >= i && rewind > 0)
                                    panel.setBigPixel(startX + (j * 10), startY - ((int) i * 10), 9, new Color(210, 204, 35));
//                                if (consumeFlame >= i && consumeFlame > 0)
//                                    panel.setBigPixel(startX + (j * 10), startY - ((int) i * 10), 9, new Color(215, 55, 35));
                                else if (graph.heal.get(j) >= i)
                                    panel.setBigPixel(startX + (j * 10), startY - ((int) i * 10), 9, new Color(22, 237, 64));
                                else
                                    panel.setBigPixel(startX + (j * 10), startY - ((int) i * 10), 9, new Color(39, 150, 60));
                            }
                            if (graph.damage.get(j) == i)
                                panel.setRectangle(startX + (j * 10) - 3, startY - ((int) i * 10) - 1, 7, 3, new Color(230, 41, 28));
                        }
                }
            });
        }

        /**
         * Passes a keyboard input into the scene.
         *
         * @param key     The value of the key pressed.
         * @param pressed True if the key was pressed, false if it was released.
         */
        @Override
        public void keyboardInput(KeyValue key, boolean pressed) {

        }

        /**
         * Passes a mouse input into the scene.
         *
         * @param pos     The position of the mouse when it was clicked.
         * @param pressed True if the button was pressed, false if it was released.
         */
        @Override
        public void mouseInput(Point pos, boolean pressed) {

        }
    }

    public UltimateHealerAssistantGame (GraphicsPanel panel, GameKeyListener listener, BasicMouseListener mouseListener) {
        super(panel, listener, mouseListener);
        scenes.put("main", new EssentialScene());
    }

    public UltimateHealerAssistantGame () {
        scenes.put("main", new EssentialScene());
    }
}
