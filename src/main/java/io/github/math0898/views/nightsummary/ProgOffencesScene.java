package io.github.math0898.views.nightsummary;

import io.github.math0898.Main;
import io.github.math0898.processing.Encounter;
import io.github.math0898.processing.LogManager;
import io.github.math0898.processing.logentries.UnitDeathEntry;
import io.github.math0898.processing.logentries.UnitTypes;
import io.github.math0898.utils.Utils;
import suga.engine.GameEngine;
import suga.engine.game.BasicScene;
import suga.engine.game.Game;
import suga.engine.game.objects.GameObject;
import suga.engine.input.keyboard.KeyValue;
import suga.engine.logger.Level;
import suga.engine.physics.Vector;

import java.awt.*;
import java.util.*;

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
    private static final int COLUMN_COUNT = 5;

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
     * A helper method to find all the players who are involved in the log file.
     */
    private Stack<String> findAllPlayers () {
        ArrayList<String> list = new ArrayList<>(); // todo: Deaths are probably not the best way to find all players in a night.
        for (Encounter e : LogManager.getInstance().getAllEncounters())
            for (UnitDeathEntry death : e.getUnitDeaths())
                if (death.getUnitType() == UnitTypes.PLAYER)
                    if (!list.contains(death.getUnitName()))
                        list.add(death.getUnitName());
        Stack<String> toReturn = new Stack<>();
        toReturn.addAll(list);
        return toReturn;
    }

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
            Stack<String> characters = findAllPlayers();
            for (int x = 0; x < COLUMN_COUNT; x++) {
                for (int y = 0; y < ROW_COUNT; y++) {
                    GameEngine.getLogger().log("Characters Left: " + characters.size(), Level.DEBUG);
                    if (characters.isEmpty()) continue;
                    String character = characters.pop();
                    String key = "Placard " + x + ":" + y;
                    GameObject obj = new PlayerPlacard(
                            Utils.parseRealm(character),
                            Utils.parseCharName(character),
                        ((WIDTH - (SIDE_BUFFERS * 2)) / COLUMN_COUNT) * x + SIDE_BUFFERS - PlayerPlacard.ICON_OFFSET_HOR + (PlayerPlacard.ICON_WIDTH / 2),
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
                case NUM_2 -> game.loadScene("encounter");
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
