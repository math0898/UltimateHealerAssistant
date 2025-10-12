package io.github.math0898.views.fileselect;

import suga.engine.game.objects.BasicGameObject;
import suga.engine.graphics.GraphicsPanel;

import java.awt.*;

public class FileDisplayer extends BasicGameObject {

    /**
     * Called every drawing frame so programs have a chance to make their voices heard on what gets drawn.
     *
     * @param width  The width of the pixel map.
     * @param height The height of the pixel map.
     * @param panel  The panel to apply changes to.
     */
    @Override
    public void applyChanges (int width, int height, GraphicsPanel panel) {
        panel.setBigPixel(960, 540, 50, Color.RED);
    }
}
