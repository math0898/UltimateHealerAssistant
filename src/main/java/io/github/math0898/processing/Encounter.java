package io.github.math0898.processing;

import io.github.math0898.processing.logentries.HealAbsorbEntry;
import io.github.math0898.processing.logentries.HealEntry;
import io.github.math0898.processing.logentries.LogEntry;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * An encounter represents all the data contained in a single encounter. This includes sums and results of multiple
 * events as well the raw data from the logs file.
 */
public class Encounter {

    /**
     * Raw log data included in this encounter.
     */
    private final String data;

    /**
     * An entire list of all the entries contained within this Encounter.
     */
    private List<LogEntry> entries = new ArrayList<>();

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
        Scanner s = new Scanner(data);
        while (s.hasNextLine()) {
            String line = s.nextLine();
            if (line.contains(" SPELL_HEAL_ABSORBED,")) entries.add(new HealAbsorbEntry(line));
            else if (line.contains(" SPELL_HEAL,")) entries.add(new HealEntry(line));
            else if (line.contains(" SPELL_PERIODIC_HEAL,")) entries.add(new HealEntry(line)); // todo: Almost identical data but distinction would be nice.
        }
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
     * Prints a summary of this Encounter to System.out.
     */
    public void summarize () {
        System.out.println(" ==== Encounter Summary ==== ");
        System.out.println("Entries: " + entries.size());
        long healing = 0;
        long overhealing = 0;
        long total = 0;
        for (LogEntry e : entries) {
            if (e.getType().equals(EntryType.HEALING)) {
                if (e instanceof HealAbsorbEntry heal) {
                    total += heal.getTotalHeal();
                    healing += heal.getHeal();
                    overhealing += heal.getOverheal();
                } else if (e instanceof HealEntry heal) {
                    healing += heal.getHeal();
                    overhealing += heal.getOverheal();
                    total += heal.getTotalHeal();
                }
            }
        }
        System.out.println("Healing: " + NumberFormat.getInstance().format(healing));
        System.out.println("Overheal: " + NumberFormat.getInstance().format(overhealing));
        System.out.println("Total Healing: " + NumberFormat.getInstance().format(total));
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
        Graph toReturn = new Graph();
        for (int i = 0; i < entries.size(); i++) {
            long windowStart = entries.get(i).getTime();
            long windowHealing = ((HealEntry) entries.get(i)).getTotalHeal();
            i++;
            while (i < entries.size() && entries.get(i).getTime() < windowStart + timeStep) {
                windowHealing += ((HealEntry) entries.get(i)).getTotalHeal();
                i++;
            }
            toReturn.addColumn(windowHealing / (1000000 * 3));
        }
        return toReturn;
    }
}
