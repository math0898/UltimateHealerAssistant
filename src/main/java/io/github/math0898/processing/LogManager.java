package io.github.math0898.processing;

import suga.engine.GameEngine;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * The LogManager handles all the encounters listed in the currently loaded log file. There are some helpful mechanics
 * in this class such as picking and highlighting a specific encounter.
 *
 * @author Suagku
 */
public class LogManager {

    /**
     * The active LogManager.
     */
    private static LogManager instance = null;

    /**
     * Some systems of UltimateHealerAssistant highlights and pushes a single encounter to the forefront. This is the
     * index of that encounter.
     */
    private int highlightedEncounterIndex = 0;

    /**
     * A list of encounters found in this log file.
     */
    private final List<Encounter> encounters = new ArrayList<>();

    /**
     * Creates a new LogManager.
     */
    private LogManager () {

    }

    /**
     * Accessor method for the active LogManager instance.
     *
     * @return The active LogManager.
     */
    public static LogManager getInstance () {
        if (instance == null) instance = new LogManager();
        return instance;
    }

    /**
     * Accessor method for the given encounter.
     *
     * @param index The index of the encounter to access.
     * @return The encounter at the given index.
     */
    public Encounter getEncounter (int index) {
        return encounters.get(index);
    }

    /**
     * Accessor method for the number of encounters contained in the LogManager.
     *
     * @return The number of encounters in the loaded file. If no file is loaded this will return 0.
     */
    public int getEncounterCount () {
        return encounters.size();
    }

    /**
     * Changes the index of the highlighted encounter by the given value. Bounds checking is done.
     *
     * @param delta The amount to shift the highlighted encounter by.
     */
    public void changeHighlightedEncounter (int delta) {
        highlightedEncounterIndex = Math.min(Math.max(highlightedEncounterIndex + delta, 0), encounters.size() - 1);
    }

    /**
     * Accessor for the highlighted encounter. Changed by {@link #changeHighlightedEncounter(int)}
     *
     * @return The highlighted encounter.
     */
    public Encounter getHighlightedEncounter () {
        return encounters.get(highlightedEncounterIndex);
    }

    /**
     * Accessor for the highlighted encounter index.
     *
     * @return The highlighted encounter.
     */
    public int getHighlightedEncounterIndex () {
        return highlightedEncounterIndex;
    }

    /**
     * Accessor method for a dump of all the encounters loaded.
     *
     * @return All currently loaded encounters.
     */
    public List<Encounter> getAllEncounters () {
        return encounters;
    }

    /**
     * Loads the given file into the LogManager.
     *
     * @param logFile The log file to process.
     */
    public void processFile (String logFile) { // todo: Simplify.
        try (InputStream inputStream = new FileInputStream(logFile)) {
            StringBuilder builder = new StringBuilder();
            boolean encounter_start = false;
            String data = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8).lines().collect(Collectors.joining("\n"));
            String[] lines = data.split("\n");
            try (ExecutorService executor = new ThreadPoolExecutor(4, 8, 5, TimeUnit.SECONDS, new LinkedBlockingQueue<>())) {
                List<Future<?>> futures = new ArrayList<>();
                for (int i = 0; i < lines.length; i++) {
                    String line = lines[i];
                    if (line.contains("ENCOUNTER_END")) {
                        builder.append("\n").append(line);
                        String finalDataString = builder.toString();
                        Runnable thread = () -> {
                            Encounter encounter = new Encounter(finalDataString);
                            encounter.process();
                            if (encounter.eventCount() > 50 && encounter.encounterLengthMillis() > 3000)
                                encounters.add(encounter);
                        };
                        futures.add(executor.submit(thread));
                        encounter_start = false;
                        builder = new StringBuilder();
                    } else if (line.contains("ENCOUNTER_START")) encounter_start = true;
                    if (encounter_start) builder.append("\n").append(line);
                }
                for (Future<?> f : futures) f.get(); // Wait for all to finish.
                encounters.sort((e1, e2) -> Math.toIntExact(e1.getEncounterStartMillis() - e2.getEncounterStartMillis()));
            } catch (Exception e) {
                GameEngine.getLogger().log(e);
            }
        } catch (Exception e) {
            GameEngine.getLogger().log(e);
        }
    }
}
