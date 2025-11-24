package com.daqem.yamlconfig.api.config.entry.serializer;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.node.IMapNode;
import net.minecraft.network.RegistryFriendlyByteBuf;

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
    void valueToNetwork(RegistryFriendlyByteBuf buf, C configEntry, T value);
    T valueFromNetwork(RegistryFriendlyByteBuf buf);
    void toNetwork(RegistryFriendlyByteBuf buf, C configEntry);
    C fromNetwork(RegistryFriendlyByteBuf buf);
}