package io.github.math0898;

import io.github.math0898.processing.Encounter;
import io.github.math0898.processing.LogManager;
import io.github.math0898.processing.logentries.LogEntry;
import suga.engine.GameEngine;

/**
 * A simple scripting class to run one-time processing and get results. Ideas prototyped here may make their way into
 * the main program.
 *
 * @author Suagku
 */
public class Scripting {

    private static final String FILE_PATH = "/home/sugaku/BlizzardGames/World of Warcraft/_retail_/Logs/warcraftlogsarchive/Archive-WoWCombatLog-040226_191432.txt";

    /**
     * The main execution point of the scripting process.
     */
    public static void main (String[] args) {
        LogManager.getInstance().processFile(FILE_PATH);
        for (Encounter e : LogManager.getInstance().getAllEncounters()) {
            GameEngine.getLogger().log(e.getEnemyName() + " ---- ");
            for (LogEntry entry : e.getRawEntries()) {
                String str = entry.toString();
                if (str.contains("UNIT_DIED"))
                    GameEngine.getLogger().log(str);
            }
        }
    }
}
