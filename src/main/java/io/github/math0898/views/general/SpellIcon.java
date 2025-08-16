package io.github.math0898.views.general;

import io.github.math0898.game.UltimateHealerAssistantGame;
import io.github.math0898.utils.*;
import suga.engine.game.objects.BasicGameObject;
import suga.engine.graphics.DrawListener;
import suga.engine.graphics.GraphicsPanel;
import suga.engine.input.mouse.GameMouseListener;
import suga.engine.physics.Vector;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A simple icon for a spell. Supports mousing over to display spell details.
 *
 * @author Sugaku
 */
public class SpellIcon extends BasicGameObject implements DrawListener {

    /**
     * The height of the hover over box.
     */
    private static final int HOVER_OVER_HEIGHT = 120;

    /**
     * The maximum width of the hover over box.
     */
    private static final int HOVER_OVER_WIDTH = 400;

    /**
     * The x position of the hover over box.
     */
    private static final int HOVER_OVER_X = 1920 - HOVER_OVER_WIDTH - 10;

    /**
     * The y position of the hover over box.
     */
    private static final int HOVER_OVER_Y = 1080 - 35 - HOVER_OVER_HEIGHT - 10;

    /**
     * The horizontal name padding.
     */
    private static final int NAME_PADDING_HOR = 10;

    /**
     * The vertical name padding.
     */
    private static final int NAME_OFFSET_VERT = -20;

    /**
     * The buffer to place around the large spell icon shown in the hover over.
     */
    private static final int LARGE_ICON_PADDING = 10;

    /**
     * Vertical padding added to the spell description bellow the spell name.
     */
    private static final int DESCRIPTION_PADDING_VERT = 32;

    /**
     * A saved version of the spell icon in memory so we don't need to bother blizzard all the time.
     */
    private BufferedImage icon;

    /**
     * A saved version of the spell details in memory, so we don't need to bother the SpellDatabase every time.
     */
    private SpellDetails details;

    /**
     * The id of the spell this icon is representing.
     */
    private final long spellId;

    /**
     * The height of this SpellIcon.
     */
    private final int height;

    /**
     * The width of this SpellIcon.
     */
    private final int width;

    /**
     * A simple boolean to determine whether this object is being hovered over or not.
     */
    private boolean hovered = false;

    /**
     * Creates a new SpellIcon of the given spell id.
     *
     * @param spellId this is the id of the spell.
     * @param width The total width of the SpellIcon.
     * @param height The total height of the SpellIcon.
     * @param x The x coordinate of this SpellIcon. This is the center point.
     * @param y The y coordinate of this SpellIcon. This is the center point.
     */
    public SpellIcon (long spellId, int width, int height, int x, int y) {
        this.width = width;
        this.height = height;
        this.spellId = spellId;
        this.pos = new Vector(x, y, 0);
    }

    /**
     * Checks whether the mouse is hovering over this spell icon or not.
     *
     * @param cursor The position of the mouse.
     */
    public boolean checkHovered (Point cursor) {
        if (cursor.x > (int) pos.getX() + (this.width / 2)) return false;
        if (cursor.x < (int) pos.getX() - (this.width / 2)) return false;
        if (cursor.y > (int) pos.getY() + (this.height / 2)) return false;
        if (cursor.y < (int) pos.getY() - (this.height / 2)) return false;
        return true;
    }

    /**
     * Gets an updated mouse position.
     */
    public void updateMousePos () {
        GameMouseListener mouseListener = UltimateHealerAssistantGame.getInstance().getMouseListener();
        Point cursor = mouseListener.getMousePos();
        if (cursor == null) return;
        Point translated = new Point(cursor.x, cursor.y - 35); // This might be related to the X-Border, but I have no clue.
        hovered = checkHovered(translated);
    }

    /**
     * Called every logic frame to run the logic on this GameObject.
     */
    @Override
    public void runLogic () {
        super.runLogic();
        updateMousePos();
        if (icon == null) {
            BufferedImage buffered = BlizzardResourcesCache.getInstance().loadResource(spellId + "", ResourceTypes.SPELL_ICONS);
            if (buffered == null) return;
            Image img = buffered.getScaledInstance(width, height, Image.SCALE_DEFAULT);
            icon = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D tmp = icon.createGraphics();
            tmp.drawImage(img, 0, 0, null);
            tmp.dispose();
        }
        if (details == null)
            details = SpellDatabase.getInstance().getDetails(spellId);
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
        if (icon == null) return;
        panel.addImage((int) pos.getX() - this.width / 2, (int) pos.getY() - (this.height / 2), this.width, this.height, icon);
        if (hovered) {
            panel.setRectangle(HOVER_OVER_X, HOVER_OVER_Y, HOVER_OVER_WIDTH, HOVER_OVER_HEIGHT, new Color(20, 20, 20));
            int iconSize = Math.min(HOVER_OVER_HEIGHT, HOVER_OVER_WIDTH) - (LARGE_ICON_PADDING * 2);
            panel.addImage(HOVER_OVER_X + LARGE_ICON_PADDING, HOVER_OVER_Y + LARGE_ICON_PADDING, iconSize, iconSize, icon);
            BufferedImage spellName = Utils.imageFromText(new Font("Comic Sans", Font.BOLD, 32), Color.WHITE, details.spellName());
            panel.addImage(HOVER_OVER_X + LARGE_ICON_PADDING + iconSize + NAME_PADDING_HOR, HOVER_OVER_Y + LARGE_ICON_PADDING + NAME_OFFSET_VERT, spellName.getWidth(), spellName.getHeight(), spellName);
            BufferedImage spellDescription = Utils.imageFromText(new Font("Comic Sans", Font.ITALIC, 18), Color.WHITE, details.description());
            panel.addImage(HOVER_OVER_X + LARGE_ICON_PADDING + iconSize + NAME_PADDING_HOR, HOVER_OVER_Y + LARGE_ICON_PADDING + NAME_OFFSET_VERT + DESCRIPTION_PADDING_VERT, spellName.getWidth(), spellName.getHeight(), spellDescription);
        } // todo: Add line breaks to spell hover over.
    }
}
