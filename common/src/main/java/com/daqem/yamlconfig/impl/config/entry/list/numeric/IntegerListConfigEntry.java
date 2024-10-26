package com.daqem.yamlconfig.impl.config.entry.list.numeric;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.list.numeric.IIntegerListConfigEntry;
import com.daqem.yamlconfig.api.config.entry.serializer.IConfigEntrySerializer;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.api.gui.component.IConfigEntryComponent;
import com.daqem.yamlconfig.client.gui.component.entry.list.numeric.IntegerListConfigEntryComponent;
import com.daqem.yamlconfig.impl.config.entry.type.ConfigEntryTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.snakeyaml.engine.v2.common.FlowStyle;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.*;

import java.util.List;

public class IntegerListConfigEntry extends BaseNumericListConfigEntry<Integer> implements IIntegerListConfigEntry {

    public IntegerListConfigEntry(String key, List<Integer> value) {
        super(key, value, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public IntegerListConfigEntry(String key, List<Integer> value, int minLength, int maxLength) {
        super(key, value, minLength, maxLength, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public IntegerListConfigEntry(String key, List<Integer> value, int minLength, int maxLength, Integer minValue, Integer maxValue) {
        super(key, value, minLength, maxLength, minValue, maxValue);
    }

    @Override
    public IConfigEntryType<IConfigEntry<List<Integer>>, List<Integer>> getType() {
        //noinspection unchecked
        return (IConfigEntryType<IConfigEntry<List<Integer>>, List<Integer>>) (IConfigEntryType<?, ?>) ConfigEntryTypes.INTEGER_LIST;
    }

    @Override
    public IConfigEntryComponent<?, ?> createComponent(String key) {
        return new IntegerListConfigEntryComponent(key, this);
    }

    public static class Serializer implements IConfigEntrySerializer<IIntegerListConfigEntry, List<Integer>> {

        @Override
        public void encodeNode(IIntegerListConfigEntry configEntry, NodeTuple nodeTuple) {
            if (nodeTuple.getValueNode() instanceof SequenceNode sequenceNode) {
                configEntry.set(sequenceNode.getValue().stream()
                        .filter(n -> n instanceof ScalarNode scalarNode && scalarNode.getTag().equals(Tag.INT))
                        .map(n -> Integer.parseInt(((ScalarNode) n).getValue()))
                        .toList());
            }
        }

        @Override
        public NodeTuple decodeNode(IIntegerListConfigEntry configEntry) {
            ScalarNode keyNode = configEntry.createKeyNode();
            SequenceNode valueNode = new SequenceNode(Tag.SEQ, configEntry.get().stream()
                    .map(s -> (Node) new ScalarNode(Tag.INT, Integer.toString(s), ScalarStyle.PLAIN))
                    .toList(), FlowStyle.BLOCK);
            return new NodeTuple(keyNode, valueNode);
        }

        @Override
        public void valueToNetwork(RegistryFriendlyByteBuf buf, IIntegerListConfigEntry configEntry, List<Integer> value) {
            buf.writeCollection(value, FriendlyByteBuf::writeInt);
        }

        @Override
        public List<Integer> valueFromNetwork(RegistryFriendlyByteBuf buf) {
            return buf.readList(FriendlyByteBuf::readInt);
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, IIntegerListConfigEntry configEntry) {
            buf.writeUtf(configEntry.getKey());
            buf.writeCollection(configEntry.get(), FriendlyByteBuf::writeInt);
            buf.writeInt(configEntry.getMinLength());
            buf.writeInt(configEntry.getMaxLength());
            buf.writeInt(configEntry.getMinValue());
            buf.writeInt(configEntry.getMaxValue());
        }

        @Override
        public IIntegerListConfigEntry fromNetwork(RegistryFriendlyByteBuf buf) {
            String key = buf.readUtf();
            List<Integer> value = buf.readList(FriendlyByteBuf::readInt);
            int minLength = buf.readInt();
            int maxLength = buf.readInt();
            int minValue = buf.readInt();
            int maxValue = buf.readInt();
            IntegerListConfigEntry configEntry = new IntegerListConfigEntry(key, value, minLength, maxLength, minValue, maxValue);
            configEntry.set(configEntry.getDefaultValue());
            return configEntry;
        }
    }
}
