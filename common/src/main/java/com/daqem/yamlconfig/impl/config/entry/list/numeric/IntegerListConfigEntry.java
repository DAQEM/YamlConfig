package com.daqem.yamlconfig.impl.config.entry.list.numeric;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.list.numeric.IIntegerListConfigEntry;
import com.daqem.yamlconfig.api.config.entry.serializer.IConfigEntrySerializer;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.impl.config.entry.type.ConfigEntryTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.snakeyaml.engine.v2.common.FlowStyle;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.*;

import java.util.List;

public class IntegerListConfigEntry extends BaseNumericListConfigEntry<Integer> implements IIntegerListConfigEntry {

    public IntegerListConfigEntry(String key, List<Integer> value) {
        super(key, value);
    }

    public IntegerListConfigEntry(String key, List<Integer> value, int minLength, int maxLength) {
        super(key, value, minLength, maxLength);
    }

    public IntegerListConfigEntry(String key, List<Integer> value, int minLength, int maxLength, Integer minValue, Integer maxValue) {
        super(key, value, minLength, maxLength, minValue, maxValue);
    }

    @Override
    public IConfigEntryType<IConfigEntry<List<Integer>>, List<Integer>> getType() {
        //noinspection unchecked
        return (IConfigEntryType<IConfigEntry<List<Integer>>, List<Integer>>) (IConfigEntryType<?, ?>) ConfigEntryTypes.INTEGER_LIST;
    }

    public static class Serializer implements IConfigEntrySerializer<IIntegerListConfigEntry, List<Integer>> {

        @Override
        public void encodeNode(IIntegerListConfigEntry configEntry, NodeTuple nodeTuple) {
            if (nodeTuple.getValueNode() instanceof SequenceNode sequenceNode) {
                configEntry.setValue(sequenceNode.getValue().stream()
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
        public void toNetwork(RegistryFriendlyByteBuf buf, IIntegerListConfigEntry configEntry, List<Integer> value) {
            buf.writeCollection(value, FriendlyByteBuf::writeInt);
        }

        @Override
        public List<Integer> fromNetwork(RegistryFriendlyByteBuf buf) {
            return buf.readList(FriendlyByteBuf::readInt);
        }
    }
}
