package com.daqem.yamlconfig.impl.config.entry;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.IStackConfigEntry;
import com.daqem.yamlconfig.api.config.entry.serializer.IConfigEntrySerializer;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.api.gui.component.IConfigEntryComponent;
import com.daqem.yamlconfig.impl.config.entry.type.ConfigEntryTypes;
import com.daqem.yamlconfig.registry.YamlConfigRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.snakeyaml.engine.v2.common.FlowStyle;
import org.snakeyaml.engine.v2.nodes.MappingNode;
import org.snakeyaml.engine.v2.nodes.NodeTuple;
import org.snakeyaml.engine.v2.nodes.ScalarNode;
import org.snakeyaml.engine.v2.nodes.Tag;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StackConfigEntry extends BaseConfigEntry<Map<String, IConfigEntry<?>>> implements IStackConfigEntry {

    public StackConfigEntry(String key, Map<String, IConfigEntry<?>> defaultValue) {
        super(key, defaultValue);
    }

    @Override
    public void validate(Map<String, IConfigEntry<?>> value) {
    }

    @Override
    public IConfigEntryType<IConfigEntry<Map<String, IConfigEntry<?>>>, Map<String, IConfigEntry<?>>> getType() {
        //noinspection unchecked
        return (IConfigEntryType<IConfigEntry<Map<String, IConfigEntry<?>>>, Map<String, IConfigEntry<?>>>) (IConfigEntryType<?, ?>) ConfigEntryTypes.STACK;
    }

    @Override
    public IConfigEntryComponent<?, ?> createComponent(String key) {
        throw new UnsupportedOperationException("StackConfigEntry does not support components");
    }

    public static class Serializer implements IConfigEntrySerializer<IStackConfigEntry, Map<String, IConfigEntry<?>>> {

        @Override
        public void encodeNode(IStackConfigEntry configEntry, NodeTuple nodeTuple) {
            if (nodeTuple.getValueNode() instanceof MappingNode mappingNode && configEntry.get() != null) {
                for (Map.Entry<String, IConfigEntry<?>> entry : configEntry.get().entrySet()) {
                    //noinspection unchecked
                    mappingNode.getValue().stream()
                            .filter(nodeTuple1 -> nodeTuple1.getKeyNode() instanceof ScalarNode keyNode
                                    && keyNode.getValue().equals(entry.getKey()))
                            .findFirst()
                            .ifPresent(valueNode -> ((IConfigEntry<Object>) entry.getValue()).getType().getSerializer()
                                    .encodeNode((IConfigEntry<Object>) entry.getValue(), valueNode));
                }
            }
        }

        @Override
        public NodeTuple decodeNode(IStackConfigEntry configEntry) {
            ScalarNode keyNode = configEntry.createKeyNode();
            //noinspection unchecked
            MappingNode mappingNode = new MappingNode(Tag.MAP, configEntry.get()
                    .values().stream().map(configEntry1 ->
                            ((IConfigEntry<Object>) configEntry1).getType().getSerializer()
                                    .decodeNode((IConfigEntry<Object>) configEntry1)
                    ).toList(), FlowStyle.BLOCK);
            return new NodeTuple(keyNode, mappingNode);
        }

        @Override
        public void valueToNetwork(RegistryFriendlyByteBuf buf, IStackConfigEntry configEntry, Map<String, IConfigEntry<?>> value) {
        }

        @Override
        public Map<String, IConfigEntry<?>> valueFromNetwork(RegistryFriendlyByteBuf buf) {
            return null;
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, IStackConfigEntry configEntry) {
            buf.writeUtf(configEntry.getKey());
            buf.writeInt(configEntry.get().size());
            for (Map.Entry<String, IConfigEntry<?>> entry : configEntry.get().entrySet()) {
                buf.writeUtf(entry.getKey());
                buf.writeResourceLocation(entry.getValue().getType().getId());
                //noinspection unchecked
                ((IConfigEntry<Object>) entry.getValue()).getType().getSerializer()
                        .toNetwork(buf, (IConfigEntry<Object>) entry.getValue());
            }
        }

        @Override
        public IStackConfigEntry fromNetwork(RegistryFriendlyByteBuf buf) {
            String key = buf.readUtf();
            int size = buf.readInt();
            Map<String, IConfigEntry<?>> value = new HashMap<>();
            for (int i = 0; i < size; i++) {
                String entryKey = buf.readUtf();
                IConfigEntryType<? extends IConfigEntry<?>, ?> type = YamlConfigRegistry.CONFIG_ENTRY.get(buf.readResourceLocation());
                IConfigEntry<?> entry = Objects.requireNonNull(type).getSerializer().fromNetwork(buf);
                value.put(entryKey, entry);
            }
            StackConfigEntry configEntry = new StackConfigEntry(key, value);
            configEntry.setValue(configEntry.getDefaultValue());
            return configEntry;
        }
    }
}
