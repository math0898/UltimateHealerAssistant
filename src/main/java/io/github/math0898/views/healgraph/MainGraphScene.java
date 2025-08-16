package io.github.math0898.views.healgraph;

import io.github.math0898.processing.LogManager;
import io.github.math0898.views.general.SpellIcon;
import suga.engine.GameEngine;
import suga.engine.game.BasicScene;
import suga.engine.game.Game;
import suga.engine.input.keyboard.KeyValue;
import suga.engine.logger.Level;
import suga.engine.logger.Logger;

import java.awt.*;

public class MainGraphScene extends BasicScene {

    /**
     * The GraphGameObject that actually draws the graph.
     */
    private GraphGameObject graphGameObject;

    /**
     * Loads this scene into the given game.
     *
     * @param game The game to load this scene into.
     * @return True if loading was successful. Otherwise, false.
     */
    @Override
    public boolean load (Game game) {
        this.game = game;
        game.clear();
        graphGameObject = new GraphGameObject();
        game.addGameObject("Main Graph", graphGameObject);
        game.addGameObject("Encounter Indicator", new EncounterIndicator());
        game.addGameObject("Pres Icon", new SpecIcon(0, "/home/sugaku/Development/Standalone/Java/UltimateHealerAssistant/icons/classicon_evoker_preservation.jpg"));
        game.addGameObject("Holy Icon", new SpecIcon(70, "/home/sugaku/Development/Standalone/Java/UltimateHealerAssistant/icons/spell_holy_guardianspirit.jpg"));
        game.addGameObject("Disc Icon", new SpecIcon(140, "/home/sugaku/Development/Standalone/Java/UltimateHealerAssistant/icons/spell_holy_powerwordshield.jpg"));
        game.addGameObject("Resto Icon", new SpecIcon(210, "/home/sugaku/Development/Standalone/Java/UltimateHealerAssistant/icons/inv_1115_shaman_chainheal.jpg"));
        game.addGameObject("Resto Druid Icon", new SpecIcon(280, "/home/sugaku/Development/Standalone/Java/UltimateHealerAssistant/icons/talentspec_druid_restoration.jpg"));
        game.addGameObject("Pres Engulf", new CastIndicator(SpellQueries.CONSUME_FLAME, 0));
        game.addGameObject("Pres Rewind", new CastIndicator(SpellQueries.REWIND, 30));
        game.addGameObject("Pres Emerald Communion", new CastIndicator(SpellQueries.EMERALD_COMMUNION, 60));
        game.addGameObject("Holy Divine Hymn", new CastIndicator(SpellQueries.DIVINE_HYMN, 0));
        game.addGameObject("Disc Evangelism", new CastIndicator(SpellQueries.EVANGELISM, 0));
        game.addGameObject("Disc Piety", new CastIndicator(SpellQueries.PIETY, 30)); // todo: Consider triple buff.
//        game.addGameObject("Disc Atonement", new CastIndicator(SpellQueries.ATONEMENT, 60)); // todo: This is causing a lot of lag because it's technically drawing each cooldown every cast, not once per block.
        game.addGameObject("Resto Healing Tide", new CastIndicator(SpellQueries.HEALING_TIDE, 0));
        game.addGameObject("Resto Spirit Link", new CastIndicator(SpellQueries.SPIRIT_LINK, 30));
        game.addGameObject("Druid Regrowth", new CastIndicator(SpellQueries.REGROWTH, 0));
        game.addGameObject("Druid Tranquility", new CastIndicator(SpellQueries.TRANQUILITY, 0));
        game.addGameObject("Verdant Embrace", new SpellIcon(360995, 100, 100, 1920 / 2, 1080 / 2));
        return true;
    }

    /**
     * Passes a keyboard input into the scene.
     *
     * @param key     The value of the key pressed.
     * @param pressed True if the key was pressed, false if it was released.
     */
    @Override
    public void keyboardInput (KeyValue key, boolean pressed) {
        Logger logger = GameEngine.getLogger();
        if (pressed) logger.log("You pressed: " + key + "!", Level.VERBOSE);
        else logger.log("You released: " + key + "!", Level.VERBOSE);
        if (!pressed) {
            switch (key) {
                case ARROW_UP -> {
                    LogManager.getInstance().changeHighlightedEncounter(1);
                    updateGraph();
                }
                case ARROW_DOWN -> {
                    LogManager.getInstance().changeHighlightedEncounter(-1);
                    updateGraph();
                }
                case NUM_2 -> game.loadScene("prog");
                case NUM_3 -> game.loadScene("encounter");
            }
        }
    }

    /**
     * Forces the graph to update.
     */
    private void updateGraph () {
        graphGameObject.setEncounter(LogManager.getInstance().getHighlightedEncounter());
    }

    /**
     * Passes a mouse input into the scene.
     *
     * @param pos     The position of the mouse when it was clicked.
     * @param pressed True if the button was pressed, false if it was released.
     */
    @Override
    public void mouseInput (Point pos, boolean pressed) {
        Logger logger = GameEngine.getLogger();
        if (pressed) logger.log("Clicked at " + pos + "!", Level.VERBOSE);
        else logger.log("Click released at " + pos + "!", Level.VERBOSE);
        if (pressed) {
            if (pos.x < (1920 / 16) + 56 && pos.x > (1920 / 16)) {
                if (pos.y > 1080 / 8 + 30 && pos.y < 1080 / 8 + 30 + 56) {
                    graphGameObject.toggleSpec("pres");
                    ((CastIndicator) game.getGameObject("Pres Engulf")).toggle();
                    ((CastIndicator) game.getGameObject("Pres Rewind")).toggle();
                    ((CastIndicator) game.getGameObject("Pres Emerald Communion")).toggle();
                }
                if (pos.y > 1080 / 8 + 30 + 70 && pos.y < 1080 / 8 + 30 + 70 + 56) {
                    graphGameObject.toggleSpec("holy");
                    ((CastIndicator) game.getGameObject("Holy Divine Hymn")).toggle();
                }
                if (pos.y > 1080 / 8 + 30 + 140 && pos.y < 1080 / 8 + 30 + 140 + 56) {
                    graphGameObject.toggleSpec("disc");
                    ((CastIndicator) game.getGameObject("Disc Evangelism")).toggle();
                    ((CastIndicator) game.getGameObject("Disc Piety")).toggle();
//                    ((CastIndicator) game.getGameObject("Disc Atonement")).toggle();
                }
                if (pos.y > 1080 / 8 + 30 + 210 && pos.y < 1080 / 8 + 30 + 210 + 56) {
                    graphGameObject.toggleSpec("resto");
                    ((CastIndicator) game.getGameObject("Resto Healing Tide")).toggle();
                    ((CastIndicator) game.getGameObject("Resto Spirit Link")).toggle();
                }
                if (pos.y > 1080 / 8 + 30 + 280 && pos.y < 1080 / 8 + 30 + 280 + 56) {
                    graphGameObject.toggleSpec("druid");
//                    ((CastIndicator) game.getGameObject("Druid Regrowth")).toggle();
                    ((CastIndicator) game.getGameObject("Druid Tranquility")).toggle();
                }
            }
        }
    }
}
