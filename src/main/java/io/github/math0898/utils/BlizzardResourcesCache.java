package io.github.math0898.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import suga.engine.GameEngine;
import suga.engine.logger.Level;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
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
        File cacheFolder = new File("./cache/");
        if (!cacheFolder.exists() && !cacheFolder.mkdirs()) {
            GameEngine.getLogger().log("Failed to make cache folder.", Level.WARNING);
            return;
        }
        File registry = new File("./cache/registry.json");
        if (!registry.exists()) {
            try {
                registry.createNewFile();
            } catch (Exception exception) {
                GameEngine.getLogger().log("Failed to make cache registry file.", Level.WARNING);
                GameEngine.getLogger().log(exception);
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<Resource> foundResources = mapper.readValue(registry, new TypeReference<>() {});
            for (Resource r : foundResources)
                resourcesMap.put(r.name(), r);
        } catch (Exception exception) {
            GameEngine.getLogger().log("Failed read registry file.", Level.WARNING);
            GameEngine.getLogger().log(exception);
        }
    }

    /**
     * Accessor method to the active BlizzardResourcesCache.
     *
     * @return The current BlizzardResources cache.
     */
    public static BlizzardResourcesCache getInstance () {
        if (instance == null) instance = new BlizzardResourcesCache();
        return instance;
    }

    /**
     * Saves the given image to the disc at the given location.
     *
     * @param image The image to save.
     * @param path The path to save the image at.
     */
    private void saveImage (BufferedImage image, String path) {
        File file = new File(path);
        if (file.exists()) if (!file.delete()) GameEngine.getLogger().log("Failed to delete old cache file " + path, Level.WARNING);
        try {
            ImageIO.write(image, "png", file);
        } catch (Exception exception) {
            GameEngine.getLogger().log(exception);
        }
    }

    /**
     * Updates the registry file with resources contained in the resources Map.
     */
    private void updateRegistry () {
        File file = new File("./cache/registry.json");
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        Resource[] list = resourcesMap.values().toArray(new Resource[0]);
        try (FileWriter writer = new FileWriter(file)) {
            String str = mapper.writeValueAsString(list);
            writer.write(str);
        } catch (Exception exception) {
            GameEngine.getLogger().log(exception);
        }
    }

    /**
     * Use when a spell icon has been requested that is either not in the cache, the one in the cache is out of date,
     * or force a refresh of the SpellIcon. This will freeze the thread as it pings Blizzard's server.
     *
     * @param resourceName The resource name of the icon being requested.
     */
    public BufferedImage requestSpellResource (String resourceName) {
        BufferedImage image = BlizzardAPIHelper.getInstance().requestSpellIcon(Long.parseLong(resourceName));
        resourcesMap.put(resourceName, new Resource(resourceName, "./cache/" + resourceName + ".png", System.currentTimeMillis(), ResourceTypes.SPELL_ICONS));
        saveImage(image, "./cache/" + resourceName + ".png");
        updateRegistry();
        return image;
    }

    /**
     * Use when a player icon has been requested that is either not in the cache, the one in the cache is out of date,
     * or force a refresh of the PlayerIcon. This will freeze the thread as it pings Blizzard's server.
     *
     * @param resourceName The resource name of the icon being requested.
     */
    public BufferedImage requestPlayerIconResource (String resourceName) {
        BufferedImage image = BlizzardAPIHelper.getInstance().requestPlayerIcon(Utils.parseRealm(resourceName), Utils.parseCharName(resourceName));
        resourcesMap.put(resourceName, new Resource(resourceName, "./cache/" + resourceName + ".png", System.currentTimeMillis(), ResourceTypes.PLAYER_ICONS));
        saveImage(image, "./cache/" + resourceName + ".png");
        updateRegistry();
        return image;
    }

    /**
     * Grabs the request resource from the cache. This will access the BlizzardAPI if the resource is stale or missing.
     *
     * @param resourceName The name of the resource to grab.
     * @param type The type of that is being grabbed. This is used to help populate resources if they're missing.
     * @return The resource in the form of an image.
     */
    public BufferedImage loadResource (String resourceName, ResourceTypes type) {
        Resource resource = resourcesMap.get(resourceName);
        boolean expired = false;
        if (resource != null)
            if (resource.lastUpdated() + resource.type().getExpiration() < System.currentTimeMillis())
                expired = true;
        if (resource == null || expired) {
            return switch (type) {
                default -> null;
                case SPELL_ICONS -> requestSpellResource(resourceName);
                case PLAYER_ICONS -> requestPlayerIconResource(resourceName);
            };
        }
        try {
            return ImageIO.read(new File(resource.path()));
        } catch (Exception exception) {
            GameEngine.getLogger().log(exception);
            return null;
        }
    }
}
