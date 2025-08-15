package io.github.math0898.views.healgraph;

import io.github.math0898.processing.Encounter;
import io.github.math0898.processing.LogManager;
import suga.engine.game.objects.BasicGameObject;
import suga.engine.graphics.DrawListener;
import suga.engine.graphics.GraphicsPanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 *
 */
public class EncounterIndicator extends BasicGameObject implements DrawListener {

    /**
     * Called every drawing frame so programs have a chance to make their voices heard on what gets drawn.
     *
     * @param width  The width of the pixel map.
     * @param height The height of the pixel map.
     * @param panel  The panel to apply changes to.
     */
    @Override
    public void applyChanges(int width, int height, GraphicsPanel panel) {
        BufferedImage buffer = new BufferedImage((width * 14) / 16, 48, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = buffer.createGraphics();
        graphics.setFont(new Font("Comic Sans", Font.BOLD, 32));
        graphics.setColor(new Color(200,200,200));

        graphics.drawString("Pull: " + LogManager.getInstance().getHighlightedEncounterIndex() + 1, 0, 32);

        FontMetrics metrics = graphics.getFontMetrics();
        Encounter encounter = LogManager.getInstance().getHighlightedEncounter();
        int length = metrics.stringWidth(encounter.getEnemyName());
        graphics.drawString(encounter.getEnemyName(), width / 2 - (length / 2), 32);

        Instant date = Instant.ofEpochMilli(encounter.getEncounterStartMillis());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm (M/dd)").withZone(TimeZone.getDefault().toZoneId());
        String dateStr = formatter.format(date);
        length = metrics.stringWidth(dateStr);
        graphics.drawString(dateStr, ((width * 14) / 16) - length, 32 );

        panel.addImage(width / 16, height / 16, (width * 14) / 16, 48, buffer);
    }
}
