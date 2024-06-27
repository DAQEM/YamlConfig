package com.daqem.yamlconfig.api.entry;

import net.minecraft.network.codec.StreamCodec;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.NodeTuple;
import org.snakeyaml.engine.v2.nodes.ScalarNode;
import org.snakeyaml.engine.v2.nodes.Tag;

public interface IEnumConfigEntry<E extends Enum<E>> extends IConfigEntry<E> {

    static <E extends Enum<E>> StreamCodec<IEnumConfigEntry<E>, NodeTuple> createCodec() {
        return StreamCodec.of(
                (enumConfigEntry, node) -> {
                    if (node.getValueNode() instanceof ScalarNode scalarNode && scalarNode.getTag().equals(Tag.STR)) {
                        enumConfigEntry.setValue(Enum.valueOf(enumConfigEntry.getEnumClass(), scalarNode.getValue()));
                    }
                },
                enumConfigEntry -> {
                    ScalarNode keyNode = enumConfigEntry.createKeyNode();
                    ScalarNode valueNode = new ScalarNode(Tag.STR, enumConfigEntry.getValue().toString(), ScalarStyle.SINGLE_QUOTED);
                    return new NodeTuple(keyNode, valueNode);
                }
        );
    }

    Class<E> getEnumClass();

    @Override
    default <B extends IConfigEntry<E>> StreamCodec<B, NodeTuple> getCodec() {
        //noinspection unchecked
        return (StreamCodec<B, NodeTuple>) createCodec();
    }
}
