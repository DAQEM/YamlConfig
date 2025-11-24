package com.daqem.yamlconfig.impl.config.entry.map.numeric;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.map.numeric.IDoubleMapConfigEntry;
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

    public static class Serializer implements IConfigEntrySerializer<IDoubleMapConfigEntry, Map<String, Double>> {

        @Override
        public void toNode(IDoubleMapConfigEntry configEntry, IMapNode parentMap) {
            ConfigMapNode mapNode = new ConfigMapNode();
            for (Map.Entry<String, Double> entry : configEntry.get().entrySet()) {
                mapNode.put(entry.getKey(), new ConfigValueNode<>(entry.getValue()));
            }
            mapNode.setComments(configEntry.getComments().getComments());
            parentMap.put(configEntry.getKey(), mapNode);
        }

        @Override
        public void fromNode(IDoubleMapConfigEntry configEntry, IMapNode parentMap) {
            IConfigNode node = parentMap.get(configEntry.getKey());
            if (node instanceof IMapNode mapNode) {
                Map<String, Double> map = new LinkedHashMap<>();
                for (Map.Entry<String, IConfigNode> entry : mapNode.entrySet()) {
                    if (entry.getValue() instanceof IValueNode<?> valueNode && valueNode.getValue() instanceof Number number) {
                        map.put(entry.getKey(), number.doubleValue());
                    }
                }
                configEntry.set(map);
            }
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
            buf.writeMap(configEntry.getDefaultValue(), FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeDouble);
            buf.writeInt(configEntry.getMinLength());
            buf.writeInt(configEntry.getMaxLength());
            buf.writeDouble(configEntry.getMinValue());
            buf.writeDouble(configEntry.getMaxValue());
            buf.writeMap(configEntry.get(), FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeDouble);
            buf.writeCollection(configEntry.getComments().getComments(false), FriendlyByteBuf::writeUtf);
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
            configEntry.set(buf.readMap(FriendlyByteBuf::readUtf, FriendlyByteBuf::readDouble));
            buf.readList(FriendlyByteBuf::readUtf).forEach(configEntry.getComments()::addComment);
            return configEntry;
        }
    }
}
