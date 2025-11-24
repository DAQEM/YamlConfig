package com.daqem.yamlconfig.impl.config.entry.list.numeric;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.list.numeric.IIntegerListConfigEntry;
import com.daqem.yamlconfig.api.config.entry.serializer.IConfigEntrySerializer;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.api.node.IConfigNode;
import com.daqem.yamlconfig.api.node.IListNode;
import com.daqem.yamlconfig.api.node.IMapNode;
import com.daqem.yamlconfig.api.node.IValueNode;
import com.daqem.yamlconfig.impl.config.entry.type.ConfigEntryTypes;
import com.daqem.yamlconfig.impl.node.ConfigListNode;
import com.daqem.yamlconfig.impl.node.ConfigValueNode;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.snakeyaml.engine.v2.common.FlowStyle;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.*;

import java.util.ArrayList;
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

    public static class Serializer implements IConfigEntrySerializer<IIntegerListConfigEntry, List<Integer>> {

        @Override
        public void toNode(IIntegerListConfigEntry configEntry, IMapNode parentMap) {
            ConfigListNode listNode = new ConfigListNode();
            for (Integer i : configEntry.get()) {
                listNode.add(new ConfigValueNode<>(i));
            }
            listNode.setComments(configEntry.getComments().getComments());
            parentMap.put(configEntry.getKey(), listNode);
        }

        @Override
        public void fromNode(IIntegerListConfigEntry configEntry, IMapNode parentMap) {
            IConfigNode node = parentMap.get(configEntry.getKey());
            if (node instanceof IListNode listNode) {
                List<Integer> list = new ArrayList<>();
                for (IConfigNode child : listNode.getValue()) {
                    if (child instanceof IValueNode<?> valueNode && valueNode.getValue() instanceof Number number) {
                        list.add(number.intValue());
                    }
                }
                configEntry.set(list);
            }
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
            buf.writeCollection(configEntry.getDefaultValue(), FriendlyByteBuf::writeInt);
            buf.writeInt(configEntry.getMinLength());
            buf.writeInt(configEntry.getMaxLength());
            buf.writeInt(configEntry.getMinValue());
            buf.writeInt(configEntry.getMaxValue());
            buf.writeCollection(configEntry.getComments().getComments(false), FriendlyByteBuf::writeUtf);
        }

        @Override
        public IIntegerListConfigEntry fromNetwork(RegistryFriendlyByteBuf buf) {
            String key = buf.readUtf();
            List<Integer> value = buf.readList(FriendlyByteBuf::readInt);
            List<Integer> defaultValue = buf.readList(FriendlyByteBuf::readInt);
            int minLength = buf.readInt();
            int maxLength = buf.readInt();
            int minValue = buf.readInt();
            int maxValue = buf.readInt();
            IntegerListConfigEntry configEntry = new IntegerListConfigEntry(key, defaultValue, minLength, maxLength, minValue, maxValue);
            configEntry.set(value);
            buf.readList(FriendlyByteBuf::readUtf).forEach(configEntry.getComments()::addComment);
            return configEntry;
        }
    }
}
