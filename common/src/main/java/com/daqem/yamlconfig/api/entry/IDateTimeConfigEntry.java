package com.daqem.yamlconfig.api.entry;

import net.minecraft.network.codec.StreamCodec;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.NodeTuple;
import org.snakeyaml.engine.v2.nodes.ScalarNode;
import org.snakeyaml.engine.v2.nodes.Tag;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface IDateTimeConfigEntry extends IConfigEntry<LocalDateTime> {

    StreamCodec<IDateTimeConfigEntry, NodeTuple> CODEC = StreamCodec.of(
            (stringConfigEntry, node) -> {
                if (node.getValueNode() instanceof ScalarNode scalarNode && scalarNode.getTag().equals(Tag.STR)) {
                    stringConfigEntry.setValue(LocalDateTime.parse(scalarNode.getValue(), IDateTimeConfigEntry.DATE_TIME_FORMATTER));
                }
            },
            stringListConfigEntry -> {
                ScalarNode keyNode = stringListConfigEntry.createKeyNode();
                ScalarNode valueNode = new ScalarNode(Tag.STR, stringListConfigEntry.getValue().format(IDateTimeConfigEntry.DATE_TIME_FORMATTER), ScalarStyle.SINGLE_QUOTED);
                return new NodeTuple(keyNode, valueNode);
            }
    );

    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    LocalDateTime getMinDateTime();

    LocalDateTime getMaxDateTime();

    @Override
    default <B extends IConfigEntry<LocalDateTime>> StreamCodec<B, NodeTuple> getCodec() {
        //noinspection unchecked
        return (StreamCodec<B, NodeTuple>) CODEC;
    }
}
