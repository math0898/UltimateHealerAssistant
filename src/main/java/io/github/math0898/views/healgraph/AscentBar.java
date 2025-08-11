package io.github.math0898.views.healgraph;

import java.awt.*;
import java.util.List;

/**
 * An ascent bar that displays as a bar on the graph.
 *
 * @author Sugaku
 */
public class AscentBar {

    /**
     * A list of values for these bars.
     */
    private final List<Long> values;

    /**
     * The color of this ascent bar.
     */
    private final Color color;

    /**
     * Creates a new AscentBar with the given values and the given color.
     *
     * @param values The values of these bars.
     * @param color  The color for the bars.
     */
    public AscentBar (List<Long> values, Color color) {
        this.values = values;
        this.color = color;
    }

    /**
     * An accessor method for the values contained inside this AscentBar.
     *
     * @return The values in this AscentBar.
     */
    public List<Long> getValues () {
        return values;
    }

    /**
     * An accessor method for the color that  this AscentBar should be.
     *
     * @return The color that should be in this AscentBar.
     */
    public Color getColor () {
        return color;
    }
}
