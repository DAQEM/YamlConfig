package com.daqem.yamlconfig.api.entry;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.NodeTuple;
import org.snakeyaml.engine.v2.nodes.ScalarNode;
import org.snakeyaml.engine.v2.nodes.Tag;

public interface IBooleanConfigEntry extends IConfigEntry<Boolean> {

    static StreamCodec<IBooleanConfigEntry, NodeTuple> createCodec() {
        return StreamCodec.of(
                (booleanConfigEntry, node) -> {
                    if (node.getValueNode() instanceof ScalarNode scalarNode && scalarNode.getTag().equals(Tag.BOOL)) {
                        booleanConfigEntry.setValue(Boolean.parseBoolean(scalarNode.getValue()));
                    }
                },
                booleanConfigEntry -> {
                    ScalarNode keyNode = booleanConfigEntry.createKeyNode();
                    ScalarNode valueNode = new ScalarNode(Tag.BOOL, Boolean.toString(booleanConfigEntry.getValue()), ScalarStyle.PLAIN);
                    return new NodeTuple(keyNode, valueNode);
                }
        );
    }

    @Override
    default <B extends IConfigEntry<Boolean>> StreamCodec<B, NodeTuple> getCodec() {
        //noinspection unchecked
        return (StreamCodec<B, NodeTuple>) createCodec();
    }
}
