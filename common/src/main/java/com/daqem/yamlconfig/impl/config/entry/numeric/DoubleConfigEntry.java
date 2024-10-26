package com.daqem.yamlconfig.impl.config.entry.numeric;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.numeric.IDoubleConfigEntry;
import com.daqem.yamlconfig.api.config.entry.serializer.IConfigEntrySerializer;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.api.gui.component.IConfigEntryComponent;
import com.daqem.yamlconfig.client.gui.component.entry.numeric.DoubleConfigEntryComponent;
import com.daqem.yamlconfig.impl.config.entry.type.ConfigEntryTypes;
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

    @Override
    public IConfigEntryComponent<?, ?> createComponent(String key) {
        return new DoubleConfigEntryComponent(key, this);
    }

    public static class Serializer implements IConfigEntrySerializer<IDoubleConfigEntry, Double> {

        @Override
        public void encodeNode(IDoubleConfigEntry configEntry, NodeTuple nodeTuple) {
            if (nodeTuple.getValueNode() instanceof ScalarNode scalarNode && scalarNode.getTag().equals(Tag.FLOAT)) {
                configEntry.setValue(Double.parseDouble(scalarNode.getValue()));
            }
        }

        @Override
        public NodeTuple decodeNode(IDoubleConfigEntry configEntry) {
            ScalarNode keyNode = configEntry.createKeyNode();
            ScalarNode valueNode = new ScalarNode(Tag.FLOAT, Double.toString(configEntry.get()), ScalarStyle.PLAIN);
            return new NodeTuple(keyNode, valueNode);
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
            buf.writeDouble(configEntry.get());
            buf.writeDouble(configEntry.getMinValue());
            buf.writeDouble(configEntry.getMaxValue());
        }

        @Override
        public IDoubleConfigEntry fromNetwork(RegistryFriendlyByteBuf buf) {
            DoubleConfigEntry configEntry = new DoubleConfigEntry(
                    buf.readUtf(),
                    buf.readDouble(),
                    buf.readDouble(),
                    buf.readDouble()
            );
            configEntry.setValue(configEntry.getDefaultValue());
            return configEntry;
        }
    }
}
