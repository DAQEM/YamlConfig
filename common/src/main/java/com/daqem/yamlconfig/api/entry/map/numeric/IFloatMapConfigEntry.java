package com.daqem.yamlconfig.api.entry.map.numeric;

import com.daqem.yamlconfig.api.entry.IConfigEntry;
import com.daqem.yamlconfig.api.entry.IStackConfigEntry;
import net.minecraft.network.codec.StreamCodec;
import org.snakeyaml.engine.v2.common.FlowStyle;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.MappingNode;
import org.snakeyaml.engine.v2.nodes.NodeTuple;
import org.snakeyaml.engine.v2.nodes.ScalarNode;
import org.snakeyaml.engine.v2.nodes.Tag;

import java.util.Map;
import java.util.stream.Collectors;

public interface IFloatMapConfigEntry extends INumericMapConfigEntry<Float> {

    static StreamCodec<IFloatMapConfigEntry, NodeTuple> createCodec() {
        return StreamCodec.of(
                (floatMapConfigEntry, node) -> {
                    if (node.getValueNode() instanceof MappingNode mappingNode) {
                        floatMapConfigEntry.setValue(mappingNode.getValue().stream()
                                .filter(n -> n.getKeyNode() instanceof ScalarNode keyNode && (keyNode.getTag().equals(Tag.FLOAT) || keyNode.getTag().equals(Tag.INT)))
                                .collect(Collectors.toMap(
                                        n -> ((ScalarNode) n.getKeyNode()).getValue(),
                                        n -> Float.parseFloat(((ScalarNode) n.getValueNode()).getValue())
                                )));
                    }
                },
                floatMapConfigEntry -> {
                    ScalarNode keyNode = floatMapConfigEntry.createKeyNode();
                    MappingNode valueNode = new MappingNode(Tag.MAP, floatMapConfigEntry.getValue().entrySet().stream()
                            .map(e -> new NodeTuple(
                                    new ScalarNode(Tag.STR, e.getKey(), ScalarStyle.SINGLE_QUOTED),
                                    new ScalarNode(Tag.FLOAT, e.getValue().toString(), ScalarStyle.PLAIN)
                            ))
                            .toList(), FlowStyle.BLOCK);
                    return new NodeTuple(keyNode, valueNode);
                }
        );
    }

    @Override
    default <B extends IConfigEntry<Map<String, Float>>> StreamCodec<B, NodeTuple> getCodec() {
        //noinspection unchecked
        return (StreamCodec<B, NodeTuple>) createCodec();
    }
}
