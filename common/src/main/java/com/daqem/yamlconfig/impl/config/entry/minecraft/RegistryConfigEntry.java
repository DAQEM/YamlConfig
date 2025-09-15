package com.daqem.yamlconfig.impl.config.entry.minecraft;

import com.daqem.yamlconfig.api.config.entry.comment.IComments;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.minecraft.IRegistryConfigEntry;
import com.daqem.yamlconfig.api.config.entry.serializer.IConfigEntrySerializer;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.api.exception.ConfigEntryValidationException;
import com.daqem.yamlconfig.impl.config.entry.BaseConfigEntry;
import com.daqem.yamlconfig.impl.config.entry.type.ConfigEntryTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.NodeTuple;
import org.snakeyaml.engine.v2.nodes.ScalarNode;
import org.snakeyaml.engine.v2.nodes.Tag;

import java.util.Objects;
import java.util.Optional;

public class RegistryConfigEntry<T> extends BaseConfigEntry<T> implements IRegistryConfigEntry<T> {

    private final Registry<T> registry;

    public RegistryConfigEntry(String key, T defaultValue, Registry<T> registry) {
        super(key, defaultValue);
        this.registry = registry;
    }


    @Override
    public void validate(T value) throws ConfigEntryValidationException {
        if (registry.getKey(value) == null) {
            throw new ConfigEntryValidationException(getKey(), "Value is not in registry.");
        }
    }

    @Override
    public IConfigEntryType<IConfigEntry<T>, T> getType() {
        //noinspection unchecked
        return (IConfigEntryType<IConfigEntry<T>, T>) (IConfigEntryType<?, ?>) ConfigEntryTypes.REGISTRY;
    }

    @Override
    public Registry<T> getRegistry() {
        return registry;
    }

    @Override
    public IComments getComments() {
        IComments comments = super.getComments();
        if (comments.showValidationParameters()) {
            comments.addValidationParameter("Registry: " + getRegistry().key().location());
        }
        if (comments.showDefaultValues()) {
            comments.addDefaultValues("'" + getRegistry().getKey(getDefaultValue()) + "'");
        }
        return comments;
    }

    public static class Serializer<T> implements IConfigEntrySerializer<IRegistryConfigEntry<T>, T> {

        @Override
        public void encodeNode(IRegistryConfigEntry<T> configEntry, NodeTuple nodeTuple) {
            if (nodeTuple.getValueNode() instanceof ScalarNode scalarNode && scalarNode.getTag().equals(Tag.STR)) {
                ResourceLocation resourceLocation = ResourceLocation.parse(scalarNode.getValue());
                Optional<Holder.Reference<T>> reference = configEntry.getRegistry().get(resourceLocation);
                reference.ifPresent(tReference -> configEntry.set(tReference.value()));
            }
        }

        @Override
        public NodeTuple decodeNode(IRegistryConfigEntry<T> configEntry) {
            ScalarNode keyNode = configEntry.createKeyNode();
            ResourceLocation key = configEntry.getRegistry().getKey(configEntry.get());
            ScalarNode valueNode = new ScalarNode(Tag.STR, Objects.requireNonNull(key).toString(), ScalarStyle.SINGLE_QUOTED);
            return new NodeTuple(keyNode, valueNode);
        }

        @Override
        public void valueToNetwork(RegistryFriendlyByteBuf buf, IRegistryConfigEntry<T> configEntry, T value) {
            buf.writeResourceKey(configEntry.getRegistry().key());
            ResourceLocation resourceLocation = configEntry.getRegistry().getKey(value);
            buf.writeResourceLocation(Objects.requireNonNull(resourceLocation));
        }

        @Override
        @SuppressWarnings("unchecked")
        public T valueFromNetwork(RegistryFriendlyByteBuf buf) {
            ResourceKey<Registry<Object>> key = (ResourceKey<Registry<Object>>) buf.readRegistryKey();
            ResourceLocation resourceLocation = buf.readResourceLocation();
            Optional<Holder.Reference<Registry<Object>>> reference = ((Registry<Registry<Object>>) BuiltInRegistries.REGISTRY).get(key);
            return (T) reference.map(registry -> registry.value().get(resourceLocation).get().value()).orElse(null);
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, IRegistryConfigEntry<T> configEntry) {
            buf.writeUtf(configEntry.getKey());
            buf.writeResourceKey(configEntry.getRegistry().key());
            buf.writeResourceLocation(Objects.requireNonNull(configEntry.getRegistry().getKey(configEntry.get())));
            buf.writeResourceLocation(Objects.requireNonNull(configEntry.getRegistry().getKey(configEntry.getDefaultValue())));
            buf.writeCollection(configEntry.getComments().getComments(false), FriendlyByteBuf::writeUtf);
        }

        @Override
        @SuppressWarnings("unchecked")
        public IRegistryConfigEntry<T> fromNetwork(RegistryFriendlyByteBuf buf) {
            String key = buf.readUtf();
            ResourceKey<Registry<Object>> registryKey = (ResourceKey<Registry<Object>>) buf.readRegistryKey();
            ResourceLocation resourceLocation = buf.readResourceLocation();
            ResourceLocation defaultResourceLocation = buf.readResourceLocation();
            Optional<Holder.Reference<Registry<Object>>> reference = ((Registry<Registry<Object>>) BuiltInRegistries.REGISTRY).get(registryKey);
            RegistryConfigEntry<Object> configEntry = new RegistryConfigEntry<>(key, reference.map(registry -> registry.value().get(defaultResourceLocation).get().value()).orElse(null), reference.map(Holder.Reference::value).orElse(null));
            configEntry.set(reference.map(registry -> registry.value().get(resourceLocation).get().value()).orElse(null));
            buf.readList(FriendlyByteBuf::readUtf).forEach(configEntry.getComments()::addComment);
            return (IRegistryConfigEntry<T>) configEntry;
        }
    }
}
