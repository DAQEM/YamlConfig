package com.daqem.yamlconfig.impl.config.entry.numeric;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.numeric.IDoubleConfigEntry;
import com.daqem.yamlconfig.api.config.entry.serializer.IConfigEntrySerializer;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.impl.config.entry.type.ConfigEntryTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.NodeTuple;
import org.snakeyaml.engine.v2.nodes.ScalarNode;
import org.snakeyaml.engine.v2.nodes.Tag;

public class DoubleConfigEntry extends BaseNumericConfigEntry<Double> implements IDoubleConfigEntry {

    public DoubleConfigEntry(String key, Double defaultValue) {
        super(key, defaultValue);
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
        public void toNetwork(RegistryFriendlyByteBuf buf, IDoubleConfigEntry configEntry, Double value) {
            buf.writeDouble(value);
        }

        @Override
        public Double fromNetwork(RegistryFriendlyByteBuf buf) {
            return buf.readDouble();
        }
    }
}
