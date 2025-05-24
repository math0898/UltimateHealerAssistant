package io.github.math0898.processing.logentries;

import io.github.math0898.processing.EntryType;

import java.util.Scanner;

public class DamageTakenEntry extends LogEntry {

    /**
     * The amount of damage in this log entry.
     */
    private long damageTaken = 0;

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
        Scanner s = new Scanner(data);
        s.useDelimiter(",");
        for (int i = 0; i < 12; i++)
            s.next();
        String target = s.next();
        if (!target.contains("Player-")) return;
        for (int i = 13; i < 31; i++)
            s.next();
        damageTaken = Long.parseLong(s.next());
        s.next(); // Unmitigated
        long overkill = Math.max(Long.parseLong(s.next()), 0); // > 0 Is overkill
        damageTaken = damageTaken - overkill;
        s.next(); // 8
        s.next(); // 0
        s.next(); // 0
        damageTaken = damageTaken - Long.parseLong(s.next()); // Absorb
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
     * Returns the type of LogEntry this class represents.
     *
     * @return The entry type represented by this class.
     */
    @Override
    public EntryType getType () {
        return EntryType.DAMAGE;
    }
}
