package io.github.math0898.views.general;

import io.github.math0898.game.UltimateHealerAssistantGame;
import io.github.math0898.utils.BlizzardResourcesCache;
import io.github.math0898.utils.ResourceTypes;
import io.github.math0898.utils.SpellDatabase;
import io.github.math0898.utils.SpellDetails;
import suga.engine.GameEngine;
import suga.engine.game.objects.BasicGameObject;
import suga.engine.graphics.DrawListener;
import suga.engine.graphics.GraphicsPanel;
import suga.engine.input.mouse.GameMouseListener;
import suga.engine.logger.Level;
import suga.engine.physics.Vector;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SpellHoverOver extends BasicGameObject implements DrawListener {

    /**
     * The height of the hover over box.
     */
    private static final int HOVER_OVER_HEIGHT = 120;

    /**
     * The maximum width of the hover over box.
     */
    private static final int HOVER_OVER_WIDTH = 300;

    /**
     * The buffer to place around the large spell icon shown in the hover over.
     */
    private static final int LARGE_ICON_PADDING = 10;

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
     * The position of the mouse last time it was read.
     */
    private Point lastPos = new Point(0, 0);

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
    public SpellHoverOver (long spellId, int width, int height, int x, int y) {
        this.width = width;
        this.height = height;
        this.spellId = spellId;
        this.pos = new Vector(x, y, 0);
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
        if (hovered) {
            panel.setRectangle(lastPos.x, lastPos.y, HOVER_OVER_WIDTH, HOVER_OVER_HEIGHT, new Color(20, 20, 20));
            GameEngine.getLogger().log("Mouseover! " + spellId, Level.DEBUG);
        }
    }
}