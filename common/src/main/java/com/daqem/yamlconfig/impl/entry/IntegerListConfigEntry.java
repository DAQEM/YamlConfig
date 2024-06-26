package com.daqem.yamlconfig.impl.entry;

import com.daqem.yamlconfig.api.entry.IIntegerListConfigEntry;
import com.daqem.yamlconfig.api.exception.ConfigEntryValidationException;

import java.util.List;

public class IntegerListConfigEntry extends BaseListConfigEntry<Integer> implements IIntegerListConfigEntry {

    private final int minValue;
    private final int maxValue;

    public IntegerListConfigEntry(String key, List<Integer> value) {
        this(key, value, -1, -1);
    }

    public IntegerListConfigEntry(String key, List<Integer> value, int minValue, int maxValue) {
        this(key, value, minValue, maxValue, -1, -1);
    }

    public IntegerListConfigEntry(String key, List<Integer> value, int minValue, int maxValue, int minLength, int maxLength) {
        super(key, value, minLength, maxLength);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public int getMinValue() {
        return minValue;
    }

    @Override
    public int getMaxValue() {
        return maxValue;
    }

    @Override
    public void validate(List<Integer> value) throws ConfigEntryValidationException {
        super.validate(value);
        for (int element : value) {
            if (minValue != -1 && element < minValue) {
                throw new ConfigEntryValidationException(getKey(), "Element is too small. Expected at least " + minValue);
            }
            if (maxValue != -1 && element > maxValue) {
                throw new ConfigEntryValidationException(getKey(), "Element is too large. Expected at most " + maxValue);
            }
        }
    }
}
