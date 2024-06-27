package com.daqem.yamlconfig.impl.entry.numeric;

import com.daqem.yamlconfig.api.entry.numeric.IIntegerConfigEntry;
import com.daqem.yamlconfig.impl.entry.numeric.BaseNumericConfigEntry;

public class IntegerConfigEntry extends BaseNumericConfigEntry<Integer> implements IIntegerConfigEntry {

    public IntegerConfigEntry(String key, int defaultValue) {
        super(key, defaultValue);
    }

    public IntegerConfigEntry(String key, int defaultValue, int minValue, int maxValue) {
        super(key, defaultValue, minValue, maxValue);
    }
}
