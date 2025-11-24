package com.daqem.yamlconfig.client.gui.registry;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.gui.component.IConfigEntryComponent;

@FunctionalInterface
public interface IConfigEntryComponentFactory<C extends IConfigEntry<?>> {
    IConfigEntryComponent<C> create(String key, C entry);
}
