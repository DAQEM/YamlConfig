package com.daqem.yamlconfig.impl.entry;

import com.daqem.yamlconfig.api.entry.IIntegerConfigEntry;
import com.daqem.yamlconfig.api.exception.ConfigEntryParseException;

public class IntegerConfigEntry extends BaseConfigEntry<Integer> implements IIntegerConfigEntry {

    private final int minValue;
    private final int maxValue;

    public IntegerConfigEntry(String key, int defaultValue) {
        this(key, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public IntegerConfigEntry(String key, int defaultValue, int minValue, int maxValue) {
        super(key, defaultValue);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public void parse(Integer value) throws ConfigEntryParseException {
        if (getValue() < minValue || getValue() > maxValue) {
            throw new ConfigEntryParseException(getKey(), "Value is out of bounds. Expected between " + minValue + " and " + maxValue);
        }
    }

    @Override
    public int getMinValue() {
        return minValue;
    }

    @Override
    public int getMaxValue() {
        return maxValue;
    }
}
