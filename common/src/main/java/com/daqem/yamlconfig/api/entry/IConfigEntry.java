package com.daqem.yamlconfig.api.entry;

import com.daqem.yamlconfig.api.exception.ConfigEntryParseException;

public interface IConfigEntry<T> {

    String getKey();

    T getDefaultValue();

    T getValue();

    void setValue(T value);

    void parse(T value) throws ConfigEntryParseException;
}
