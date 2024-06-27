package com.daqem.yamlconfig.api.entry.list.numeric;

import com.daqem.yamlconfig.api.entry.IConfigEntry;
import com.daqem.yamlconfig.api.entry.list.IListConfigEntry;
import net.minecraft.network.codec.StreamCodec;
import org.snakeyaml.engine.v2.common.FlowStyle;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.*;

import java.util.List;

public interface IIntegerListConfigEntry extends IListConfigEntry<Integer> {

    StreamCodec<IIntegerListConfigEntry, NodeTuple> CODEC = StreamCodec.of(
            (integerListConfigEntry, node) -> {
                if (node.getValueNode() instanceof SequenceNode sequenceNode) {
                    integerListConfigEntry.setValue(sequenceNode.getValue().stream()
                            .filter(n -> n instanceof ScalarNode scalarNode && scalarNode.getTag().equals(Tag.INT))
                            .map(n -> Integer.parseInt(((ScalarNode) n).getValue()))
                            .toList());
                }
            },
            integerListConfigEntry -> {
                ScalarNode keyNode = integerListConfigEntry.createKeyNode();
                SequenceNode valueNode = new SequenceNode(Tag.SEQ, integerListConfigEntry.getValue().stream()
                        .map(s -> (Node) new ScalarNode(Tag.INT, Integer.toString(s), ScalarStyle.PLAIN))
                        .toList(), FlowStyle.BLOCK);
                return new NodeTuple(keyNode, valueNode);
            }
    );

    int getMinValue();

    int getMaxValue();

    @Override
    default <B extends IConfigEntry<List<Integer>>> StreamCodec<B, NodeTuple> getCodec() {
        //noinspection unchecked
        return (StreamCodec<B, NodeTuple>) CODEC;
    }
}
