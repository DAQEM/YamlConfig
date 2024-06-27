package com.daqem.yamlconfig.impl.entry.map.numeric;

import com.daqem.yamlconfig.api.entry.map.numeric.IDoubleMapConfigEntry;

import java.util.Map;

public class DoubleMapConfigEntry extends BaseNumericMapConfigEntry<Double> implements IDoubleMapConfigEntry {

    public DoubleMapConfigEntry(String key, Map<String, Double> defaultValue) {
        super(key, defaultValue);
    }

    public DoubleMapConfigEntry(String key, Map<String, Double> defaultValue, int minLength, int maxLength) {
        super(key, defaultValue, minLength, maxLength);
    }

    public DoubleMapConfigEntry(String key, Map<String, Double> defaultValue, int minLength, int maxLength, Double minValue, Double maxValue) {
        super(key, defaultValue, minLength, maxLength, minValue, maxValue);
    }
}
