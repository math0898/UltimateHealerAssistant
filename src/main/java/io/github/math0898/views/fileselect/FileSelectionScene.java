package io.github.math0898.views.fileselect;

import suga.engine.game.BasicScene;
import suga.engine.game.Game;
import suga.engine.game.objects.GameObject;
import suga.engine.input.keyboard.KeyValue;
import suga.engine.physics.Vector;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * The FileSelectionScene is used to select files from a list of potential logs to analyze.
 *
 * @author Sugaku
 */
public class FileSelectionScene extends BasicScene {

    /**
     * The starting file path.
     */
    private static final String STARTING_FILE_PATH = "/home/sugaku/Development/Standalone/Java/UltimateHealerAssistant/testfiles/";

    /**
     * A list of FileDisplayer objects to more easily access after creation.
     */
    private final List<FileDisplayer> displayers = new ArrayList<>();

    /**
     * Loads this scene into the given game.
     *
     * @param game The game to load this scene into.
     * @return True if loading was successful. Otherwise, false.
     */
    @Override
    public boolean load (Game game) {
        game.clear();
        File[] files = new File(STARTING_FILE_PATH).listFiles();
        Arrays.sort(files, Comparator.comparing(File::getName));
        Vector vec = new Vector(960, 20, 0);
        for (int i = 0; i < files.length; i++) {
            vec.add(new Vector(0, 42, 0));
            FileDisplayer displayer = new FileDisplayer(files[i], vec);
            game.addGameObject("File-" + i, displayer);
            displayers.add(displayer);
        }
        return super.load(game);
    }

    /**
     * Passes a keyboard input into the scene.
     *
     * @param key     The value of the key pressed.
     * @param pressed True if the key was pressed, false if it was released.
     */
    @Override
    public void keyboardInput (KeyValue key, boolean pressed) {
        if (!pressed) return;
        switch (key) {
            case ARROW_DOWN -> {
                for (FileDisplayer dis : displayers)
                    dis.getPos().add(new Vector(0, -42, 0));
            }
            case ARROW_UP -> {
                for (FileDisplayer dis : displayers)
                    dis.getPos().add(new Vector(0, 42, 0));
            }
        }
    }

    /**
     * Passes a mouse input into the scene.
     *
     * @param pos     The position of the mouse when it was clicked.
     * @param pressed True if the button was pressed, false if it was released.
     */
    @Override
    public void mouseInput (Point pos, boolean pressed) {

    }
}
