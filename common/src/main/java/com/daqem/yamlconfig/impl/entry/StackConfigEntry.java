package com.daqem.yamlconfig.impl.entry;

import com.daqem.yamlconfig.api.entry.IConfigEntry;
import com.daqem.yamlconfig.api.entry.IStackConfigEntry;

import java.util.Map;

public class StackConfigEntry extends BaseConfigEntry<Map<String, IConfigEntry<?>>> implements IStackConfigEntry {

    public StackConfigEntry(String key, Map<String, IConfigEntry<?>> defaultValue) {
        super(key, defaultValue);
    }

    @Override
    public void validate(Map<String, IConfigEntry<?>> value) {
    }
}
