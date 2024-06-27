package com.daqem.yamlconfig.impl.entry.map;

import com.daqem.yamlconfig.api.entry.map.IMapConfigEntry;
import com.daqem.yamlconfig.impl.entry.BaseConfigEntry;

import java.util.Map;

public abstract class BaseMapConfigEntry<T> extends BaseConfigEntry<Map<String, T>> implements IMapConfigEntry<T> {

    public BaseMapConfigEntry(String key, Map<String, T> defaultValue) {
        super(key, defaultValue);
    }
}
