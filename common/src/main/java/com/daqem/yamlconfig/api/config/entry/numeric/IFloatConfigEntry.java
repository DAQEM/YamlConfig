package com.daqem.yamlconfig.api.config.entry.numeric;

import net.minecraft.network.codec.StreamCodec;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.NodeTuple;
import org.snakeyaml.engine.v2.nodes.ScalarNode;
import org.snakeyaml.engine.v2.nodes.Tag;

public interface IFloatConfigEntry extends INumericConfigEntry<Float> {

    static StreamCodec<IFloatConfigEntry, NodeTuple> createCodec() {
        return StreamCodec.of(
                (floatConfigEntry, node) -> {
                    if (node.getValueNode() instanceof ScalarNode scalarNode && (scalarNode.getTag().equals(Tag.FLOAT) || scalarNode.getTag().equals(Tag.INT))) {
                        floatConfigEntry.set(Float.parseFloat(scalarNode.getValue()));
                    }
                },
                integerConfigEntry -> {
                    ScalarNode keyNode = integerConfigEntry.createKeyNode();
                    ScalarNode valueNode = new ScalarNode(Tag.FLOAT, Float.toString(integerConfigEntry.get()), ScalarStyle.PLAIN);
                    return new NodeTuple(keyNode, valueNode);
                }
        );
    }
}
