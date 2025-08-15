package io.github.math0898.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import suga.engine.GameEngine;
import suga.engine.logger.Level;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
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
        File cacheFolder = new File("./cache/");
        if (!cacheFolder.exists() && !cacheFolder.mkdirs()) {
            GameEngine.getLogger().log("Failed to make cache folder.", Level.WARNING);
            return;
        }
        File registry = new File("./cache/spells.json");
        if (!registry.exists()) {
            try {
                registry.createNewFile();
            } catch (Exception exception) {
                GameEngine.getLogger().log("Failed to make database file.", Level.WARNING);
                GameEngine.getLogger().log(exception);
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<SpellDetails> foundResources = mapper.readValue(registry, new TypeReference<>() {});
            for (SpellDetails details : foundResources)
                spells.put(details.id(), details);
        } catch (Exception exception) {
            GameEngine.getLogger().log("Failed read database file.", Level.WARNING);
            GameEngine.getLogger().log(exception);
        }
    }

    /**
     * Updates the database file with details contained in the spells map.
     */
    private void updateDatabase () {
        File file = new File("./cache/spells.json");
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        SpellDetails[] list = spells.values().toArray(new SpellDetails[0]);
        try (FileWriter writer = new FileWriter(file)) {
            String str = mapper.writeValueAsString(list);
            writer.write(str);
        } catch (Exception exception) {
            GameEngine.getLogger().log(exception);
        }
    }

    /**
     * Accessor method for the active SpellDatabase instance.
     *
     * @return The active SpellDatabase.
     */
    public static SpellDatabase getInstance () {
        if (instance == null) instance = new SpellDatabase();
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
        boolean expired = false;
        if (details != null) // Perhaps change the expiration time to be tied to its own value.
            if (details.lastGrabbed() + ResourceTypes.SPELL_ICONS.getExpiration() < System.currentTimeMillis())
                expired = true;
        if (details == null || expired) {
            details = BlizzardAPIHelper.getInstance().requestSpellDetails(id);
            if (details == null) return null;
            spells.put(id, details);
            updateDatabase();
        }
        return details;
    }
}
