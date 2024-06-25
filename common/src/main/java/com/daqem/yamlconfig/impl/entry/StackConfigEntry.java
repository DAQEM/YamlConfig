package com.daqem.yamlconfig.impl.entry;

import com.daqem.yamlconfig.api.entry.IConfigEntry;

import java.util.Map;

public class StackConfigEntry extends BaseConfigEntry<Map<String, IConfigEntry<?>>> {

    public StackConfigEntry(String key, Map<String, IConfigEntry<?>> defaultValue) {
        super(key, defaultValue);
    }

    @Override
    public void parse(Map<String, IConfigEntry<?>> value) {
    }
}
