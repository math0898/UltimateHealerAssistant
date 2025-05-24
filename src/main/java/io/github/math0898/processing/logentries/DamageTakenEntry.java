package io.github.math0898.processing.logentries;

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
        String[] lines = data.split(",");
        String target = lines[12];
        if (!target.contains("Player-")) return;
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
}
