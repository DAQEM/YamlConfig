package com.daqem.yamlconfig.api.gui.component;

import com.daqem.uilib.api.component.IComponent;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;

public interface IConfigEntryComponent<C extends IConfigEntry<?>> extends IComponent {

    C getConfigEntry();

    void applyValue();

    boolean hasValidationErrors();
}
