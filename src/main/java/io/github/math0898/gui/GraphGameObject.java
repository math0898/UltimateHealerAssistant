package io.github.math0898.gui;

import io.github.math0898.Main;
import io.github.math0898.processing.Encounter;
import io.github.math0898.processing.Graph;
import io.github.math0898.utils.Utils;
import suga.engine.game.objects.BasicGameObject;
import suga.engine.graphics.DrawListener;
import suga.engine.graphics.GraphicsPanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
     *
     */
    private enum SpellQueries {

        // Pres spells
        CONSUME_FLAME("Consume Flame", new Color(196, 30, 58)),
        EMERALD_COMMUNION("Emerald Communion", new Color(0, 255, 152)),
        REWIND("Rewind", new Color(255, 244, 104)),
        // Holy Priest
        DIVINE_HYMN("Divine Hymn", new Color(196, 30, 58)),
        // Disc Priest
        EVANGELISM("Evangelism", new Color(255, 255, 255)), // todo: Requires special handling.
        PIETY("Premonition of Piety", new Color(255, 255, 255)), // todo: Requires special handling.
        ATONEMENT("Atonement", new Color(255, 255, 255)), // todo: Requires special handling.
        // Resto Shammy
        SPIRIT_LINK("Spirit Link", new Color(0, 255, 152)),
        HEALING_TIDE("Healing Tide Totem", new Color(63, 199, 235)); // todo: Requires special handling.

        public final String spellName;
        public final Color color;

        SpellQueries (String spellName, Color color) {
            this.spellName = spellName;
            this.color = color;
        }

        public static SpellQueries fromOrdinal (int ordinal) {
            for (SpellQueries query : SpellQueries.values())
                if (ordinal == query.ordinal())
                    return query;
            return REWIND;
        }
    }

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
            final long timeStepSize = encounter.encounterLengthMillis() / timeStepCount;
            if (timeStepCount == 0) return;
            if (encounter == null) return;
            Graph graph = encounter.graph(encounter.encounterLengthMillis() / timeStepCount, SCALE);
            Color[] colors = {new Color(51, 147, 127), new Color(255, 255, 255), new Color(255, 244, 104), new Color(0, 112, 221)};
            Boolean pres = specs.get("pres");
            Boolean holy = specs.get("holy");
            Boolean disc = specs.get("disc");
            Boolean resto = specs.get("resto");
            if (pres != null && pres) {
                for (SpellQueries spell : new SpellQueries[]{SpellQueries.CONSUME_FLAME, SpellQueries.EMERALD_COMMUNION, SpellQueries.REWIND})
                    graph.addStackedAscent(new AscentBar(Utils.scaleList(encounter.queryHealingInstantsBySpell(spell.spellName, timeStepSize), SCALE), spell.color));
                graph.addAscent(new AscentBar(Utils.scaleList(encounter.queryHealingInstantsByCaster("Nillath", timeStepSize), SCALE), colors[0]));
            }
            if (holy != null && holy){
                graph.addStackedAscent(new AscentBar(Utils.scaleList(encounter.queryHealingInstantsBySpell(SpellQueries.DIVINE_HYMN.spellName, timeStepSize), SCALE), SpellQueries.DIVINE_HYMN.color));
                graph.addAscent(new AscentBar(Utils.scaleList(encounter.queryHealingInstantsByCaster("Sunfl", timeStepSize), SCALE), colors[1]));
            } if (disc != null && disc) {
                // todo: disc priest is special with their cooldowns.
                for (SpellQueries spell : new SpellQueries[]{SpellQueries.PIETY, SpellQueries.ATONEMENT, SpellQueries.EVANGELISM})
                    graph.addStackedAscent(new AscentBar(Utils.scaleList(encounter.queryHealingInstantsBySpell(spell.spellName, timeStepSize), SCALE), spell.color));
                graph.addAscent(new AscentBar(Utils.scaleList(encounter.queryHealingInstantsByCaster("Skullz", timeStepSize), SCALE), colors[2]));
            } if (resto != null && resto) {
                graph.addStackedAscent(new AscentBar(Utils.scaleList(encounter.queryHealingInstantsBySpell(SpellQueries.SPIRIT_LINK.spellName, timeStepSize), SCALE), SpellQueries.SPIRIT_LINK.color));
                List<Long> playerHealing = Utils.addLists(encounter.queryHealingInstantsByCaster("Taryn", timeStepSize),
                                                          encounter.queryHealingInstantsByCaster("Healing Tide Totem", timeStepSize));
                graph.addAscent(new AscentBar(Utils.scaleList(playerHealing, SCALE), colors[3]));
                graph.addStackedAscent(new AscentBar(Utils.scaleList(encounter.queryHealingInstantsByCaster("Healing Tide Totem", timeStepSize), SCALE), SpellQueries.HEALING_TIDE.color));
            }
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
