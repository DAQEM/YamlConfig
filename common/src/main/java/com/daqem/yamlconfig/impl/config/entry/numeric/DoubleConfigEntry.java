package com.daqem.yamlconfig.impl.config.entry.numeric;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.numeric.IDoubleConfigEntry;
import com.daqem.yamlconfig.api.config.entry.serializer.IConfigEntrySerializer;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.api.node.IConfigNode;
import com.daqem.yamlconfig.api.node.IMapNode;
import com.daqem.yamlconfig.api.node.IValueNode;
import com.daqem.yamlconfig.impl.config.entry.type.ConfigEntryTypes;
import com.daqem.yamlconfig.impl.node.ConfigValueNode;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.NodeTuple;
import org.snakeyaml.engine.v2.nodes.ScalarNode;
import org.snakeyaml.engine.v2.nodes.Tag;

public class DoubleConfigEntry extends BaseNumericConfigEntry<Double> implements IDoubleConfigEntry {

    public DoubleConfigEntry(String key, Double defaultValue) {
        super(key, defaultValue, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    public DoubleConfigEntry(String key, Double defaultValue, Double minValue, Double maxValue) {
        super(key, defaultValue, minValue, maxValue);
    }

    @Override
    public IConfigEntryType<IConfigEntry<Double>, Double> getType() {
        //noinspection unchecked
        return (IConfigEntryType<IConfigEntry<Double>, Double>) (IConfigEntryType<?, ?>) ConfigEntryTypes.DOUBLE;
    }

    public static class Serializer implements IConfigEntrySerializer<IDoubleConfigEntry, Double> {

        @Override
        public void toNode(IDoubleConfigEntry configEntry, IMapNode parentMap) {
            ConfigValueNode<Double> node = new ConfigValueNode<>(configEntry.get());
            node.setComments(configEntry.getComments().getComments());
            parentMap.put(configEntry.getKey(), node);
        }

        @Override
        public void fromNode(IDoubleConfigEntry configEntry, IMapNode parentMap) {
            IConfigNode node = parentMap.get(configEntry.getKey());
            if (node instanceof IValueNode<?> valueNode) {
                Object val = valueNode.getValue();
                if (val instanceof Number number) {
                    configEntry.set(number.doubleValue());
                } else if (val instanceof String) {
                    try { configEntry.set(Double.parseDouble((String) val)); } catch (NumberFormatException ignored) {}
                }
            }
        }

        @Override
        public void valueToNetwork(RegistryFriendlyByteBuf buf, IDoubleConfigEntry configEntry, Double value) {
            buf.writeDouble(value);
        }

        @Override
        public Double valueFromNetwork(RegistryFriendlyByteBuf buf) {
            return buf.readDouble();
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, IDoubleConfigEntry configEntry) {
            buf.writeUtf(configEntry.getKey());
            buf.writeDouble(configEntry.getDefaultValue());
            buf.writeDouble(configEntry.getMinValue());
            buf.writeDouble(configEntry.getMaxValue());
            buf.writeDouble(configEntry.get());
            buf.writeCollection(configEntry.getComments().getComments(false), FriendlyByteBuf::writeUtf);
        }

        @Override
        public IDoubleConfigEntry fromNetwork(RegistryFriendlyByteBuf buf) {
            DoubleConfigEntry configEntry = new DoubleConfigEntry(
                    buf.readUtf(),
                    buf.readDouble(),
                    buf.readDouble(),
                    buf.readDouble()
            );
            configEntry.set(buf.readDouble());
            buf.readList(FriendlyByteBuf::readUtf).forEach(configEntry.getComments()::addComment);
            return configEntry;
        }
    }
}
