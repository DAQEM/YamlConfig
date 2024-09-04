package com.daqem.yamlconfig.impl.config.entry.list.numeric;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.list.numeric.IDoubleListConfigEntry;
import com.daqem.yamlconfig.api.config.entry.serializer.IConfigEntrySerializer;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.api.gui.component.IConfigEntryComponent;
import com.daqem.yamlconfig.client.gui.component.entry.list.numeric.DoubleListConfigEntryComponent;
import com.daqem.yamlconfig.impl.config.entry.type.ConfigEntryTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.snakeyaml.engine.v2.common.FlowStyle;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.*;

import java.util.List;

public class DoubleListConfigEntry extends BaseNumericListConfigEntry<Double> implements IDoubleListConfigEntry {

    public DoubleListConfigEntry(String key, List<Double> value) {
        super(key, value, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    public DoubleListConfigEntry(String key, List<Double> value, int minLength, int maxLength) {
        super(key, value, minLength, maxLength, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    public DoubleListConfigEntry(String key, List<Double> value, int minLength, int maxLength, Double minValue, Double maxValue) {
        super(key, value, minLength, maxLength, minValue, maxValue);
    }

    @Override
    public IConfigEntryType<IConfigEntry<List<Double>>, List<Double>> getType() {
        //noinspection unchecked
        return (IConfigEntryType<IConfigEntry<List<Double>>, List<Double>>) (IConfigEntryType<?, ?>) ConfigEntryTypes.DOUBLE_LIST;
    }

    @Override
    public IConfigEntryComponent<?, ?> createComponent(String key) {
        return new DoubleListConfigEntryComponent(key, this);
    }

    public static class Serializer implements IConfigEntrySerializer<IDoubleListConfigEntry, List<Double>> {

        @Override
        public void encodeNode(IDoubleListConfigEntry configEntry, NodeTuple nodeTuple) {
            if (nodeTuple.getValueNode() instanceof SequenceNode sequenceNode) {
                configEntry.setValue(sequenceNode.getValue().stream()
                        .filter(n -> n instanceof ScalarNode scalarNode && (scalarNode.getTag().equals(Tag.FLOAT) || scalarNode.getTag().equals(Tag.INT)))
                        .map(n -> Double.parseDouble(((ScalarNode) n).getValue()))
                        .toList());
            }
        }

        @Override
        public NodeTuple decodeNode(IDoubleListConfigEntry configEntry) {
            ScalarNode keyNode = configEntry.createKeyNode();
            SequenceNode valueNode = new SequenceNode(Tag.SEQ, configEntry.get().stream()
                    .map(s -> (Node) new ScalarNode(Tag.FLOAT, Double.toString(s), ScalarStyle.PLAIN))
                    .toList(), FlowStyle.BLOCK);
            return new NodeTuple(keyNode, valueNode);
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, IDoubleListConfigEntry configEntry, List<Double> value) {
            buf.writeCollection(value, FriendlyByteBuf::writeDouble);
        }

        @Override
        public List<Double> fromNetwork(RegistryFriendlyByteBuf buf) {
            return buf.readList(FriendlyByteBuf::readDouble);
        }
    }
}
