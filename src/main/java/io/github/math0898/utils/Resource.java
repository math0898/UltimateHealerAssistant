package io.github.math0898.utils;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Defines a resource accessible to the UltimateHealerAssistant program such as spell icons, player profile icons, and
 * item icons.
 *
 * @param name The name of this resource.
 * @param path The location of the image resource.
 * @param lastUpdated When this resource was last grabbed from Blizzard's server.
 * @param type The type of resource that this is.
 *
 * @author Sugaku
 */
@JsonSerialize
public record Resource (String name, String path, long lastUpdated, ResourceTypes type) { }
