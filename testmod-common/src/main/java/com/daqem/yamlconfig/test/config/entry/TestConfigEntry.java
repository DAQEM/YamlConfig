package com.daqem.yamlconfig.test.config.entry;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.api.exception.ConfigEntryValidationException;
import com.daqem.yamlconfig.impl.config.entry.BaseConfigEntry;
import com.daqem.yamlconfig.test.TestMod;

public class TestConfigEntry extends BaseConfigEntry<String> {

    public TestConfigEntry(String key, String defaultValue) {
        super(key, defaultValue);
    }

    @Override
    public void validate(String value) throws ConfigEntryValidationException {
        // Validation logic here if needed
    }

    @Override
    public IConfigEntryType<IConfigEntry<String>, String> getType() {
        return (IConfigEntryType<IConfigEntry<String>, String>) (IConfigEntryType<?, ?>) TestMod.TEST_ENTRY_TYPE;
    }
}