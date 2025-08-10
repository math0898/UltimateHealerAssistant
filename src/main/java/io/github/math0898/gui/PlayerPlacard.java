package io.github.math0898.gui;

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
     */
    public PlayerPlacard (String realmSlug, String characterName, int x, int y) {
        this.realm = realmSlug;
        this.character = characterName;
        this.pos = new Vector(x, y, 0);
        subObjects.put("Player Icon", new PlayerIcon(realmSlug, characterName, ICON_WIDTH, ICON_HEIGHT, x + ICON_OFFSET_HOR, y + ICON_OFFSET_VERT));
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
        BufferedImage buffer = new BufferedImage(300, 48, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = buffer.createGraphics();
        graphics.setFont(new Font("Comic Sans", Font.BOLD, 36));
        graphics.setColor(new Color(200,200,200));
        graphics.drawString(character.toUpperCase(), 0, 32);
        panel.addImage((int) pos.getX() + ICON_OFFSET_HOR + (ICON_WIDTH / 2) + HOR_PADDING_NAME,
                (int) pos.getY() + VERT_OFFSET_NAME, 300, 48, buffer);
    }
}
