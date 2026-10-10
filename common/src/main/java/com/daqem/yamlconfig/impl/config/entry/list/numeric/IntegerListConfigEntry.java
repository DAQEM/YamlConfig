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
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

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

        private static final StreamCodec<ByteBuf, List<Integer>> LIST_CODEC =
                ByteBufCodecs.INT.apply(ByteBufCodecs.list());

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
            LIST_CODEC.encode(buf, value);
        }

        @Override
        public List<Integer> valueFromNetwork(RegistryFriendlyByteBuf buf) {
            return LIST_CODEC.decode(buf);
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, IIntegerListConfigEntry configEntry) {
            buf.writeUtf(configEntry.getKey());
            LIST_CODEC.encode(buf, configEntry.get());
            LIST_CODEC.encode(buf, configEntry.getDefaultValue());
            buf.writeInt(configEntry.getMinLength());
            buf.writeInt(configEntry.getMaxLength());
            buf.writeInt(configEntry.getMinValue());
            buf.writeInt(configEntry.getMaxValue());
            configEntry.getComments().toNetwork(buf);
        }

        @Override
        public IIntegerListConfigEntry fromNetwork(RegistryFriendlyByteBuf buf) {
            String key = buf.readUtf();
            List<Integer> value = LIST_CODEC.decode(buf);
            List<Integer> defaultValue = LIST_CODEC.decode(buf);
            int minLength = buf.readInt();
            int maxLength = buf.readInt();
            int minValue = buf.readInt();
            int maxValue = buf.readInt();
            IntegerListConfigEntry configEntry = new IntegerListConfigEntry(key, defaultValue, minLength, maxLength, minValue, maxValue);
            configEntry.set(value);
            configEntry.getComments().fromNetwork(buf);
            return configEntry;
        }
    }
}
