package io.github.math0898.processing.logentries;

/**
 * Used to quickly and easily categorize unit types.
 *
 * @author Sugaku
 */
public enum UnitTypes {

    /**
     * Creatures have the most complicated ids with the 'Creature-0-3782-2769-16374-235737-00015C9021' pattern.
     */
    CREATURE,

    /**
     * Pets are not id to creatures for some reason => 'Pet-0-3782-2769-16374-417-02054157F1' pattern.
     */
    PET,

    /**
     * Players have an id that follows the 'Player-11-0E607EF6' pattern.
     */
    PLAYER;

    /**
     * Quickly determines the type of unit the given id belongs to.
     *
     * @param id The id to investigate.
     * @return The enum value for the UnitType.
     */
    public static UnitTypes getUnitType (String id) {
        char[] array = id.toCharArray();
        if (array[1] == 'l') return PLAYER;
        else if (array[0] == 'C') return CREATURE;
        return PET;
    }
}
