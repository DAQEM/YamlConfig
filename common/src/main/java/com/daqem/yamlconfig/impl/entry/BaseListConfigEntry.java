package com.daqem.yamlconfig.impl.entry;

import com.daqem.yamlconfig.api.entry.IListConfigEntry;
import com.daqem.yamlconfig.api.exception.ConfigEntryParseException;

import java.util.List;

public class BaseListConfigEntry<T> extends BaseConfigEntry<List<T>> implements IListConfigEntry<T> {

    private final int minLength;
    private final int maxLength;

    public BaseListConfigEntry(String key, List<T> value) {
        this(key, value, -1, -1);
    }

    public BaseListConfigEntry(String key, List<T> value, int minLength, int maxLength) {
        super(key, value);
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    @Override
    public void parse(List<T> value) throws ConfigEntryParseException {
        if (minLength != -1 && value.size() < minLength) {
            throw new ConfigEntryParseException(getKey(), "List is too short. Expected at least " + minLength + " elements");
        }
        if (maxLength != -1 && value.size() > maxLength) {
            throw new ConfigEntryParseException(getKey(), "List is too long. Expected at most " + maxLength + " elements");
        }
    }

    @Override
    public int getMinLength() {
        return minLength;
    }

    @Override
    public int getMaxLength() {
        return maxLength;
    }
}