package com.daqem.yamlconfig.api.config.entry.minecraft;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import net.minecraft.core.Registry;

public interface IRegistryConfigEntry<T> extends IConfigEntry<T> {

    Registry<T> getRegistry();
}
