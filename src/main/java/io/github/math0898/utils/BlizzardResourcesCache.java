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
     * A collection of known resources by their name.
     */
    private final Map<String, Resource> resourcesMap = new HashMap<>();

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
