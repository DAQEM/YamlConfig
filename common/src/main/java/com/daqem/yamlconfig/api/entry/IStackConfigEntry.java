package com.daqem.yamlconfig.api.entry;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.snakeyaml.engine.v2.common.FlowStyle;
import org.snakeyaml.engine.v2.nodes.MappingNode;
import org.snakeyaml.engine.v2.nodes.NodeTuple;
import org.snakeyaml.engine.v2.nodes.ScalarNode;
import org.snakeyaml.engine.v2.nodes.Tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IStackConfigEntry extends IConfigEntry<Map<String, IConfigEntry<?>>> {

    static StreamCodec<IStackConfigEntry, NodeTuple> createCodec() {
        return StreamCodec.of(
                (stackConfigEntry, tuple) -> {
                    if (tuple.getValueNode() instanceof MappingNode mappingNode && stackConfigEntry.getValue() != null) {
                        for (Map.Entry<String, IConfigEntry<?>> entry : stackConfigEntry.getValue().entrySet()) {
                            mappingNode.getValue().stream()
                                    .filter(nodeTuple -> nodeTuple.getKeyNode() instanceof ScalarNode keyNode
                                            && keyNode.getValue().equals(entry.getKey()))
                                    .findFirst()
                                    .ifPresent(valueNode -> entry.getValue().encode(valueNode));
                        }
                    }
                },
                stackConfigEntry -> {
                    ScalarNode keyNode = stackConfigEntry.createKeyNode();
                    MappingNode mappingNode = new MappingNode(Tag.MAP, stackConfigEntry.getValue()
                            .values().stream().map(IConfigEntry::decode).toList(), FlowStyle.BLOCK);
                    return new NodeTuple(keyNode, mappingNode);
                }
        );
    }

    default Map<String, IConfigEntry<?>> getEntries() {
        return getEntries("");
    }

    private Map<String, IConfigEntry<?>> getEntries(String previousKey) {
        return getValue().entrySet().stream()
                .collect(HashMap::new,
                        (entries, entry) -> {
                            String key = previousKey.isEmpty() ? entry.getKey() : previousKey + "." + entry.getKey();
                            if (entry.getValue() instanceof IStackConfigEntry stackConfigEntry) {
                                entries.putAll(stackConfigEntry.getEntries(key));
                            } else {
                                entries.put(key, entry.getValue());
                            }
                        }, Map::putAll);
    }

    @Override
    default <B extends IConfigEntry<Map<String, IConfigEntry<?>>>> StreamCodec<B, NodeTuple> getCodec() {
        //noinspection unchecked
        return (StreamCodec<B, NodeTuple>) createCodec();
    }
}
