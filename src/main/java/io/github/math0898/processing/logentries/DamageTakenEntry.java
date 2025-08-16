package io.github.math0898.processing.logentries;

import io.github.math0898.utils.SpellDatabase;

public class DamageTakenEntry extends LogEntry {

    /**
     * The amount of damage in this log entry.
     */
    private long damageTaken = 0;

    /**
     * The id of the spell.
     */
    private long spellId = 0;

    /**
     * The name of the target of this damage taken event.
     */
    private String targetName = "";

    /**
     * Creates this LogEntry from the given data.
     *
     * @param data The raw data to create this LogEntry from.
     */
    public DamageTakenEntry (String data) {
        super(data);
        init();
    }

    /**
     * Initializes the data contained within this entry.
     */
    protected void init () { // todo: This still doesn't consider all the incoming damage.
        String[] lines = data.split(",");
//        0: 8/14/2025 19:34:17.985-5  SPELL_PERIODIC_DAMAGE
//        1: Player-3676-0DFDE738
//        2: "Reaperdiogun-Area52-US"
//        3: 0x514
//        4: 0x80000000
//        5: Vehicle-0-4215-2810-14131-233815-00001E7FC6
//        6: "Loom'ithar"
//        7: 0x10a48
//        8: 0x80000000
//        9: 458169
//        10: "Hyperpyrexia"
//        11: 0x10
//        12: Vehicle-0-4215-2810-14131-233815-00001E7FC6
//        13: 0000000000000000
//        14: 14930214889
//        15: 16059695720
//        16: 0
//        17: 0
//        18: 42857
//        19: 0
//        20: 0
//        21: 0
//        22: 3
//        23: 18
//        24: 100
//        25: 0
//        26: 1581.32
//        27: 3258.00
//        28: 2462
//        29: 0.2718
//        30: 83
//        31: 962139
//        32: 962138
//        33: -1
//        34: 16
//        35: 0
//        36: 0
//        37: 0
//        38: nil
//        39: nil
//        40: nil
//        41: ST
        String target = lines[12];
        if (!target.contains("Player-")) return;
        targetName = lines[6];
        spellId = Long.parseLong(lines[9]);
        String spellName = lines[10];
        SpellDatabase.getInstance().reportSpellName(spellId, spellName.replace("\"", ""));
        damageTaken = Long.parseLong(lines[31]);
        long overkill = Math.max(Long.parseLong(lines[33]), 0); // > 0 Is overkill
        damageTaken = damageTaken - overkill;
        damageTaken = damageTaken - Long.parseLong(lines[37]); // Absorb
        data = null;
    }

    /**
     * Returns the amount of player damage taken in this log event.
     *
     * @return The amount of player damage taken.
     */
    public long damageTaken () {
        return damageTaken;
    }

    /**
     * Returns the id of the spell that dealt damage in this log event.
     *
     * @return The spellId of this log.
     */
    public long getSpellId () {
        return spellId;
    }

    /**
     * Returns the actor name of what took damage in this entry.
     *
     * @return The name of the target of this damage taken entry.
     */
    public String getTargetName () {
        return targetName;
    }
}
