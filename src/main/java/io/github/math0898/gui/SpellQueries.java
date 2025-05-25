package io.github.math0898.gui;

import java.awt.*;

/**
 *
 */
public enum SpellQueries {

    // Pres spells
    CONSUME_FLAME("Consume Flame", new Color(196, 30, 58), "/home/sugaku/Development/Standalone/Java/UltimateHealerAssistant/icons/inv_ability_flameshaperevoker_engulf.jpg"),
    EMERALD_COMMUNION("Emerald Communion", new Color(0, 255, 152), "/home/sugaku/Development/Standalone/Java/UltimateHealerAssistant/icons/ability_evoker_green_01.jpg"),
    REWIND("Rewind", new Color(255, 244, 104), "/home/sugaku/Development/Standalone/Java/UltimateHealerAssistant/icons/ability_evoker_rewind.jpg"),
    // Holy Priest
    DIVINE_HYMN("Divine Hymn", new Color(196, 30, 58), "/home/sugaku/Development/Standalone/Java/UltimateHealerAssistant/icons/spell_holy_divinehymn.jpg"),
    // Disc Priest
    EVANGELISM("Evangelism", new Color(255, 255, 255), "/home/sugaku/Development/Standalone/Java/UltimateHealerAssistant/icons/spell_holy_divineillumination.jpg"), // todo: Requires special handling.
    PIETY("Premonition of Piety", new Color(255, 255, 255), "/home/sugaku/Development/Standalone/Java/UltimateHealerAssistant/icons/inv_ability_oraclepriest_premonitionpiety.jpg"), // todo: Requires special handling.
    ATONEMENT("Atonement", new Color(255, 255, 255), "/home/sugaku/Development/Standalone/Java/UltimateHealerAssistant/icons/ability_priest_atonement.jpg"), // todo: Requires special handling.
    // Resto Shammy
    SPIRIT_LINK("Spirit Link", new Color(0, 255, 152), "/home/sugaku/Development/Standalone/Java/UltimateHealerAssistant/icons/spell_shaman_spiritlink.jpg"),
    HEALING_TIDE("Healing Tide Totem", new Color(63, 199, 235), "/home/sugaku/Development/Standalone/Java/UltimateHealerAssistant/icons/ability_shaman_healingtide.jpg");

    public final String spellName;
    public final Color color;
    public final String iconPath;

    SpellQueries (String spellName, Color color, String iconPath) {
        this.spellName = spellName;
        this.color = color;
        this.iconPath = iconPath;
    }

    public static SpellQueries fromOrdinal (int ordinal) {
        for (SpellQueries query : SpellQueries.values())
            if (ordinal == query.ordinal())
                return query;
        return REWIND;
    }
}
