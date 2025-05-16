package io.github.math0898.processing.logentries;

import io.github.math0898.processing.EntryType;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

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
        Scanner s = new Scanner(data);
        s.useDelimiter(" ");
        String date = s.next() + " " + s.next().replaceAll("-\\d", "");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/dd/yyyy HH:mm:ss.SSS"); // todo: 2 digit dates?
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
        time = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
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
