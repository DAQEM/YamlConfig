package com.daqem.yamlconfig.impl.entry;

import com.daqem.yamlconfig.api.IComments;
import com.daqem.yamlconfig.api.entry.IIntegerListConfigEntry;
import com.daqem.yamlconfig.api.exception.ConfigEntryValidationException;

import java.util.List;

public class IntegerListConfigEntry extends BaseListConfigEntry<Integer> implements IIntegerListConfigEntry {

    private final int minValue;
    private final int maxValue;

    public IntegerListConfigEntry(String key, List<Integer> value) {
        this(key, value, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public IntegerListConfigEntry(String key, List<Integer> value, int minValue, int maxValue) {
        this(key, value, minValue, maxValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
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
            if (minValue != Integer.MIN_VALUE && element < minValue) {
                throw new ConfigEntryValidationException(getKey(), "Element is too small. Expected at least " + minValue);
            }
            if (maxValue != Integer.MAX_VALUE && element > maxValue) {
                throw new ConfigEntryValidationException(getKey(), "Element is too large. Expected at most " + maxValue);
            }
        }
    }

    @Override
    public IComments getComments() {
        IComments comments = super.getComments();
        if (comments.showValidationParameters()) {
            if (minValue != Integer.MIN_VALUE) {
                comments.addValidationParameter("Minimum value: " + minValue);
            }
            if (maxValue != Integer.MAX_VALUE) {
                comments.addValidationParameter("Maximum value: " + maxValue);
            }
        }
        if (comments.showDefaultValues()) {
            comments.addValidationParameter("Default value: " + getDefaultValue());
        }
        return comments;
    }
}
