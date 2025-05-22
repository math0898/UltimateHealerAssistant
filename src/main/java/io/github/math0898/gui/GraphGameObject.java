package io.github.math0898.gui;

import io.github.math0898.Main;
import io.github.math0898.processing.Encounter;
import io.github.math0898.processing.Graph;
import suga.engine.game.objects.GameObject;
import suga.engine.graphics.DrawListener;
import suga.engine.graphics.GraphicsPanel;
import suga.engine.physics.collidables.Collidable;

import java.awt.*;

/**
 * The Graph is considered a GameObject so that we have access to {@link #runLogic()}. This runs on a separate thread
 * from graphics and allows our GUI to be responsive even during computations. We don't have a need for a dynamic
 * DrawListener, so we also simply implement it here.
 *
 * @author Sugaku
 */
public class GraphGameObject implements GameObject, DrawListener {

    /**
     * The encounter to graph.
     */
    private static final Encounter ENCOUNTER = Main.encounters.get(39);

    /**
     * The vertical scale component of the healing graph.
     */
    private static final int SCALE = 1000000; // todo: Figure out a way to dynamicly calculate.

    /**
     * This is the width at which the graph should be calculated at. It may lag behind a cycle or two since it needs
     * assigning from graphics thread {@link #applyChanges(int, int, GraphicsPanel)} and computed in {@link #runLogic()}
     * to make the change.
     */
    private int targetWidth;

    /**
     * Whether the graph needs recomputed or not.
     */
    private boolean recompute = true;

    /**
     * The graph calculated on the logic thread that is then used to display.
     */
    private Graph graph = null;

    /**
     * Called every drawing frame so programs have a chance to make their voices heard on what gets drawn.
     *
     * @param width  The width of the pixel map.
     * @param height The height of the pixel map.
     * @param panel  The panel to apply changes to.
     */
    @Override
    public void applyChanges (int width, int height, GraphicsPanel panel) {
        if (targetWidth != width) {
            targetWidth = width;
            recompute = true;
            graph = null;
            return;
        }
        if (graph == null) return;
        final int timeStepCount = ((panel.getWidth() * 3) / 4) / 10; // todo: Graphics shouldn't need intimate details of time steps.
        final int startX = panel.getWidth() / 8;
        final int startY = (panel.getHeight() * 7) / 8;
        for (long i = graph.max; i >= 0; i--)
            for (int j = 0; j < graph.overheal.size(); j++) {
                if (graph.overheal.get(j) >= i) { // todo: Refactor so queries don't need to be ran during the graphics thread. Perhaps an "accents" list of arrays.
                    long consumeFlame = ENCOUNTER.queryHealingBySpell("Lifebind",
                            (ENCOUNTER.encounterLengthMillis() / timeStepCount) * j,
                            (ENCOUNTER.encounterLengthMillis() / timeStepCount)) / SCALE;
//                                long rewind = encounter.queryHealingBySpell(Arrays.asList("Atonement", "Premonition of Piety", "Dark Reprimand", "Penance", "Divine Aegis"),
//                                (encounter.encounterLengthMillis() / timeStepCount) * j,
//                                        (encounter.encounterLengthMillis() / timeStepCount)) / scale;
                    long rewind = ENCOUNTER.queryHealingByCaster("Skullz",
                            (ENCOUNTER.encounterLengthMillis() / timeStepCount) * j,
                            (ENCOUNTER.encounterLengthMillis() / timeStepCount)) / SCALE;
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

    /**
     * Called every logic frame to run the logic on this GameObject.
     */
    @Override
    public void runLogic () {
        if (recompute) {
            final int timeStepCount = ((targetWidth * 3) / 4) / 10;
            if (timeStepCount == 0) return;
            Graph graph = ENCOUNTER.graph(ENCOUNTER.encounterLengthMillis() / timeStepCount, SCALE);
            graph.smooth(1);
            recompute = false;
            this.graph = graph;
        }
    }

    /**
     * Attaches a DrawListener to this GameObject.
     *
     * @param listener The DrawListener to attach to this GameObject.
     */
    @Override
    public void setDrawListener (DrawListener listener) {

    }

    /**
     * If present, returns the DrawListener associated with this GameObject. May be null.
     *
     * @return Either the DrawListener attached to this GameObject or null.
     */
    @Override
    public DrawListener getDrawListener() {
        return this;
    }

    /**
     * Assigns a collider to this GameObject.
     *
     * @param collider The collider to assign to this GameObject.
     */
    @Override
    public void setCollider (Collidable collider) {

    }

    /**
     * Gets a collider that is present on this object. If none are present returns null.
     *
     * @return Either the Collider attached to this GameObject or null.
     */
    @Override
    public Collidable getCollider () {
        return null;
    }
}
