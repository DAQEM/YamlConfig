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
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.LinkedHashMap;
import java.util.Map;

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

        private static final StreamCodec<ByteBuf, Map<String, Double>> MAP_CODEC =
                ByteBufCodecs.map(LinkedHashMap::new, ByteBufCodecs.STRING_UTF8, ByteBufCodecs.DOUBLE);

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
            MAP_CODEC.encode(buf, value);
        }

        @Override
        public Map<String, Double> valueFromNetwork(RegistryFriendlyByteBuf buf) {
            return MAP_CODEC.decode(buf);
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, IDoubleMapConfigEntry configEntry) {
            buf.writeUtf(configEntry.getKey());
            MAP_CODEC.encode(buf, configEntry.getDefaultValue());
            buf.writeInt(configEntry.getMinLength());
            buf.writeInt(configEntry.getMaxLength());
            buf.writeDouble(configEntry.getMinValue());
            buf.writeDouble(configEntry.getMaxValue());
            MAP_CODEC.encode(buf, configEntry.get());
            configEntry.getComments().toNetwork(buf);
        }

        @Override
        public IDoubleMapConfigEntry fromNetwork(RegistryFriendlyByteBuf buf) {
            String key = buf.readUtf();
            Map<String, Double> defaultValue = MAP_CODEC.decode(buf);
            int minLength = buf.readInt();
            int maxLength = buf.readInt();
            double minValue = buf.readDouble();
            double maxValue = buf.readDouble();
            Map<String, Double> value = MAP_CODEC.decode(buf);
            DoubleMapConfigEntry configEntry = new DoubleMapConfigEntry(key, defaultValue, minLength, maxLength, minValue, maxValue);
            configEntry.set(value);
            configEntry.getComments().fromNetwork(buf);
            return configEntry;
        }
    }
}
