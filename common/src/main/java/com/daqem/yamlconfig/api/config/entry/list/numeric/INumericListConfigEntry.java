package com.daqem.yamlconfig.api.config.entry.list.numeric;

import com.daqem.yamlconfig.api.config.entry.list.IListConfigEntry;

/**
 * Represents a list of numeric values configuration entry.
 *
 * @param <T> The numeric type.
 */
public interface INumericListConfigEntry<T extends Number> extends IListConfigEntry<T> {

    /**
     * Gets the minimum allowed value for elements in the list.
     *
     * @return The minimum value.
     */
    T getMinValue();

    /**
     * Gets the maximum allowed value for elements in the list.
     *
     * @return The maximum value.
     */
    T getMaxValue();
}
