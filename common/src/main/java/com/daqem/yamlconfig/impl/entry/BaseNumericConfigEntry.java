package com.daqem.yamlconfig.impl.entry;

import com.daqem.yamlconfig.api.IComments;
import com.daqem.yamlconfig.api.entry.INumericConfigEntry;
import com.daqem.yamlconfig.api.exception.ConfigEntryValidationException;

public abstract class BaseNumericConfigEntry<T extends Number & Comparable<T>> extends BaseConfigEntry<T> implements INumericConfigEntry<T> {

    private final T minValue;
    private final T maxValue;

    public BaseNumericConfigEntry(String key, T defaultValue) {
        this(key, defaultValue, null, null);
    }

    public BaseNumericConfigEntry(String key, T defaultValue, T minValue, T maxValue) {
        super(key, defaultValue);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public void validate(T value) throws ConfigEntryValidationException {
        if ((minValue != null && value.compareTo(minValue) < 0) || (maxValue != null && value.compareTo(maxValue) > 0)) {
            throw new ConfigEntryValidationException(getKey(), "Value is out of bounds. Expected between " + minValue + " and " + maxValue);
        }
    }

    @Override
    public T getMinValue() {
        return minValue;
    }

    @Override
    public T getMaxValue() {
        return maxValue;
    }

    @Override
    public IComments getComments() {
        IComments comments = super.getComments();
        if (comments.showValidationParameters()) {
            if (minValue != null) {
                comments.addValidationParameter("Minimum value: " + minValue);
            }
            if (maxValue != null) {
                comments.addValidationParameter("Maximum value: " + maxValue);
            }

        }
        if (comments.showDefaultValues()) {
            comments.addValidationParameter("Default value: " + getDefaultValue());
        }
        return comments;
    }
}
