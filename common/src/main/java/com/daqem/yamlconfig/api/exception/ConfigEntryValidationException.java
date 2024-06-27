package com.daqem.yamlconfig.api.exception;

public class ConfigEntryValidationException extends YamlConfigException {

    private final String key;

    public ConfigEntryValidationException(String key, String message) {
        super(message);
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
