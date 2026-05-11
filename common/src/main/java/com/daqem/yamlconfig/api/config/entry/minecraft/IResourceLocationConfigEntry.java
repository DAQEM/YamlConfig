package com.daqem.yamlconfig.api.config.entry.minecraft;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;

import net.minecraft.resources.ResourceLocation;

/**
 * Represents a resource location configuration entry.
 */
public interface IResourceLocationConfigEntry extends IConfigEntry<ResourceLocation> {
    /**
     * Gets the regex pattern the resource location must match.
     *
     * @return The regex pattern, or null if none.
     */
    String getPattern();
}
