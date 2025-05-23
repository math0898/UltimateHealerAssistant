package io.github.math0898.gui;

import io.github.math0898.Main;
import io.github.math0898.processing.Encounter;
import io.github.math0898.processing.Graph;
import suga.engine.game.objects.GameObject;
import suga.engine.graphics.DrawListener;
import suga.engine.graphics.GraphicsPanel;
import suga.engine.physics.collidables.Collidable;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

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
     * Whether to stack ascent bars or not.
     */
    private static final boolean STACKED = true;

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
        final int startX = panel.getWidth() / 8;
        final int startY = (panel.getHeight() * 7) / 8;
        for (long i = graph.max; i >= 0; i--)
            for (int j = 0; j < graph.overheal.size(); j++) {
                if (graph.overheal.get(j) >= i) { // todo: Refactor so queries don't need to be ran during the graphics thread. Perhaps an "accents" list of arrays.
                    if (graph.heal.get(j) >= i) // todo: Make all of these ascent bars.
                        panel.setBigPixel(startX + (j * 10), startY - ((int) i * 10), 9, new Color(22, 237, 64));
                    else
                        panel.setBigPixel(startX + (j * 10), startY - ((int) i * 10), 9, new Color(39, 150, 60));
                }
                for (int k = 0; k < graph.ascentBars.size(); k++) {
                    AscentBar bar = graph.ascentBars.get(k);
                    long value = bar.getValues().get(j);
                    if (!STACKED) {
                        if (value >= i && value > 0)
                            panel.setBigPixel(startX + (j * 10), startY - ((int) i * 10), 9, bar.getColor());
                    }
                }
                if (STACKED) { // todo: Latest graph in the daisy chain is overwriting earlier ones. Need to also have an option to color if within it's realm.
                    long lastSum = 0;
                    long currentSum = 0;
                    for (int k = 0; k < graph.ascentBars.size(); k++) {
                        long mod = graph.ascentBars.get(k).getValues().get(j);
                        currentSum += mod;
                        if (i >= lastSum && i <= currentSum && mod != 0)
                            panel.setBigPixel(startX + (j * 10), startY - ((int) i * 10), 9, graph.ascentBars.get(k).getColor());
                        lastSum = currentSum;
                    }
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
            ArrayList<Long> consumeFlameList = new ArrayList<>();
            ArrayList<Long> rewindList = new ArrayList<>();
            ArrayList<Long> sunflower = new ArrayList<>();
            ArrayList<Long> syudou = new ArrayList<>();
            ArrayList<Long> mylove = new ArrayList<>();
            ArrayList<Long> consumes = new ArrayList<>();
            for (int j = 0; j < timeStepCount; j++) {
                long consumeFlame = ENCOUNTER.queryHealingByCaster("Nillath",
                        (ENCOUNTER.encounterLengthMillis() / timeStepCount) * j,
                        (ENCOUNTER.encounterLengthMillis() / timeStepCount)) / SCALE;
//                                long rewind = encounter.queryHealingBySpell(Arrays.asList("Atonement", "Premonition of Piety", "Dark Reprimand", "Penance", "Divine Aegis"),
//                                (encounter.encounterLengthMillis() / timeStepCount) * j,
//                                        (encounter.encounterLengthMillis() / timeStepCount)) / scale;
                consumeFlameList.add(consumeFlame);
                long rewind = ENCOUNTER.queryHealingByCaster("Skullz",
                        (ENCOUNTER.encounterLengthMillis() / timeStepCount) * j,
                        (ENCOUNTER.encounterLengthMillis() / timeStepCount)) / SCALE;
                rewindList.add(rewind);
                long sunflowerItem = ENCOUNTER.queryHealingByCaster("Sunfl",
                        (ENCOUNTER.encounterLengthMillis() / timeStepCount) * j,
                        (ENCOUNTER.encounterLengthMillis() / timeStepCount)) / SCALE;
                sunflower.add(sunflowerItem);
                long syudouItem = ENCOUNTER.queryHealingByCaster("Taryn",
                        (ENCOUNTER.encounterLengthMillis() / timeStepCount) * j,
                        (ENCOUNTER.encounterLengthMillis() / timeStepCount)) / SCALE;
                syudou.add(syudouItem);
                long myloveItem = ENCOUNTER.queryHealingByCaster("Mylov",
                        (ENCOUNTER.encounterLengthMillis() / timeStepCount) * j,
                        (ENCOUNTER.encounterLengthMillis() / timeStepCount)) / SCALE;
                mylove.add(myloveItem);
                long healingPotion = ENCOUNTER.queryHealingBySpell(Arrays.asList("Algari Healing Potion", "Healthstone"),
                        (ENCOUNTER.encounterLengthMillis() / timeStepCount) * j,
                        (ENCOUNTER.encounterLengthMillis() / timeStepCount)) / SCALE;
                consumes.add(healingPotion);
            }
            graph.addAscent(new AscentBar(consumeFlameList, new Color(51, 147, 127))); // Uravaal
//            graph.addAscent(new AscentBar(rewindList, new Color(210, 204, 35))); // Skullz
//            graph.addAscent(new AscentBar(sunflower, new Color(215, 215, 230)));
//            graph.addAscent(new AscentBar(syudou, new Color(85, 95, 230)));
//            graph.addAscent(new AscentBar(mylove, new Color(163, 48, 201)));
//            graph.addAscent(new AscentBar(consumes, new Color(213, 76, 76)));
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
    public DrawListener getDrawListener () {
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
