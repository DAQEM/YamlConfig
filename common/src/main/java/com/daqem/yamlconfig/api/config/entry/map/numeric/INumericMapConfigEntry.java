package com.daqem.yamlconfig.api.config.entry.map.numeric;

import com.daqem.yamlconfig.api.config.entry.map.IMapConfigEntry;

/**
 * Represents a map of numeric values configuration entry.
 *
 * @param <T> The numeric type.
 */
public interface INumericMapConfigEntry<T extends Number & Comparable<T>> extends IMapConfigEntry<T> {

    /**
     * Gets the minimum allowed value for elements in the map.
     *
     * @return The minimum value.
     */
    T getMinValue();

    /**
     * Gets the maximum allowed value for elements in the map.
     *
     * @return The maximum value.
     */
    T getMaxValue();
}
