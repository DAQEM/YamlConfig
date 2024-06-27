package com.daqem.yamlconfig.impl.entry;

import com.daqem.yamlconfig.api.entry.IFloatConfigEntry;

public class FloatConfigEntry extends BaseNumericConfigEntry<Float> implements IFloatConfigEntry {

    public FloatConfigEntry(String key, Float defaultValue) {
        super(key, defaultValue);
    }

    public FloatConfigEntry(String key, Float defaultValue, Float minValue, Float maxValue) {
        super(key, defaultValue, minValue, maxValue);
    }
}
