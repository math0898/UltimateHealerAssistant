package io.github.math0898.processing.logentries;

import io.github.math0898.processing.EntryType;

/**
 * Represents a single activity in an Encounter.
 *
 * @author Sugaku
 */
public abstract class LogEntry {

    /**
     * The data contained in this LogEntry.
     */
    private final String data;

    /**
     * Creates this LogEntry from the given data.
     *
     * @param data The raw data to create this LogEntry from.
     */
    public LogEntry (String data) {
        this.data = data;
    }

    /**
     * Returns the type of LogEntry this class represents.
     *
     * @return The entry type represented by this class.
     */
    public abstract EntryType getType ();

    /**
     * Accessor method for the raw data contained within this LogEntry.
     *
     * @return The raw data that creates this LogEntry.
     */
    public String rawData () {
        return data;
    }
}
