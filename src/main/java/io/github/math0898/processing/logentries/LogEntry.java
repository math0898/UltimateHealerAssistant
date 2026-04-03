package io.github.math0898.processing.logentries;

import io.github.math0898.utils.Utils;
import lombok.Getter;

/**
 * Represents a single activity in an Encounter.
 *
 * @author Sugaku
 */
public class LogEntry {

    /**
     * The data contained in this LogEntry.
     */
    protected String data;

    /**
     * The time at which this log occurs.
     * -- GETTER --
     *  Accessor method for the time this log entry occurred at.
     *
     * @return The time in millis since epoch this event occurred.
     */
    @Getter
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
     * Turns this LogEntry into it's string version.
     *
     * @return A string version of this LogEntry.
     */
    @Override
    public String toString () {
        return "LogEntry{" +
                "data='" + data + '\'' +
                '}';
    }
}
