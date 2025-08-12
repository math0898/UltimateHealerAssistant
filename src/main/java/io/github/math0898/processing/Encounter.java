package io.github.math0898.processing;

import io.github.math0898.processing.logentries.*;
import io.github.math0898.utils.Utils;

import java.text.NumberFormat;
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
    private String data;

    /**
     * An entire list of all the entries contained within this Encounter.
     */
    private final List<LogEntry> entries = new ArrayList<>();

    /**
     * A list of deaths that happened during this encounter.
     */
    private final List<UnitDeathEntry> unitDeaths = new ArrayList<>();

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
        String[] lines = data.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (line.contains(" SPELL_HEAL_ABSORBED,")) entries.add(new HealAbsorbEntry(line));
            else if (line.contains(" SPELL_HEAL,")) entries.add(new HealEntry(line));
            else if (line.contains(" SPELL_PERIODIC_HEAL,")) entries.add(new HealEntry(line)); // todo: Almost identical data but distinction would be nice.
            else if (line.contains("  SPELL_PERIODIC_DAMAGE,")) entries.add(new DamageTakenEntry(line));
            else if (line.contains("  SPELL_DAMAGE,")) entries.add(new DamageTakenEntry(line));
            else if (line.contains(" ENCOUNTER_START")) processEncounterStart(line);
            else if (line.contains(" ENCOUNTER_END")) processEncounterEnd(line);
            else if (line.contains(" UNIT_DIED")) unitDeaths.add(new UnitDeathEntry(line));
        }
        data = null;
        unitDeaths.sort((u1, u2) -> {
            long val = u1.getTime() - u2.getTime();
            if (val > 0L) return 1;
            else if (val == 0) return 0;
            else return -1;
        });
    }

    /**
     * Processes the start of the Encounter.
     *
     * @param line The encounter start data.
     */
    private void processEncounterStart (String line) {
        String[] split = line.split(",");
        encounterStartMillis = Utils.millisFromLogTime(split[0]);
        enemyName = split[2].replace("\"", "");
    }

    /**
     * Processes the end of the Encounter.
     *
     * @param line The encounter end data.
     */
    private void processEncounterEnd (String line) {
        encounterEndMillis = Utils.millisFromLogTime(line.split(",")[0]);
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
                else if (entries.get(i) instanceof HealEntry healEntry) {
                    windowTotalHealing += healEntry.getTotalHeal();
                    windowHealing += healEntry.getHeal();
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
     * Calculates the healing from a specific caster at each moment from encounter start to encounter end.
     *
     * @param casterName The name of the caster to query for.
     * @param timeStep  The size of each time step.
     */
    public List<Long> queryHealingInstantsByCaster (String casterName, long timeStep) {
        List<Long> values = new ArrayList<>();
        int indexTracker = -1;
        for (LogEntry log : entries) {
            // Logs are sorted by time by construction. So we can safely move to the next window once we break past the current one.
            if (log.getTime() > encounterStartMillis + (timeStep * indexTracker)) {
                indexTracker++;
                values.add(0L);
            }
            if (log instanceof HealEntry heal) {
                if (heal.getCaster().contains(casterName))
                    values.set(indexTracker, values.get(indexTracker) + heal.getTotalHeal());
            }
        }
        List<Long> swap = new ArrayList<>(); // todo: This syncs it with the indicator but might not be correct.
        for (int i = 1; i < values.size(); i++)
            swap.add(values.get(i));
        return swap;
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
     * Calculates the healing from a specific spell at each moment from encounter start to encounter end.
     *
     * @param spellName The name of the spell to query for.
     * @param timeStep  The size of each time step.
     */
    public List<Long> queryHealingInstantsBySpell (String spellName, long timeStep) {
        List<Long> values = new ArrayList<>();
        int indexTracker = -1;
        for (LogEntry log : entries) {
            // Logs are sorted by time by construction. So we can safely move to the next window once we break past the current one.
            if (log.getTime() >= encounterStartMillis + (timeStep * indexTracker)) {
                indexTracker++;
                values.add(0L);
            }
            if (log instanceof HealEntry heal) {
                if (heal.getSpellName().contains(spellName))
                    values.set(indexTracker, values.get(indexTracker) + heal.getTotalHeal());
            }
        }
        List<Long> swap = new ArrayList<>(); // todo: This syncs it with the indicator but might not be correct.
        for (int i = 1; i < values.size(); i++)
            swap.add(values.get(i));
        return swap;
    }

    /**
     * Accessor method to a list of all the deaths that occurred during this event.
     *
     * @return The encounter death list.
     */
    public List<UnitDeathEntry> getUnitDeaths () {
        return unitDeaths;
    }

    /**
     * Queries the timeline for when this specific spell appears. Returns a list of values that may be empty.
     *
     * @param spellName The name of the spell to query for.
     */
    public List<Long> querySpellHealingInstances (String spellName) {
        List<Long> list = new ArrayList<>();
        for (LogEntry log : entries) {
            if (log instanceof HealEntry heal)
                if (spellName.equalsIgnoreCase(heal.getSpellName()))
                    list.add(log.getTime());
        }
        return list;
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
