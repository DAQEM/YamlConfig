package com.daqem.yamlconfig.impl.config.entry.numeric;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.numeric.IIntegerConfigEntry;
import com.daqem.yamlconfig.api.config.entry.serializer.IConfigEntrySerializer;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.api.gui.component.IConfigEntryComponent;
import com.daqem.yamlconfig.client.gui.component.entry.numeric.IntegerConfigEntryComponent;
import com.daqem.yamlconfig.impl.config.entry.type.ConfigEntryTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.NodeTuple;
import org.snakeyaml.engine.v2.nodes.ScalarNode;
import org.snakeyaml.engine.v2.nodes.Tag;

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

    @Override
    public IConfigEntryComponent<?, ?> createComponent(String key) {
        return new IntegerConfigEntryComponent(key, this);
    }

    public static class Serializer implements IConfigEntrySerializer<IIntegerConfigEntry, Integer> {

        @Override
        public void encodeNode(IIntegerConfigEntry configEntry, NodeTuple nodeTuple) {
            if (nodeTuple.getValueNode() instanceof ScalarNode scalarNode && scalarNode.getTag().equals(Tag.INT)) {
                configEntry.set(Integer.parseInt(scalarNode.getValue()));
            }
        }

        @Override
        public NodeTuple decodeNode(IIntegerConfigEntry configEntry) {
            ScalarNode keyNode = configEntry.createKeyNode();
            ScalarNode valueNode = new ScalarNode(Tag.INT, Integer.toString(configEntry.get()), ScalarStyle.PLAIN);
            return new NodeTuple(keyNode, valueNode);
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
            buf.writeInt(configEntry.get());
            buf.writeInt(configEntry.getMinValue());
            buf.writeInt(configEntry.getMaxValue());
        }

        @Override
        public IIntegerConfigEntry fromNetwork(RegistryFriendlyByteBuf buf) {
            IntegerConfigEntry configEntry = new IntegerConfigEntry(
                    buf.readUtf(),
                    buf.readInt(),
                    buf.readInt(),
                    buf.readInt()
            );
            configEntry.set(configEntry.getDefaultValue());
            return configEntry;
        }
    }
}
