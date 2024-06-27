package com.daqem.yamlconfig.impl.entry.numeric;

import com.daqem.yamlconfig.api.entry.numeric.IDoubleConfigEntry;
import com.daqem.yamlconfig.impl.entry.numeric.BaseNumericConfigEntry;

public class DoubleConfigEntry extends BaseNumericConfigEntry<Double> implements IDoubleConfigEntry {

    public DoubleConfigEntry(String key, Double defaultValue) {
        super(key, defaultValue);
    }

    public DoubleConfigEntry(String key, Double defaultValue, Double minValue, Double maxValue) {
        super(key, defaultValue, minValue, maxValue);
    }
}
