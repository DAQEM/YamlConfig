package com.daqem.yamlconfig.api.config.entry.serializer;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.node.IMapNode;

import net.minecraft.network.RegistryFriendlyByteBuf;

/**
 * Serializes and deserializes configuration entries.
 *
 * @param <C> The type of the configuration entry.
 * @param <T> The type of the value stored in the entry.
 */
public interface IConfigEntrySerializer<C extends IConfigEntry<T>, T> {

    /**
     * Serializes the config entry into the abstract Node system.
     * @param configEntry The entry to serialize.
     * @param parentMap The parent map node to attach this entry to.
     */
    void toNode(C configEntry, IMapNode parentMap);

    /**
     * Deserializes data from the abstract Node system into the config entry.
     * @param configEntry The entry to populate.
     * @param parentMap The parent map node containing the data.
     */
    void fromNode(C configEntry, IMapNode parentMap);

    // Networking
    /**
     * Serializes a value to the network buffer.
     *
     * @param buf         The network buffer.
     * @param configEntry The configuration entry.
     * @param value       The value to serialize.
     */
    void valueToNetwork(RegistryFriendlyByteBuf buf, C configEntry, T value);
    /**
     * Deserializes a value from the network buffer.
     *
     * @param buf The network buffer.
     * @return The deserialized value.
     */
    T valueFromNetwork(RegistryFriendlyByteBuf buf);
    /**
     * Serializes the configuration entry to the network buffer.
     *
     * @param buf         The network buffer.
     * @param configEntry The configuration entry to serialize.
     */
    void toNetwork(RegistryFriendlyByteBuf buf, C configEntry);
    /**
     * Deserializes a configuration entry from the network buffer.
     *
     * @param buf The network buffer.
     * @return The deserialized configuration entry.
     */
    C fromNetwork(RegistryFriendlyByteBuf buf);
}