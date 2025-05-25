package io.github.math0898.gui;

import suga.engine.game.objects.BasicGameObject;
import suga.engine.graphics.GraphicsPanel;
import suga.engine.physics.collidables.Collidable;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class SpecIcon extends BasicGameObject {

    /**
     * The icon file path.
     */
    private final String iconPath;

    /**
     * Any vertical offset to apply when drawing this graphic.
     */
    private final int offset;

    /**
     * Creates a spec icon with the given file and vertical offset.
     *
     * @param offset The vertical offset to apply to this icon.
     * @param path   The file path for this icon.
     */
    public SpecIcon (int offset, String path) {
        super();
        this.offset = offset;
        iconPath = path;
    }

    /**
     * Gets a collider that is present on this object. If none are present returns null.
     *
     * @return Either the Collider attached to this GameObject or null.
     */
    @Override
    public Collidable getCollider () {
        // todo: Will likely be more consistent and easier to make a collision object that follows the mouse to determine when to activate buttons.
        return null;
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
        try {
            panel.addImage(width / 16, height / 8 + offset, 50, 50, ImageIO.read(new File(iconPath)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
