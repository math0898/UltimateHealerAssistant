package io.github.math0898.views.nightsummary;

import suga.engine.game.BasicScene;
import suga.engine.game.Game;
import suga.engine.input.keyboard.KeyValue;

import java.awt.*;
import java.util.Random;

/**
 * The ProgOffencesScene is used to generate nice looking graphics to track how many times players mess up during prog.
 * Our guild is using this to help track down and monitor players who regularly cause raid wide wipes.
 *
 * @author Sugaku
 */
public class ProgOffencesScene extends BasicScene {

    /**
     * The number of rows to have in the summary.
     */
    private static final int ROW_COUNT = 5;

    /**
     * The number of columns to have in the summary.
     */
    private static final int COLUMN_COUNT = 4;

    /**
     * The amount of vertical space to leave at the top. This isn't precise and goes to the center of the placards.
     */
    private static final int TOP_BUFFER = 150;

    /**
     * The amount of vertical space to leave at the bottom. This isn't precise and goes to the center of the placards.
     */
    private static final int BOTTOM_BUFFER = 50;

    /**
     * The amount of space to leave on either side of the screen. This isn't precise and goes to the center of the placards.
     */
    private static final int SIDE_BUFFERS = 250;

    /**
     * Loads this scene into the given game.
     *
     * @param game The game to load this scene into.
     * @return True if loading was successful. Otherwise, false.
     */
    @Override
    public boolean load (Game game) {
        game.clear();
        Random rand = new Random(3);
        // Assumed 1040 height.
        // Assumed 1920 width.
        final int WIDTH = 1920;
        final int HEIGHT = 1040;
        // todo: Populate with actual data.
        String[] names = new String[]{ "holyføx", "nillath", "seranite", "auriprodeo", "heiderich", "shadowbat", "skullzdrood", "syudou", "skyvana", "thvnder", "citlalith", "vandalism", "khamosh", "delphian", "berzx", "weblock", "chuber", "mylovè", "sarinias", "sinys" };
        String[] realms = new String[]{ "area-52", "stormrage", "malganis", "stormrage", "moon-guard", "stormrage", "stormrage", "stormrage", "tichondrius", "aegwynn", "stormrage", "greymane", "zuljin", "zuljin", "bleeding-hollow", "stormrage", "illidan", "stormrage", "moon-guard", "stormrage" };
        for (int x = 0; x < ROW_COUNT; x++) {
            for (int y = 0; y < COLUMN_COUNT; y++) {
                game.addGameObject("Placard" + x + y, new PlayerPlacard(realms[x * COLUMN_COUNT + y], names[x * COLUMN_COUNT + y], rand.nextInt(10), rand.nextInt(10), rand.nextInt(10),
                        ((WIDTH - SIDE_BUFFERS * 2) / COLUMN_COUNT) * x + SIDE_BUFFERS, ((HEIGHT - BOTTOM_BUFFER - TOP_BUFFER) / ROW_COUNT) * y + TOP_BUFFER));
            }
        }
//        game.addGameObject("Seranite Placard", new PlayerPlacard("malganis", "seranite", rand.nextInt(10), rand.nextInt(10), rand.nextInt(10), 1920 / 4, 1080 / 4));
//        game.addGameObject("Nillath Placard", new PlayerPlacard("stormrage", "nillath", rand.nextInt(10), rand.nextInt(10), rand.nextInt(10), (1920 / 4) * 3, 1080 / 4));
//        game.addGameObject("Skullzdrood Placard", new PlayerPlacard("stormrage", "skullzdrood", rand.nextInt(10), rand.nextInt(10), rand.nextInt(10), 1920 / 4, (1080 / 4) * 3));
//        game.addGameObject("Syudou Placard", new PlayerPlacard("stormrage", "syudou", rand.nextInt(10), rand.nextInt(10), rand.nextInt(10), (1920 / 4) * 3, (1080 / 4) * 3));
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
        switch (key) {
            case NUM_1 -> game.loadScene("main");
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
