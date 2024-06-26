package com.daqem.yamlconfig.api.entry;

import net.minecraft.network.codec.StreamCodec;
import org.snakeyaml.engine.v2.nodes.Node;
import org.snakeyaml.engine.v2.nodes.ScalarNode;
import org.snakeyaml.engine.v2.nodes.Tag;

import java.util.List;

public interface IStringConfigEntry extends IConfigEntry<String> {

    StreamCodec<IStringConfigEntry, Node> CODEC = StreamCodec.of(
            (stringConfigEntry, valueNode) -> {
                if (valueNode instanceof ScalarNode scalarNode && scalarNode.getTag().equals(Tag.STR)) {
                    stringConfigEntry.setValue(scalarNode.getValue());
                }
            },
            stringListConfigEntry -> {
                return null;
            }
    );

    int getMinLength();

    int getMaxLength();

    String getPattern();

    List<String> getValidValues();

    @Override
    default <B extends IConfigEntry<String>> StreamCodec<B, Node> getCodec() {
        //noinspection unchecked
        return (StreamCodec<B, Node>) CODEC;
    }
}
