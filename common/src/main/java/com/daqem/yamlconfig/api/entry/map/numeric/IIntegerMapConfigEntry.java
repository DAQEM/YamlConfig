package com.daqem.yamlconfig.api.entry.map.numeric;

import com.daqem.yamlconfig.api.entry.IConfigEntry;
import com.daqem.yamlconfig.api.entry.IStackConfigEntry;
import net.minecraft.network.codec.StreamCodec;
import org.snakeyaml.engine.v2.common.FlowStyle;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.*;

import java.util.Map;
import java.util.stream.Collectors;

public interface IIntegerMapConfigEntry extends INumericMapConfigEntry<Integer> {

    static StreamCodec<IIntegerMapConfigEntry, NodeTuple> createCodec() {
        return StreamCodec.of(
                (mapConfigEntry, node) -> {
                    if (node.getValueNode() instanceof MappingNode mappingNode) {
                        mapConfigEntry.setValue(mappingNode.getValue().stream()
                                .filter(n ->
                                        n.getKeyNode() instanceof ScalarNode keyNode
                                                && n.getValueNode() instanceof ScalarNode valueNode
                                                && keyNode.getTag().equals(Tag.STR)
                                                && valueNode.getTag().equals(Tag.INT))
                                .collect(Collectors.toMap(
                                        n -> ((ScalarNode) n.getKeyNode()).getValue(),
                                        n -> Integer.parseInt(((ScalarNode) n.getValueNode()).getValue()))));
                    }
                },
                integerListConfigEntry -> {
                    ScalarNode keyNode = integerListConfigEntry.createKeyNode();
                    MappingNode valueNode = new MappingNode(Tag.MAP, integerListConfigEntry.getValue().entrySet().stream()
                            .map(e -> {
                                ScalarNode key = new ScalarNode(Tag.STR, e.getKey(), ScalarStyle.PLAIN);
                                ScalarNode value = new ScalarNode(Tag.INT, Integer.toString(e.getValue()), ScalarStyle.PLAIN);
                                return new NodeTuple(key, value);
                            }).toList(), FlowStyle.BLOCK);
                    return new NodeTuple(keyNode, valueNode);
                }
        );
    }

    @Override
    default <B extends IConfigEntry<Map<String, Integer>>> StreamCodec<B, NodeTuple> getCodec() {
        //noinspection unchecked
        return (StreamCodec<B, NodeTuple>) createCodec();
    }
}
