package io.github.math0898.views.nightsummary;

import io.github.math0898.utils.Utils;
import io.github.math0898.views.healgraph.SpellQueries;
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
 * The main box containing a Player in the ProgOffencesScene. It includes their username, class, spec, role, and player
 * icon.
 *
 * @author Sugaku
 */
public class PlayerPlacard extends BasicGameObject implements DrawListener {

    /**
     * Horizontal offset for the PlayerIcon relative to the PlayerPlacard's actual position.
     */
    private static final int ICON_OFFSET_HOR = -100;

    /**
     * Vertical offset for the PlayerIcon relative to the PlayerPlacard's actual position.
     */
    private static final int ICON_OFFSET_VERT = 0;

    /**
     * The vertical height for the icon in the Player's placard.
     */
    private static final int ICON_HEIGHT = 150;

    /**
     * The horizontal width for the icon in the PLayer's placard.
     */
    private static final int ICON_WIDTH = 150;

    /**
     * Horizontal padding between the character name text and the icon.
     */
    private static final int HOR_PADDING_NAME = 5;

    /**
     * A vertical offset for the character name text.
     */
    private static final int VERT_OFFSET_NAME = -75;

    /**
     * The horizontal padding for the starting point of the offences bars. This is anchored horizontally to the icon.
     */
    private static final int HOR_PADDING_BARS = 5;

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
    private static final int MAX_NAME_WIDTH = 190;

    /**
     * The name of the realm the Player in this placard is on.
     */
    private final String realm;

    /**
     * The name of the character this Player placard represents.
     */
    private final String character;

    /**
     * A collection of sub objects that the PlayerPlacard encapsulates.
     */
    private final Map<String, GameObject> subObjects = new HashMap<>();

    /**
     * Creates a new PlayerIcon with the given realm slug and character name.
     *
     * @param realmSlug This is the server name without US and a dash. Full lowercase.
     * @param characterName This is the name of the character. Full lowercase.
     * @param grievousOffenses The number of offenses that are especially bad.
     * @param moderateOffenses The number of offenses that are kinda mid.
     * @param notReallyOffenses Offenses that don't really matter in the grand scale of things.
     * @param x The x position of this placard.
     * @param y The y position of this placard.
     */
    public PlayerPlacard (String realmSlug, String characterName, int grievousOffenses, int moderateOffenses, int notReallyOffenses, int x, int y) {
        this.realm = realmSlug;
        this.character = characterName;
        this.pos = new Vector(x, y, 0);
        subObjects.put("Player Icon", new PlayerIcon(realmSlug, characterName, "DPS", ICON_WIDTH, ICON_HEIGHT, x + ICON_OFFSET_HOR, y + ICON_OFFSET_VERT));
        subObjects.put("Grievous Offenses", new RectangleBar(SpellQueries.CONSUME_FLAME.color, BARS_WIDTHS, BARS_HEIGHT, HOR_PADDING_BARS, grievousOffenses, x + ICON_OFFSET_HOR + (ICON_WIDTH / 2) + HOR_PADDING_BARS, y + VERT_OFFSET_BARS));
        subObjects.put("Moderate Offenses", new RectangleBar(SpellQueries.PIETY.color, BARS_WIDTHS, BARS_HEIGHT, HOR_PADDING_BARS, moderateOffenses, x + ICON_OFFSET_HOR + (ICON_WIDTH / 2) + HOR_PADDING_BARS, y + VERT_OFFSET_BARS + BARS_HEIGHT + VERT_PADDING_BARS));
        subObjects.put("Not Really Offenses", new RectangleBar(SpellQueries.EMERALD_COMMUNION.color, BARS_WIDTHS, BARS_HEIGHT, HOR_PADDING_BARS, notReallyOffenses, x + ICON_OFFSET_HOR + (ICON_WIDTH / 2) + HOR_PADDING_BARS, y + VERT_OFFSET_BARS + (BARS_HEIGHT + VERT_PADDING_BARS) * 2));
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
        for (GameObject o : subObjects.values()) o.getDrawListener().applyChanges(width, height, panel);
        Font font = new Font("Comic Sans", Font.BOLD, 36);
        FontMetrics metrics = panel.getFontMetrics(font);
        final int textWidth = metrics.stringWidth(character.toUpperCase()) + 5;
        BufferedImage buffer = Utils.imageFromText(font, new Color(200, 200, 200), character.toUpperCase(), Math.max(textWidth, MAX_NAME_WIDTH), 48);
        panel.addImage((int) pos.getX() + ICON_OFFSET_HOR + (ICON_WIDTH / 2) + HOR_PADDING_NAME,
                (int) pos.getY() + VERT_OFFSET_NAME - 17, MAX_NAME_WIDTH, 48, buffer); // todo: These numbers need to be cleaned up.
    }
}
