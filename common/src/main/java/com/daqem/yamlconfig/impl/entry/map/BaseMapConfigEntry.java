package com.daqem.yamlconfig.impl.entry.map;

import com.daqem.yamlconfig.api.entry.map.IMapConfigEntry;
import com.daqem.yamlconfig.api.exception.ConfigEntryValidationException;
import com.daqem.yamlconfig.impl.entry.BaseConfigEntry;

import java.util.Map;

public abstract class BaseMapConfigEntry<T> extends BaseConfigEntry<Map<String, T>> implements IMapConfigEntry<T> {

    private final int minLength;
    private final int maxLength;

    public BaseMapConfigEntry(String key, Map<String, T> defaultValue) {
        this(key, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public BaseMapConfigEntry(String key, Map<String, T> defaultValue, int minLength, int maxLength) {
        super(key, defaultValue);
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    @Override
    public int getMinLength() {
        return minLength;
    }

    @Override
    public int getMaxLength() {
        return maxLength;
    }

    @Override
    public void validate(Map<String, T> value) throws ConfigEntryValidationException {
        if (minLength != Integer.MIN_VALUE && value.size() < minLength) {
            throw new ConfigEntryValidationException(getKey(), "Map is too small. Expected at least " + minLength + " elements");
        }
        if (maxLength != Integer.MAX_VALUE && value.size() > maxLength) {
            throw new ConfigEntryValidationException(getKey(), "Map is too large. Expected at most " + maxLength + " elements");
        }
    }
}
