package io.github.math0898.gui;

import io.github.math0898.Main;
import io.github.math0898.processing.Encounter;
import suga.engine.GameEngine;
import suga.engine.game.objects.BasicGameObject;
import suga.engine.graphics.DrawListener;
import suga.engine.graphics.GraphicsPanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class CastIndicator extends BasicGameObject implements DrawListener {

    /**
     * The MainGraphScene this indicator is part of.
     */
    private final MainGraphScene parentScene;

    /**
     * Creates a new CastIndicator with the given parent scene.
     *
     * @param parentScene The parent scene of this indicator.
     */
    public CastIndicator (MainGraphScene parentScene) {
        this.parentScene = parentScene;
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
//        Encounter encounter = Main.encounters.get(parentScene.getGraphedEncounterIndex());
//        List<Long> results = encounter.querySpellHealingInstances("Consume Flame");
//        final int widthByPixel = ((width * 3) / 4);
//        final long widthByTime = encounter.encounterLengthMillis();
//        for (long l : results) {
//            float ratio = ((float) (l - encounter.getEncounterStartMillis())) / widthByTime;
//            panel.setRectangle(((width / 8) + ((int) (ratio * widthByPixel) / 10) * 10) - 1, ((height * 7) / 8) /*- 490*/, 3, 20, new Color(221, 33, 59));
//            try {
//                panel.addImage(((width / 8) + ((int) (ratio * widthByPixel) / 10) * 10) - 13, ((height * 7) / 8), 25, 25, ImageIO.read(new File("/home/sugaku/Development/Standalone/Java/UltimateHealerAssistant/icons/inv_ability_flameshaperevoker_engulf.jpg")));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        results = encounter.querySpellHealingInstances("Rewind");
//        for (long l : results) {
//            float ratio = ((float) (l - encounter.getEncounterStartMillis())) / widthByTime;
//            panel.setRectangle(((width / 8) + ((int) (ratio * widthByPixel) / 10) * 10) - 1, ((height * 7) / 8) /*- 490*/, 3, 20 + 30, new Color(241, 199, 63));
//            try {
//                panel.addImage(((width / 8) + ((int) (ratio * widthByPixel) / 10) * 10) - 13, ((height * 7) / 8) + 30, 25, 25, ImageIO.read(new File("/home/sugaku/Development/Standalone/Java/UltimateHealerAssistant/icons/ability_evoker_rewind.jpg")));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        results = encounter.querySpellHealingInstances("Emerald Communion");
//        for (long l : results) {
//            float ratio = ((float) (l - encounter.getEncounterStartMillis())) / widthByTime;
//            panel.setRectangle(((width / 8) + ((int) (ratio * widthByPixel) / 10) * 10) - 1, ((height * 7) / 8) /*- 490*/, 3, 20 + 60, new Color(68, 236, 222));
//            try {
//                panel.addImage(((width / 8) + ((int) (ratio * widthByPixel) / 10) * 10) - 13, ((height * 7) / 8) + 60, 25, 25, ImageIO.read(new File("/home/sugaku/Development/Standalone/Java/UltimateHealerAssistant/icons/ability_evoker_green_01.jpg")));
//            } catch (IOException e) {
//                throw new RuntimeException(e); // todo: Add a third layer.
//            }
//        }
    }
}
