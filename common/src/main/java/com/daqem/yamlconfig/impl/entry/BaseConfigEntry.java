package com.daqem.yamlconfig.impl.entry;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.api.entry.IConfigEntry;
import com.daqem.yamlconfig.api.exception.ConfigEntryValidationException;

public abstract class BaseConfigEntry<T> implements IConfigEntry<T> {

    private final String key;
    private final T defaultValue;
    private T value;

    public BaseConfigEntry(String key, T defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public T getDefaultValue() {
        return defaultValue;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public void setValue(T value) {
        try {
            validate(value);
            this.value = value;
        } catch (ConfigEntryValidationException e) {
            YamlConfig.LOGGER.error("Failed to parse config entry: " + e.getKey());
            YamlConfig.LOGGER.error("Reason: " + e.getMessage());
            this.value = defaultValue;
        }
    }
}
