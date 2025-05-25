package io.github.math0898.gui;

import io.github.math0898.Main;
import io.github.math0898.processing.Encounter;
import io.github.math0898.processing.Graph;
import suga.engine.game.objects.BasicGameObject;
import suga.engine.graphics.DrawListener;
import suga.engine.graphics.GraphicsPanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The Graph is considered a GameObject so that we have access to {@link #runLogic()}. This runs on a separate thread
 * from graphics and allows our GUI to be responsive even during computations. We don't have a need for a dynamic
 * DrawListener, so we also simply implement it here.
 *
 * @author Sugaku
 */
public class GraphGameObject extends BasicGameObject implements  DrawListener {

    /**
     * The encounter to graph.
     */
    private Encounter encounter = Main.encounters.get(39);

    /**
     * The vertical scale component of the healing graph.
     */
    private static final int SCALE = 1000000; // todo: Figure out a way to dynamicly calculate.

    /**
     * Whether to stack ascent bars or not.
     */
    private static final boolean STACKED = false;

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
     * A map of specs which including their current toggle state.
     */
    private Map<String, Boolean> specs = new HashMap<>();

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
                if (graph.overheal.get(j) >= i && graph.overheal.get(j) != 0) { // todo: Refactor so queries don't need to be ran during the graphics thread. Perhaps an "accents" list of arrays.
                    if (graph.heal.get(j) >= i && graph.heal.get(j) != 0) // todo: Make all of these ascent bars.
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
//                if (STACKED) { // todo: Latest graph in the daisy chain is overwriting earlier ones. Need to also have an option to color if within it's realm.
                    long lastSum = 0;
                    long currentSum = 0;
                    for (int k = 0; k < graph.stackedAscentBars.size(); k++) {
                        long mod = graph.stackedAscentBars.get(k).getValues().get(j);
                        currentSum += mod;
                        if (i >= lastSum && i <= currentSum && mod != 0)
                            panel.setBigPixel(startX + (j * 10), startY - ((int) i * 10), 9, graph.stackedAscentBars.get(k).getColor());
                        lastSum = currentSum;
                    }
//                }
                if (graph.damage.get(j) == i && graph.damage.get(j) != 0)
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
            if (encounter == null) return;
            Graph graph = encounter.graph(encounter.encounterLengthMillis() / timeStepCount, SCALE);
            // todo: If this will be the strategy let's make an enum.
            ArrayList<Long>[] queries = new ArrayList[13];
            Color[] colors = {new Color(51, 147, 127), new Color(255, 255, 255), new Color(255, 244, 104), new Color(0, 112, 221),
                    new Color(196, 30, 58), new Color(0, 255, 152), new Color(255, 244, 104),
                    new Color(196, 30, 58),
                    new Color(255, 255, 255), new Color(255, 255, 255), new Color(255, 255, 255),
                    new Color(0, 255, 152), new Color(63, 199, 235)};
            Boolean pres = specs.get("pres");
            Boolean holy = specs.get("holy");
            Boolean disc = specs.get("disc");
            Boolean resto = specs.get("resto");
            for (int j = 0; j < timeStepCount; j++) {
                if (pres != null && pres) {
                    ArrayList<Long> list = queries[0];
                    if (list == null) {
                        list = new ArrayList<>();
                        queries[0] = list;
                    }
                    long playerHealing = encounter.queryHealingByCaster("Nillath",
                            (encounter.encounterLengthMillis() / timeStepCount) * j,
                            (encounter.encounterLengthMillis() / timeStepCount)) / SCALE;
                    list.add(playerHealing);
                    list = queries[4];
                    if (list == null) {
                        list = new ArrayList<>();
                        queries[4] = list;
                    }
                    long spellHealing = encounter.queryHealingBySpell("Consume Flame",
                            (encounter.encounterLengthMillis() / timeStepCount) * j,
                            (encounter.encounterLengthMillis() / timeStepCount)) / SCALE;
                    list.add(spellHealing);
                    list = queries[5];
                    if (list == null) {
                        list = new ArrayList<>();
                        queries[5] = list;
                    }
                    spellHealing = encounter.queryHealingBySpell("Emerald Communion",
                            (encounter.encounterLengthMillis() / timeStepCount) * j,
                            (encounter.encounterLengthMillis() / timeStepCount)) / SCALE;
                    list.add(spellHealing);
                    list = queries[6];
                    if (list == null) {
                        list = new ArrayList<>();
                        queries[6] = list;
                    }
                    spellHealing = encounter.queryHealingBySpell("Rewind",
                            (encounter.encounterLengthMillis() / timeStepCount) * j,
                            (encounter.encounterLengthMillis() / timeStepCount)) / SCALE;
                    list.add(spellHealing);
                }
                if (holy != null && holy) {
                    ArrayList<Long> list = queries[1];
                    if (list == null) {
                        list = new ArrayList<>();
                        queries[1] = list;
                    }
                    long playerHealing = encounter.queryHealingByCaster("Sunfl",
                            (encounter.encounterLengthMillis() / timeStepCount) * j,
                            (encounter.encounterLengthMillis() / timeStepCount)) / SCALE;
                    list.add(playerHealing);
                    list = queries[7];
                    if (list == null) {
                        list = new ArrayList<>();
                        queries[7] = list;
                    }
                    long spellHealing = encounter.queryHealingBySpell("Divine Hymn",
                            (encounter.encounterLengthMillis() / timeStepCount) * j,
                            (encounter.encounterLengthMillis() / timeStepCount)) / SCALE;
                    list.add(spellHealing);
                }
                if (disc != null && disc) {
                    ArrayList<Long> list = queries[2];
                    if (list == null) {
                        list = new ArrayList<>();
                        queries[2] = list;
                    }
                    long playerHealing = encounter.queryHealingByCaster("Skullz",
                            (encounter.encounterLengthMillis() / timeStepCount) * j,
                            (encounter.encounterLengthMillis() / timeStepCount)) / SCALE;
                    list.add(playerHealing);
                    list = queries[8];
                    if (list == null) {
                        list = new ArrayList<>();
                        queries[8] = list;
                    }
                    long spellHealing = encounter.queryHealingBySpell("Evangalism",
                            (encounter.encounterLengthMillis() / timeStepCount) * j,
                            (encounter.encounterLengthMillis() / timeStepCount)) / SCALE;
                    list.add(spellHealing);
                    // todo: Check timing relative to Evangalism.
                    list = queries[9];
                    if (list == null) {
                        list = new ArrayList<>();
                        queries[9] = list;
                    }
                    spellHealing = encounter.queryHealingBySpell("Premonition of Piety",
                            (encounter.encounterLengthMillis() / timeStepCount) * j,
                            (encounter.encounterLengthMillis() / timeStepCount)) / SCALE;
                    list.add(spellHealing);
                    // todo: Check timing relative to Evangalism.
                    list = queries[10];
                    if (list == null) {
                        list = new ArrayList<>();
                        queries[10] = list;
                    }
                    spellHealing = encounter.queryHealingBySpell("Atonement",
                            (encounter.encounterLengthMillis() / timeStepCount) * j,
                            (encounter.encounterLengthMillis() / timeStepCount)) / SCALE;
                    list.add(spellHealing);
                }
                if (resto != null && resto) {
                    ArrayList<Long> list = queries[3];
                    if (list == null) {
                        list = new ArrayList<>();
                        queries[3] = list;
                    }
                    long playerHealing = encounter.queryHealingByCaster("Taryn", // todo: Syudou
                            (encounter.encounterLengthMillis() / timeStepCount) * j,
                            (encounter.encounterLengthMillis() / timeStepCount)) / SCALE;
                    playerHealing += encounter.queryHealingBySpell("Spirit Link",
                            (encounter.encounterLengthMillis() / timeStepCount) * j,
                            (encounter.encounterLengthMillis() / timeStepCount)) / SCALE;
                    list.add(playerHealing);
                    list = queries[11];
                    if (list == null) {
                        list = new ArrayList<>();
                        queries[11] = list;
                    }
                    long spellHealing = encounter.queryHealingBySpell("Spirit Link",
                            (encounter.encounterLengthMillis() / timeStepCount) * j,
                            (encounter.encounterLengthMillis() / timeStepCount)) / SCALE;
                    list.add(spellHealing);
                    list = queries[12];
                    if (list == null) {
                        list = new ArrayList<>();
                        queries[12] = list;
                    }
                    spellHealing = encounter.queryHealingByCaster("Healing Tide Totem",
                            (encounter.encounterLengthMillis() / timeStepCount) * j,
                            (encounter.encounterLengthMillis() / timeStepCount)) / SCALE;
                    list.add(spellHealing);
                }
            }
            for (int i = 0; i < 4; i++)
                if (queries[i] != null)
                    graph.addAscent(new AscentBar(queries[i], colors[i]));
            for (int i = 4; i < queries.length; i++)
                if (queries[i] != null)
                    graph.addStackedAscent(new AscentBar(queries[i], colors[i]));
            graph.smooth(1);
            recompute = false;
            this.graph = graph;
        }
    }

    /**
     * Sets the encounter that this GraphGameObject should be drawing.
     *
     * @param encounter The encounter to graph.
     */
    public void setEncounter (Encounter encounter) {
        this.encounter = encounter;
        recompute = true;
    }

    /**
     * Toggles the given spec to either on or off.
     *
     * @param spec The spec to toggle.
     */
    public void toggleSpec (String spec) {
        Boolean current = specs.get(spec);
        if (current == null) current = false;
        current = !current;
        specs.put(spec, current);
        recompute = true;
    }
}
