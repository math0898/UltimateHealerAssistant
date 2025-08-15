package io.github.math0898.views.encountersummary;

import io.github.math0898.processing.LogManager;
import io.github.math0898.processing.logentries.UnitDeathEntry;
import io.github.math0898.processing.logentries.UnitTypes;
import io.github.math0898.views.general.SpellIcon;
import io.github.math0898.views.healgraph.EncounterIndicator;
import io.github.math0898.views.nightsummary.PlayerPlacard;
import suga.engine.game.BasicGame;
import suga.engine.game.BasicScene;
import suga.engine.game.Game;
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
     * The number of deaths to show in the summary.
     */
    private static int deathsCount = 3;

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
        java.util.List<UnitDeathEntry> deaths = LogManager.getInstance().getHighlightedEncounter().getUnitDeaths();
        final ArrayList<UnitDeathEntry> events = new ArrayList<>();
        for (UnitDeathEntry entry : deaths) {
            if (entry.getUnitType() != UnitTypes.PLAYER) continue;
            events.add(entry);
        }
        for (int x = 0; x < deathsCount; x++) {
            String key = "Death Report " + x;
            if (x >= events.size()) continue;
            PlayerDeathReport obj = new PlayerDeathReport(events.get(x), ((WIDTH - (SIDE_BUFFERS * 2)) / deathsCount) * x + SIDE_BUFFERS - PlayerPlacard.ICON_OFFSET_HOR + (PlayerPlacard.ICON_WIDTH / 2), TOP_BUFFER + (PlayerDeathReport.ICON_HEIGHT / 2), (BasicGame) game);
            game.addGameObject(key, obj);
        }

        game.addGameObject("Test Spell", new SpellIcon(360995, 125, 125, 960, 540));

        game.addGameObject("Encounter Indicator", new EncounterIndicator());
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
                    LogManager.getInstance().changeHighlightedEncounter(1);
                    load(game); // todo: Make an update or reload function instead of simply completely reloading this scene.
                }
                case ARROW_DOWN -> {
                    LogManager.getInstance().changeHighlightedEncounter(-1);
                    load(game);
                }
                case Q -> {
                    deathsCount = Math.min(deathsCount + 1, 10);
                    load(game);
                }
                case A -> {
                    deathsCount = Math.max(deathsCount - 1, 0);
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
