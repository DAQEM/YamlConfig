package com.daqem.yamlconfig.impl.config.entry.map.numeric;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.map.numeric.IIntegerMapConfigEntry;
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

import java.util.LinkedHashMap;
import java.util.Map;

public class IntegerMapConfigEntry extends BaseNumericMapConfigEntry<Integer> implements IIntegerMapConfigEntry {

    public IntegerMapConfigEntry(String key, Map<String, Integer> defaultValue) {
        super(key, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public IntegerMapConfigEntry(String key, Map<String, Integer> defaultValue, int minLength, int maxLength) {
        super(key, defaultValue, minLength, maxLength, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public IntegerMapConfigEntry(String key, Map<String, Integer> defaultValue, int minLength, int maxLength, Integer minValue, Integer maxValue) {
        super(key, defaultValue, minLength, maxLength, minValue, maxValue);
    }

    @Override
    public IConfigEntryType<IConfigEntry<Map<String, Integer>>, Map<String, Integer>> getType() {
        //noinspection unchecked
        return (IConfigEntryType<IConfigEntry<Map<String, Integer>>, Map<String, Integer>>) (IConfigEntryType<?, ?>) ConfigEntryTypes.INTEGER_MAP;
    }

    public static class Serializer implements IConfigEntrySerializer<IIntegerMapConfigEntry, Map<String, Integer>> {

        @Override
        public void toNode(IIntegerMapConfigEntry configEntry, IMapNode parentMap) {
            ConfigMapNode mapNode = new ConfigMapNode();
            for (Map.Entry<String, Integer> entry : configEntry.get().entrySet()) {
                mapNode.put(entry.getKey(), new ConfigValueNode<>(entry.getValue()));
            }
            mapNode.setComments(configEntry.getComments().getComments());
            parentMap.put(configEntry.getKey(), mapNode);
        }

        @Override
        public void fromNode(IIntegerMapConfigEntry configEntry, IMapNode parentMap) {
            IConfigNode node = parentMap.get(configEntry.getKey());
            if (node instanceof IMapNode mapNode) {
                Map<String, Integer> map = new LinkedHashMap<>();
                for (Map.Entry<String, IConfigNode> entry : mapNode.entrySet()) {
                    if (entry.getValue() instanceof IValueNode<?> valueNode && valueNode.getValue() instanceof Number number) {
                        map.put(entry.getKey(), number.intValue());
                    }
                }
                configEntry.set(map);
            }
        }

        @Override
        public void valueToNetwork(RegistryFriendlyByteBuf buf, IIntegerMapConfigEntry configEntry, Map<String, Integer> value) {
            buf.writeMap(value, FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeInt);
        }

        @Override
        public Map<String, Integer> valueFromNetwork(RegistryFriendlyByteBuf buf) {
            return buf.readMap(FriendlyByteBuf::readUtf, FriendlyByteBuf::readInt);
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, IIntegerMapConfigEntry configEntry) {
            buf.writeUtf(configEntry.getKey());
            buf.writeMap(configEntry.getDefaultValue(), FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeInt);
            buf.writeInt(configEntry.getMinLength());
            buf.writeInt(configEntry.getMaxLength());
            buf.writeInt(configEntry.getMinValue());
            buf.writeInt(configEntry.getMaxValue());
            buf.writeMap(configEntry.get(), FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeInt);
            buf.writeCollection(configEntry.getComments().getComments(false), FriendlyByteBuf::writeUtf);
        }

        @Override
        public IIntegerMapConfigEntry fromNetwork(RegistryFriendlyByteBuf buf) {
            IntegerMapConfigEntry configEntry = new IntegerMapConfigEntry(
                    buf.readUtf(),
                    buf.readMap(FriendlyByteBuf::readUtf, FriendlyByteBuf::readInt),
                    buf.readInt(),
                    buf.readInt(),
                    buf.readInt(),
                    buf.readInt()
            );
            configEntry.set(buf.readMap(FriendlyByteBuf::readUtf, FriendlyByteBuf::readInt));
            buf.readList(FriendlyByteBuf::readUtf).forEach(configEntry.getComments()::addComment);
            return configEntry;
        }
    }
}
