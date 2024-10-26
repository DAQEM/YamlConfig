package com.daqem.yamlconfig.impl.config.entry;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.api.config.entry.comment.IComments;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.exception.ConfigEntryValidationException;
import com.daqem.yamlconfig.impl.config.entry.comment.Comments;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseConfigEntry<T> implements IConfigEntry<T> {

    private final String key;
    private final T defaultValue;
    private T value;
    private final IComments comments = new Comments(new ArrayList<>());
    private boolean shouldBeSynced = true;

    public BaseConfigEntry(String key, T defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public T getDefaultValue() {
        return defaultValue;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public void set(T value) {
        try {
            validate(value);
            this.value = value;
        } catch (ConfigEntryValidationException e) {
            YamlConfig.LOGGER.error("Failed to parse config entry: " + e.getKey());
            YamlConfig.LOGGER.error("Reason: " + e.getMessage());
            this.value = defaultValue;
        }
    }

    @Override
    public IComments getComments() {
        comments.resetValidationParameters();
        return comments;
    }

    @Override
    public IConfigEntry<T> withComments(String... comments) {
        return withComments(true, comments);
    }

    @Override
    public IConfigEntry<T> withComments(boolean showDefaultValues, String... comments) {
        return withComments(showDefaultValues, true, comments);
    }

    @Override
    public IConfigEntry<T> withComments(boolean showDefaultValues, boolean showValidationParameters, String... comments) {
        this.comments.setComments(new ArrayList<>(List.of(comments)));
        this.comments.setShowDefaultValues(showDefaultValues);
        this.comments.setShowValidationParameters(showValidationParameters);
        return this;
    }

    @Override
    public boolean shouldBeSynced() {
        return shouldBeSynced;
    }

    @Override
    public IConfigEntry<T> dontSync() {
        shouldBeSynced = false;
        return this;
    }

    @Override
    public IConfigEntry<T> setShouldBeSynced(boolean shouldBeSynced) {
        this.shouldBeSynced = shouldBeSynced;
        return this;
    }
}
