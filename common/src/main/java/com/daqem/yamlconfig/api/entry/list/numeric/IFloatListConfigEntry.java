package com.daqem.yamlconfig.api.entry.list.numeric;

import com.daqem.yamlconfig.api.entry.IConfigEntry;
import com.daqem.yamlconfig.api.entry.IStackConfigEntry;
import net.minecraft.network.codec.StreamCodec;
import org.snakeyaml.engine.v2.common.FlowStyle;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.*;

import java.util.List;

public interface IFloatListConfigEntry extends INumericListConfigEntry<Float> {

    static StreamCodec<IFloatListConfigEntry, NodeTuple> createCodec() {
        return StreamCodec.of(
                (floatListConfigEntry, node) -> {
                    if (node.getValueNode() instanceof SequenceNode sequenceNode) {
                        floatListConfigEntry.setValue(sequenceNode.getValue().stream()
                                .filter(n -> n instanceof ScalarNode scalarNode && (scalarNode.getTag().equals(Tag.FLOAT) || scalarNode.getTag().equals(Tag.INT)))
                                .map(n -> Float.parseFloat(((ScalarNode) n).getValue()))
                                .toList());
                    }
                },
                floatListConfigEntry -> {
                    ScalarNode keyNode = floatListConfigEntry.createKeyNode();
                    SequenceNode valueNode = new SequenceNode(Tag.SEQ, floatListConfigEntry.getValue().stream()
                            .map(s -> (Node) new ScalarNode(Tag.FLOAT, Float.toString(s), ScalarStyle.PLAIN))
                            .toList(), FlowStyle.BLOCK);
                    return new NodeTuple(keyNode, valueNode);
                }
        );
    }

    @Override
    default <B extends IConfigEntry<List<Float>>> StreamCodec<B, NodeTuple> getCodec() {
        //noinspection unchecked
        return (StreamCodec<B, NodeTuple>) createCodec();
    }
}
