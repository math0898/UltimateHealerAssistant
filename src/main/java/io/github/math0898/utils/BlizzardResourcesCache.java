package io.github.math0898.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Used as an additional layer between this program and Blizzard's API to save resources on disc.
 *
 * @author Sugaku
 */
public class BlizzardResourcesCache {

    /**
     * The amount of time before player icons are considered stale and should be updated.
     */
    private static final long PLAYER_ICONS = 1000L * 60 * 60 * 24;

    /**
     * The amount of time before spell icons are considered stale and should be updated.
     */
    private static final long SPELL_ICONS = 1000L * 60 * 60 * 24 * 31;

    /**
     * A collection of known resources by their name.
     */
    private final Map<String, String> resourcesMap = new HashMap<>();

    /**
     * The active BlizzardResourceCache.
     */
    private static BlizzardResourcesCache instance = null;

    /**
     * Creates a new BlizzardResourceCache.
     */
    private BlizzardResourcesCache () {
        // todo: Implement.
    }

    /**
     *
     */
    public static BlizzardResourcesCache getInstance () {
        if (instance == null) instance = new BlizzardResourcesCache();
        return instance;
    }
}
