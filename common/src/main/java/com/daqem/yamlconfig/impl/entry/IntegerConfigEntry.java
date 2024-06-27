package com.daqem.yamlconfig.impl.entry;

import com.daqem.yamlconfig.api.IComments;
import com.daqem.yamlconfig.api.entry.IIntegerConfigEntry;
import com.daqem.yamlconfig.api.exception.ConfigEntryValidationException;

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
    public void validate(Integer value) throws ConfigEntryValidationException {
        if (getValue() < minValue || getValue() > maxValue) {
            throw new ConfigEntryValidationException(getKey(), "Value is out of bounds. Expected between " + minValue + " and " + maxValue);
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
