package io.github.math0898.views.fileselect;

import io.github.math0898.Main;
import io.github.math0898.utils.Utils;
import suga.engine.game.objects.BasicGameObject;
import suga.engine.graphics.GraphicsPanel;
import suga.engine.physics.Vector;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * The FileDisplayer is used to represent a specific file in the filesystem that can be loaded to analyze.
 *
 * @author Sugaku
 */
public class FileDisplayer extends BasicGameObject {

    /**
     * The font size of the FileDisplayer.
     */
    private static final int FONT_SIZE = 36;

    /**
     * The font to use for FileDisplayers.
     */
    private static final Font FONT = new Font("Comic Sans", Font.PLAIN, FONT_SIZE);

    /**
     * The name of the file to represent.
     */
    private final String fileName;

    /**
     * The path of the file this represents.
     */
    private final String filePath;

    /**
     * Creates a new FileDisplayer that points to the given file name.
     *
     * @param file The file to display with this object.
     */
    public FileDisplayer (File file, Vector pos) {
        this.fileName = file.getName();
        this.filePath = file.getAbsolutePath();
        this.pos = pos.clone();
    }

    /**
     * Loads this file into the program.
     */
    public void loadFile () {
        Main.setLogfile(filePath);
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
        FontMetrics metrics = panel.getFontMetrics(FONT);
        BufferedImage img = Utils.imageFromText(FONT, new Color(200, 200, 200), fileName, metrics.stringWidth(fileName), (int) (FONT_SIZE * 1.5));
        panel.addImage((int) pos.getX(), (int) pos.getY(), img.getWidth(), img.getHeight(), img);
    }
}
