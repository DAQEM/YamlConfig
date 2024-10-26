package com.daqem.yamlconfig.api.config.entry.map;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;

import java.util.Map;

public interface IMapConfigEntry<T> extends IConfigEntry<Map<String, T>> {

    int getMinLength();

    int getMaxLength();
}
