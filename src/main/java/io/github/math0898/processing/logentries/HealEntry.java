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
        Scanner s = new Scanner(data);
        int total_heal = 0;
        s.useDelimiter(",");
        for (int i = 0; s.hasNext(); i++) {
            switch (i) {
                case 2 -> caster = s.next().replace("\"", "");
                case 10 -> spellName = s.next().replace("\"", "");
                case 31 -> total_heal = Integer.parseInt(s.next());
                case 33 -> overheal = Integer.parseInt(s.next()); // todo: So far everything has been RAW_HEAL_AMOUNT, RAW_HEAL_AMOUNT. Is there heal absorb in here?
                // default -> System.out.println(i + ": " + s.next());
                default -> s.next();
            }
        }
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
     * Returns the spell name responsible for this heal event.
     *
     * @return The spell name for this heal event.
     */
    public String getSpellName () {
        return spellName;
    }

    /**
     * Returns the caster responsible for this heal event.
     *
     * @return The caster that created this heal event.
     */
    public String getCaster () {
        return caster;
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
