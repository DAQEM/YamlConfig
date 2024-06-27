package com.daqem.yamlconfig.impl.entry.map.numeric;

import com.daqem.yamlconfig.api.entry.map.numeric.INumericMapConfigEntry;
import com.daqem.yamlconfig.api.exception.ConfigEntryValidationException;
import com.daqem.yamlconfig.impl.entry.map.BaseMapConfigEntry;

import java.util.Map;

public abstract class BaseNumericMapConfigEntry<T extends Number> extends BaseMapConfigEntry<T> implements INumericMapConfigEntry<T> {

    public BaseNumericMapConfigEntry(String key, Map<String, T> defaultValue) {
        super(key, defaultValue);
    }

    @Override
    public void validate(Map<String, T> value) throws ConfigEntryValidationException {

    }
}
