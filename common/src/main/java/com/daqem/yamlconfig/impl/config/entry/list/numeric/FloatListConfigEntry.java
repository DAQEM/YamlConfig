package com.daqem.yamlconfig.impl.config.entry.list.numeric;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.list.numeric.IFloatListConfigEntry;
import com.daqem.yamlconfig.api.config.entry.serializer.IConfigEntrySerializer;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.impl.config.entry.type.ConfigEntryTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.snakeyaml.engine.v2.common.FlowStyle;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.*;

import java.util.List;

public class FloatListConfigEntry extends BaseNumericListConfigEntry<Float> implements IFloatListConfigEntry {

    public FloatListConfigEntry(String key, List<Float> value) {
        super(key, value);
    }

    public FloatListConfigEntry(String key, List<Float> value, int minLength, int maxLength) {
        super(key, value, minLength, maxLength);
    }

    public FloatListConfigEntry(String key, List<Float> value, int minLength, int maxLength, Float minValue, Float maxValue) {
        super(key, value, minLength, maxLength, minValue, maxValue);
    }

    @Override
    public IConfigEntryType<IConfigEntry<List<Float>>, List<Float>> getType() {
        //noinspection unchecked
        return (IConfigEntryType<IConfigEntry<List<Float>>, List<Float>>) (IConfigEntryType<?, ?>) ConfigEntryTypes.FLOAT_LIST;
    }

    public static class Serializer implements IConfigEntrySerializer<IFloatListConfigEntry, List<Float>> {

        @Override
        public void encodeNode(IFloatListConfigEntry configEntry, NodeTuple nodeTuple) {
            if (nodeTuple.getValueNode() instanceof SequenceNode sequenceNode) {
                configEntry.setValue(sequenceNode.getValue().stream()
                        .filter(n -> n instanceof ScalarNode scalarNode && (scalarNode.getTag().equals(Tag.FLOAT) || scalarNode.getTag().equals(Tag.INT)))
                        .map(n -> Float.parseFloat(((ScalarNode) n).getValue()))
                        .toList());
            }
        }

        @Override
        public NodeTuple decodeNode(IFloatListConfigEntry configEntry) {
            ScalarNode keyNode = configEntry.createKeyNode();
            SequenceNode valueNode = new SequenceNode(Tag.SEQ, configEntry.get().stream()
                    .map(s -> (Node) new ScalarNode(Tag.FLOAT, Float.toString(s), ScalarStyle.PLAIN))
                    .toList(), FlowStyle.BLOCK);
            return new NodeTuple(keyNode, valueNode);
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, IFloatListConfigEntry configEntry, List<Float> value) {
            buf.writeCollection(value, FriendlyByteBuf::writeFloat);
        }

        @Override
        public List<Float> fromNetwork(RegistryFriendlyByteBuf buf) {
            return buf.readList(FriendlyByteBuf::readFloat);
        }
    }
}
