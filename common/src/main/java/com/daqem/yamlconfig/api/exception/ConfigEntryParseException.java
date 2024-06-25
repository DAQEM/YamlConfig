package com.daqem.yamlconfig.api.exception;

public class ConfigEntryParseException extends YamlConfigException {

    private final String key;

    public ConfigEntryParseException(String key, String message, Throwable cause) {
        super(message, cause);
        this.key = key;
    }

    public ConfigEntryParseException(String key, String message) {
        super(message);
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
