package io.github.math0898.views.general;

import io.github.math0898.utils.*;
import suga.engine.game.objects.BasicGameObject;
import suga.engine.graphics.DrawListener;
import suga.engine.graphics.GraphicsPanel;
import suga.engine.physics.Vector;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A simple icon for a spell. Supports mousing over to display spell details.
 * // todo: Add mouseover support.
 * @author Sugaku
 */
public class SpellIcon extends BasicGameObject implements DrawListener {

    /**
     * A saved version of the spell icon in memory so we don't need to bother blizzard all the time.
     */
    private final BufferedImage icon;

    /**
     * The height of this SpellIcon.
     */
    private final int height;

    /**
     * The width of this SpellIcon.
     */
    private final int width;

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
        this.pos = new Vector(x, y, 0);
        BufferedImage buffered = BlizzardResourcesCache.getInstance().loadResource(spellId + "", ResourceTypes.SPELL_ICONS);
        Image img = buffered.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        icon = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D tmp = icon.createGraphics();
        tmp.drawImage(img, 0, 0, null);
        tmp.dispose();
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
    }
}
