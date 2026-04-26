package io.github.math0898;

import io.github.math0898.processing.Encounter;
import io.github.math0898.processing.LogManager;
import io.github.math0898.processing.logentries.LogEntry;
import suga.engine.GameEngine;
import suga.engine.logger.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple scripting class to run one-time processing and get results. Ideas prototyped here may make their way into
 * the main program.
 *
 * @author Suagku
 */
public class Scripting {

    private static final String FILE_PATH = "/home/sugaku/BlizzardGames/World of Warcraft/_retail_/Logs/warcraftlogsarchive/Archive-WoWCombatLog-041626_185655.txt";

    /**
     * The main execution point of the scripting process.
     */
    public static void main (String[] args) {
        alnsight();
    }

    /**
     * Alnsight scripting.
     */
    private static void alnsight () {

        List<List<String>> expectedSoakers = Arrays.asList(
                Arrays.asList("Emagonadye", "Seranite", "Auriprodeo", "Heiderich", "Isrez", "Skullzpal", "Deacslock", "Nillath", "Sheenzy", "Steinlick"),
                Arrays.asList("Uwuhooves", "Syudou"),
                Arrays.asList("Silmeriel", "Syna", "Berzx", "Holyføx", "Pawlicker", "Syudou", "Chwamz", "Tinderhulk", "Uwuhooves", "Windzaster"),
                Arrays.asList("Sheenzy", "Seranite"),
                Arrays.asList("Emagonadye", "Seranite", "Auriprodeo", "Heiderich", "Isrez", "Skullzpal", "Deacslock", "Nillath", "Sheenzy", "Steinlick"),
                Arrays.asList("Emagonadye", "Seranite", "Auriprodeo", "Heiderich", "Isrez", "Skullzpal", "Deacslock", "Nillath", "Sheenzy", "Steinlick"),
                Arrays.asList("Uwuhooves", "Syudou"),
                Arrays.asList("Silmeriel"),
                Arrays.asList("") // Overflow protection
        );

        String FILE_PATH = "/home/sugaku/BlizzardGames/World of Warcraft/_retail_/Logs/warcraftlogsarchive/Archive-WoWCombatLog-041626_185655.txt";
        LogManager.getInstance().processFile(FILE_PATH);
        int i = 0;
        for (Encounter e : LogManager.getInstance().getAllEncounters()) {
            i++;
            List<LogEntry> culledEntries = new ArrayList<>();
            GameEngine.getLogger().log(e.getEnemyName() + " ---- (Wipe: " + i + ")");
            for (LogEntry entry : e.getRawEntries()) {
                String str = entry.toString();
                if (str.contains("Alnsight") && str.contains("SPELL_AURA_APPLIED") && str.contains("Chimaerus") && str.contains("DEBUFF"))
                    culledEntries.add(entry);
            }

            // Initial setup for checking soaks.
            int group = 0;
            long GROUP_EPSILON_MILLIS = 1000L * 15;
            long currentGroupStartMillis = 0;
            try {
                currentGroupStartMillis = culledEntries.getFirst().getTime();
            } catch (Exception exception) {
                GameEngine.getLogger().log("No soak found in this encounter.", Level.WARNING);
                continue;
            }
            List<String> currentGroup = new ArrayList<>(expectedSoakers.get(group));

            for (LogEntry entry : culledEntries) {
                long logTime = entry.getTime();

                // This current event happend in the next group. Print the result and shift frame.
                if (logTime > currentGroupStartMillis + GROUP_EPSILON_MILLIS) {
                    group++;
                    currentGroupStartMillis = logTime;
                    if (currentGroup.isEmpty()) GameEngine.getLogger().log("Set " + group + ": All players soaked!", Level.INFO, false);
                    else {
                        StringBuilder builder = new StringBuilder();
                        for (String s : currentGroup)
                            builder.append(s).append(", ");
                        GameEngine.getLogger().log("Set " + group + ": Missed Soak: " + builder, Level.WARNING);
                    }
                    currentGroup = new ArrayList<>(expectedSoakers.get(group));
                }

                // This current event happened during this group. Note the frame has already shifted if it needs to have been.
                if (!currentGroup.removeIf((s -> entry.toString().contains(s))))
                    GameEngine.getLogger().log("Set " + group + ": Did " + entry + " soak out of turn?", Level.WARNING);
            }
        }
    }
}
