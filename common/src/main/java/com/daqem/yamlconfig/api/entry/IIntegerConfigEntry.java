package com.daqem.yamlconfig.api.entry;

import net.minecraft.network.codec.StreamCodec;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.NodeTuple;
import org.snakeyaml.engine.v2.nodes.ScalarNode;
import org.snakeyaml.engine.v2.nodes.Tag;

import java.util.List;

public interface IIntegerConfigEntry extends IConfigEntry<Integer> {

    StreamCodec<IIntegerConfigEntry, NodeTuple> CODEC = StreamCodec.of(
            (integerConfigEntry, node) -> {
                if (node.getValueNode() instanceof ScalarNode scalarNode && scalarNode.getTag().equals(Tag.INT)) {
                    integerConfigEntry.setValue(Integer.parseInt(scalarNode.getValue()));
                }
            },
            integerConfigEntry -> {
                ScalarNode keyNode = integerConfigEntry.createKeyNode();
                ScalarNode valueNode = new ScalarNode(Tag.INT, Integer.toString(integerConfigEntry.getValue()), ScalarStyle.PLAIN);
                return new NodeTuple(keyNode, valueNode);
            }
    );

    int getMinValue();

    int getMaxValue();

    @Override
    default <B extends IConfigEntry<Integer>> StreamCodec<B, NodeTuple> getCodec() {
        //noinspection unchecked
        return (StreamCodec<B, NodeTuple>) CODEC;
    }
}
