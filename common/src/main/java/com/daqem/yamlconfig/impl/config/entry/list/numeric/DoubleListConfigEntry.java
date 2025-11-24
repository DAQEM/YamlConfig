package com.daqem.yamlconfig.impl.config.entry.list.numeric;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.list.numeric.IDoubleListConfigEntry;
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

import java.util.ArrayList;
import java.util.List;

public class DoubleListConfigEntry extends BaseNumericListConfigEntry<Double> implements IDoubleListConfigEntry {

    public DoubleListConfigEntry(String key, List<Double> value) {
        super(key, value, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    public DoubleListConfigEntry(String key, List<Double> value, int minLength, int maxLength) {
        super(key, value, minLength, maxLength, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    public DoubleListConfigEntry(String key, List<Double> value, int minLength, int maxLength, Double minValue, Double maxValue) {
        super(key, value, minLength, maxLength, minValue, maxValue);
    }

    @Override
    public IConfigEntryType<IConfigEntry<List<Double>>, List<Double>> getType() {
        //noinspection unchecked
        return (IConfigEntryType<IConfigEntry<List<Double>>, List<Double>>) (IConfigEntryType<?, ?>) ConfigEntryTypes.DOUBLE_LIST;
    }

    public static class Serializer implements IConfigEntrySerializer<IDoubleListConfigEntry, List<Double>> {

        @Override
        public void toNode(IDoubleListConfigEntry configEntry, IMapNode parentMap) {
            ConfigListNode listNode = new ConfigListNode();
            for (Double d : configEntry.get()) {
                listNode.add(new ConfigValueNode<>(d));
            }
            listNode.setComments(configEntry.getComments().getComments());
            parentMap.put(configEntry.getKey(), listNode);
        }

        @Override
        public void fromNode(IDoubleListConfigEntry configEntry, IMapNode parentMap) {
            IConfigNode node = parentMap.get(configEntry.getKey());
            if (node instanceof IListNode listNode) {
                List<Double> list = new ArrayList<>();
                for (IConfigNode child : listNode.getValue()) {
                    if (child instanceof IValueNode<?> valueNode && valueNode.getValue() instanceof Number number) {
                        list.add(number.doubleValue());
                    }
                }
                configEntry.set(list);
            }
        }

        @Override
        public void valueToNetwork(RegistryFriendlyByteBuf buf, IDoubleListConfigEntry configEntry, List<Double> value) {
            buf.writeCollection(value, FriendlyByteBuf::writeDouble);
        }

        @Override
        public List<Double> valueFromNetwork(RegistryFriendlyByteBuf buf) {
            return buf.readList(FriendlyByteBuf::readDouble);
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, IDoubleListConfigEntry configEntry) {
            buf.writeUtf(configEntry.getKey());
            buf.writeCollection(configEntry.get(), FriendlyByteBuf::writeDouble);
            buf.writeCollection(configEntry.getDefaultValue(), FriendlyByteBuf::writeDouble);
            buf.writeInt(configEntry.getMinLength());
            buf.writeInt(configEntry.getMaxLength());
            buf.writeDouble(configEntry.getMinValue());
            buf.writeDouble(configEntry.getMaxValue());
            buf.writeCollection(configEntry.getComments().getComments(false), FriendlyByteBuf::writeUtf);
        }

        @Override
        public IDoubleListConfigEntry fromNetwork(RegistryFriendlyByteBuf buf) {
            String key = buf.readUtf();
            List<Double> value = buf.readList(FriendlyByteBuf::readDouble);
            List<Double> defaultValue = buf.readList(FriendlyByteBuf::readDouble);
            int minLength = buf.readInt();
            int maxLength = buf.readInt();
            double minValue = buf.readDouble();
            double maxValue = buf.readDouble();
            DoubleListConfigEntry configEntry = new DoubleListConfigEntry(key, defaultValue, minLength, maxLength, minValue, maxValue);
            configEntry.set(value);
            buf.readList(FriendlyByteBuf::readUtf).forEach(configEntry.getComments()::addComment);
            return configEntry;
        }
    }
}
