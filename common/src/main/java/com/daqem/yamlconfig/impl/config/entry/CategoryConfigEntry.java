package com.daqem.yamlconfig.impl.config.entry;

import com.daqem.yamlconfig.api.config.entry.ICategoryConfigEntry;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.api.exception.ConfigEntryValidationException;
import com.daqem.yamlconfig.api.gui.component.IConfigEntryComponent;

import java.util.ArrayList;
import java.util.List;

public class CategoryConfigEntry  extends BaseConfigEntry<List<IConfigEntry<?>>> implements ICategoryConfigEntry {

    public CategoryConfigEntry(String key) {
        super(key, new ArrayList<>());
    }

    public CategoryConfigEntry(String key, List<IConfigEntry<?>> defaultValue) {
        super(key, defaultValue);
    }

    @Override
    public void validate(List<IConfigEntry<?>> value) throws ConfigEntryValidationException {

    }

    @Override
    public IConfigEntryType<IConfigEntry<List<IConfigEntry<?>>>, List<IConfigEntry<?>>> getType() {
        return null;
    }

    @Override
    public IConfigEntryComponent<?, ?> createComponent(String key) {
        return null;
    }
}
