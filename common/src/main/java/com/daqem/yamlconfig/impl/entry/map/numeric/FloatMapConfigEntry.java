package com.daqem.yamlconfig.impl.entry.map.numeric;

import com.daqem.yamlconfig.api.entry.map.numeric.IFloatMapConfigEntry;

import java.util.Map;

public class FloatMapConfigEntry extends BaseNumericMapConfigEntry<Float> implements IFloatMapConfigEntry {

    public FloatMapConfigEntry(String key, Map<String, Float> defaultValue) {
        super(key, defaultValue);
    }

    public FloatMapConfigEntry(String key, Map<String, Float> defaultValue, int minLength, int maxLength) {
        super(key, defaultValue, minLength, maxLength);
    }

    public FloatMapConfigEntry(String key, Map<String, Float> defaultValue, int minLength, int maxLength, Float minValue, Float maxValue) {
        super(key, defaultValue, minLength, maxLength, minValue, maxValue);
    }
}
