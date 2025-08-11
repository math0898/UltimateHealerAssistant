package io.github.math0898.views.nightsummary;

import suga.engine.game.objects.BasicGameObject;
import suga.engine.graphics.DrawListener;
import suga.engine.graphics.GraphicsPanel;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A rectangle bar is a bar made out of rectangles. It has a color, size of each rectangle, padding between rectangles,
 * and a value for the number of rectangles.
 *
 * @author Sugaku
 */
public class RectangleBar extends BasicGameObject implements DrawListener {

    /**
     * The color of this RectangleBar.
     */
    private final Color color;

    /**
     * The width of the rectangles.
     */
    private final int width;

    /**
     * The height of the rectangles.
     */
    private final int height;

    /**
     * Any horizontal padding between rectangles.
     */
    private final int padding;

    /**
     * The number of rectangles to draw.
     */
    private final int count;

    /**
     * The x coordinate for the bottom left rectangle's bottom left corner.
     */
    private final int xPos;

    /**
     * The y coordinate for the bottom left rectangle's bottom left corner.
     */
    private final int yPos;

    /**
     * Whether we should show a summarizing number at the end of the bar.
     */
    private final boolean showNumber;

    /**
     * Creates a new RectangleBar with the given display values and actual value.
     *
     * @param color The color of the rectangle.
     * @param width The width of each individual rectangle.
     * @param height The height of each rectangle.
     * @param padding Any space to put between each rectangle.
     * @param count The number of rectangles to draw. Also known as the value.
     * @param x The x value of the bottom left of the first rectangle.
     * @param y The y value of the bottom left of the first rectangle.
     */
    public RectangleBar (Color color, int width, int height, int padding, int count, int x, int y) {
        this(color, width, height, padding, count, x, y, true);
    }

    /**
     * Creates a new RectangleBar with the given display values and actual value.
     *
     * @param color The color of the rectangle.
     * @param width The width of each individual rectangle.
     * @param height The height of each rectangle.
     * @param padding Any space to put between each rectangle.
     * @param count The number of rectangles to draw. Also known as the value.
     * @param x The x value of the bottom left of the first rectangle.
     * @param y The y value of the bottom left of the first rectangle.
     */
    public RectangleBar (Color color, int width, int height, int padding, int count, int x, int y, boolean number) {
        this.color = color;
        this.width = width;
        this.height = height;
        this.padding = padding;
        this.count = count;
        this.xPos = x;
        this.yPos = y;
        this.showNumber = number;
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
     * Called every drawing frame so programs have a chance to make their voices heard on what gets drawn.
     *
     * @param width  The width of the pixel map.
     * @param height The height of the pixel map.
     * @param panel  The panel to apply changes to.
     */
    @Override
    public void applyChanges (int width, int height, GraphicsPanel panel) {
        final int PER_STEP_OFFSET = this.width + padding;
        for (int i = 0; i < count; i++)
            panel.setRectangle(xPos + (PER_STEP_OFFSET * i) , yPos - this.height, this.width, this.height, color);
        if (showNumber) {
            // todo: We need a better way to do text. Probably also need to make a utility function out of this.
            BufferedImage buffer = new BufferedImage(300, this.height, BufferedImage.TYPE_INT_ARGB);
            Graphics graphics = buffer.createGraphics();
            graphics.setFont(new Font("Comic Sans", Font.BOLD, (int) (this.height * 0.8)));
            graphics.setColor(new Color(200,200,200));
            graphics.drawString("(" + count + ")", 0, this.height);
            panel.addImage(xPos + (PER_STEP_OFFSET * count), yPos - (int) (this.height * 0.2) - this.height, 300, this.height, buffer);
        }
    }
}
