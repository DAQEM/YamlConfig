package com.daqem.yamlconfig.api.config.entry.type;

import com.daqem.yamlconfig.api.config.entry.serializer.IConfigEntrySerializer;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import net.minecraft.resources.ResourceLocation;

public interface IConfigEntryType<C extends IConfigEntry<T>, T> {

    ResourceLocation getId();

    IConfigEntrySerializer<C, T> getSerializer();
}
