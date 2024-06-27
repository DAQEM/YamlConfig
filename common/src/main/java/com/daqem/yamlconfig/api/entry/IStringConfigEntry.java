package com.daqem.yamlconfig.api.entry;

import net.minecraft.network.codec.StreamCodec;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.Node;
import org.snakeyaml.engine.v2.nodes.NodeTuple;
import org.snakeyaml.engine.v2.nodes.ScalarNode;
import org.snakeyaml.engine.v2.nodes.Tag;

import java.util.List;

public interface IStringConfigEntry extends IConfigEntry<String> {

    StreamCodec<IStringConfigEntry, NodeTuple> CODEC = StreamCodec.of(
            (stringConfigEntry, node) -> {
                if (node.getValueNode() instanceof ScalarNode scalarNode && scalarNode.getTag().equals(Tag.STR)) {
                    stringConfigEntry.setValue(scalarNode.getValue());
                }
            },
            stringListConfigEntry -> {
                ScalarNode keyNode = new ScalarNode(Tag.STR, stringListConfigEntry.getKey(), ScalarStyle.PLAIN);
                ScalarNode valueNode = new ScalarNode(Tag.STR, String.valueOf(stringListConfigEntry.getValue()), ScalarStyle.SINGLE_QUOTED);
                return new NodeTuple(keyNode, valueNode);
            }
    );

    int getMinLength();

    int getMaxLength();

    String getPattern();

    List<String> getValidValues();

    @Override
    default <B extends IConfigEntry<String>> StreamCodec<B, NodeTuple> getCodec() {
        //noinspection unchecked
        return (StreamCodec<B, NodeTuple>) CODEC;
    }
}
