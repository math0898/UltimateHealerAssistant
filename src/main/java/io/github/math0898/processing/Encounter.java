package io.github.math0898.processing;

import io.github.math0898.processing.logentries.DamageTakenEntry;
import io.github.math0898.processing.logentries.HealAbsorbEntry;
import io.github.math0898.processing.logentries.HealEntry;
import io.github.math0898.processing.logentries.LogEntry;
import suga.engine.GameEngine;
import suga.engine.logger.Level;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * An encounter represents all the data contained in a single encounter. This includes sums and results of multiple
 * events as well the raw data from the logs file.
 */
public class Encounter { // todo: Might be worthwhile during processing to create an index of players and spells so speed up queries.
    //                            Maybe call it, "deep processing" and make it an option for faster graph generations at the cost of
    //                            increased processing time and RAM usage.

    /**
     * Raw log data included in this encounter.
     */
    private final String data;

    /**
     * An entire list of all the entries contained within this Encounter.
     */
    private final List<LogEntry> entries = new ArrayList<>();

    /**
     * A quick boolean to determine whether this Encounter has undergone processing or not.
     */
    private boolean processed = false;

    /**
     * The start time of this Encounter in millis since epoch.
     */
    private long encounterStartMillis;

    /**
     * The end time of this Encounter in millis since epoch.
     */
    private long encounterEndMillis;

    /**
     * The name of the mob that this Encounter is with.
     */
    private String enemyName;

    /**
     * Creates a new Encounter with the given data.
     *
     * @param data This encounter as written in a log file.
     */
    public Encounter (String data) {
        this.data = data;
    }

    /**
     * Processes the data contained within this Encounter.
     */
    public void process () {
        if (processed) return;
        processed = true;
        Scanner s = new Scanner(data);
        while (s.hasNextLine()) {
            String line = s.nextLine();
            if (line.contains(" SPELL_HEAL_ABSORBED,")) entries.add(new HealAbsorbEntry(line));
            else if (line.contains(" SPELL_HEAL,")) entries.add(new HealEntry(line));
            else if (line.contains(" SPELL_PERIODIC_HEAL,")) entries.add(new HealEntry(line)); // todo: Almost identical data but distinction would be nice.
            else if (line.contains("  SPELL_PERIODIC_DAMAGE,")) entries.add(new DamageTakenEntry(line));
            else if (line.contains("  SPELL_DAMAGE,")) entries.add(new DamageTakenEntry(line));
            else if (line.contains(" ENCOUNTER_START")) processEncounterStart(line);
            else if (line.contains(" ENCOUNTER_END")) processEncounterEnd(line);
        }
    }

    /**
     * Processes the start of the Encounter.
     *
     * @param line The encounter start data.
     */
    private void processEncounterStart (String line) {
        Scanner s = new Scanner(line);
        s.useDelimiter(",");
        for (int i = 0; s.hasNext(); i++) {
            switch (i) {
                case 0 -> {
                    String date = s.next().replaceAll("  ENCOUNTER_START", ""); // todo: Timezone?
                    String offset;
                    if (date.contains("-")) offset = "-";
                    else offset = "\\+";
                    String zone = offset + "0" + date.replaceAll(".+" + offset, "");
                    date = date.replaceAll("-\\d", "");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/dd/yyyy HH:mm:ss.SSS").withZone(ZoneId.of(zone)); // todo: 2 digit dates?
                    LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
                    encounterStartMillis = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
                }
                case 2 -> enemyName = s.next().replace("\"", "");
                default -> s.next();
            }
        }
        s.close();
    }

    /**
     * Processes the end of the Encounter.
     *
     * @param line The encounter end data.
     */
    private void processEncounterEnd (String line) {
        Scanner s = new Scanner(line);
        s.useDelimiter(" ");
        String date = s.next() + " " + s.next().replaceAll("-\\d", ""); // todo: Timezone?
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/dd/yyyy HH:mm:ss.SSS"); // todo: 2 digit dates?
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
        encounterEndMillis = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    /**
     * An accessor method for the number of events contained in this encounter.
     *
     * @return The number of entries listed in this encounter.
     */
    public int eventCount () {
        return entries.size();
    }

    /**
     * Accessor method for the name of the enemy in this encounter.
     *
     * @return The enemy in this encounter.
     */
    public String getEnemyName () {
        return enemyName;
    }

    /**
     * Accessor method for the time that this encounter occurred.
     *
     * @return The date/time of this encounter in millis since epoch.
     */
    public long getEncounterStartMillis () {
        return encounterStartMillis;
    }

    /**
     * Prints a summary of this Encounter to System.out.
     */
    public void summarize () {
        System.out.println(" ==== Encounter Summary ==== ");
        System.out.println("Entries: " + entries.size());
        long healing = 0;
        long overhealing = 0;
        long total = 0;
        long damageTaken = 0;
        for (LogEntry e : entries) {
            if (e instanceof HealAbsorbEntry heal) {
                total += heal.getTotalHeal();
                healing += heal.getHeal();
                overhealing += heal.getOverheal();
            } else if (e instanceof HealEntry heal) {
                healing += heal.getHeal();
                overhealing += heal.getOverheal();
                total += heal.getTotalHeal();
            } else if (e instanceof DamageTakenEntry damage) {
                damageTaken += damage.damageTaken();
            }
        }
        System.out.println("Healing: " + NumberFormat.getInstance().format(healing));
        System.out.println("Overheal: " + NumberFormat.getInstance().format(overhealing));
        System.out.println("Total Healing: " + NumberFormat.getInstance().format(total));
        System.out.println("Damage Taken: " + NumberFormat.getInstance().format(damageTaken));
        System.out.println("Duration: " + (entries.getLast().getTime() - entries.getFirst().getTime()) / 1000 + "s");
        System.out.println(" ==== End Summary ==== ");
    }

    /**
     * Generates a healing graph using the data contained in this encounter.
     *
     * @return A graph object that represents this encounter's healing.
     */
    public Graph graph () {
        return graph(1000);
    }

    /**
     * Generates a healing graph using the data contained in this encounter.
     *
     * @param timeStep The amount of time in millis to consider for each 'bar.'
     * @return A graph object that represents this encounter's healing.
     */
    public Graph graph (long timeStep) {
        return graph(timeStep, (1000000 * 3));
    }

    /**
     * Generates a healing graph using the data contained in this encounter.
     *
     * @param timeStep The amount of time in millis to consider for each 'bar.'
     * @param scale The amount of healing that goes into a single block.
     * @return A graph object that represents this encounter's healing.
     */
    public Graph graph (long timeStep, long scale) {
        Graph toReturn = new Graph();
        for (int i = 0; i < entries.size(); i++) {
            LogEntry entry = entries.get(i);
            long windowStart = entry.getTime();
            long windowTotalHealing = 0;
            long windowHealing = 0;
            long windowDamage = 0;
            while (i < entries.size() && entries.get(i).getTime() < windowStart + timeStep) {
                if (entries.get(i) instanceof DamageTakenEntry damage)
                    windowDamage += damage.damageTaken();
                else {
                    windowTotalHealing += ((HealEntry) entries.get(i)).getTotalHeal();
                    windowHealing += ((HealEntry) entries.get(i)).getHeal();
                }
                i++;
            }
            toReturn.addColumn(windowTotalHealing / scale, windowHealing / scale, windowDamage / scale);
        }
        return toReturn;
    }

    /**
     * Calculates the healing from a specific caster within the given time window.
     *
     * @param casterName The name of the caster to query for.
     * @param startTime  The time to start at.
     * @param length     The amount of time to look after the start time.
     */
    public long queryHealingByCaster (String casterName, long startTime, long length) {
        long total = 0;
        for (LogEntry log : entries) {
            // Logs are sorted by time by construction. So we can safely stop searching once we break past the search bounds.
            if (log.getTime() > (startTime + encounterStartMillis) + length) break;
            if (log.getTime() < (startTime + entries.getFirst().getTime())) continue;
            if (log instanceof HealEntry heal)
                if (heal.getCaster().contains(casterName))
                    total += heal.getHeal() + heal.getOverheal();
        }
        return total;
    }

    /**
     * Calculates the healing from a specific spell within the given time window.
     *
     * @param spellName The name of the spell to query for.
     * @param startTime The time to start at.
     * @param length    The amount of time to look after the start time.
     */
    public long queryHealingBySpell (String spellName, long startTime, long length) {
        return queryHealingBySpell(Collections.singleton(spellName), startTime, length);
    }

    /**
     * Calculates the healing from a specific spells within the given time window.
     *
     * @param spellNames The name of the spell to query for.
     * @param startTime  The time to start at.
     * @param length     The amount of time to look after the start time.
     */
    public long queryHealingBySpell (Collection<String> spellNames, long startTime, long length) {
        long total = 0;
        for (LogEntry log : entries) {
            // Logs are sorted by time by construction. So we can safely stop searching once we break past the search bounds.
            if (log.getTime() > (startTime + encounterStartMillis) + length) break;
            if (log.getTime() < (startTime + encounterStartMillis))
                continue;
            if (log instanceof HealEntry heal)
                if (spellNames.contains(heal.getSpellName()))
                    total += heal.getHeal() + heal.getOverheal();
        }
        return total;
    }

    /**
     * The amount of time this encounter takes in milliseconds.
     *
     * @return The number of milliseconds that transpire in this encounter.
     */
    public long encounterLengthMillis () {
        return encounterEndMillis - encounterStartMillis;
    }
}
