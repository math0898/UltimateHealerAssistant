package io.github.math0898.utils;

import java.awt.*;

/**
 * A enum containing default color values for UltimateHealerAssistant.
 *
 * @author Sugaku
 */
public enum ColorPalette {

    /**
     * The background color for the entire program.
     */
    BACKGROUND(0, 0, 0.05f),
    
    /**
     * The class color for Death Knight used by World of Warcraft.
     */
    CLASS_DEATH_KNIGHT(196,30,58),

    /**
     * The class color for Demon Hunter used by World of Warcraft.
     */
    CLASS_DEMON_HUNTER(163,48,201),

    /**
     * The class color for Druid used by World of Warcraft.
     */
    CLASS_DRUID(255,124,10),

    /**
     * The class color for Evoker used by World of Warcraft.
     */
    CLASS_EVOKER(51,147,127),

    /**
     * The class color for Hunter used by World of Warcraft.
     */
    CLASS_HUNTER(170,211,114),

    /**
     * The class color for Mage used by World of Warcraft.
     */
    CLASS_MAGE(63,199,235),

    /**
     * The class color for Monk used by World of Warcraft.
     */
    CLASS_MONK(0,255,152),

    /**
     * The class color for Paladin used by World of Warcraft.
     */
    CLASS_PALADIN(244,140,186),

    /**
     * The class color for Priest used by World of Warcraft.
     */
    CLASS_PRIEST(255,255,255),

    /**
     * The class color for Rogue used by World of Warcraft.
     */
    CLASS_ROGUE(255,244,104),

    /**
     * The class color for Shaman used by World of Warcraft.
     */
    CLASS_SHAMAN(0,112,221),

    /**
     * The class color for Warlock used by World of Warcraft.
     */
    CLASS_WARLOCK(135,136,238),

    /**
     * The class color for Warrior used by World of Warcraft.
     */
    CLASS_WARRIOR(198,155,109);

    /**
     * An internal rgb to hold ColorPalette information.
     */
    private final int[] rgb = new int[3];

    /**
     * Creates a new ColorPalette color based on the given red, green, and blue values.
     *
     * @param r The red component of the color.
     * @param g The green component of the color.
     * @param b The blue component of the color.
     */
    ColorPalette (int r, int g, int b) {
        rgb[0] = r;
        rgb[1] = g;
        rgb[2] = b;
    }

    /**
     * Creates a new ColorPalette color based on hue, saturation, and balance (value).
     *
     * @param h The hue of the color.
     * @param s The saturation of the color.
     * @param b The brightness of the color.
     */
    ColorPalette (float h, float s, float b) {
        Color color = Color.getHSBColor(h, s, b);
        rgb[0] = color.getRed();
        rgb[1] = color.getGreen();
        rgb[2] = color.getBlue();
    }

    /**
     * Creates a new ColorPalette color with the given HTML color code.
     *
     * @param htmlCode The HTML color code of this color.
     */
    ColorPalette (String htmlCode) {
        // todo: Implement.
    }

    /**
     * Spits out a Color object based on this color.
     *
     * @return The Color object version of this color.
     */
    public Color getColor () {
        return new Color(rgb[0], rgb[1], rgb[2]);
    }
}
