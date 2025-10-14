package io.github.math0898.views.fileselect;

import io.github.math0898.Main;
import io.github.math0898.utils.Utils;
import suga.engine.game.objects.BasicGameObject;
import suga.engine.graphics.GraphicsPanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class FileDisplayer extends BasicGameObject {

    /**
     * The name of the file to represent.
     */
    private final String fileName;

    /**
     * Creates a new FileDisplayer that points to the given file name.
     *
     * @param name The name of the file to load.
     */
    public FileDisplayer (String name) {
        fileName = name;
    }

    /**
     * Loads this file into the program.
     */
    public void loadFile () {
        Main.setLogfile(fileName);
        Main.analyzeFile();
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
        BufferedImage img = Utils.imageFromText(new Font("Comic Sans", Font.PLAIN, 36), new Color(200, 200, 200), fileName);
        panel.addImage(960, 540, img.getWidth(), img.getHeight(), img);
//        panel.setBigPixel(960, 540, 50, Color.RED);
    }
}
