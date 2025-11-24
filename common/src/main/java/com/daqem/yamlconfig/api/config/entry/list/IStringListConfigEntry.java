package com.daqem.yamlconfig.api.config.entry.list;

import java.util.List;

/**
 * Represents a list of strings configuration entry.
 */
public interface IStringListConfigEntry extends IListConfigEntry<String> {

    /**
     * Gets the regex pattern the strings in the list must match.
     *
     * @return The regex pattern, or null if none.
     */
    String getPattern();

    /**
     * Gets the list of valid values for the strings in the list.
     *
     * @return A list of valid strings, or null if none.
     */
    List<String> getValidValues();
}
