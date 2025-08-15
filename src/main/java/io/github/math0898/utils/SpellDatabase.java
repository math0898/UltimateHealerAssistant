package io.github.math0898.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * The SpellDatabase contains the details of spells including their names and descriptions.
 *
 * @author Sugaku
 */
public class SpellDatabase {

    /**
     * The active SpellDatabase.
     */
    private static SpellDatabase instance;

    /**
     * The spells contained in the SpellDatabase by id.
     */
    private final Map<Long, SpellDetails> spells = new HashMap<>();

    /**
     * Creates a new SpellDatabase.
     */
    private SpellDatabase () {
        // todo: Implement.
    }

    /**
     * Accessor method for the active SpellDatabase instance.
     *
     * @return The active SpellDatabase.
     */
    public static SpellDatabase getInstance () {
        return instance;
    }

    /**
     * Gets the details about a spell from the database. This will ping Blizzard's API if a spell's data is stale or not
     * present.
     *
     * @param id The id of the spell to grab.
     * @return SpellDetails about the requested spell or null.
     */
    public SpellDetails getDetails (long id) {
        SpellDetails details = spells.get(id);
        // todo: Implement refreshing and grabbing new spells.
        return details;
    }
}
