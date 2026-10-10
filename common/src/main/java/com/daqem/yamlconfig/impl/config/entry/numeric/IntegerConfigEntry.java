package com.daqem.yamlconfig.impl.config.entry.numeric;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.numeric.IIntegerConfigEntry;
import com.daqem.yamlconfig.api.config.entry.serializer.IConfigEntrySerializer;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.api.node.IConfigNode;
import com.daqem.yamlconfig.api.node.IMapNode;
import com.daqem.yamlconfig.api.node.IValueNode;
import com.daqem.yamlconfig.impl.config.entry.type.ConfigEntryTypes;
import com.daqem.yamlconfig.impl.node.ConfigValueNode;
import net.minecraft.network.RegistryFriendlyByteBuf;

public class IntegerConfigEntry extends BaseNumericConfigEntry<Integer> implements IIntegerConfigEntry {

    public IntegerConfigEntry(String key, int defaultValue) {
        super(key, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public IntegerConfigEntry(String key, int defaultValue, int minValue, int maxValue) {
        super(key, defaultValue, minValue, maxValue);
    }

    @Override
    public IConfigEntryType<IConfigEntry<Integer>, Integer> getType() {
        //noinspection unchecked
        return (IConfigEntryType<IConfigEntry<Integer>, Integer>) (IConfigEntryType<?, ?>) ConfigEntryTypes.INTEGER;
    }

    public static class Serializer implements IConfigEntrySerializer<IIntegerConfigEntry, Integer> {

        @Override
        public void toNode(IIntegerConfigEntry configEntry, IMapNode parentMap) {
            ConfigValueNode<Integer> node = new ConfigValueNode<>(configEntry.get());
            node.setComments(configEntry.getComments().getComments());
            parentMap.put(configEntry.getKey(), node);
        }

        @Override
        public void fromNode(IIntegerConfigEntry configEntry, IMapNode parentMap) {
            IConfigNode node = parentMap.get(configEntry.getKey());
            if (node instanceof IValueNode<?> valueNode) {
                Object val = valueNode.getValue();
                if (val instanceof Number number) {
                    configEntry.set(number.intValue());
                } else if (val instanceof String) {
                    try { configEntry.set(Integer.parseInt((String) val)); } catch (NumberFormatException ignored) {}
                }
            }
        }

        @Override
        public void valueToNetwork(RegistryFriendlyByteBuf buf, IIntegerConfigEntry configEntry, Integer value) {
            buf.writeInt(value);
        }

        @Override
        public Integer valueFromNetwork(RegistryFriendlyByteBuf buf) {
            return buf.readInt();
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, IIntegerConfigEntry configEntry) {
            buf.writeUtf(configEntry.getKey());
            buf.writeInt(configEntry.getDefaultValue());
            buf.writeInt(configEntry.getMinValue());
            buf.writeInt(configEntry.getMaxValue());
            buf.writeInt(configEntry.get());
            configEntry.getComments().toNetwork(buf);
        }

        @Override
        public IIntegerConfigEntry fromNetwork(RegistryFriendlyByteBuf buf) {
            IntegerConfigEntry configEntry = new IntegerConfigEntry(
                    buf.readUtf(),
                    buf.readInt(),
                    buf.readInt(),
                    buf.readInt()
            );
            configEntry.set(buf.readInt());
            configEntry.getComments().fromNetwork(buf);
            return configEntry;
        }
    }
}
