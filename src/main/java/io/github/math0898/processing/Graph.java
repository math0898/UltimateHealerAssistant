package io.github.math0898.processing;

import java.util.ArrayList;
import java.util.List;

/**
 * Graphs are a collection of points each with a column coordinate and height.
 *
 * @author Sugaku
 */
public class Graph {

    /**
     * The maximum height achieved by this graph.
     */
    private long max = 0;

    /**
     * Values for each of the columns in this graph.
     */
    private List<Long> columns = new ArrayList<>();

    /**
     * Adds a column entry on the graph at the next available point.
     *
     * @param value The value to add.
     */
    public void addColumn (long value) {
        if (max < value) max = value;
        columns.add(value);
    }

    /**
     * Prints this graph to console.
     */
    public void print () {
        for (long i = max; i >= 0; i--) {
            for (int j = 0; j < columns.size(); j++) {
                if (columns.get(j) >= i) System.out.print("|");
                else System.out.print(" ");
            }
            System.out.print("\n");
        }
    }
    // Maximal height and count for each column.
    // Work your way down the columns printing any that have a value >= Y.
    // Naturally starting at the maximal height and spaces for no data.
}
