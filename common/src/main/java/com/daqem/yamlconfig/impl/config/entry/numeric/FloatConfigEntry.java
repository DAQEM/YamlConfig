package com.daqem.yamlconfig.impl.config.entry.numeric;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.numeric.IFloatConfigEntry;
import com.daqem.yamlconfig.api.config.entry.serializer.IConfigEntrySerializer;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.impl.config.entry.type.ConfigEntryTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.NodeTuple;
import org.snakeyaml.engine.v2.nodes.ScalarNode;
import org.snakeyaml.engine.v2.nodes.Tag;

public class FloatConfigEntry extends BaseNumericConfigEntry<Float> implements IFloatConfigEntry {

    public FloatConfigEntry(String key, Float defaultValue) {
        super(key, defaultValue);
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
        public void toNetwork(RegistryFriendlyByteBuf buf, IFloatConfigEntry configEntry, Float value) {
            buf.writeFloat(value);
        }

        @Override
        public Float fromNetwork(RegistryFriendlyByteBuf buf) {
            return buf.readFloat();
        }
    }
}
