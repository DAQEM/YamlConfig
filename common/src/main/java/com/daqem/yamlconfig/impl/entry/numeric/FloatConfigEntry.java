package com.daqem.yamlconfig.impl.entry.numeric;

import com.daqem.yamlconfig.api.entry.numeric.IFloatConfigEntry;
import com.daqem.yamlconfig.impl.entry.numeric.BaseNumericConfigEntry;

public class FloatConfigEntry extends BaseNumericConfigEntry<Float> implements IFloatConfigEntry {

    public FloatConfigEntry(String key, Float defaultValue) {
        super(key, defaultValue);
    }

    public FloatConfigEntry(String key, Float defaultValue, Float minValue, Float maxValue) {
        super(key, defaultValue, minValue, maxValue);
    }
}
