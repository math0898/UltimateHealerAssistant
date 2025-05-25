package io.github.math0898.processing;

import io.github.math0898.gui.AscentBar;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Graphs are a collection of points each with a column coordinate and height.
 *
 * @author Sugaku
 */
public class Graph {

    /**
     * The maximum height achieved by this graph.
     */
    public long max = 0;

    /**
     * Values for each of the columns in this graph.
     */
    public List<Long> overheal = new ArrayList<>();

    /**
     * Values for each of the columns in this graph for heal amount.
     */
    public List<Long> heal = new ArrayList<>();

    /**
     * Values for each of the columns in this graph for damage amount.
     */
    public List<Long> damage = new ArrayList<>();

    /**
     * A List of ascent bars to add to this graph.
     */
    public List<AscentBar> ascentBars = new ArrayList<>();

    /**
     * A list of ascent bars that are stacked and drawn in-front of the existing ascent bars.
     */
    public List<AscentBar> stackedAscentBars = new ArrayList<>();

    /**
     * Smooths the graph by looking ahead and behind to average the current note.
     *
     * @param factor This is the number of columns ahead and behind to look in order to average values.
     */
    public void smooth (int factor) {
        List<Long> oldOverheal = overheal;
        List<Long> oldHeal = heal;
        List<Long> oldDamage = damage;
        overheal = new ArrayList<>();
        heal = new ArrayList<>(); // todo: Consider factor neq 1
        damage = new ArrayList<>();
        for (int i = 0; i < oldOverheal.size(); i++) {
            long sum = 0;
            for (int j = i - factor; j <= factor + i; j++) {
                if (j > 0 && j < oldOverheal.size())
                    sum += oldOverheal.get(j);
            }
            overheal.add(sum / ((factor * 2L) + 1));
        }
        for (int i = 0; i < oldHeal.size(); i++) {
            long sum = 0;
            for (int j = i - factor; j <= factor + i; j++) {
                if (j > 0 && j < oldHeal.size())
                    sum += oldHeal.get(j);
            }
            heal.add(sum / ((factor * 2L) + 1));
        }
        for (int i = 0; i < oldDamage.size(); i++) {
            long sum = 0;
            for (int j = i - factor; j <= factor + i; j++) {
                if (j > 0 && j < oldDamage.size())
                    sum += oldDamage.get(j);
            }
            damage.add(sum / ((factor * 2L) + 1));
        }
        Stream<AscentBar> bars = Stream.concat(ascentBars.stream(), stackedAscentBars.stream());
        for (AscentBar bar : bars.toList()) {
            List<Long> list = bar.getValues();
            List<Long> swap = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                long sum = 0;
                for (int j = i - factor; j <= factor + i; j++) {
                    if (j > 0 && j < list.size())
                        sum += list.get(j);
                }
                swap.add(sum / ((factor * 2L) + 1));
            }
            list.clear();
            list.addAll(swap);
        }
    }

    /**
     * Adds a column entry on the graph at the next available point.
     *
     * @param value The value to add.
     */
    public void addColumn (long value) {
        addColumn(value, value);
    }

    /**
     * Adds a column entry on the graph at the next available point.
     *
     * @param overheal The value to add.
     * @param heal The heal value to add.
     */
    public void addColumn (long overheal, long heal) {
        if (max < overheal) max = overheal;
        this.overheal.add(overheal);
        this.heal.add(heal);
    }

    /**
     * Adds an ascent bar to this graph.
     *
     * @param bar The bar to add to this graph.
     */
    public void addAscent (AscentBar bar) {
        ascentBars.add(bar);
    }

    /**
     * Adds a stacked ascent bar to this graph.
     *
     * @param bar The bar to add to this graph.
     */
    public void addStackedAscent (AscentBar bar) {
        stackedAscentBars.add(bar);
    }

    /**
     * Adds a column entry on the graph at the next available point.
     *
     * @param overheal The value to add.
     * @param heal The heal value to add.
     * @param damage The damage value to add.
     */
    public void addColumn (long overheal, long heal, long damage) {
        if (max < overheal) max = overheal;
        this.overheal.add(overheal);
        this.heal.add(heal);
        this.damage.add(damage);
    }

    /**
     * Prints this graph to console.
     */
    public void print () {
        for (long i = max; i >= 0; i--) {
            for (int j = 0; j < overheal.size(); j++) {
                if (overheal.get(j) >= i) System.out.print("|");
                else System.out.print(" ");
            }
            System.out.print("\n");
        }
    }
    // Maximal height and count for each column.
    // Work your way down the columns printing any that have a value >= Y.
    // Naturally starting at the maximal height and spaces for no data.
}
