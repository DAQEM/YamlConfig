package com.daqem.yamlconfig.api.entry.map;

import com.daqem.yamlconfig.api.entry.IConfigEntry;
import com.daqem.yamlconfig.api.entry.IStackConfigEntry;
import net.minecraft.network.codec.StreamCodec;
import org.snakeyaml.engine.v2.common.FlowStyle;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.MappingNode;
import org.snakeyaml.engine.v2.nodes.NodeTuple;
import org.snakeyaml.engine.v2.nodes.ScalarNode;
import org.snakeyaml.engine.v2.nodes.Tag;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface IStringMapConfigEntry extends IMapConfigEntry<String> {

    static StreamCodec<IStringMapConfigEntry, NodeTuple> createCodec() {
        return StreamCodec.of(
                (stringMapConfigEntry, node) -> {
                    if (node.getValueNode() instanceof MappingNode mappingNode) {
                        stringMapConfigEntry.setValue(mappingNode.getValue().stream()
                                .filter(n -> n.getKeyNode() instanceof ScalarNode keyNode && keyNode.getTag().equals(Tag.STR))
                                .collect(Collectors.toMap(
                                        n -> ((ScalarNode) n.getKeyNode()).getValue(),
                                        n -> ((ScalarNode) n.getValueNode()).getValue()
                                )));
                    }
                },
                stringMapConfigEntry -> {
                    ScalarNode keyNode = stringMapConfigEntry.createKeyNode();
                    MappingNode valueNode = new MappingNode(Tag.MAP, stringMapConfigEntry.getValue().entrySet().stream()
                            .map(e -> new NodeTuple(
                                    new ScalarNode(Tag.STR, e.getKey(), ScalarStyle.SINGLE_QUOTED),
                                    new ScalarNode(Tag.STR, e.getValue(), ScalarStyle.SINGLE_QUOTED)
                            ))
                            .toList(), FlowStyle.BLOCK);
                    return new NodeTuple(keyNode, valueNode);
                }
        );
    }

    String getPattern();

    List<String> getValidValues();

    @Override
    default <B extends IConfigEntry<Map<String, String>>> StreamCodec<B, NodeTuple> getCodec() {
        //noinspection unchecked
        return (StreamCodec<B, NodeTuple>) createCodec();
    }
}
