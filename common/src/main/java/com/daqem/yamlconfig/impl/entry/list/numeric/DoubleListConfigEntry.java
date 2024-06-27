package com.daqem.yamlconfig.impl.entry.list.numeric;

import com.daqem.yamlconfig.api.entry.list.numeric.IDoubleListConfigEntry;

import java.util.List;

public class DoubleListConfigEntry extends BaseNumericListConfigEntry<Double> implements IDoubleListConfigEntry {

    public DoubleListConfigEntry(String key, List<Double> value) {
        super(key, value);
    }

    public DoubleListConfigEntry(String key, List<Double> value, int minLength, int maxLength) {
        super(key, value, minLength, maxLength);
    }

    public DoubleListConfigEntry(String key, List<Double> value, int minLength, int maxLength, Double minValue, Double maxValue) {
        super(key, value, minLength, maxLength, minValue, maxValue);
    }
}
