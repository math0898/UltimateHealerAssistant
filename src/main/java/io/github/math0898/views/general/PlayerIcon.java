package io.github.math0898.views.general;

import io.github.math0898.utils.BlizzardResourcesCache;
import io.github.math0898.utils.ResourceTypes;
import suga.engine.GameEngine;
import suga.engine.game.objects.BasicGameObject;
import suga.engine.graphics.DrawListener;
import suga.engine.graphics.GraphicsPanel;
import suga.engine.physics.Vector;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * The Player icon is an icon generated with a player name and server which allows this to grab the image from Blizzard's
 * WoW armory api.
 * @see <a href="https://develop.battle.net/documentation/world-of-warcraft/guides/character-renders">Blizzard API Character Renders.</a>
 *
 * @author Sugaku
 */
public class PlayerIcon extends BasicGameObject implements DrawListener {

    /**
     * The size of the role icon to display.
     */
    private static final double ROLE_ICON_SIZE_PERCENTAGE = 0.24;

    /**
     * A saved version of the Player icon in memory so we don't need to bother blizzard all the time.
     */
    private final BufferedImage icon;

    /**
     * The height of this PlayerIcon.
     */
    private final int height;

    /**
     * The width of this PlayerIcon.
     */
    private final int width;

    /**
     * This player's role if one is specified.
     */
    private String role;

    /**
     * Creates a new PlayerIcon with the given realm slug and character name.
     *
     * @param actorName This is the name of the character as it appears in log files.
     * @param width The total width of the PlayerIcon.
     * @param height The total height of the PlayerIcon.
     * @param x The x coordinate of this PlayerIcon. This is the center point.
     * @param y The y coordinate of this PlayerIcon. This is the center point.
     */
    public PlayerIcon (String actorName, int width, int height, int x, int y) {
        this(actorName, "none", width, height, x, y);
    }

    /**
     * Creates a new PlayerIcon with the given realm slug and character name.
     *
     * @param actorName This is the name of the character as it appears in log files.
     * @param role The role of this character. Can be DPS, Tank, Healer, or one of the specs. // todo: requires spec implementations.
     * @param width The total width of the PlayerIcon.
     * @param height The total height of the PlayerIcon.
     * @param x The x coordinate of this PlayerIcon. This is the center point.
     * @param y The y coordinate of this PlayerIcon. This is the center point.
     */
    public PlayerIcon (String actorName, String role, int width, int height, int x, int y) {
        this.pos = new Vector(x, y, 0);
        this.height = height;
        this.width = width;
        this.role = role;
        BufferedImage bufferedImage = BlizzardResourcesCache.getInstance().loadResource(actorName, ResourceTypes.PLAYER_ICONS);
        Image img = bufferedImage.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        icon = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D tmp = icon.createGraphics();
        tmp.drawImage(img, 0, 0, null);
        tmp.dispose();
    }

    /**
     * Sets the role for this PlayerIcon.
     *
     * @param role The new role for this PlayerIcon.
     */
    public void setRole (String role) {
        this.role = role;
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
        panel.addImage((int) pos.getX() - this.width / 2, (int) pos.getY() - this.height / 2, this.width, this.height, icon);
        BufferedImage roleIcon = null;
        switch (role) {
            case "DPS" -> {
                try {
                    roleIcon = ImageIO.read(new File("/home/sugaku/Development/Standalone/Java/UltimateHealerAssistant/icons/Dps_icon.png"));
                } catch (Exception exception) {
                    GameEngine.getLogger().log(exception);
                }
            }
            case "Healer" -> {
                try {
                    roleIcon = ImageIO.read(new File("/home/sugaku/Development/Standalone/Java/UltimateHealerAssistant/icons/Healer_icon.png"));
                } catch (Exception exception) {
                    GameEngine.getLogger().log(exception);
                }
            }
            case "Tank" -> {
                try {
                    roleIcon = ImageIO.read(new File("/home/sugaku/Development/Standalone/Java/UltimateHealerAssistant/icons/Tank_icon.png"));
                } catch (Exception exception) {
                    GameEngine.getLogger().log(exception);
                }
            }
        }
        if (roleIcon != null)
            panel.addImage((int) pos.getX() - this.width / 2, (int) pos.getY() - this.height / 2, (int) (this.width * ROLE_ICON_SIZE_PERCENTAGE), (int) (this.height * ROLE_ICON_SIZE_PERCENTAGE), roleIcon);
    }
}
