package com.daqem.yamlconfig.impl.config.entry.map.numeric;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.map.numeric.IFloatMapConfigEntry;
import com.daqem.yamlconfig.api.config.entry.serializer.IConfigEntrySerializer;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.api.node.IConfigNode;
import com.daqem.yamlconfig.api.node.IMapNode;
import com.daqem.yamlconfig.api.node.IValueNode;
import com.daqem.yamlconfig.impl.config.entry.type.ConfigEntryTypes;
import com.daqem.yamlconfig.impl.node.ConfigMapNode;
import com.daqem.yamlconfig.impl.node.ConfigValueNode;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.snakeyaml.engine.v2.common.FlowStyle;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.MappingNode;
import org.snakeyaml.engine.v2.nodes.NodeTuple;
import org.snakeyaml.engine.v2.nodes.ScalarNode;
import org.snakeyaml.engine.v2.nodes.Tag;

import java.util.LinkedHashMap;
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

    public static class Serializer implements IConfigEntrySerializer<IFloatMapConfigEntry, Map<String, Float>> {

        @Override
        public void toNode(IFloatMapConfigEntry configEntry, IMapNode parentMap) {
            ConfigMapNode mapNode = new ConfigMapNode();
            for (Map.Entry<String, Float> entry : configEntry.get().entrySet()) {
                mapNode.put(entry.getKey(), new ConfigValueNode<>(entry.getValue()));
            }
            mapNode.setComments(configEntry.getComments().getComments());
            parentMap.put(configEntry.getKey(), mapNode);
        }

        @Override
        public void fromNode(IFloatMapConfigEntry configEntry, IMapNode parentMap) {
            IConfigNode node = parentMap.get(configEntry.getKey());
            if (node instanceof IMapNode mapNode) {
                Map<String, Float> map = new LinkedHashMap<>();
                for (Map.Entry<String, IConfigNode> entry : mapNode.entrySet()) {
                    if (entry.getValue() instanceof IValueNode<?> valueNode && valueNode.getValue() instanceof Number number) {
                        map.put(entry.getKey(), number.floatValue());
                    }
                }
                configEntry.set(map);
            }
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
            buf.writeMap(configEntry.getDefaultValue(), FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeFloat);
            buf.writeInt(configEntry.getMinLength());
            buf.writeInt(configEntry.getMaxLength());
            buf.writeFloat(configEntry.getMinValue());
            buf.writeFloat(configEntry.getMaxValue());
            buf.writeMap(configEntry.get(), FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeFloat);
            buf.writeCollection(configEntry.getComments().getComments(false), FriendlyByteBuf::writeUtf);
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
            configEntry.set(buf.readMap(FriendlyByteBuf::readUtf, FriendlyByteBuf::readFloat));
            buf.readList(FriendlyByteBuf::readUtf).forEach(configEntry.getComments()::addComment);
            return configEntry;
        }
    }
}
