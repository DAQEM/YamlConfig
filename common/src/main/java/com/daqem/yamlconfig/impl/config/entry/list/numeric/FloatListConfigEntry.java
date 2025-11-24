package com.daqem.yamlconfig.impl.config.entry.list.numeric;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.list.numeric.IFloatListConfigEntry;
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

public class FloatListConfigEntry extends BaseNumericListConfigEntry<Float> implements IFloatListConfigEntry {

    public FloatListConfigEntry(String key, List<Float> value) {
        super(key, value, Float.MIN_VALUE, Float.MAX_VALUE);
    }

    public FloatListConfigEntry(String key, List<Float> value, int minLength, int maxLength) {
        super(key, value, minLength, maxLength, Float.MIN_VALUE, Float.MAX_VALUE);
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
        public void toNode(IFloatListConfigEntry configEntry, IMapNode parentMap) {
            ConfigListNode listNode = new ConfigListNode();
            for (Float f : configEntry.get()) {
                listNode.add(new ConfigValueNode<>(f));
            }
            listNode.setComments(configEntry.getComments().getComments());
            parentMap.put(configEntry.getKey(), listNode);
        }

        @Override
        public void fromNode(IFloatListConfigEntry configEntry, IMapNode parentMap) {
            IConfigNode node = parentMap.get(configEntry.getKey());
            if (node instanceof IListNode listNode) {
                List<Float> list = new ArrayList<>();
                for (IConfigNode child : listNode.getValue()) {
                    if (child instanceof IValueNode<?> valueNode && valueNode.getValue() instanceof Number number) {
                        list.add(number.floatValue());
                    }
                }
                configEntry.set(list);
            }
        }

        @Override
        public void valueToNetwork(RegistryFriendlyByteBuf buf, IFloatListConfigEntry configEntry, List<Float> value) {
            buf.writeCollection(value, FriendlyByteBuf::writeFloat);
        }

        @Override
        public List<Float> valueFromNetwork(RegistryFriendlyByteBuf buf) {
            return buf.readList(FriendlyByteBuf::readFloat);
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, IFloatListConfigEntry configEntry) {
            buf.writeUtf(configEntry.getKey());
            buf.writeCollection(configEntry.get(), FriendlyByteBuf::writeFloat);
            buf.writeCollection(configEntry.getDefaultValue(), FriendlyByteBuf::writeFloat);
            buf.writeInt(configEntry.getMinLength());
            buf.writeInt(configEntry.getMaxLength());
            buf.writeFloat(configEntry.getMinValue());
            buf.writeFloat(configEntry.getMaxValue());
            buf.writeCollection(configEntry.getComments().getComments(false), FriendlyByteBuf::writeUtf);
        }

        @Override
        public IFloatListConfigEntry fromNetwork(RegistryFriendlyByteBuf buf) {
            String key = buf.readUtf();
            List<Float> value = buf.readList(FriendlyByteBuf::readFloat);
            List<Float> defaultValue = buf.readList(FriendlyByteBuf::readFloat);
            int minLength = buf.readInt();
            int maxLength = buf.readInt();
            float minValue = buf.readFloat();
            float maxValue = buf.readFloat();
            FloatListConfigEntry configEntry = new FloatListConfigEntry(key, defaultValue, minLength, maxLength, minValue, maxValue);
            configEntry.set(value);
            buf.readList(FriendlyByteBuf::readUtf).forEach(configEntry.getComments()::addComment);
            return configEntry;
        }
    }
}
