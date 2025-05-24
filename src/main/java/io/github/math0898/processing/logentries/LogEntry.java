package io.github.math0898.processing.logentries;

import io.github.math0898.processing.EntryType;
import io.github.math0898.utils.Utils;

/**
 * Represents a single activity in an Encounter.
 *
 * @author Sugaku
 */
public abstract class LogEntry {

    /**
     * The data contained in this LogEntry.
     */
    protected final String data;

    /**
     * The time at which this log occurs.
     */
    protected final long time;

    /**
     * Creates this LogEntry from the given data.
     *
     * @param data The raw data to create this LogEntry from.
     */
    public LogEntry (String data) {
        this.data = data;
        time = Utils.millisFromLogTime(data.split(",")[0]);
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

    /**
     * Accessor method for the time this log entry occurred at.
     *
     * @return The time in millis since epoch this event occurred.
     */
    public long getTime () {
        return time;
    }
}
