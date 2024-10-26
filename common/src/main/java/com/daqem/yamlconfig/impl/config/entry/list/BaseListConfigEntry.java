package com.daqem.yamlconfig.impl.config.entry.list;

import com.daqem.yamlconfig.api.config.entry.comment.IComments;
import com.daqem.yamlconfig.api.config.entry.list.IListConfigEntry;
import com.daqem.yamlconfig.api.exception.ConfigEntryValidationException;
import com.daqem.yamlconfig.impl.config.entry.BaseConfigEntry;

import java.util.List;

public abstract class BaseListConfigEntry<T> extends BaseConfigEntry<List<T>> implements IListConfigEntry<T> {

    private final int minLength;
    private final int maxLength;

    public BaseListConfigEntry(String key, List<T> value) {
        this(key, value, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public BaseListConfigEntry(String key, List<T> value, int minLength, int maxLength) {
        super(key, value);
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    @Override
    public void validate(List<T> value) throws ConfigEntryValidationException {
        if (minLength != Integer.MIN_VALUE && value.size() < minLength) {
            throw new ConfigEntryValidationException(getKey(), "List is too short. Expected at least " + minLength + " elements");
        }
        if (maxLength != Integer.MAX_VALUE && value.size() > maxLength) {
            throw new ConfigEntryValidationException(getKey(), "List is too long. Expected at most " + maxLength + " elements");
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

    @Override
    public IComments getComments() {
        IComments comments = super.getComments();
        if (comments.showValidationParameters()) {
            if (minLength != Integer.MIN_VALUE) {
                comments.addValidationParameter("Minimum length: " + minLength);
            }
            if (maxLength != Integer.MAX_VALUE) {
                comments.addValidationParameter("Maximum length: " + maxLength);
            }
        }
        return comments;
    }
}
