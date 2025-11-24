package com.daqem.yamlconfig.impl.config.entry;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.IStackConfigEntry;
import com.daqem.yamlconfig.api.config.entry.serializer.IConfigEntrySerializer;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.api.node.IMapNode;
import com.daqem.yamlconfig.impl.config.entry.type.ConfigEntryTypes;
import com.daqem.yamlconfig.registry.YamlConfigRegistry;
import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;

import java.util.*;

public class StackConfigEntry extends BaseConfigEntry<LinkedHashMap<String, IConfigEntry<?>>> implements IStackConfigEntry {

    public StackConfigEntry(String key, LinkedHashMap<String, IConfigEntry<?>> defaultValue) {
        super(key, defaultValue);
    }

    @Override
    public void validate(LinkedHashMap<String, IConfigEntry<?>> value) {
    }

    @Override
    public IConfigEntryType<IConfigEntry<LinkedHashMap<String, IConfigEntry<?>>>, LinkedHashMap<String, IConfigEntry<?>>> getType() {
        //noinspection unchecked
        return (IConfigEntryType<IConfigEntry<LinkedHashMap<String, IConfigEntry<?>>>, LinkedHashMap<String, IConfigEntry<?>>>) (IConfigEntryType<?, ?>) ConfigEntryTypes.STACK;
    }

    public static class Serializer implements IConfigEntrySerializer<IStackConfigEntry, LinkedHashMap<String, IConfigEntry<?>>> {

        @Override
        public void toNode(IStackConfigEntry configEntry, IMapNode parentMap) {
            // Handled recursively by BaseConfig
        }

        @Override
        public void fromNode(IStackConfigEntry configEntry, IMapNode parentMap) {
            // Handled recursively by BaseConfig
        }

        @Override
        public void valueToNetwork(RegistryFriendlyByteBuf buf, IStackConfigEntry configEntry, LinkedHashMap<String, IConfigEntry<?>> value) {
        }

        @Override
        public LinkedHashMap<String, IConfigEntry<?>> valueFromNetwork(RegistryFriendlyByteBuf buf) {
            return null;
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, IStackConfigEntry configEntry) {
            buf.writeUtf(configEntry.getKey());
            buf.writeInt(configEntry.getDefaultValue().size());
            for (Map.Entry<String, IConfigEntry<?>> entry : configEntry.getDefaultValue().entrySet()) {
                buf.writeUtf(entry.getKey());
                buf.writeResourceLocation(entry.getValue().getType().getId());
                //noinspection unchecked
                ((IConfigEntry<Object>) entry.getValue()).getType().getSerializer()
                        .toNetwork(buf, (IConfigEntry<Object>) entry.getValue());
            }
            buf.writeInt(configEntry.get().size());
            for (Map.Entry<String, IConfigEntry<?>> entry : configEntry.get().entrySet()) {
                buf.writeUtf(entry.getKey());
                buf.writeResourceLocation(entry.getValue().getType().getId());
                //noinspection unchecked
                ((IConfigEntry<Object>) entry.getValue()).getType().getSerializer()
                        .toNetwork(buf, (IConfigEntry<Object>) entry.getValue());
            }
            buf.writeCollection(configEntry.getComments().getComments(false), FriendlyByteBuf::writeUtf);
        }

        @SuppressWarnings("DuplicatedCode")
        @Override
        public IStackConfigEntry fromNetwork(RegistryFriendlyByteBuf buf) {
            String key = buf.readUtf();
            int defaultValueSize = buf.readInt();
            LinkedHashMap<String, IConfigEntry<?>> defaultValue = new LinkedHashMap<>();
            for (int i = 0; i < defaultValueSize; i++) {
                String entryKey = buf.readUtf();
                Optional<Holder.Reference<IConfigEntryType<?, ?>>> reference = YamlConfigRegistry.CONFIG_ENTRY.get(buf.readResourceLocation());
                IConfigEntryType<?, ?> type = reference.map(Holder.Reference::value).orElse(null);
                IConfigEntry<?> entry = Objects.requireNonNull(type).getSerializer().fromNetwork(buf);
                defaultValue.put(entryKey, entry);
            }

            int size = buf.readInt();
            LinkedHashMap<String, IConfigEntry<?>> value = new LinkedHashMap<>();
            for (int i = 0; i < size; i++) {
                String entryKey = buf.readUtf();
                Optional<Holder.Reference<IConfigEntryType<?, ?>>> reference = YamlConfigRegistry.CONFIG_ENTRY.get(buf.readResourceLocation());
                IConfigEntryType<?, ?> type = reference.map(Holder.Reference::value).orElse(null);
                IConfigEntry<?> entry = Objects.requireNonNull(type).getSerializer().fromNetwork(buf);
                value.put(entryKey, entry);
            }

            StackConfigEntry configEntry = new StackConfigEntry(key, defaultValue);
            configEntry.set(value);
            buf.readList(FriendlyByteBuf::readUtf).forEach(configEntry.getComments()::addComment);
            return configEntry;
        }
    }
}
