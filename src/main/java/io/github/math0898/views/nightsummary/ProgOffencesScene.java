package io.github.math0898.views.nightsummary;

import suga.engine.game.BasicScene;
import suga.engine.game.Game;
import suga.engine.game.objects.GameObject;
import suga.engine.input.keyboard.KeyValue;
import suga.engine.physics.Vector;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

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
    private static final int TOP_BUFFER = 200;

    /**
     * The amount of vertical space to leave at the bottom. This isn't precise and goes to the center of the placards.
     */
    private static final int BOTTOM_BUFFER = 50;

    /**
     * The amount of space to leave on either side of the screen. This isn't precise and goes to the center of the placards.
     */
    private static final int SIDE_BUFFERS = 50;

    /**
     * A map of game objects we keep to buffer. This means we don't need to regenerate them when scene is changed back.
     */
    private final Map<String, GameObject> bufferedObjects = new HashMap<>();

    /**
     * Whether this scene has been loaded and initialized yet.
     */
    private boolean firstLoad = true;

    /**
     * The active Placard that is potentially being modified.
     */
    private PlayerPlacard placard = null;

    /**
     * The coordinates of the currently selected Placard.
     */
    private Vector selectionPos = new Vector(0, 0, 0);

    /**
     * Loads this scene into the given game.
     *
     * @param game The game to load this scene into.
     * @return True if loading was successful. Otherwise, false.
     */
    @Override
    public boolean load (Game game) {
        game.clear();
        if (firstLoad) {
            // Assumed 1040 height.
            // Assumed 1920 width.
            final int WIDTH = 1920;
            final int HEIGHT = 1040;
            // todo: Populate with actual data.
            String[] names = new String[]{"holyføx", "nillath", "seranite", "auriprodeo", "heiderich", "shadowbat", "skullzdrood", "syudou", "skyvana", "thvnder", "citlalith", "vandalism", "khamosh", "delphian", "berzx", "weblock", "chuber", "mylovè", "sarinias", "sinys"};
            String[] realms = new String[]{"area-52", "stormrage", "malganis", "stormrage", "moon-guard", "stormrage", "stormrage", "stormrage", "tichondrius", "aegwynn", "stormrage", "greymane", "zuljin", "zuljin", "bleeding-hollow", "stormrage", "illidan", "stormrage", "moon-guard", "stormrage"};
            for (int x = 0; x < COLUMN_COUNT; x++) {
                for (int y = 0; y < ROW_COUNT; y++) {
                    String key = "Placard " + x + ":" + y;
                    GameObject obj = new PlayerPlacard(realms[x * COLUMN_COUNT + y], names[x * COLUMN_COUNT + y],
                            ((WIDTH - (SIDE_BUFFERS * 2)) / (COLUMN_COUNT + 1)) * (x + 1) + SIDE_BUFFERS,
                            ((HEIGHT - BOTTOM_BUFFER - TOP_BUFFER) / ROW_COUNT) * y + TOP_BUFFER);
                    game.addGameObject(key, obj);
                    bufferedObjects.put(key, obj);
                }
            }
            SelectionRectangle o = new SelectionRectangle();
            game.addGameObject("SelectionBox", o);
            bufferedObjects.put("SelectionBox", o);
            firstLoad = false;
        } else {
            bufferedObjects.forEach(game::addGameObject);
        }
        return super.load(game);
    }

    /**
     * A utility method to grab the specific placard at the given coordinates.
     *
     * @param pos The grid position of the placard to grab.
     */
    private PlayerPlacard getPlacardAt (Vector pos) {
        int x = (int) pos.getX();
        int y = (int) pos.getY();
        return (PlayerPlacard) game.getGameObject("Placard " + x + ":" + y);
    }

    /**
     * Passes a keyboard input into the scene.
     *
     * @param key     The value of the key pressed.
     * @param pressed True if the key was pressed, false if it was released.
     */
    @Override
    public void keyboardInput (KeyValue key, boolean pressed) {
        if (pressed) {
            switch (key) {
                case NUM_1 -> game.loadScene("main");
                case ESC -> {
                    selectionPos = new Vector(0, 0, 0);
                    placard = null;
                    SelectionRectangle rect = (SelectionRectangle) game.getGameObject("SelectionBox");
                    rect.setActive(false);
                }
                case ARROW_DOWN, ARROW_UP, ARROW_LEFT, ARROW_RIGHT -> {
                    switch (key) {
                        case ARROW_DOWN -> selectionPos.setY(Math.min(ROW_COUNT - 1, selectionPos.getY() + 1));
                        case ARROW_UP -> selectionPos.setY(Math.max(0, selectionPos.getY() - 1));
                        case ARROW_RIGHT -> selectionPos.setX(Math.min(COLUMN_COUNT - 1, selectionPos.getX() + 1));
                        case ARROW_LEFT -> selectionPos.setX(Math.max(0, selectionPos.getX() - 1));
                    }
                    placard = getPlacardAt(selectionPos);
                    SelectionRectangle rect = (SelectionRectangle) game.getGameObject("SelectionBox");
                    rect.setPos(placard.getPos());
                    rect.setActive(true);
                }
                case Q -> {
                    if (placard != null) placard.modifyBar("Red Bar", 1);
                }
                case W -> {
                    if (placard != null) placard.modifyBar("Yellow Bar", 1);
                }
                case E -> {
                    if (placard != null) placard.modifyBar("Green Bar", 1);
                }
                case A -> {
                    if (placard != null) placard.modifyBar("Red Bar", -1);
                }
                case S -> {
                    if (placard != null) placard.modifyBar("Yellow Bar", -1);
                }
                case D -> {
                    if (placard != null) placard.modifyBar("Green Bar", -1);
                }
                case J -> {
                    if (placard != null) placard.setRole("DPS");
                }
                case K -> {
                    if (placard != null) placard.setRole("Healer");
                }
                case L -> {
                    if (placard != null) placard.setRole("Tank");
                }
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
