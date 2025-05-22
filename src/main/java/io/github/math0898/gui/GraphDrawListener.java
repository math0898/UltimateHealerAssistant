package io.github.math0898.gui;

import io.github.math0898.Main;
import io.github.math0898.processing.Encounter;
import io.github.math0898.processing.Graph;
import suga.engine.graphics.DrawListener;
import suga.engine.graphics.GraphicsPanel;

import java.awt.*;

public class GraphDrawListener implements DrawListener {

    /**
     * Called every drawing frame so programs have a chance to make their voices heard on what gets drawn.
     *
     * @param width  The width of the pixel map.
     * @param height The height of the pixel map.
     * @param panel  The panel to apply changes to.
     */
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
                    long consumeFlame = encounter.queryHealingBySpell("Lifebind",
                            (encounter.encounterLengthMillis() / timeStepCount) * j,
                            (encounter.encounterLengthMillis() / timeStepCount)) / scale;
//                                long rewind = encounter.queryHealingBySpell(Arrays.asList("Atonement", "Premonition of Piety", "Dark Reprimand", "Penance", "Divine Aegis"),
//                                (encounter.encounterLengthMillis() / timeStepCount) * j,
//                                        (encounter.encounterLengthMillis() / timeStepCount)) / scale;
                    long rewind = encounter.queryHealingByCaster("Skullz", (encounter.encounterLengthMillis() / timeStepCount) * j,
                            (encounter.encounterLengthMillis() / timeStepCount)) / scale;
                    if (consumeFlame >= i && consumeFlame > 0)
                        panel.setBigPixel(startX + (j * 10), startY - ((int) i * 10), 9, new Color(215, 55, 35));
                    else if (rewind >= i && rewind > 0) {
                        panel.setBigPixel(startX + (j * 10), startY - ((int) i * 10), 9, new Color(210, 204, 35));
                    }
                    else if (graph.heal.get(j) >= i)
                        panel.setBigPixel(startX + (j * 10), startY - ((int) i * 10), 9, new Color(22, 237, 64));
                    else
                        panel.setBigPixel(startX + (j * 10), startY - ((int) i * 10), 9, new Color(39, 150, 60));
                }
                if (graph.damage.get(j) == i)
                    panel.setRectangle(startX + (j * 10) - 3, startY - ((int) i * 10) - 1, 7, 3, new Color(230, 41, 28));
            }
    }
}
