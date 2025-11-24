package com.daqem.yamlconfig.api.config.entry.numeric;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;

/**
 * Represents a numeric configuration entry.
 *
 * @param <T> The numeric type.
 */
public interface INumericConfigEntry<T extends Number & Comparable<T>> extends IConfigEntry<T> {

    /**
     * Gets the minimum allowed value.
     *
     * @return The minimum value.
     */
    T getMinValue();

    /**
     * Gets the maximum allowed value.
     *
     * @return The maximum value.
     */
    T getMaxValue();
}
