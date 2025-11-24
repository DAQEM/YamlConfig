package com.daqem.yamlconfig.api.config.entry;

import com.daqem.yamlconfig.api.config.entry.comment.IComments;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.api.exception.ConfigEntryValidationException;

import java.util.function.Supplier;

public interface IConfigEntry<T> extends Supplier<T> {

    String getKey();

    T getDefaultValue();

    void set(T value);

    IComments getComments();

    IConfigEntry<T> withComments(String... comments);

    IConfigEntry<T> withComments(boolean showDefaultValues, String... comments);

    IConfigEntry<T> withComments(boolean showDefaultValues, boolean showValidationParameters, String... comments);

    void validate(T value) throws ConfigEntryValidationException;

    IConfigEntryType<IConfigEntry<T>, T> getType();

    boolean shouldBeSynced();

    IConfigEntry<T> dontSync();

    IConfigEntry<T> setShouldBeSynced(boolean shouldBeSynced);
}
