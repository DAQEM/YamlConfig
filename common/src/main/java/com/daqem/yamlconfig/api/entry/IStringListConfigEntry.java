package com.daqem.yamlconfig.api.entry;

import net.minecraft.network.codec.StreamCodec;
import org.snakeyaml.engine.v2.common.FlowStyle;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.*;

import java.util.List;

public interface IStringListConfigEntry extends IListConfigEntry<String> {

    StreamCodec<IStringListConfigEntry, NodeTuple> CODEC = StreamCodec.of(
            (stringListConfigEntry, node) -> {
                if (node.getValueNode() instanceof SequenceNode sequenceNode) {
                    stringListConfigEntry.setValue(sequenceNode.getValue().stream()
                            .filter(n -> n instanceof ScalarNode scalarNode && scalarNode.getTag().equals(Tag.STR))
                            .map(n -> ((ScalarNode) n).getValue())
                            .toList());
                }
            },
            stringListConfigEntry -> {
                ScalarNode keyNode = new ScalarNode(Tag.STR, stringListConfigEntry.getKey(), ScalarStyle.PLAIN);
                SequenceNode valueNode = new SequenceNode(Tag.SEQ, stringListConfigEntry.getValue().stream()
                        .map(s -> (Node) new ScalarNode(Tag.STR, s, ScalarStyle.SINGLE_QUOTED))
                        .toList(), FlowStyle.BLOCK);
                return new NodeTuple(keyNode, valueNode);
            }
    );

    String getPattern();

    List<String> getValidValues();

    @Override
    default <B extends IConfigEntry<List<String>>> StreamCodec<B, NodeTuple> getCodec() {
        //noinspection unchecked
        return (StreamCodec<B, NodeTuple>) CODEC;
    }
}
