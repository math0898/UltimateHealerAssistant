package io.github.math0898.views.encountersummary;

import io.github.math0898.processing.logentries.UnitDeathEntry;
import io.github.math0898.processing.logentries.UnitTypes;
import io.github.math0898.utils.Utils;
import io.github.math0898.views.general.PlayerIcon;
import io.github.math0898.views.general.RectangleBar;
import io.github.math0898.views.healgraph.SpellQueries;
import suga.engine.game.BasicGame;
import suga.engine.game.objects.BasicGameObject;
import suga.engine.game.objects.GameObject;
import suga.engine.graphics.DrawListener;
import suga.engine.graphics.GraphicsPanel;
import suga.engine.physics.Vector;

import java.awt.*;
import java.awt.image.BufferedImage;
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
     * The horizontal padding for the starting point of the offences bars. This is anchored horizontally to the icon.
     */
    private static final int HOR_PADDING_BARS = 3;

    /**
     * The vertical offset for the first set of bars.
     */
    private static final int VERT_OFFSET_BARS = -15;

    /**
     * The vertical padding between each set of bars.
     */
    private static final int VERT_PADDING_BARS = 10;

    /**
     * The width of the bar rectangles.
     */
    private static final int BARS_WIDTHS = 5;

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
        game.addDrawingListener(new PlayerIcon(Utils.parseRealm(event.getUnitName()), Utils.parseCharName(event.getUnitName()), ICON_WIDTH, ICON_HEIGHT, x + ICON_OFFSET_HOR, y + ICON_OFFSET_VERT));
        setPos(new Vector(x, y, 0));
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
        Font font = new Font("Comic Sans", Font.BOLD, 36);
        FontMetrics metrics = panel.getFontMetrics(font);
        final int textWidth = metrics.stringWidth(entry.getUnitName().toUpperCase()) + 5;
        BufferedImage buffer = Utils.imageFromText(font, new Color(200, 200, 200), entry.getUnitName().toUpperCase(), Math.max(textWidth, MAX_NAME_WIDTH), 48);
        panel.addImage((int) pos.getX() + ICON_OFFSET_HOR + (ICON_WIDTH / 2) + HOR_PADDING_NAME,
                (int) pos.getY() + VERT_OFFSET_NAME - 17, MAX_NAME_WIDTH, 48, buffer); // todo: These numbers need to be cleaned up.
    }
}
