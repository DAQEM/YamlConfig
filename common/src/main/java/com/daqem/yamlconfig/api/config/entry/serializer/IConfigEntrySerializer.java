package com.daqem.yamlconfig.api.config.entry.serializer;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.snakeyaml.engine.v2.nodes.NodeTuple;

public interface IConfigEntrySerializer<C extends IConfigEntry<T>, T> {

    void encodeNode(C configEntry, NodeTuple nodeTuple);

    NodeTuple decodeNode(C configEntry);

    void toNetwork(RegistryFriendlyByteBuf buf, C configEntry, T value);

    T fromNetwork(RegistryFriendlyByteBuf buf);
}
