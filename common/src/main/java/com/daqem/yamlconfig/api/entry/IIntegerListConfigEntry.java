package com.daqem.yamlconfig.api.entry;

import net.minecraft.network.codec.StreamCodec;
import org.snakeyaml.engine.v2.nodes.Node;
import org.snakeyaml.engine.v2.nodes.ScalarNode;
import org.snakeyaml.engine.v2.nodes.SequenceNode;
import org.snakeyaml.engine.v2.nodes.Tag;

import java.util.List;

public interface IIntegerListConfigEntry extends IListConfigEntry<Integer> {

    StreamCodec<IIntegerListConfigEntry, Node> CODEC = StreamCodec.of(
            (integerListConfigEntry, node) -> {
                if (node instanceof SequenceNode sequenceNode) {
                    integerListConfigEntry.setValue(sequenceNode.getValue().stream()
                            .filter(n -> n instanceof ScalarNode scalarNode && scalarNode.getTag().equals(Tag.INT))
                            .map(n -> Integer.parseInt(((ScalarNode) n).getValue()))
                            .toList());
                }
            },
            stringListConfigEntry -> {
                return null;
            }
    );

    int getMinValue();

    int getMaxValue();

    @Override
    default <B extends IConfigEntry<List<Integer>>> StreamCodec<B, Node> getCodec() {
        //noinspection unchecked
        return (StreamCodec<B, Node>) CODEC;
    }
}
