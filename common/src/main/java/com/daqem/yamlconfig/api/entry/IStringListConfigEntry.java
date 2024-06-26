package com.daqem.yamlconfig.api.entry;

import net.minecraft.network.codec.StreamCodec;
import org.snakeyaml.engine.v2.nodes.Node;
import org.snakeyaml.engine.v2.nodes.ScalarNode;
import org.snakeyaml.engine.v2.nodes.SequenceNode;
import org.snakeyaml.engine.v2.nodes.Tag;

import java.util.List;

public interface IStringListConfigEntry extends IListConfigEntry<String> {

    StreamCodec<IStringListConfigEntry, Node> CODEC = StreamCodec.of(
            (stringListConfigEntry, node) -> {
                if (node instanceof SequenceNode sequenceNode) {
                    stringListConfigEntry.setValue(sequenceNode.getValue().stream()
                            .filter(n -> n instanceof ScalarNode scalarNode && scalarNode.getTag().equals(Tag.STR))
                            .map(n -> ((ScalarNode) n).getValue())
                            .toList());
                }
            },
            stringListConfigEntry -> {
                return null;
            }
    );

    String getPattern();

    List<String> getValidValues();

    @Override
    default <B extends IConfigEntry<List<String>>> StreamCodec<B, Node> getCodec() {
        //noinspection unchecked
        return (StreamCodec<B, Node>) CODEC;
    }
}
