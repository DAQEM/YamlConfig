package com.daqem.yamlconfig.impl.config.entry.numeric;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.numeric.IFloatConfigEntry;
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

public class FloatConfigEntry extends BaseNumericConfigEntry<Float> implements IFloatConfigEntry {

    public FloatConfigEntry(String key, Float defaultValue) {
        super(key, defaultValue, Float.MIN_VALUE, Float.MAX_VALUE);
    }

    public FloatConfigEntry(String key, Float defaultValue, Float minValue, Float maxValue) {
        super(key, defaultValue, minValue, maxValue);
    }

    @Override
    public IConfigEntryType<IConfigEntry<Float>, Float> getType() {
        //noinspection unchecked
        return (IConfigEntryType<IConfigEntry<Float>, Float>) (IConfigEntryType<?, ?>) ConfigEntryTypes.FLOAT;
    }

    public static class Serializer implements IConfigEntrySerializer<IFloatConfigEntry, Float> {

        @Override
        public void toNode(IFloatConfigEntry configEntry, IMapNode parentMap) {
            ConfigValueNode<Float> node = new ConfigValueNode<>(configEntry.get());
            node.setComments(configEntry.getComments().getComments());
            parentMap.put(configEntry.getKey(), node);
        }

        @Override
        public void fromNode(IFloatConfigEntry configEntry, IMapNode parentMap) {
            IConfigNode node = parentMap.get(configEntry.getKey());
            if (node instanceof IValueNode<?> valueNode) {
                Object val = valueNode.getValue();
                if (val instanceof Number number) {
                    configEntry.set(number.floatValue());
                } else if (val instanceof String) {
                    try { configEntry.set(Float.parseFloat((String) val)); } catch (NumberFormatException ignored) {}
                }
            }
        }

        @Override
        public void valueToNetwork(RegistryFriendlyByteBuf buf, IFloatConfigEntry configEntry, Float value) {
            buf.writeFloat(value);
        }

        @Override
        public Float valueFromNetwork(RegistryFriendlyByteBuf buf) {
            return buf.readFloat();
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, IFloatConfigEntry configEntry) {
            buf.writeUtf(configEntry.getKey());
            buf.writeFloat(configEntry.getDefaultValue());
            buf.writeFloat(configEntry.getMinValue());
            buf.writeFloat(configEntry.getMaxValue());
            buf.writeFloat(configEntry.get());
            buf.writeCollection(configEntry.getComments().getComments(false), FriendlyByteBuf::writeUtf);
        }

        @Override
        public IFloatConfigEntry fromNetwork(RegistryFriendlyByteBuf buf) {
            FloatConfigEntry configEntry = new FloatConfigEntry(
                    buf.readUtf(),
                    buf.readFloat(),
                    buf.readFloat(),
                    buf.readFloat()
            );
            configEntry.set(buf.readFloat());
            buf.readList(FriendlyByteBuf::readUtf).forEach(configEntry.getComments()::addComment);
            return configEntry;
        }
    }
}
