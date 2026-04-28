package io.github.math0898.game;

import lombok.Getter;
import suga.engine.GameEngine;
import suga.engine.logger.Level;

import java.util.concurrent.Callable;

/**
 * A button is a sub-object under a GameObject that is used to determine click events.
 *
 * @author Sugaku
 */
public class Button {

    /**
     * The top left corner's x position.
     */
    @Getter
    private final int posX;

    /**
     * The top left corner's y position.
     */
    @Getter
    private final int posY;

    /**
     * The width of the object.
     */
    @Getter
    private final int width;

    /**
     * The height of the object.
     */
    @Getter
    private final int height;

    /**
     * The function to callback when this Button is clicked.
     */
    private Callable<Void> callback;

    /**
     * Creates a new Button object.
     *
     * @param x The top left corner x position.
     * @param y The top left corner y position.
     * @param width The width of the object.
     * @param height The height of the object.
     * @param callback The function to invoke.
     */
    public Button (int x, int y, int width, int height, Callable<Void> callback) {
        posX = x;
        posY = y;
        this.width = width;
        this.height = height;
        this.callback = callback;
    }

    /**
     * Replaces this Button's callback method.
     *
     * @param callback The new callback.
     */
    public void replaceCallback (Callable<Void> callback) {
        this.callback = callback;
    }

    /**
     * Checks whether the given click hits this button or not. Will call "callback" method if it does.
     *
     * @param x The x position to check.
     * @param y The y position to check.
     */
    public void checkClick (int x, int y) {
        if (x >= posX && x <= posX + width && y >= posY && y <= posY + height) {
            try {
                callback.call();
            } catch (Exception e) {
                GameEngine.getLogger().log("Attempted to callback.", Level.WARNING);
                GameEngine.getLogger().log(e);
            }
        }
    }
}
