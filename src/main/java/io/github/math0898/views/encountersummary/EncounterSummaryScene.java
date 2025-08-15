package io.github.math0898.views.encountersummary;

import io.github.math0898.Main;
import io.github.math0898.processing.logentries.UnitDeathEntry;
import io.github.math0898.processing.logentries.UnitTypes;
import io.github.math0898.views.healgraph.EncounterIndicator;
import io.github.math0898.views.nightsummary.PlayerPlacard;
import suga.engine.game.BasicGame;
import suga.engine.game.BasicScene;
import suga.engine.game.Game;
import suga.engine.input.keyboard.KeyValue;

import java.awt.*;
import java.util.ArrayList;

import static io.github.math0898.views.healgraph.MainGraphScene.graphedEncounterIndex;

/**
 * The EncounterSummaryView is used to summarize a specific encounter including details like player deaths.
 *
 * @author Sugaku
 */
public class EncounterSummaryScene extends BasicScene {
    /**
     * The number of deaths to show in the summary.
     */
    private static final int DEATHS_COUNT = 3;

    /**
     * The amount of vertical space to leave at the top. This isn't precise and goes to the center of the placards.
     */
    private static final int TOP_BUFFER = 250;

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
            // Assumed 1920 width.
            final int WIDTH = 1920;
            java.util.List<UnitDeathEntry> deaths = Main.encounters.get(graphedEncounterIndex).getUnitDeaths();
            final ArrayList<UnitDeathEntry> events = new ArrayList<>();
            for (UnitDeathEntry entry : deaths) {
                if (entry.getUnitType() != UnitTypes.PLAYER) continue;
                events.add(entry);
            }
            for (int x = 0; x < DEATHS_COUNT; x++) {
                    String key = "Death Report " + x;
                    if (x >= events.size()) continue;
                    PlayerDeathReport obj = new PlayerDeathReport(events.get(x), ((WIDTH - (SIDE_BUFFERS * 2)) / DEATHS_COUNT) * x + SIDE_BUFFERS - PlayerPlacard.ICON_OFFSET_HOR + (PlayerPlacard.ICON_WIDTH / 2), TOP_BUFFER + (PlayerDeathReport.ICON_HEIGHT / 2), (BasicGame) game);
                    game.addGameObject(key, obj);
            }
            game.addGameObject("Encounter Indicator", new EncounterIndicator(null));
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
                case ARROW_UP -> {
                    graphedEncounterIndex = Math.min(graphedEncounterIndex + 1, Main.encounters.size() - 1);
                    load(game);
                }
                case ARROW_DOWN -> {
                    graphedEncounterIndex = Math.max(graphedEncounterIndex - 1, 0);
                    load(game);
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
