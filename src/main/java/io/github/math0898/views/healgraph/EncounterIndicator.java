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
        final int OUTLINE_WIDTH = 8;
        final int OUTLINE_RADIUS = 6;

        BufferedImage buffer = new BufferedImage((width * 14) / 16, 48, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = buffer.createGraphics();
        graphics.setFont(new Font("Comic Sans", Font.BOLD, 32));

        // todo: {@link Utils#imageFromText()}
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        String name = "Pull: " + (LogManager.getInstance().getHighlightedEncounterIndex() + 1);
        graphics.setColor(Color.getHSBColor(0, 0, 0.05f));
        for (int i = -OUTLINE_WIDTH; i <= OUTLINE_WIDTH; i++)
            for (int j = -OUTLINE_WIDTH; j <= OUTLINE_WIDTH; j++)
                if (Math.abs(i) + Math.abs(j) < OUTLINE_RADIUS)
                    graphics.drawString(name, 0 + i, 32 + j);
        graphics.setColor(new Color(200,200,200));
        graphics.drawString(name, 0, 32);

        FontMetrics metrics = graphics.getFontMetrics();
        Encounter encounter = LogManager.getInstance().getHighlightedEncounter();
        int length = metrics.stringWidth(encounter.getEnemyName());
        String encounterName = encounter.getEnemyName();
        graphics.setColor(Color.getHSBColor(0, 0, 0.05f));
        for (int i = -OUTLINE_WIDTH; i <= OUTLINE_WIDTH; i++)
            for (int j = -OUTLINE_WIDTH; j <= OUTLINE_WIDTH; j++)
                if (Math.abs(i) + Math.abs(j) < OUTLINE_RADIUS)
                    graphics.drawString(encounterName, width / 2 - (length / 2) + i, 32 + j);
        graphics.setColor(new Color(200,200,200));
        graphics.drawString(encounterName, width / 2 - (length / 2), 32);

        Instant date = Instant.ofEpochMilli(encounter.getEncounterStartMillis());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm (M/dd)").withZone(TimeZone.getDefault().toZoneId());
        String dateStr = formatter.format(date);
        length = metrics.stringWidth(dateStr);
        graphics.setColor(Color.getHSBColor(0, 0, 0.05f));
        for (int i = -OUTLINE_WIDTH; i <= OUTLINE_WIDTH; i++)
            for (int j = -OUTLINE_WIDTH; j <= OUTLINE_WIDTH; j++)
                if (Math.abs(i) + Math.abs(j) < OUTLINE_RADIUS)
                    graphics.drawString(dateStr, ((width * 14) / 16) - length + i, 32 + j);
        graphics.setColor(new Color(200,200,200));
        graphics.drawString(dateStr, ((width * 14) / 16) - length, 32 );

        panel.addImage(width / 16, height / 16, (width * 14) / 16, 48, buffer);
    }
}
