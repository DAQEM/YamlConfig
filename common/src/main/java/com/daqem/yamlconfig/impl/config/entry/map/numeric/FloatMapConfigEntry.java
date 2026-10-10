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
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.LinkedHashMap;
import java.util.Map;

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

        private static final StreamCodec<ByteBuf, Map<String, Float>> MAP_CODEC =
                ByteBufCodecs.map(LinkedHashMap::new, ByteBufCodecs.STRING_UTF8, ByteBufCodecs.FLOAT);

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
            MAP_CODEC.encode(buf, value);
        }

        @Override
        public Map<String, Float> valueFromNetwork(RegistryFriendlyByteBuf buf) {
            return MAP_CODEC.decode(buf);
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, IFloatMapConfigEntry configEntry) {
            buf.writeUtf(configEntry.getKey());
            MAP_CODEC.encode(buf, configEntry.getDefaultValue());
            buf.writeInt(configEntry.getMinLength());
            buf.writeInt(configEntry.getMaxLength());
            buf.writeFloat(configEntry.getMinValue());
            buf.writeFloat(configEntry.getMaxValue());
            MAP_CODEC.encode(buf, configEntry.get());
            configEntry.getComments().toNetwork(buf);
        }

        @Override
        public IFloatMapConfigEntry fromNetwork(RegistryFriendlyByteBuf buf) {
            String key = buf.readUtf();
            Map<String, Float> defaultValue = MAP_CODEC.decode(buf);
            int minLength = buf.readInt();
            int maxLength = buf.readInt();
            float minValue = buf.readFloat();
            float maxValue = buf.readFloat();
            Map<String, Float> value = MAP_CODEC.decode(buf);
            FloatMapConfigEntry configEntry = new FloatMapConfigEntry(key, defaultValue, minLength, maxLength, minValue, maxValue);
            configEntry.set(value);
            configEntry.getComments().fromNetwork(buf);
            return configEntry;
        }
    }
}
