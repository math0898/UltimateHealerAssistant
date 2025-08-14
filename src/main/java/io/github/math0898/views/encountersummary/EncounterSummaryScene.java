package io.github.math0898.views.encountersummary;

import io.github.math0898.Main;
import io.github.math0898.processing.logentries.UnitDeathEntry;
import io.github.math0898.processing.logentries.UnitTypes;
import io.github.math0898.utils.Utils;
import io.github.math0898.views.healgraph.MainGraphScene;
import io.github.math0898.views.nightsummary.PlayerPlacard;
import io.github.math0898.views.nightsummary.SelectionRectangle;
import suga.engine.game.BasicScene;
import suga.engine.game.Game;
import suga.engine.game.objects.GameObject;
import suga.engine.input.keyboard.KeyValue;

import java.awt.*;
import java.util.ArrayList;

/**
 * The EncounterSummaryView is used to summarize a specific encounter including details like player deaths.
 *
 * @author Sugaku
 */
public class EncounterSummaryScene extends BasicScene {

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
    private static final int BOTTOM_BUFFER = -100;

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
            // Assumed 1040 height.
            // Assumed 1920 width.
            final int WIDTH = 1920;
            final int HEIGHT = 1040;
            java.util.List<UnitDeathEntry> deaths = Main.encounters.get(MainGraphScene.graphedEncounterIndex).getUnitDeaths();
            final long lastDeath = deaths.getLast().getTime();
            final ArrayList<UnitDeathEntry> events = new ArrayList<>();
            for (UnitDeathEntry entry : deaths) {
                if (entry.getUnitType() != UnitTypes.PLAYER) continue;
                events.add(entry);
            }
            for (int x = 0; x < ROW_COUNT; x++) {
                for (int y = 0; y < COLUMN_COUNT; y++) {
                    String key = "Placard " + x + ":" + y;
                    int index = x * COLUMN_COUNT + y;
                    if (index >= events.size()) continue;
                    int differenceInDeath = (int) (lastDeath - events.get(index).getTime());
                    GameObject obj = new PlayerPlacard(
                            Utils.parseRealm(events.get(index).getUnitName()),
                            Utils.parseCharName(events.get(index).getUnitName()),
                            differenceInDeath / 10000,             // todo: Populate with actual data.
                            (differenceInDeath % 10000) / 1000,
                            (differenceInDeath % 1000) / 100,
                            ((WIDTH - SIDE_BUFFERS * 2) / COLUMN_COUNT) * x + SIDE_BUFFERS,
                            ((HEIGHT - BOTTOM_BUFFER - TOP_BUFFER) / ROW_COUNT) * y + TOP_BUFFER);
                    game.addGameObject(key, obj);
                }
            }
            SelectionRectangle o = new SelectionRectangle();
            game.addGameObject("SelectionBox", o);
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
        if (pressed) {
            switch (key) {
                case NUM_1 -> game.loadScene("main");
                case NUM_2 -> game.loadScene("prog");
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
