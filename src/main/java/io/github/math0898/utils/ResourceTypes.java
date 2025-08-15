package io.github.math0898.utils;

/**
 * An enum to specify resource types as well as static configuration options for each.
 *
 * @author Sugaku
 */
public enum ResourceTypes {

    /**
     * The amount of time before player icons are considered stale and should be updated.
     */
    PLAYER_ICONS(1000L * 60 * 60 * 24),

    /**
     * The amount of time before spell icons are considered stale and should be updated.
     */
    SPELL_ICONS(1000L * 60 * 60 * 24 * 31),

    /**
     * Resources provided by UltimateHealerAssistant which never become stale.
     */
    STATIC_ICONS(Long.MAX_VALUE);

    /**
     * The amount of time before a cached resource should be grabbed from Blizzard's servers again.
     */
    private final long expiration;

    /**
     * Creates a new ResourceType with the given configuration options.
     *
     * @param expiration How long in millis before a resource is considered 'stale' and needs updated.
     */
    ResourceTypes (long expiration) {
        this.expiration = expiration;
    }

    /**
     * Accessor method for the expiration time of this resource type.
     *
     * @return The amount of time before this resource type expires.
     */
    public long getExpiration () {
        return expiration;
    }

    /**
     * Locates the ResourceTypes that corresponds to the given string value.
     *
     * @param val The value to search for.
     */
    public static ResourceTypes getType (String val) {
        for (ResourceTypes type : ResourceTypes.values())
            if (val.equalsIgnoreCase(type.toString()))
                return type;
        return SPELL_ICONS;
    }
}
