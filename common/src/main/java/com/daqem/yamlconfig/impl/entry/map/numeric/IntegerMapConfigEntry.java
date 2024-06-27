package com.daqem.yamlconfig.impl.entry.map.numeric;

import com.daqem.yamlconfig.api.entry.map.numeric.IIntegerMapConfigEntry;

import java.util.Map;

public class IntegerMapConfigEntry extends BaseNumericMapConfigEntry<Integer> implements IIntegerMapConfigEntry {

    public IntegerMapConfigEntry(String key, Map<String, Integer> defaultValue) {
        super(key, defaultValue);
    }

    public IntegerMapConfigEntry(String key, Map<String, Integer> defaultValue, int minLength, int maxLength) {
        super(key, defaultValue, minLength, maxLength);
    }

    public IntegerMapConfigEntry(String key, Map<String, Integer> defaultValue, int minLength, int maxLength, Integer minValue, Integer maxValue) {
        super(key, defaultValue, minLength, maxLength, minValue, maxValue);
    }
}
