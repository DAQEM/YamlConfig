package com.daqem.yamlconfig.impl.config.entry.map.numeric;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.map.numeric.IFloatMapConfigEntry;
import com.daqem.yamlconfig.api.config.entry.serializer.IConfigEntrySerializer;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.api.gui.component.IConfigEntryComponent;
import com.daqem.yamlconfig.client.gui.component.entry.map.numeric.FloatMapConfigEntryComponent;
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

public class FloatMapConfigEntry extends BaseNumericMapConfigEntry<Float> implements IFloatMapConfigEntry {

    public FloatMapConfigEntry(String key, Map<String, Float> defaultValue) {
        super(key, defaultValue, Float.MIN_VALUE, Float.MAX_VALUE);
    }

    public FloatMapConfigEntry(String key, Map<String, Float> defaultValue, int minLength, int maxLength) {
        super(key, defaultValue, minLength, maxLength, Float.MIN_VALUE, Float.MAX_VALUE);
    }

    public FloatMapConfigEntry(String key, Map<String, Float> defaultValue, int minLength, int maxLength, Float minValue, Float maxValue) {
        super(key, defaultValue, minLength, maxLength, minValue, maxValue);
    }

    @Override
    public IConfigEntryType<IConfigEntry<Map<String, Float>>, Map<String, Float>> getType() {
        //noinspection unchecked
        return (IConfigEntryType<IConfigEntry<Map<String, Float>>, Map<String, Float>>) (IConfigEntryType<?, ?>) ConfigEntryTypes.FLOAT_MAP;
    }

    @Override
    public IConfigEntryComponent<?, ?> createComponent(String key) {
        return new FloatMapConfigEntryComponent(key, this);
    }

    public static class Serializer implements IConfigEntrySerializer<IFloatMapConfigEntry, Map<String, Float>> {

        @Override
        public void encodeNode(IFloatMapConfigEntry configEntry, NodeTuple nodeTuple) {
            if (nodeTuple.getValueNode() instanceof MappingNode mappingNode) {
                configEntry.setValue(mappingNode.getValue().stream()
                        .filter(n ->
                                n.getKeyNode() instanceof ScalarNode keyNode
                                        && n.getValueNode() instanceof ScalarNode valueNode
                                        && keyNode.getTag().equals(Tag.STR)
                                        && (valueNode.getTag().equals(Tag.FLOAT) || valueNode.getTag().equals(Tag.INT)))
                        .collect(Collectors.toMap(
                                n -> ((ScalarNode) n.getKeyNode()).getValue(),
                                n -> Float.parseFloat(((ScalarNode) n.getValueNode()).getValue())
                        )));
            }
        }

        @Override
        public NodeTuple decodeNode(IFloatMapConfigEntry configEntry) {
            ScalarNode keyNode = configEntry.createKeyNode();
            MappingNode valueNode = new MappingNode(Tag.MAP, configEntry.get().entrySet().stream()
                    .map(e -> new NodeTuple(
                            new ScalarNode(Tag.STR, e.getKey(), ScalarStyle.SINGLE_QUOTED),
                            new ScalarNode(Tag.FLOAT, e.getValue().toString(), ScalarStyle.PLAIN)
                    ))
                    .toList(), FlowStyle.BLOCK);
            return new NodeTuple(keyNode, valueNode);
        }

        @Override
        public void valueToNetwork(RegistryFriendlyByteBuf buf, IFloatMapConfigEntry configEntry, Map<String, Float> value) {
            buf.writeMap(value, FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeFloat);
        }

        @Override
        public Map<String, Float> valueFromNetwork(RegistryFriendlyByteBuf buf) {
            return buf.readMap(FriendlyByteBuf::readUtf, FriendlyByteBuf::readFloat);
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, IFloatMapConfigEntry configEntry) {
            buf.writeUtf(configEntry.getKey());
            buf.writeMap(configEntry.get(), FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeFloat);
            buf.writeInt(configEntry.getMinLength());
            buf.writeInt(configEntry.getMaxLength());
            buf.writeFloat(configEntry.getMinValue());
            buf.writeFloat(configEntry.getMaxValue());
        }

        @Override
        public IFloatMapConfigEntry fromNetwork(RegistryFriendlyByteBuf buf) {
            FloatMapConfigEntry configEntry = new FloatMapConfigEntry(
                    buf.readUtf(),
                    buf.readMap(FriendlyByteBuf::readUtf, FriendlyByteBuf::readFloat),
                    buf.readInt(),
                    buf.readInt(),
                    buf.readFloat(),
                    buf.readFloat()
            );
            configEntry.setValue(configEntry.getDefaultValue());
            return configEntry;
        }
    }
}
