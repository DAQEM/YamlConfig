package com.daqem.yamlconfig.api.entry;

import net.minecraft.network.codec.StreamCodec;
import org.snakeyaml.engine.v2.common.FlowStyle;
import org.snakeyaml.engine.v2.nodes.MappingNode;
import org.snakeyaml.engine.v2.nodes.NodeTuple;
import org.snakeyaml.engine.v2.nodes.ScalarNode;
import org.snakeyaml.engine.v2.nodes.Tag;

import java.util.Map;

public interface IStackConfigEntry extends IConfigEntry<Map<String, IConfigEntry<?>>> {

    StreamCodec<IStackConfigEntry, NodeTuple> CODEC = StreamCodec.of(
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

    @Override
    default <B extends IConfigEntry<Map<String, IConfigEntry<?>>>> StreamCodec<B, NodeTuple> getCodec() {
        //noinspection unchecked
        return (StreamCodec<B, NodeTuple>) CODEC;
    }
}
