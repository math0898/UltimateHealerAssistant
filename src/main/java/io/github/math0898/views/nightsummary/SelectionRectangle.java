package io.github.math0898.views.nightsummary;

import suga.engine.game.objects.BasicGameObject;
import suga.engine.graphics.DrawListener;
import suga.engine.graphics.GraphicsPanel;

import java.awt.*;

import static io.github.math0898.views.nightsummary.PlayerPlacard.*;

/**
 * The SelectionRectangle is a rectangle around player placards to help indicate which one is selected.
 *
 * @author Sugaku
 */
public class SelectionRectangle extends BasicGameObject implements DrawListener {

    /**
     * The amount of padding to place between the rectangle and the placard it's highlighting.
     */
    private static final int PLACARD_PADDING = 3;

    /**
     * The width of this selection rectangle.
     */
    private static final int RECTANGLE_SIZE = 3;

    /**
     * Whether to draw this SelectionRectangle or not.
     */
    private boolean isActive = false;

    /**
     * Sets whether this SelectionRectangle is active or not.
     *
     * @param active Whether this selection should be active or not.
     */
    public void setActive (boolean active) {
        this.isActive = active;
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
        if (!isActive) return;
        final int farLeftXCoordinate = (int) pos.getX() - ICON_WIDTH / 2 + ICON_OFFSET_HOR - PLACARD_PADDING - RECTANGLE_SIZE;
        final int farRightXCoordinate = (int) pos.getX() + ICON_WIDTH / 2 + ICON_OFFSET_HOR + HOR_PADDING_NAME + PLACARD_PADDING + RECTANGLE_SIZE + MAX_NAME_WIDTH;
        final int topYCoordinate = (int) pos.getY() - (ICON_HEIGHT / 2) - PLACARD_PADDING - RECTANGLE_SIZE;
        final int bottomYCoordinate = (int) pos.getY() + (ICON_HEIGHT / 2) + PLACARD_PADDING + RECTANGLE_SIZE;
        // Vertical Left Slice
        panel.setRectangle(farLeftXCoordinate, topYCoordinate, RECTANGLE_SIZE, ICON_HEIGHT + (PLACARD_PADDING + RECTANGLE_SIZE) * 2, new Color(200, 200, 200));
        // Horizontal Top Slice
        panel.setRectangle(farLeftXCoordinate, topYCoordinate, ICON_WIDTH + HOR_PADDING_NAME + MAX_NAME_WIDTH + (PLACARD_PADDING + RECTANGLE_SIZE) * 2, RECTANGLE_SIZE, new Color(200, 200, 200));
        // Horizontal Bottom Slice
        panel.setRectangle(farLeftXCoordinate, bottomYCoordinate, ICON_WIDTH + HOR_PADDING_NAME + MAX_NAME_WIDTH + (PLACARD_PADDING + RECTANGLE_SIZE) * 2, RECTANGLE_SIZE, new Color(200, 200, 200));
        // Vertical Right Slice
        panel.setRectangle(farRightXCoordinate, topYCoordinate, RECTANGLE_SIZE, ICON_HEIGHT + (PLACARD_PADDING + RECTANGLE_SIZE) * 2 + RECTANGLE_SIZE, new Color(200, 200, 200));
    }
}
