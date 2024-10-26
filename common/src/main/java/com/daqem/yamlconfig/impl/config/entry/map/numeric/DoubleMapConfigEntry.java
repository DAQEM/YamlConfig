package com.daqem.yamlconfig.impl.config.entry.map.numeric;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.map.numeric.IDoubleMapConfigEntry;
import com.daqem.yamlconfig.api.config.entry.serializer.IConfigEntrySerializer;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.api.gui.component.IConfigEntryComponent;
import com.daqem.yamlconfig.client.gui.component.entry.map.numeric.DoubleMapConfigEntryComponent;
import com.daqem.yamlconfig.impl.config.entry.type.ConfigEntryTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.snakeyaml.engine.v2.common.FlowStyle;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.MappingNode;
import org.snakeyaml.engine.v2.nodes.NodeTuple;
import org.snakeyaml.engine.v2.nodes.ScalarNode;
import org.snakeyaml.engine.v2.nodes.Tag;

import java.util.Map;
import java.util.stream.Collectors;

public class DoubleMapConfigEntry extends BaseNumericMapConfigEntry<Double> implements IDoubleMapConfigEntry {

    public DoubleMapConfigEntry(String key, Map<String, Double> defaultValue) {
        super(key, defaultValue, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    public DoubleMapConfigEntry(String key, Map<String, Double> defaultValue, int minLength, int maxLength) {
        super(key, defaultValue, minLength, maxLength, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    public DoubleMapConfigEntry(String key, Map<String, Double> defaultValue, int minLength, int maxLength, Double minValue, Double maxValue) {
        super(key, defaultValue, minLength, maxLength, minValue, maxValue);
    }

    @Override
    public IConfigEntryType<IConfigEntry<Map<String, Double>>, Map<String, Double>> getType() {
        //noinspection unchecked
        return (IConfigEntryType<IConfigEntry<Map<String, Double>>, Map<String, Double>>) (IConfigEntryType<?, ?>) ConfigEntryTypes.DOUBLE_MAP;
    }

    @Override
    public IConfigEntryComponent<?, ?> createComponent(String key) {
        return new DoubleMapConfigEntryComponent(key, this);
    }

    public static class Serializer implements IConfigEntrySerializer<IDoubleMapConfigEntry, Map<String, Double>> {

        @Override
        public void encodeNode(IDoubleMapConfigEntry configEntry, NodeTuple nodeTuple) {
            if (nodeTuple.getValueNode() instanceof MappingNode mappingNode) {
                configEntry.setValue(mappingNode.getValue().stream()
                        .filter(n ->
                                n.getKeyNode() instanceof ScalarNode keyNode
                                        && n.getValueNode() instanceof ScalarNode valueNode
                                        && keyNode.getTag().equals(Tag.STR)
                                        && (valueNode.getTag().equals(Tag.FLOAT) || valueNode.getTag().equals(Tag.INT)))                        .collect(Collectors.toMap(
                                n -> ((ScalarNode) n.getKeyNode()).getValue(),
                                n -> Double.parseDouble(((ScalarNode) n.getValueNode()).getValue())
                        )));
            }
        }

        @Override
        public NodeTuple decodeNode(IDoubleMapConfigEntry configEntry) {
            ScalarNode keyNode = configEntry.createKeyNode();
            MappingNode valueNode = new MappingNode(Tag.MAP, configEntry.get().entrySet().stream()
                    .map(e -> new NodeTuple(
                            new ScalarNode(Tag.STR, e.getKey(), ScalarStyle.PLAIN),
                            new ScalarNode(Tag.FLOAT, e.getValue().toString(), ScalarStyle.PLAIN)
                    ))
                    .toList(), FlowStyle.BLOCK);
            return new NodeTuple(keyNode, valueNode);
        }

        @Override
        public void valueToNetwork(RegistryFriendlyByteBuf buf, IDoubleMapConfigEntry configEntry, Map<String, Double> value) {
            buf.writeMap(value, FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeDouble);
        }

        @Override
        public Map<String, Double> valueFromNetwork(RegistryFriendlyByteBuf buf) {
            return buf.readMap(FriendlyByteBuf::readUtf, FriendlyByteBuf::readDouble);
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, IDoubleMapConfigEntry configEntry) {
            buf.writeUtf(configEntry.getKey());
            buf.writeMap(configEntry.get(), FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeDouble);
            buf.writeInt(configEntry.getMinLength());
            buf.writeInt(configEntry.getMaxLength());
            buf.writeDouble(configEntry.getMinValue());
            buf.writeDouble(configEntry.getMaxValue());
        }

        @Override
        public IDoubleMapConfigEntry fromNetwork(RegistryFriendlyByteBuf buf) {
            DoubleMapConfigEntry configEntry = new DoubleMapConfigEntry(
                    buf.readUtf(),
                    buf.readMap(FriendlyByteBuf::readUtf, FriendlyByteBuf::readDouble),
                    buf.readInt(),
                    buf.readInt(),
                    buf.readDouble(),
                    buf.readDouble()
            );
            configEntry.setValue(configEntry.getDefaultValue());
            return configEntry;
        }
    }
}
