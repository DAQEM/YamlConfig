package com.daqem.yamlconfig.impl.config.entry.numeric;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.numeric.ILongConfigEntry;
import com.daqem.yamlconfig.api.config.entry.serializer.IConfigEntrySerializer;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.api.node.IConfigNode;
import com.daqem.yamlconfig.api.node.IMapNode;
import com.daqem.yamlconfig.api.node.IValueNode;
import com.daqem.yamlconfig.impl.config.entry.type.ConfigEntryTypes;
import com.daqem.yamlconfig.impl.node.ConfigValueNode;
import net.minecraft.network.RegistryFriendlyByteBuf;

public class LongConfigEntry extends BaseNumericConfigEntry<Long> implements ILongConfigEntry {

    public LongConfigEntry(String key, long defaultValue) {
        super(key, defaultValue, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    public LongConfigEntry(String key, long defaultValue, long minValue, long maxValue) {
        super(key, defaultValue, minValue, maxValue);
    }

    @Override
    public IConfigEntryType<IConfigEntry<Long>, Long> getType() {
        //noinspection unchecked
        return (IConfigEntryType<IConfigEntry<Long>, Long>) (IConfigEntryType<?, ?>) ConfigEntryTypes.LONG;
    }

    public static class Serializer implements IConfigEntrySerializer<ILongConfigEntry, Long> {

        @Override
        public void toNode(ILongConfigEntry configEntry, IMapNode parentMap) {
            ConfigValueNode<Long> node = new ConfigValueNode<>(configEntry.get());
            node.setComments(configEntry.getComments().getComments());
            parentMap.put(configEntry.getKey(), node);
        }

        @Override
        public void fromNode(ILongConfigEntry configEntry, IMapNode parentMap) {
            IConfigNode node = parentMap.get(configEntry.getKey());
            if (node instanceof IValueNode<?> valueNode) {
                Object val = valueNode.getValue();
                if (val instanceof Number number) {
                    configEntry.set(number.longValue());
                } else if (val instanceof String) {
                    try { configEntry.set(Long.parseLong((String) val)); } catch (NumberFormatException ignored) {}
                }
            }
        }

        @Override
        public void valueToNetwork(RegistryFriendlyByteBuf buf, ILongConfigEntry configEntry, Long value) {
            buf.writeLong(value);
        }

        @Override
        public Long valueFromNetwork(RegistryFriendlyByteBuf buf) {
            return buf.readLong();
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, ILongConfigEntry configEntry) {
            buf.writeUtf(configEntry.getKey());
            buf.writeLong(configEntry.getDefaultValue());
            buf.writeLong(configEntry.getMinValue());
            buf.writeLong(configEntry.getMaxValue());
            buf.writeLong(configEntry.get());
            configEntry.getComments().toNetwork(buf);
        }

        @Override
        public ILongConfigEntry fromNetwork(RegistryFriendlyByteBuf buf) {
            LongConfigEntry configEntry = new LongConfigEntry(
                    buf.readUtf(),
                    buf.readLong(),
                    buf.readLong(),
                    buf.readLong()
            );
            configEntry.set(buf.readLong());
            configEntry.getComments().fromNetwork(buf);
            return configEntry;
        }
    }
}
