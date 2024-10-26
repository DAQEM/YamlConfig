package com.daqem.yamlconfig.impl.config.entry;

import com.daqem.yamlconfig.api.config.entry.comment.IComments;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.IStringConfigEntry;
import com.daqem.yamlconfig.api.config.entry.serializer.IConfigEntrySerializer;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.api.exception.ConfigEntryValidationException;
import com.daqem.yamlconfig.api.gui.component.IConfigEntryComponent;
import com.daqem.yamlconfig.client.gui.component.entry.StringConfigEntryComponent;
import com.daqem.yamlconfig.impl.config.entry.type.ConfigEntryTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.jetbrains.annotations.Nullable;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.NodeTuple;
import org.snakeyaml.engine.v2.nodes.ScalarNode;
import org.snakeyaml.engine.v2.nodes.Tag;

import java.util.List;

public class StringConfigEntry extends BaseConfigEntry<String> implements IStringConfigEntry {

    private final int minLength;
    private final int maxLength;
    private final @Nullable String pattern;
    private final List<String> validValues;

    public StringConfigEntry(String key, String defaultValue) {
        this(key, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public StringConfigEntry(String key, String defaultValue, int minLength, int maxLength) {
        this(key, defaultValue, minLength, maxLength, List.of());
    }

    public StringConfigEntry(String key, String defaultValue, int minLength, int maxLength, @Nullable String pattern) {
        this(key, defaultValue, minLength, maxLength, pattern, List.of());
    }

    public StringConfigEntry(String key, String defaultValue, int minLength, int maxLength, List<String> validValues) {
        this(key, defaultValue, minLength, maxLength, null, validValues);
    }

    public StringConfigEntry(String key, String defaultValue, int minLength, int maxLength, @Nullable String pattern, List<String> validValues) {
        super(key, defaultValue);
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.pattern = pattern;
        this.validValues = validValues;
    }

    @Override
    public void validate(String value) throws ConfigEntryValidationException {
        if (minLength != Integer.MIN_VALUE && value.length() < minLength) {
            throw new ConfigEntryValidationException(getKey(), "String length (" + value.length() + ") is less than the minimum length (" + minLength + ")");
        }
        if (maxLength != Integer.MAX_VALUE && value.length() > maxLength) {
            throw new ConfigEntryValidationException(getKey(), "String length (" + value.length() + ") is greater than the maximum length (" + maxLength + ")");
        }
        if (pattern != null && !value.matches(pattern)) {
            throw new ConfigEntryValidationException(getKey(), "String (" + value + ") does not match the pattern (" + pattern + ")");
        }
        if (!validValues.isEmpty() && !validValues.contains(value)) {
            throw new ConfigEntryValidationException(getKey(), "String (" + value + ") is not a valid value");
        }
    }

    @Override
    public int getMinLength() {
        return minLength;
    }

    @Override
    public int getMaxLength() {
        return maxLength;
    }

    @Override
    public @Nullable String getPattern() {
        return pattern;
    }

    @Override
    public List<String> getValidValues() {
        return validValues;
    }

    @Override
    public IComments getComments() {
        IComments comments = super.getComments();
        if (comments.showValidationParameters()) {
            if (minLength != Integer.MIN_VALUE) {
                comments.addValidationParameter("Minimum length: " + minLength);
            }
            if (maxLength != Integer.MAX_VALUE) {
                comments.addValidationParameter("Maximum length: " + maxLength);
            }
            if (pattern != null) {
                comments.addValidationParameter("Pattern: " + pattern);
            }
            if (!validValues.isEmpty()) {
                comments.addValidationParameter("Valid values: " + validValues);
            }
        }
        if (comments.showDefaultValues()) {
            comments.addDefaultValues("'" + getDefaultValue() + "'");
        }
        return comments;
    }

    @Override
    public IConfigEntryType<IConfigEntry<String>, String> getType() {
        //noinspection unchecked
        return (IConfigEntryType<IConfigEntry<String>, String>) (IConfigEntryType<?, ?>) ConfigEntryTypes.STRING;
    }

    @Override
    public IConfigEntryComponent<?, ?> createComponent(String key) {
        return new StringConfigEntryComponent(key, this);
    }


    public static class Serializer implements IConfigEntrySerializer<IStringConfigEntry, String> {

        @Override
        public void encodeNode(IStringConfigEntry configEntry, NodeTuple nodeTuple) {
            if (nodeTuple.getValueNode() instanceof ScalarNode scalarNode && scalarNode.getTag().equals(Tag.STR)) {
                configEntry.set(scalarNode.getValue());
            }
        }

        @Override
        public NodeTuple decodeNode(IStringConfigEntry configEntry) {
            ScalarNode keyNode = configEntry.createKeyNode();
            ScalarNode valueNode = new ScalarNode(Tag.STR, configEntry.get(), ScalarStyle.SINGLE_QUOTED);
            return new NodeTuple(keyNode, valueNode);
        }

        @Override
        public void valueToNetwork(RegistryFriendlyByteBuf buf, IStringConfigEntry configEntry, String value) {
            buf.writeUtf(value);
        }

        @Override
        public String valueFromNetwork(RegistryFriendlyByteBuf buf) {
            return buf.readUtf();
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, IStringConfigEntry configEntry) {
            buf.writeUtf(configEntry.getKey());
            buf.writeUtf(configEntry.get());
            buf.writeInt(configEntry.getMinLength());
            buf.writeInt(configEntry.getMaxLength());
        }

        @Override
        public IStringConfigEntry fromNetwork(RegistryFriendlyByteBuf buf) {
            StringConfigEntry configEntry = new StringConfigEntry(
                    buf.readUtf(),
                    buf.readUtf(),
                    buf.readInt(),
                    buf.readInt()
            );
            configEntry.set(configEntry.getDefaultValue());
            return configEntry;
        }
    }
}
