package com.daqem.yamlconfig.api.config.entry.minecraft;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import net.minecraft.resources.ResourceLocation;

public interface IResourceLocationConfigEntry extends IConfigEntry<ResourceLocation> {
    String getPattern();
}
