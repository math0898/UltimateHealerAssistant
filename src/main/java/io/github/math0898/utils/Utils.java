package io.github.math0898.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * These are helpful methods and operations I find myself doing in multiple locations across the project. Written here
 * to reduce code duplication.
 *
 * @author Sugaku
 */
public class Utils {

    /**
     * This method converts the date time stamps in log files to millis since epoch.
     *
     * @param data The raw data from the warcraft log file we are converting.
     * @return The time in millis since epoch.
     */
    public static long millisFromLogTime (String data) {
        String date = data.replaceAll("  .+", "");
        String offset;
        if (date.contains("-")) offset = "-";
        else offset = "\\+";
        String zone = offset + "0" + date.replaceAll(".+" + offset, "");
        date = date.replaceAll("-\\d", "");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/dd/yyyy HH:mm:ss.SSS").withZone(ZoneId.of(zone)); // todo: 2 digit dates?
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
        return dateTime.toInstant(ZoneOffset.of(zone)).toEpochMilli();
    }
}
