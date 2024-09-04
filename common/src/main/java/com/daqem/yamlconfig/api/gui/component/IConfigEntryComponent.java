package com.daqem.yamlconfig.api.gui.component;

import com.daqem.uilib.api.client.gui.component.IComponent;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;

public interface IConfigEntryComponent<T extends IComponent<T>, C extends IConfigEntry<?>> extends IComponent<T> {

    C getConfigEntry();
}
