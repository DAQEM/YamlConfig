package com.daqem.yamlconfig.impl.config.entry.numeric;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.numeric.IFloatConfigEntry;
import com.daqem.yamlconfig.api.config.entry.serializer.IConfigEntrySerializer;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.api.gui.component.IConfigEntryComponent;
import com.daqem.yamlconfig.client.gui.component.entry.numeric.FloatConfigEntryComponent;
import com.daqem.yamlconfig.impl.config.entry.type.ConfigEntryTypes;
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

    @Override
    public IConfigEntryComponent<?, ?> createComponent(String key) {
        return new FloatConfigEntryComponent(key, this);
    }

    public static class Serializer implements IConfigEntrySerializer<IFloatConfigEntry, Float> {

        @Override
        public void encodeNode(IFloatConfigEntry configEntry, NodeTuple nodeTuple) {
            if (nodeTuple.getValueNode() instanceof ScalarNode scalarNode && scalarNode.getTag().equals(Tag.FLOAT)) {
                configEntry.setValue(Float.parseFloat(scalarNode.getValue()));
            }
        }

        @Override
        public NodeTuple decodeNode(IFloatConfigEntry configEntry) {
            ScalarNode keyNode = configEntry.createKeyNode();
            ScalarNode valueNode = new ScalarNode(Tag.FLOAT, Float.toString(configEntry.get()), ScalarStyle.PLAIN);
            return new NodeTuple(keyNode, valueNode);
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
            buf.writeFloat(configEntry.get());
            buf.writeFloat(configEntry.getMinValue());
            buf.writeFloat(configEntry.getMaxValue());

        }

        @Override
        public IFloatConfigEntry fromNetwork(RegistryFriendlyByteBuf buf) {
            FloatConfigEntry configEntry = new FloatConfigEntry(
                    buf.readUtf(),
                    buf.readFloat(),
                    buf.readFloat(),
                    buf.readFloat()
            );
            configEntry.setValue(configEntry.getDefaultValue());
            return configEntry;
        }
    }
}
