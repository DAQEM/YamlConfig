package com.daqem.yamlconfig.api.config.entry.map;

import java.util.Map;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;

/**
 * Represents a map configuration entry.
 *
 * @param <T> The type of values in the map.
 */
public interface IMapConfigEntry<T> extends IConfigEntry<Map<String, T>> {

    /**
     * Gets the minimum size of the map.
     *
     * @return The minimum size.
     */
    int getMinLength();

    /**
     * Gets the maximum size of the map.
     *
     * @return The maximum size.
     */
    int getMaxLength();
}
