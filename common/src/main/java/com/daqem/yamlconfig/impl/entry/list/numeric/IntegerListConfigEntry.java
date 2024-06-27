package com.daqem.yamlconfig.impl.entry.list.numeric;

import com.daqem.yamlconfig.api.entry.list.numeric.IIntegerListConfigEntry;

import java.util.List;

public class IntegerListConfigEntry extends BaseNumericListConfigEntry<Integer> implements IIntegerListConfigEntry {

    public IntegerListConfigEntry(String key, List<Integer> value) {
        super(key, value);
    }

    public IntegerListConfigEntry(String key, List<Integer> value, int minLength, int maxLength) {
        super(key, value, minLength, maxLength);
    }

    public IntegerListConfigEntry(String key, List<Integer> value, int minLength, int maxLength, Integer minValue, Integer maxValue) {
        super(key, value, minLength, maxLength, minValue, maxValue);
    }
}
