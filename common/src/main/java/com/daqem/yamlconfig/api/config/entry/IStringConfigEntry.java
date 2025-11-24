package com.daqem.yamlconfig.api.config.entry;

import java.util.List;

/**
 * Represents a string configuration entry.
 */
public interface IStringConfigEntry extends IConfigEntry<String> {

    /**
     * Gets the minimum length of the string.
     *
     * @return The minimum length.
     */
    int getMinLength();

    /**
     * Gets the maximum length of the string.
     *
     * @return The maximum length.
     */
    int getMaxLength();

    /**
     * Gets the regex pattern the string must match.
     *
     * @return The regex pattern, or null if none.
     */
    String getPattern();

    /**
     * Gets the list of valid values for the string.
     *
     * @return A list of valid strings, or null if none.
     */
    List<String> getValidValues();
}
