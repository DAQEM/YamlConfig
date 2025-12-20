package com.daqem.yamlconfig.impl.config.entry.minecraft;

import com.daqem.yamlconfig.api.config.entry.comment.IComments;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.minecraft.IIdentifierConfigEntry;
import com.daqem.yamlconfig.api.config.entry.serializer.IConfigEntrySerializer;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.api.exception.ConfigEntryValidationException;
import com.daqem.yamlconfig.api.node.IConfigNode;
import com.daqem.yamlconfig.api.node.IMapNode;
import com.daqem.yamlconfig.api.node.IValueNode;
import com.daqem.yamlconfig.impl.config.entry.BaseConfigEntry;
import com.daqem.yamlconfig.impl.config.entry.type.ConfigEntryTypes;
import com.daqem.yamlconfig.impl.node.ConfigValueNode;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;

public class IdentifierConfigEntry extends BaseConfigEntry<Identifier> implements IIdentifierConfigEntry {

    private final String pattern;

    public IdentifierConfigEntry(String key, Identifier defaultValue) {
        this(key, defaultValue, null);
    }

    public IdentifierConfigEntry(String key, Identifier defaultValue, String pattern) {
        super(key, defaultValue);
        this.pattern = pattern;
    }

    @Override
    public void validate(Identifier value) throws ConfigEntryValidationException {
        if (pattern != null && !value.toString().matches(pattern)) {
            throw new ConfigEntryValidationException(getKey(), "Value does not match pattern: " + pattern);
        }
    }

    @Override
    public IConfigEntryType<IConfigEntry<Identifier>, Identifier> getType() {
        //noinspection unchecked
        return (IConfigEntryType<IConfigEntry<Identifier>, Identifier>) (IConfigEntryType<?, ?>) ConfigEntryTypes.RESOURCE_LOCATION;
    }

    @Override
    public String getPattern() {
        return pattern;
    }

    @Override
    public IComments getComments() {
        IComments comments = super.getComments();
        if (comments.showValidationParameters()) {
            if (pattern != null) {
                comments.addValidationParameter("Pattern: " + pattern);
            }
        }
        if (comments.showDefaultValues()) {
            comments.addDefaultValues("'" + getDefaultValue() + "'");
        }
        return comments;
    }

    public static class Serializer implements IConfigEntrySerializer<IIdentifierConfigEntry, Identifier> {

        @Override
        public void toNode(IIdentifierConfigEntry configEntry, IMapNode parentMap) {
            ConfigValueNode<String> node = new ConfigValueNode<>(configEntry.get().toString());
            node.setComments(configEntry.getComments().getComments());
            parentMap.put(configEntry.getKey(), node);
        }

        @Override
        public void fromNode(IIdentifierConfigEntry configEntry, IMapNode parentMap) {
            IConfigNode node = parentMap.get(configEntry.getKey());
            if (node instanceof IValueNode<?> valueNode && valueNode.getValue() != null) {
                Identifier rl = Identifier.tryParse(valueNode.getValue().toString());
                if (rl != null) {
                    configEntry.set(rl);
                }
            }
        }

        @Override
        public void valueToNetwork(RegistryFriendlyByteBuf buf, IIdentifierConfigEntry configEntry, Identifier value) {
            buf.writeIdentifier(value);
        }

        @Override
        public Identifier valueFromNetwork(RegistryFriendlyByteBuf buf) {
            return buf.readIdentifier();
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, IIdentifierConfigEntry configEntry) {
            buf.writeUtf(configEntry.getKey());
            buf.writeIdentifier(configEntry.get());
            buf.writeIdentifier(configEntry.getDefaultValue());
            buf.writeUtf(configEntry.getPattern() == null ? "" : configEntry.getPattern());
            buf.writeCollection(configEntry.getComments().getComments(false), FriendlyByteBuf::writeUtf);
        }

        @Override
        public IIdentifierConfigEntry fromNetwork(RegistryFriendlyByteBuf buf) {
            String key = buf.readUtf();
            Identifier value = buf.readIdentifier();
            Identifier defaultValue = buf.readIdentifier();
            String pattern = buf.readUtf();
            IdentifierConfigEntry configEntry = new IdentifierConfigEntry(key, defaultValue, pattern.isEmpty() ? null : pattern);
            configEntry.set(value);
            buf.readList(FriendlyByteBuf::readUtf).forEach(configEntry.getComments()::addComment);
            return configEntry;
        }
    }
}
