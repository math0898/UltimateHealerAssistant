package io.github.math0898.processing.logentries;

import io.github.math0898.utils.SpellDatabase;
import lombok.Getter;

/**
 * A HealEntry encapsulates the SPELL_HEAL event in raw log files.
 */
@Getter
public class HealEntry extends LogEntry {

    /**
     * The amount of healing in this entry.
     */
    protected long heal;

    /**
     * The amount of overhealing in this entry.
     */
    protected long overheal;

    /**
     * The name of the spell that did the healing.
     */
    protected String spellName;

    /**
     * The name of the creature/player who cast this spell.
     */
    protected String caster;


    /**
     * Creates this HealEntry from the given data.
     *
     * @param data The raw data to create this HealEntry from.
     */
    public HealEntry (String data) {
        super(data);
        init();
    }

    /**
     * Initializes the data contained within this entry.
     */
    protected void init () {
//        0: 10/9/2025 19:16:43.664-5  SPELL_PERIODIC_HEAL
//        1: Player-60-0F938807
//        2: "Nillath-Stormrage-US"
//        3: 0x511
//        4: 0x80000000
//        5: Player-3675-087A89DF
//        6: "Heiderich-MoonGuard-US"
//        7: 0x514
//        8: 0x80000000
//        9: 363534
//        10: "Rewind"
//        11: 0x40
//        12: Player-3675-087A89DF
//        13: 0000000000000000
//        14: 18438480
//        15: 18438480
//        16: 168045
//        17: 12359
//        18: 107790
//        19: 2454
//        20: 165
//        21: 0
//        22: 1
//        23: 727
//        24: 1300
//        25: 0
//        26: 1875.78
//        27: 4476.94
//        28: 2465
//        29: 0.7173
//        30: 720
//        31: 781595 // Total heal?
//        32: 781595
//        33: 61494 // Overheal.
//        34: 0
//        35: nil
        // https://www.warcraftlogs.com/reports/JQx7ZRHC6GmYfhPN?fight=1&view=events&pins=2%24Off%24%23244F4B%24healing%24-1%24261326855.0.0.Evoker%240.0.0.Any%24true%24142248415.0.0.Warrior%24true%240
        String[] lines = data.split(",");
        caster = lines[2].replace("\"", "");
        long spellId = Long.parseLong(lines[9]);
        spellName = lines[10].replace("\"", "");
        SpellDatabase.getInstance().reportSpellName(spellId, spellName);
        int total_heal = Integer.parseInt(lines[31]);
        overheal = Integer.parseInt(lines[33]); // todo: So far everything has been RAW_HEAL_AMOUNT, RAW_HEAL_AMOUNT. Is there heal absorb in here?
        heal = total_heal - overheal;
        data = null;
    }

    /**
     * Prints a summary of this entry to System.out.
     */
    public void summarize () {
        System.out.println("Heal: " + heal + ". Overheal " + overheal);
    }

    /**
     * Accessor method to the total amount of healing contained in this entry.
     *
     * @return The total amount of healing.
     */
    public long getTotalHeal () {
        return heal + overheal;
    }
}
