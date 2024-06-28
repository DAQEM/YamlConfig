package com.daqem.yamlconfig.api.entry.numeric;

import com.daqem.yamlconfig.api.entry.IConfigEntry;
import com.daqem.yamlconfig.api.entry.IStackConfigEntry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.NodeTuple;
import org.snakeyaml.engine.v2.nodes.ScalarNode;
import org.snakeyaml.engine.v2.nodes.Tag;

public interface IIntegerConfigEntry extends INumericConfigEntry<Integer> {

    static StreamCodec<IIntegerConfigEntry, NodeTuple> createCodec() {
        return StreamCodec.of(
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
    }

    @Override
    default <B extends IConfigEntry<Integer>> StreamCodec<B, NodeTuple> getCodec() {
        //noinspection unchecked
        return (StreamCodec<B, NodeTuple>) createCodec();
    }
}
