package com.daqem.yamlconfig.impl.config.entry.map.numeric;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.map.numeric.IIntegerMapConfigEntry;
import com.daqem.yamlconfig.api.config.entry.serializer.IConfigEntrySerializer;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.api.gui.component.IConfigEntryComponent;
import com.daqem.yamlconfig.client.gui.component.entry.map.numeric.IntegerMapConfigEntryComponent;
import com.daqem.yamlconfig.impl.config.entry.type.ConfigEntryTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.snakeyaml.engine.v2.common.FlowStyle;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.MappingNode;
import org.snakeyaml.engine.v2.nodes.NodeTuple;
import org.snakeyaml.engine.v2.nodes.ScalarNode;
import org.snakeyaml.engine.v2.nodes.Tag;

import java.util.Map;
import java.util.stream.Collectors;

public class IntegerMapConfigEntry extends BaseNumericMapConfigEntry<Integer> implements IIntegerMapConfigEntry {

    public IntegerMapConfigEntry(String key, Map<String, Integer> defaultValue) {
        super(key, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public IntegerMapConfigEntry(String key, Map<String, Integer> defaultValue, int minLength, int maxLength) {
        super(key, defaultValue, minLength, maxLength, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public IntegerMapConfigEntry(String key, Map<String, Integer> defaultValue, int minLength, int maxLength, Integer minValue, Integer maxValue) {
        super(key, defaultValue, minLength, maxLength, minValue, maxValue);
    }

    @Override
    public IConfigEntryType<IConfigEntry<Map<String, Integer>>, Map<String, Integer>> getType() {
        //noinspection unchecked
        return (IConfigEntryType<IConfigEntry<Map<String, Integer>>, Map<String, Integer>>) (IConfigEntryType<?, ?>) ConfigEntryTypes.INTEGER_MAP;
    }

    @Override
    public IConfigEntryComponent<?, ?> createComponent(String key) {
        return new IntegerMapConfigEntryComponent(key, this);
    }

    public static class Serializer implements IConfigEntrySerializer<IIntegerMapConfigEntry, Map<String, Integer>> {

        @Override
        public void encodeNode(IIntegerMapConfigEntry configEntry, NodeTuple nodeTuple) {
            if (nodeTuple.getValueNode() instanceof MappingNode mappingNode) {
                configEntry.setValue(mappingNode.getValue().stream()
                        .filter(n ->
                                n.getKeyNode() instanceof ScalarNode keyNode
                                        && n.getValueNode() instanceof ScalarNode valueNode
                                        && keyNode.getTag().equals(Tag.STR)
                                        && valueNode.getTag().equals(Tag.INT))
                        .collect(Collectors.toMap(
                                n -> ((ScalarNode) n.getKeyNode()).getValue(),
                                n -> Integer.parseInt(((ScalarNode) n.getValueNode()).getValue()))));
            }
        }

        @Override
        public NodeTuple decodeNode(IIntegerMapConfigEntry configEntry) {
            ScalarNode keyNode = configEntry.createKeyNode();
            MappingNode valueNode = new MappingNode(Tag.MAP, configEntry.get().entrySet().stream()
                    .map(e -> {
                        ScalarNode key = new ScalarNode(Tag.STR, e.getKey(), ScalarStyle.PLAIN);
                        ScalarNode value = new ScalarNode(Tag.INT, Integer.toString(e.getValue()), ScalarStyle.PLAIN);
                        return new NodeTuple(key, value);
                    }).toList(), FlowStyle.BLOCK);
            return new NodeTuple(keyNode, valueNode);
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, IIntegerMapConfigEntry configEntry, Map<String, Integer> value) {
            buf.writeMap(value, FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeInt);
        }

        @Override
        public Map<String, Integer> fromNetwork(RegistryFriendlyByteBuf buf) {
            return buf.readMap(FriendlyByteBuf::readUtf, FriendlyByteBuf::readInt);
        }
    }
}
