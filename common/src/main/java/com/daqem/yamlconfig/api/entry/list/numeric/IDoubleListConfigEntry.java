package com.daqem.yamlconfig.api.entry.list.numeric;

import com.daqem.yamlconfig.api.entry.IConfigEntry;
import com.daqem.yamlconfig.api.entry.IStackConfigEntry;
import net.minecraft.network.codec.StreamCodec;
import org.snakeyaml.engine.v2.common.FlowStyle;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.*;

import java.util.List;

public interface IDoubleListConfigEntry extends INumericListConfigEntry<Double> {

    static StreamCodec<IDoubleListConfigEntry, NodeTuple> createCodec() {
        return StreamCodec.of(
                (doubleListConfigEntry, node) -> {
                    if (node.getValueNode() instanceof SequenceNode sequenceNode) {
                        doubleListConfigEntry.setValue(sequenceNode.getValue().stream()
                                .filter(n -> n instanceof ScalarNode scalarNode && (scalarNode.getTag().equals(Tag.FLOAT) || scalarNode.getTag().equals(Tag.INT)))
                                .map(n -> Double.parseDouble(((ScalarNode) n).getValue()))
                                .toList());
                    }
                },
                doubleListConfigEntry -> {
                    ScalarNode keyNode = doubleListConfigEntry.createKeyNode();
                    SequenceNode valueNode = new SequenceNode(Tag.SEQ, doubleListConfigEntry.getValue().stream()
                            .map(s -> (Node) new ScalarNode(Tag.FLOAT, Double.toString(s), ScalarStyle.PLAIN))
                            .toList(), FlowStyle.BLOCK);
                    return new NodeTuple(keyNode, valueNode);
                }
        );
    }

    @Override
    default <B extends IConfigEntry<List<Double>>> StreamCodec<B, NodeTuple> getCodec() {
        //noinspection unchecked
        return (StreamCodec<B, NodeTuple>) createCodec();
    }
}
