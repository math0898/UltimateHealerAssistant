package io.github.math0898.views.general;

import io.github.math0898.game.UltimateHealerAssistantGame;
import suga.engine.game.objects.BasicGameObject;
import suga.engine.graphics.DrawListener;
import suga.engine.graphics.GraphicsPanel;

import java.awt.*;

/**
 * The MouseIndicator is used for indicating the location of the cursor on the program. Typically added when debugging
 * is enabled/disabled.
 *
 * @author Sugaku
 */
public class MouseIndicator extends BasicGameObject {

    /**
     * The current x position of the mouse.
     */
    private int xPos = 0;

    /**
     * The current y position of the mouse.
     */
    private int yPos = 0;

    /**
     * Makes this MouseIndicator degenerate, effectively removing it.
     */
    public void degenerate () {
        xPos = -1;
        yPos = -1;
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
        if (xPos > width || xPos < 0 || yPos > height || yPos < 0) return;
        panel.setBigPixel(xPos, yPos, 5, Color.RED);
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
     * Called every logic frame to run the logic on this GameObject.
     */
    @Override
    public void runLogic () {
        Point mousePos = UltimateHealerAssistantGame.getInstance().getMouseListener().getMousePos();
        if (mousePos == null) return;
        xPos = mousePos.x;
        yPos = mousePos.y;
    }
}
