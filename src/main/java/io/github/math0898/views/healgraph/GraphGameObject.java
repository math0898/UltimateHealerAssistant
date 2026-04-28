package io.github.math0898.views.healgraph;

import io.github.math0898.processing.Encounter;
import io.github.math0898.processing.Graph;
import io.github.math0898.processing.LogManager;
import io.github.math0898.utils.Utils;
import suga.engine.GameEngine;
import suga.engine.game.objects.BasicGameObject;
import suga.engine.graphics.DrawListener;
import suga.engine.graphics.GraphicsPanel;
import suga.engine.logger.Level;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.math0898.utils.ColorPalette.*;

/**
 * The Graph is considered a GameObject so that we have access to {@link #runLogic()}. This runs on a separate thread
 * from graphics and allows our GUI to be responsive even during computations. We don't have a need for a dynamic
 * DrawListener, so we also simply implement it here.
 *
 * @author Sugaku
 */
public class GraphGameObject extends BasicGameObject implements DrawListener {

    /**
     * The encounter to graph.
     */
    private Encounter encounter = LogManager.getInstance().getHighlightedEncounter();

    /**
     * The vertical scale component of the healing graph.
     */
    private static final int SCALE = 80000; // todo: Figure out a way to dynamically calculate.

    /**
     * Whether to stack ascent bars or not.
     */
    private static final boolean STACKED = false;

    /**
     * A horizontal offset applied to all elements.
     */
    private static final int HORIZONTAL_OFFSET = 170;

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
     * A list of casters which are being displayed on this GraphGameObject.
     */
    private final List<String> casters = new ArrayList<>();

    /**
     * Called every drawing frame so programs have a chance to make their voices heard on what gets drawn.
     *
     * @param width  The width of the pixel map.
     * @param height The height of the pixel map.
     * @param panel  The panel to apply changes to.
     */
    @Override
    public void applyChanges (int width, int height, GraphicsPanel panel) {
        if (targetWidth != width - HORIZONTAL_OFFSET) {
            targetWidth = width - HORIZONTAL_OFFSET;
            recompute = true;
            graph = null;
            return;
        }
        if (graph == null) return;
        final int startX = panel.getWidth() / 8 + HORIZONTAL_OFFSET;
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
                long lastSum = 0;
                long currentSum = 0;
                for (int k = 0; k < graph.stackedAscentBars.size(); k++) {
                    long mod = graph.stackedAscentBars.get(k).getValues().get(j);
                    currentSum += mod;
                    if (i >= lastSum && i <= currentSum && mod != 0)
                        panel.setBigPixel(startX + (j * 10), startY - ((int) i * 10), 9, graph.stackedAscentBars.get(k).getColor());
                    lastSum = currentSum;
                }
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
            final long timeStepSize = computeTimeStepSize();
            if (timeStepSize == -1) return;
            Graph graph = encounter.graph(timeStepSize, SCALE);

            for (String s : casters) {
                GameEngine.getLogger().log("Caster -> " + s, Level.DEBUG);
                addCasterAscent(graph, s, CLASS_EVOKER.getColor());
            } // todo: Use some logic to determine color.

            Boolean pres = specs.get("pres");
            Boolean holy = specs.get("holy");
            Boolean disc = specs.get("disc");
            Boolean resto = specs.get("resto");
            Boolean druid = specs.get("druid");
            Boolean pally = specs.get("pally");
            if (pres != null && pres) {
                addStackedSpellAscent(graph, SpellQueries.CONSUME_FLAME.spellName, SpellQueries.CONSUME_FLAME.color);
                addStackedSpellAscent(graph, SpellQueries.EMERALD_COMMUNION.spellName, SpellQueries.EMERALD_COMMUNION.color);
                addStackedSpellAscent(graph, SpellQueries.REWIND.spellName, SpellQueries.REWIND.color);
                addCasterAscent(graph, "Nillath", CLASS_EVOKER.getColor());
            }
            if (holy != null && holy){
                addStackedSpellAscent(graph, SpellQueries.DIVINE_HYMN.spellName, SpellQueries.DIVINE_HYMN.color);
                addStackedSpellAscent(graph, SpellQueries.PIETY.spellName, SpellQueries.PIETY.color);
                addCasterAscent(graph, "Seranite", CLASS_PRIEST.getColor());
            }
            if (disc != null && disc) {
                // todo: disc priest is special with their cooldowns.
                addStackedSpellAscent(graph, SpellQueries.PIETY.spellName, SpellQueries.PIETY.color);
                addStackedSpellAscent(graph, SpellQueries.ATONEMENT.spellName, SpellQueries.ATONEMENT.color);
                addStackedSpellAscent(graph, SpellQueries.EVANGELISM.spellName, SpellQueries.EVANGELISM.color);
                addCasterAscent(graph, "Skullz", CLASS_PRIEST.getColor());
            }
            if (resto != null && resto) {
                addStackedSpellAscent(graph, SpellQueries.SPIRIT_LINK.spellName, SpellQueries.SPIRIT_LINK.color);
                List<Long> playerHealing = Utils.addLists(encounter.queryHealingInstantsByCaster("Syudou", timeStepSize),
                                                          encounter.queryHealingInstantsByCaster("Healing Tide Totem", timeStepSize));
                graph.addAscent(new AscentBar(Utils.scaleList(playerHealing, SCALE), CLASS_SHAMAN.getColor()));
                graph.addStackedAscent(new AscentBar(Utils.scaleList(encounter.queryHealingInstantsByCaster("Healing Tide Totem", timeStepSize), SCALE), SpellQueries.HEALING_TIDE.color));
            }
            if (druid != null && druid) {
                addStackedSpellAscent(graph, SpellQueries.TRANQUILITY.spellName, SpellQueries.TRANQUILITY.color);
                addCasterAscent(graph, "Skullz", CLASS_DRUID.getColor());
            }
            if (pally != null && pally)
                addCasterAscent(graph,  "Skullz", CLASS_PALADIN.getColor());
            graph.smooth(1);
            recompute = false;
            this.graph = graph;
        }
    }

    /**
     * A quick method to calculate how large, in millis, each square is in the time dimension.
     */
    private long computeTimeStepSize () {
        final int timeStepCount = ((targetWidth * 3) / 4) / 10;
        if (timeStepCount == 0 || encounter == null) return -1;
        return encounter.encounterLengthMillis() / timeStepCount;
    }

    /**
     * Adds a caster to the GraphGameObject to display as an ascent bar.
     *
     * @param casterName The name of the caster. Note this is used by the {@link Encounter#queryHealingInstantsByCaster};
     *                   in its current implementation it considers a casterName to match when it's contained within the
     *                   actor name listed in logs.
     */
    public void toggleCasterAscent (String casterName) {
        if (casters.contains(casterName))
            casters.remove(casterName);
        else casters.add(casterName);
        recompute = true;
    }

    /**
     * Adds a caster of the given name as an ascent bar.
     *
     * @param graph The graph object to modify.
     * @param casterName The name of the caster. Note this is used by the {@link Encounter#queryHealingInstantsByCaster};
     *                   in its current implementation it considers a casterName to match when it's contained within the
     *                   actor name listed in logs.
     * @param color The color of the ascent.
     */ // todo: This feels kinda strange design wise. It feels like it should go in Graph but Graph has no relation to Encounter.
    public void addCasterAscent (Graph graph, String casterName, Color color) {
        final List<Long> healingInstants = encounter.queryHealingInstantsByCaster(casterName, computeTimeStepSize());
        final List<Long> scaledHealingInstants = Utils.scaleList(healingInstants, SCALE);
        final AscentBar bar = new AscentBar(scaledHealingInstants, color);
        graph.addAscent(bar);
    }

    /**
     * Adds a spell of the given name as a stacked ascent bar.
     *
     * @param graph The graph object to modify.
     * @param spellName The name of the spell. Note this is used by the {@link Encounter#queryHealingInstantsBySpell};
     *                   in its current implementation it considers a spellName to match when it's contained within the
     *                   full spell name listed in logs.
     * @param color The color of the ascent.
     */ // todo: This feels kinda strange design wise. It feels like it should go in Graph but Graph has no relation to Encounter.
    public void addStackedSpellAscent (Graph graph, String spellName, Color color) {
        final List<Long> healingInstants = encounter.queryHealingInstantsBySpell(spellName, computeTimeStepSize());
        final List<Long> scaledHealingInstants = Utils.scaleList(healingInstants, SCALE);
        final AscentBar bar = new AscentBar(scaledHealingInstants, color);
        graph.addStackedAscent(bar);
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
