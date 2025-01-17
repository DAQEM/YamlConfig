package com.daqem.yamlconfig.impl.config.entry.map.numeric;

import com.daqem.yamlconfig.api.config.entry.comment.IComments;
import com.daqem.yamlconfig.api.config.entry.map.numeric.INumericMapConfigEntry;
import com.daqem.yamlconfig.api.exception.ConfigEntryValidationException;
import com.daqem.yamlconfig.impl.config.entry.map.BaseMapConfigEntry;

import java.util.Map;

public abstract class BaseNumericMapConfigEntry<T extends Number & Comparable<T>> extends BaseMapConfigEntry<T> implements INumericMapConfigEntry<T> {

    private final T minValue;
    private final T maxValue;

    public BaseNumericMapConfigEntry(String key, Map<String, T> defaultValue, T minValue, T maxValue) {
        this(key, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE, minValue, maxValue);
    }

    public BaseNumericMapConfigEntry(String key, Map<String, T> defaultValue, int minLength, int maxLength, T minValue, T maxValue) {
        super(key, defaultValue, minLength, maxLength);
        this.minValue = minValue;
        this.maxValue = maxValue;
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
    public void validate(Map<String, T> value) throws ConfigEntryValidationException {
        super.validate(value);
        for (Map.Entry<String, T> entry : value.entrySet()) {
            T element = entry.getValue();
            if ((minValue != null && element.compareTo(minValue) < 0) || (maxValue != null && element.compareTo(maxValue) > 0)) {
                throw new ConfigEntryValidationException(getKey() + "." + entry.getKey(), "Element is out of bounds. Expected between " + minValue + " and " + maxValue);
            }
        }
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
        comments.addDefaultValues(getDefaultValue().toString());
        return comments;
    }
}
