package com.daqem.yamlconfig.impl.config.entry.minecraft;

import com.daqem.yamlconfig.api.config.entry.comment.IComments;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.minecraft.IResourceLocationConfigEntry;
import com.daqem.yamlconfig.api.config.entry.serializer.IConfigEntrySerializer;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.api.exception.ConfigEntryValidationException;
import com.daqem.yamlconfig.api.gui.component.IConfigEntryComponent;
import com.daqem.yamlconfig.client.gui.component.entry.minecraft.ResourceLocationConfigEntryComponent;
import com.daqem.yamlconfig.impl.config.entry.BaseConfigEntry;
import com.daqem.yamlconfig.impl.config.entry.type.ConfigEntryTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.NodeTuple;
import org.snakeyaml.engine.v2.nodes.ScalarNode;
import org.snakeyaml.engine.v2.nodes.Tag;

public class ResourceLocationConfigEntry extends BaseConfigEntry<ResourceLocation> implements IResourceLocationConfigEntry {

    private final String pattern;

    public ResourceLocationConfigEntry(String key, ResourceLocation defaultValue) {
        this(key, defaultValue, null);
    }

    public ResourceLocationConfigEntry(String key, ResourceLocation defaultValue, String pattern) {
        super(key, defaultValue);
        this.pattern = pattern;
    }

    @Override
    public void validate(ResourceLocation value) throws ConfigEntryValidationException {
        if (pattern != null && !value.toString().matches(pattern)) {
            throw new ConfigEntryValidationException(getKey(), "Value does not match pattern: " + pattern);
        }
    }

    @Override
    public IConfigEntryType<IConfigEntry<ResourceLocation>, ResourceLocation> getType() {
        //noinspection unchecked
        return (IConfigEntryType<IConfigEntry<ResourceLocation>, ResourceLocation>) (IConfigEntryType<?, ?>) ConfigEntryTypes.RESOURCE_LOCATION;
    }

    @Override
    public IConfigEntryComponent<?, ?> createComponent(String key) {
        return new ResourceLocationConfigEntryComponent(key, this);
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

    public static class Serializer implements IConfigEntrySerializer<IResourceLocationConfigEntry, ResourceLocation> {

        @Override
        public void encodeNode(IResourceLocationConfigEntry configEntry, NodeTuple nodeTuple) {
            if (nodeTuple.getValueNode() instanceof ScalarNode scalarNode && scalarNode.getTag().equals(Tag.STR)) {
                configEntry.set(ResourceLocation.tryParse(scalarNode.getValue()));
            }
        }

        @Override
        public NodeTuple decodeNode(IResourceLocationConfigEntry configEntry) {
            ScalarNode keyNode = configEntry.createKeyNode();
            ScalarNode valueNode = new ScalarNode(Tag.STR, configEntry.get().toString(), ScalarStyle.SINGLE_QUOTED);
            return new NodeTuple(keyNode, valueNode);
        }

        @Override
        public void valueToNetwork(RegistryFriendlyByteBuf buf, IResourceLocationConfigEntry configEntry, ResourceLocation value) {
            buf.writeResourceLocation(value);
        }

        @Override
        public ResourceLocation valueFromNetwork(RegistryFriendlyByteBuf buf) {
            return buf.readResourceLocation();
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, IResourceLocationConfigEntry configEntry) {
            buf.writeUtf(configEntry.getKey());
            buf.writeResourceLocation(configEntry.get());
            buf.writeUtf(configEntry.getPattern() == null ? "" : configEntry.getPattern());
        }

        @Override
        public IResourceLocationConfigEntry fromNetwork(RegistryFriendlyByteBuf buf) {
            String key = buf.readUtf();
            ResourceLocation value = buf.readResourceLocation();
            String pattern = buf.readUtf();
            ResourceLocationConfigEntry configEntry = new ResourceLocationConfigEntry(key, value, pattern.isEmpty() ? null : pattern);
            configEntry.set(configEntry.getDefaultValue());
            return configEntry;
        }
    }
}
