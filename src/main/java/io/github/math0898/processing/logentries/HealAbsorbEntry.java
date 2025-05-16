package io.github.math0898.processing.logentries;

import java.util.Scanner;

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
        Scanner s = new Scanner(data);
        s.useDelimiter(",");
        for (int i = 0; i < 19; i++) // Next should be total, and then amount absorbed or vice-versa
            s.next();
        absorbed = s.nextInt(); // ?
        int total_heal = s.nextInt(); // ?
        overheal = 0;
        heal = total_heal - overheal - absorbed;
    }

    /**
     * Prints a summary of this entry to System.out.
     */
    public void summarize () {
        System.out.println("Heal: " + heal + ". Overheal " + overheal + ". Absorb: " + absorbed + ".");
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
