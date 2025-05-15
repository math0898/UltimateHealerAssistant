package io.github.math0898.processing;

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
            if (line.contains(" SPELL_HEAL,")) entries.add(new HealEntry(line));
            else if (line.contains(" SPELL_PERIODIC_HEAL,")) entries.add(new HealEntry(line)); // todo: Almost identical data but distinction would be nice.
        }
    }

    /**
     * Prints a summary of this Encounter to System.out.
     */
    public void summarize () {
        System.out.println(" ==== Encounter Summary ==== ");
        System.out.println("Entries: " + entries.size());
        long healing = 0;
        long overhealing = 0;
        for (LogEntry e : entries) {
            if (e.getType().equals(EntryType.HEALING))
                if (e instanceof HealEntry heal) {
                    healing += heal.getHeal();
                    overhealing += heal.getOverheal();
                }
        }
        System.out.println("Healing: " + NumberFormat.getInstance().format(healing));
        System.out.println("Overheal: " + NumberFormat.getInstance().format(overhealing));
        System.out.println("Total Healing: " + NumberFormat.getInstance().format(healing + overhealing));
        System.out.println(" ==== End Summary ==== ");
    }
}
