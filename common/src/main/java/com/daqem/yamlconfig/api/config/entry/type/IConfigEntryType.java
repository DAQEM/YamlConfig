package com.daqem.yamlconfig.api.config.entry.type;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.serializer.IConfigEntrySerializer;

import net.minecraft.resources.Identifier;

/**
 * Represents the type of a configuration entry, used for serialization and identification.
 *
 * @param <C> The type of the configuration entry.
 * @param <T> The type of the value stored in the entry.
 */
public interface IConfigEntryType<C extends IConfigEntry<T>, T> {

    /**
     * Gets the unique identifier for this entry type.
     *
     * @return The {@link Identifier} ID.
     */
    Identifier getId();

    /**
     * Gets the serializer for this entry type.
     *
     * @return The {@link IConfigEntrySerializer}.
     */
    IConfigEntrySerializer<C, T> getSerializer();
}
