package com.daqem.yamlconfig.api.config.entry.list;

import java.util.List;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;

/**
 * Represents a list configuration entry.
 *
 * @param <T> The type of elements in the list.
 */
public interface IListConfigEntry<T> extends IConfigEntry<List<T>> {

    /**
     * Gets the minimum length of the list.
     *
     * @return The minimum length.
     */
    int getMinLength();

    /**
     * Gets the maximum length of the list.
     *
     * @return The maximum length.
     */
    int getMaxLength();
}
