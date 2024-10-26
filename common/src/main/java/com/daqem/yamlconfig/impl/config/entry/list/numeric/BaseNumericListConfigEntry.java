package com.daqem.yamlconfig.impl.config.entry.list.numeric;

import com.daqem.yamlconfig.api.config.entry.comment.IComments;
import com.daqem.yamlconfig.api.config.entry.list.numeric.INumericListConfigEntry;
import com.daqem.yamlconfig.api.exception.ConfigEntryValidationException;
import com.daqem.yamlconfig.impl.config.entry.list.BaseListConfigEntry;

import java.util.List;

public abstract class BaseNumericListConfigEntry<T extends Number & Comparable<T>> extends BaseListConfigEntry<T> implements INumericListConfigEntry<T> {

    private final T minValue;
    private final T maxValue;

    public BaseNumericListConfigEntry(String key, List<T> value, T minValue, T maxValue) {
        this(key, value, Integer.MIN_VALUE, Integer.MAX_VALUE, minValue, maxValue);
    }

    public BaseNumericListConfigEntry(String key, List<T> value, int minLength, int maxLength, T minValue, T maxValue) {
        super(key, value, minLength, maxLength);
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
    public void validate(List<T> value) throws ConfigEntryValidationException {
        super.validate(value);
        for (T element : value) {
            if (minValue != null && element.compareTo(minValue) < 0) {
                throw new ConfigEntryValidationException(getKey(), "Element is too small. Expected at least " + minValue);
            }
            if (maxValue != null && element.compareTo(maxValue) > 0) {
                throw new ConfigEntryValidationException(getKey(), "Element is too large. Expected at most " + maxValue);
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
        if (comments.showDefaultValues()) {
            comments.addDefaultValues(getDefaultValue().toString());
        }
        return comments;
    }
}
