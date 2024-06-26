package com.daqem.yamlconfig.api.entry;

import net.minecraft.network.codec.StreamCodec;
import org.snakeyaml.engine.v2.nodes.Node;
import org.snakeyaml.engine.v2.nodes.ScalarNode;
import org.snakeyaml.engine.v2.nodes.Tag;

public interface IIntegerConfigEntry extends IConfigEntry<Integer> {

    StreamCodec<IIntegerConfigEntry, Node> CODEC = StreamCodec.of(
            (integerConfigEntry, valueNode) -> {
                if (valueNode instanceof ScalarNode scalarNode && scalarNode.getTag().equals(Tag.INT)) {
                    integerConfigEntry.setValue(Integer.parseInt(scalarNode.getValue()));
                }
            },
            stringListConfigEntry -> {
                return null;
            }
    );

    int getMinValue();

    int getMaxValue();

    @Override
    default <B extends IConfigEntry<Integer>> StreamCodec<B, Node> getCodec() {
        //noinspection unchecked
        return (StreamCodec<B, Node>) CODEC;
    }
}
