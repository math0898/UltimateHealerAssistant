package io.github.math0898.processing.logentries;

import io.github.math0898.processing.EntryType;

import java.util.Scanner;

/**
 * A HealEntry encapsulates the SPELL_HEAL event in raw log files.
 */
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
        Scanner s = new Scanner(data);
        s.useDelimiter(",");
        for (int i = 0; i < 31; i++) // Next should be heal, and then overheal
            s.next();
        int total_heal = s.nextInt(); // todo: So far everything has been RAW_HEAL_AMOUNT, RAW_HEAL_AMOUNT. Is there heal absorb in here?
        int unknown = s.nextInt();
        overheal = s.nextInt();
        heal = total_heal - overheal;
    }

    /**
     * Returns the type of LogEntry this class represents.
     *
     * @return The entry type represented by this class.
     */
    @Override
    public EntryType getType() {
        return EntryType.HEALING;
    }

    /**
     * Prints a summary of this entry to System.out.
     */
    public void summarize () {
        System.out.println("Heal: " + heal + ". Overheal " + overheal);
    }

    /**
     * Accessor method to the amount of healing contained in this entry.
     *
     * @return The amount of healing.
     */
    public long getHeal () {
        return heal;
    }

    /**
     * Accessor method to the amount of overhealing contained in this entry.
     *
     * @return The amount of overhealing.
     */
    public long getOverheal () {
        return overheal;
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
