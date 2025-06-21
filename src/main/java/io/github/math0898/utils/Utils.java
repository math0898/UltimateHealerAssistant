package io.github.math0898.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy HH:mm:ss.SSS").withZone(ZoneId.of(zone)); // todo: 2 digit dates?
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
        return dateTime.toInstant(ZoneOffset.of(zone)).toEpochMilli();
    }

    /**
     * Divides all elements of the given list by the given value.
     *
     * @param list   The list to modify.
     * @param scalar The scalar to apply over the whole list.
     */
    public static List<Long> scaleList (List<Long> list, long scalar) {
        List<Long> newList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++)
            newList.add(i, list.get(i) / scalar);
        return newList;
    }

    /**
     * Joins the two lists by adding them on top of one another.
     *
     * @param list1 The first list to add.
     * @param list2 The second list to add.
     */
    public static List<Long> addLists (List<Long> list1, List<Long> list2) {
        List<Long> newList = new ArrayList<>();
        for (int i = 0; i < list1.size() && i < list2.size(); i++)
            newList.add(i, list1.get(i) + list2.get(i));
        return newList;
    }
}
