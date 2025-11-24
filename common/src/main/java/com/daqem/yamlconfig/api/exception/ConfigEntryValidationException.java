package com.daqem.yamlconfig.api.exception;

/**
 * Thrown when a configuration entry fails validation.
 */
public class ConfigEntryValidationException extends YamlConfigException {

    private final String key;

    /**
     * Creates a new exception.
     *
     * @param key     The key of the entry that failed validation.
     * @param message The error message.
     */
    public ConfigEntryValidationException(String key, String message) {
        super(message);
        this.key = key;
    }

    /**
     * Gets the key of the entry that failed validation.
     *
     * @return The key.
     */
    public String getKey() {
        return key;
    }
}
