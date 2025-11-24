package com.daqem.yamlconfig.api.config.entry.minecraft;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;

import net.minecraft.core.Registry;

/**
 * Represents a registry configuration entry.
 *
 * @param <T> The type of the registry entry.
 */
public interface IRegistryConfigEntry<T> extends IConfigEntry<T> {

    /**
     * Gets the registry associated with this entry.
     *
     * @return The {@link Registry}.
     */
    Registry<T> getRegistry();
}
