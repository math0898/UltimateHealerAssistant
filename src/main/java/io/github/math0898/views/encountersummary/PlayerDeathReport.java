package io.github.math0898.views.encountersummary;

import io.github.math0898.Main;
import io.github.math0898.processing.Encounter;
import io.github.math0898.processing.logentries.UnitDeathEntry;
import io.github.math0898.processing.logentries.UnitTypes;
import io.github.math0898.utils.Utils;
import io.github.math0898.views.general.PlayerIcon;
import io.github.math0898.views.general.RectangleBar;
import io.github.math0898.views.healgraph.MainGraphScene;
import io.github.math0898.views.healgraph.SpellQueries;
import suga.engine.GameEngine;
import suga.engine.game.BasicGame;
import suga.engine.game.objects.BasicGameObject;
import suga.engine.game.objects.GameObject;
import suga.engine.graphics.DrawListener;
import suga.engine.graphics.GraphicsPanel;
import suga.engine.logger.Level;
import suga.engine.physics.Vector;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * The PlayerDeathReport is a general report about the time a player died at in an encounter.
 *
 * @author Sugaku
 */
public class PlayerDeathReport extends BasicGameObject implements DrawListener {

    /**
     * Horizontal offset for the PlayerIcon relative to the PlayerPlacard's actual position.
     */
    public static final int ICON_OFFSET_HOR = -100;

    /**
     * Vertical offset for the PlayerIcon relative to the PlayerPlacard's actual position.
     */
    private static final int ICON_OFFSET_VERT = 0;

    /**
     * The vertical height for the icon in the Player's placard.
     */
    public static final int ICON_HEIGHT = 150;

    /**
     * The horizontal width for the icon in the PLayer's placard.
     */
    public static final int ICON_WIDTH = 150;

    /**
     * Horizontal padding between the character name text and the icon.
     */
    public static final int HOR_PADDING_NAME = 5;

    /**
     * A vertical offset for the character name text.
     */
    private static final int VERT_OFFSET_NAME = -75;

    /**
     * The horizontal padding between bars.
     */
    private static final int HOR_PADDING_BARS = 3;

    /**
     * The vertical offset for the first set of bars.
     */
    private static final int VERT_OFFSET_BARS = 10;

    /**
     * The width of the bar rectangles.
     */
    private static final int BARS_WIDTHS = 3;

    /**
     * The height of the bar rectangles.
     */
    private static final int BARS_HEIGHT = 20;

    /**
     * The max width of names.
     */
    public static final int MAX_NAME_WIDTH = 190;

    /**
     * The UnitDeathEntry for this report.
     */
    private final UnitDeathEntry entry;

    /**
     * Whether values have been computed for this Object or not yet.
     */
    private boolean computed = false;

    /**
     * A reference to the active game. Used to add in GameObjects after the fact.
     */
    private final BasicGame game;

    /**
     * How long into the pull this death was as a nicely formatted string.
     */
    private String formattedDeathTime;

    /**
     * Creates a new PlayerDeathReport with the given UnitDeathEntry.
     *
     * @param event The player death event.
     * @param x The x position of this object.
     * @param y The y position of this object.
     * @param game This constructor needs access to the Game high-level object to add additional items.
     */
    public PlayerDeathReport (UnitDeathEntry event, int x, int y, BasicGame game) {
        if (event.getUnitType() != UnitTypes.PLAYER) throw new IllegalArgumentException("UnitDeathEntry Must be a Player");
        entry = event;
        this.game = game;
        setPos(new Vector(x, y, 0));
    }

    /**
     * Called every logic frame to run the logic on this GameObject.
     */
    @Override
    public void runLogic () {
        super.runLogic();
        if (computed) return;

        game.addDrawingListener(new PlayerIcon(Utils.parseRealm(entry.getUnitName()), Utils.parseCharName(entry.getUnitName()), ICON_WIDTH, ICON_HEIGHT, (int) pos.getX() + ICON_OFFSET_HOR, (int) pos.getY() + ICON_OFFSET_VERT));

        Encounter encounter = Main.encounters.get(MainGraphScene.graphedEncounterIndex);
        final long timeAlive = entry.getTime() - encounter.getEncounterStartMillis();
        formattedDeathTime = timeAlive / (1000L * 60) + "m " + (timeAlive % (1000L * 60)) / 1000L + "s";

        final long endTime = encounter.getUnitDeaths().getLast().getTime();
        final float percentageAlive = timeAlive / (1.0f * (endTime - encounter.getEncounterStartMillis()));
        Color color = SpellQueries.EMERALD_COMMUNION.color;
        if (percentageAlive < 0.3) color = SpellQueries.CONSUME_FLAME.color;
        else if (percentageAlive < 0.8) color = SpellQueries.PIETY.color;
        game.addDrawingListener(new RectangleBar(color, BARS_WIDTHS, BARS_HEIGHT, HOR_PADDING_BARS, Math.round(percentageAlive * 10), (int) pos.getX() + ICON_OFFSET_HOR + (ICON_WIDTH / 2) + HOR_PADDING_NAME + 5, (int) pos.getY() + VERT_OFFSET_BARS, false));

        computed = true;
    }

    /**
     * If present, returns the DrawListener associated with this GameObject. May be null.
     *
     * @return Either the DrawListener attached to this GameObject or null.
     */
    @Override
    public DrawListener getDrawListener () {
        return this;
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
        if (!computed) return;
        Font font = new Font("Comic Sans", Font.BOLD, 36);
        FontMetrics metrics = panel.getFontMetrics(font);
        final String characterName = Utils.parseCharName(entry.getUnitName());
        final int textWidth = metrics.stringWidth(characterName.toUpperCase()) + 5;
        BufferedImage buffer = Utils.imageFromText(font, new Color(200, 200, 200), characterName.toUpperCase(), Math.max(textWidth, MAX_NAME_WIDTH), 48);
        panel.addImage((int) pos.getX() + ICON_OFFSET_HOR + (ICON_WIDTH / 2) + HOR_PADDING_NAME,
                (int) pos.getY() + VERT_OFFSET_NAME - 17, MAX_NAME_WIDTH, 48, buffer); // todo: These numbers need to be cleaned up.

        font = new Font("Comic Sans", Font.ITALIC + Font.BOLD, 24);
        BufferedImage buffer2 = Utils.imageFromText(font, new Color(200, 200, 200), formattedDeathTime, Math.max(textWidth, MAX_NAME_WIDTH), 48);
        panel.addImage((int) pos.getX() + ICON_OFFSET_HOR + (ICON_WIDTH / 2) + HOR_PADDING_NAME,
                (int) pos.getY() + VERT_OFFSET_NAME - 17 + 28, MAX_NAME_WIDTH, 48, buffer2); // todo: These numbers need to be cleaned up.
    }
}
