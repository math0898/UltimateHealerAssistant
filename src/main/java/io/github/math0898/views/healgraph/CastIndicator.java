package io.github.math0898.views.healgraph;

import io.github.math0898.processing.Encounter;
import io.github.math0898.processing.LogManager;
import suga.engine.game.objects.BasicGameObject;
import suga.engine.graphics.DrawListener;
import suga.engine.graphics.GraphicsPanel;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class CastIndicator extends BasicGameObject implements DrawListener {

    /**
     * The spell to be and indicator for.
     */
    private final SpellQueries spell;

    /**
     * Any vertical offset to apply to the icon placement.
     */
    private final int offset;

    /**
     * A boolean to determine whether this cast indicator is active or not.
     */
    private boolean active = false;

    /**
     * Creates a new CastIndicator with the given parent scene.
     *
     * @param spell       The spell to indicate.
     * @param offset      A vertical offset applied to the icon.
     */
    public CastIndicator (SpellQueries spell, int offset) {
        this.spell = spell;
        this.offset = offset;
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
        if (!active) return;
        Encounter encounter = LogManager.getInstance().getHighlightedEncounter();
        List<Long> castTimes = encounter.querySpellHealingInstances(spell.spellName);
        final int widthByPixel = ((width * 3) / 4);
        final long widthByTime = encounter.encounterLengthMillis();
        for (long l : castTimes) {
            float ratio = ((float) (l - encounter.getEncounterStartMillis())) / widthByTime;
            panel.setRectangle(((width / 8) + ((int) (ratio * widthByPixel) / 10) * 10) - 1, ((height * 7) / 8) /*- 490*/, 3, 20 + offset, spell.color);
            try {
                panel.addImage(((width / 8) + ((int) (ratio * widthByPixel) / 10) * 10) - 13, ((height * 7) / 8) + offset, 25, 25, ImageIO.read(new File(spell.iconPath)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Toggles whether this indicator is active or not.
     */
    public void toggle () {
        active = !active;
    }
}
