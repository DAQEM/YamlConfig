package com.daqem.yamlconfig.api.config.entry.map;

import java.util.List;

/**
 * Represents a map of strings configuration entry.
 */
public interface IStringMapConfigEntry extends IMapConfigEntry<String> {

    /**
     * Gets the regex pattern the string values in the map must match.
     *
     * @return The regex pattern, or null if none.
     */
    String getPattern();

    /**
     * Gets the list of valid values for the strings in the map.
     *
     * @return A list of valid strings, or null if none.
     */
    List<String> getValidValues();
}
