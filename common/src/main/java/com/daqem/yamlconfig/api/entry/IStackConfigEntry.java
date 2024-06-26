package com.daqem.yamlconfig.api.entry;

import net.minecraft.network.codec.StreamCodec;
import org.snakeyaml.engine.v2.nodes.MappingNode;
import org.snakeyaml.engine.v2.nodes.Node;
import org.snakeyaml.engine.v2.nodes.NodeTuple;
import org.snakeyaml.engine.v2.nodes.ScalarNode;

import java.util.Map;

public interface IStackConfigEntry extends IConfigEntry<Map<String, IConfigEntry<?>>> {

    StreamCodec<IStackConfigEntry, Node> CODEC = StreamCodec.of(
            (stackConfigEntry, node) -> {
                if (node instanceof MappingNode mappingNode && stackConfigEntry.getValue() != null) {
                    stackConfigEntry.applyValues(mappingNode);
                }
            },
            stackConfigEntry -> {
                return null;
            }
    );

    default void applyValues(MappingNode node) {
        for (Map.Entry<String, IConfigEntry<?>> entry : getValue().entrySet()) {
            node.getValue().stream()
                    .filter(nodeTuple -> nodeTuple.getKeyNode() instanceof ScalarNode keyNode
                            && keyNode.getValue().equals(entry.getKey()))
                    .map(NodeTuple::getValueNode)
                    .findFirst()
                    .ifPresent(valueNode -> entry.getValue().encode(valueNode));
        }
    }

    @Override
    default <B extends IConfigEntry<Map<String, IConfigEntry<?>>>> StreamCodec<B, Node> getCodec() {
        //noinspection unchecked
        return (StreamCodec<B, Node>) CODEC;
    }
}
