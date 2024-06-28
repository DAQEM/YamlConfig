package com.daqem.yamlconfig.api.entry;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.NodeTuple;
import org.snakeyaml.engine.v2.nodes.ScalarNode;
import org.snakeyaml.engine.v2.nodes.Tag;

import java.util.List;

public interface IStringConfigEntry extends IConfigEntry<String> {

    static StreamCodec<IStringConfigEntry, NodeTuple> createCodec() {
        return StreamCodec.of(
                (stringConfigEntry, node) -> {
                    if (node.getValueNode() instanceof ScalarNode scalarNode && scalarNode.getTag().equals(Tag.STR)) {
                        stringConfigEntry.setValue(scalarNode.getValue());
                    }
                },
                stringListConfigEntry -> {
                    ScalarNode keyNode = stringListConfigEntry.createKeyNode();
                    ScalarNode valueNode = new ScalarNode(Tag.STR, stringListConfigEntry.getValue(), ScalarStyle.SINGLE_QUOTED);
                    return new NodeTuple(keyNode, valueNode);
                }
        );
    }

    int getMinLength();

    int getMaxLength();

    String getPattern();

    List<String> getValidValues();

    @Override
    default <B extends IConfigEntry<String>> StreamCodec<B, NodeTuple> getCodec() {
        //noinspection unchecked
        return (StreamCodec<B, NodeTuple>) createCodec();
    }
}
