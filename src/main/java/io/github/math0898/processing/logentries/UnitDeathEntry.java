package io.github.math0898.processing.logentries;

/**
 * A UnitDeathEntry encapsulates the UNIT_DEATH event in raw log files.
 *
 * @author Sugaku
 */
public class UnitDeathEntry extends LogEntry {

    /**
     * The type of unit that died in this UNIT_DEATH event.
     */
    protected UnitTypes unitType;

    /**
     * The name of the unit that died.
     */
    protected String unitName;

    /**
     * Creates this HealEntry from the given data.
     *
     * @param data The raw data to create this HealEntry from.
     */
    public UnitDeathEntry (String data) {
        super(data);
        init();
    }

    /**
     * Initializes the data contained within this entry.
     */
    protected void init () {
        String[] lines = data.split(",");
//        [INFO] UNIT_DIED 0 = > 7/17/2025 20:54:37.041-5  UNIT_DIED
//        [INFO] UNIT_DIED 1 = > 0000000000000000
//        [INFO] UNIT_DIED 2 = > nil
//        [INFO] UNIT_DIED 3 = > 0x80000000
//        [INFO] UNIT_DIED 4 = > 0x80000000
//        [INFO] UNIT_DIED 5 = > Player-3675-08BD5709
//        [INFO] UNIT_DIED 6 = > "Sarinias-MoonGuard-US"
//        [INFO] UNIT_DIED 7 = > 0x512
//        [INFO] UNIT_DIED 8 = > 0x80000000
//        [INFO] UNIT_DIED 9 = > 0
        unitType = UnitTypes.getUnitType(lines[5]);
        unitName = lines[6];
    }

    /**
     * Accessor method for the type of unit that this is.
     *
     * @return The UnitTypes.
     */
    public UnitTypes getUnitType () {
        return unitType;
    }

    /**
     * Accessor method for the name that this is.
     *
     * @return The unit's name.
     */
    public String getUnitName () {
        return unitName;
    }

    /**
     * Prints a summary of this entry to System.out.
     */
    public void summarize () {
        System.out.println(unitName + " died at " + getTime());
    }
}
