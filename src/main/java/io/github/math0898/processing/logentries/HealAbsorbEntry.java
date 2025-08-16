package io.github.math0898.processing.logentries;

import io.github.math0898.utils.SpellDatabase;

/**
 * Heal Absorb events are a type of heal event but require different processing.
 *
 * @author Sugaku
 */
public class HealAbsorbEntry extends HealEntry {

    /**
     * The amount of healing that got absorbed.
     */
    private int absorbed;

    /**
     * Creates this HealEntry from the given data.
     *
     * @param data The raw data to create this HealEntry from.
     */
    public HealAbsorbEntry (String data) {
        super(data);
    }

    /**
     * Initializes the data contained within this entry.
     */
    protected void init () {
        String[] lines = data.split(",");
        caster = lines[13].replace("\"", "");
        long spellId = Long.parseLong(lines[16]);
        spellName = lines[17].replace("\"", "");
        SpellDatabase.getInstance().reportSpellName(spellId, spellName);
        absorbed = Integer.parseInt(lines[19]);
        int total_heal = Integer.parseInt(lines[20]);
        overheal = 0;// Integer.parseInt(lines[21]);
        heal = total_heal - overheal - absorbed;
        data = null;
    }

    /**
     * Prints a summary of this entry to System.out.
     */
    public void summarize () {
        System.out.println(caster + " -> " + spellName + ": " + heal + " | Overheal " + overheal + " | Absorb: " + absorbed);
    }

    /**
     * Accessor method to the total amount of healing absorbed in this entry.
     *
     * @return The total amount absorbed.
     */
    public long getAbsorbed () {
        return absorbed;
    }

    /**
     * Accessor method to the total amount of healing contained in this entry.
     *
     * @return The total amount of healing.
     */
    @Override
    public long getTotalHeal () { // todo: Need to find an instance of this event overhealing.
        return heal + absorbed + overheal;
    }
}
