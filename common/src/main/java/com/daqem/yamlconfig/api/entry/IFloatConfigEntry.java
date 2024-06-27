package com.daqem.yamlconfig.api.entry;

import net.minecraft.network.codec.StreamCodec;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.NodeTuple;
import org.snakeyaml.engine.v2.nodes.ScalarNode;
import org.snakeyaml.engine.v2.nodes.Tag;

public interface IFloatConfigEntry extends INumericConfigEntry<Float> {

    StreamCodec<IFloatConfigEntry, NodeTuple> CODEC = StreamCodec.of(
            (floatConfigEntry, node) -> {
                if (node.getValueNode() instanceof ScalarNode scalarNode && (scalarNode.getTag().equals(Tag.FLOAT) || scalarNode.getTag().equals(Tag.INT))) {
                    floatConfigEntry.setValue(Float.parseFloat(scalarNode.getValue()));
                }
            },
            integerConfigEntry -> {
                ScalarNode keyNode = integerConfigEntry.createKeyNode();
                ScalarNode valueNode = new ScalarNode(Tag.FLOAT, Float.toString(integerConfigEntry.getValue()), ScalarStyle.PLAIN);
                return new NodeTuple(keyNode, valueNode);
            }
    );

    @Override
    default <B extends IConfigEntry<Float>> StreamCodec<B, NodeTuple> getCodec() {
        //noinspection unchecked
        return (StreamCodec<B, NodeTuple>) CODEC;
    }

}
