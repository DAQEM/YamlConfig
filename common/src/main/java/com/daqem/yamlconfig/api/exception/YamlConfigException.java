package com.daqem.yamlconfig.api.exception;

/**
 * Base exception for YamlConfig related errors.
 */
public class YamlConfigException extends RuntimeException {

    /**
     * Creates a new exception.
     *
     * @param message The error message.
     */
    public YamlConfigException(String message) {
        super(message);
    }
}
