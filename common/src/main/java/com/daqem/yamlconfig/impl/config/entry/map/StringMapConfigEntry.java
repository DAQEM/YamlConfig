package com.daqem.yamlconfig.impl.config.entry.map;

import com.daqem.yamlconfig.api.config.entry.comment.IComments;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.map.IStringMapConfigEntry;
import com.daqem.yamlconfig.api.config.entry.serializer.IConfigEntrySerializer;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.api.exception.ConfigEntryValidationException;
import com.daqem.yamlconfig.api.node.IConfigNode;
import com.daqem.yamlconfig.api.node.IMapNode;
import com.daqem.yamlconfig.api.node.IValueNode;
import com.daqem.yamlconfig.impl.config.entry.type.ConfigEntryTypes;
import com.daqem.yamlconfig.impl.node.ConfigMapNode;
import com.daqem.yamlconfig.impl.node.ConfigValueNode;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StringMapConfigEntry extends BaseMapConfigEntry<String> implements IStringMapConfigEntry {

    private final String pattern;
    private final List<String> validValues;

    public StringMapConfigEntry(String key, Map<String, String> defaultValue) {
        this(key, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public StringMapConfigEntry(String key, Map<String, String> defaultValue, int minLength, int maxLength) {
        this(key, defaultValue, minLength, maxLength, null, List.of());
    }

    public StringMapConfigEntry(String key, Map<String, String> defaultValue, int minLength, int maxLength, String pattern) {
        this(key, defaultValue, minLength, maxLength, pattern, List.of());
    }

    public StringMapConfigEntry(String key, Map<String, String> defaultValue, int minLength, int maxLength, List<String> validValues) {
        this(key, defaultValue, minLength, maxLength, null, validValues);
    }

    public StringMapConfigEntry(String key, Map<String, String> defaultValue, int minLength, int maxLength, String pattern, List<String> validValues) {
        super(key, defaultValue, minLength, maxLength);
        this.pattern = pattern;
        this.validValues = validValues;
    }

    @Override
    public String getPattern() {
        return pattern;
    }

    @Override
    public List<String> getValidValues() {
        return validValues;
    }

    @Override
    public void validate(Map<String, String> value) throws ConfigEntryValidationException {
        super.validate(value);
        for (Map.Entry<String, String> entry : value.entrySet()) {
            if (pattern != null && !entry.getValue().matches(pattern)) {
                throw new ConfigEntryValidationException(getKey(), "Value '" + entry.getValue() + "' does not match pattern '" + pattern + "'");
            }
            if (!validValues.isEmpty() && !validValues.contains(entry.getValue())) {
                throw new ConfigEntryValidationException(getKey(), "Value '" + entry.getValue() + "' is not a valid value");
            }
        }
    }

    @Override
    public IConfigEntryType<IConfigEntry<Map<String, String>>, Map<String, String>> getType() {
        //noinspection unchecked
        return (IConfigEntryType<IConfigEntry<Map<String, String>>, Map<String, String>>) (IConfigEntryType<?, ?>) ConfigEntryTypes.STRING_MAP;
    }

    @Override
    public IComments getComments() {
        IComments comments = super.getComments();
        if (comments.showValidationParameters()) {
            if (pattern != null) {
                comments.addValidationParameter("Pattern: " + pattern);
            }
            if (validValues != null) {
                comments.addValidationParameter("Valid values: " + validValues);
            }
        }
        if (comments.showDefaultValues()) {
            comments.addDefaultValues(getDefaultValue().toString());
        }
        return comments;
    }

    public static class Serializer implements IConfigEntrySerializer<IStringMapConfigEntry, Map<String, String>> {

        @Override
        public void toNode(IStringMapConfigEntry configEntry, IMapNode parentMap) {
            ConfigMapNode mapNode = new ConfigMapNode();
            for (Map.Entry<String, String> entry : configEntry.get().entrySet()) {
                mapNode.put(entry.getKey(), new ConfigValueNode<>(entry.getValue()));
            }
            mapNode.setComments(configEntry.getComments().getComments());
            parentMap.put(configEntry.getKey(), mapNode);
        }

        @Override
        public void fromNode(IStringMapConfigEntry configEntry, IMapNode parentMap) {
            IConfigNode node = parentMap.get(configEntry.getKey());
            if (node instanceof IMapNode mapNode) {
                Map<String, String> map = new LinkedHashMap<>();
                for (Map.Entry<String, IConfigNode> entry : mapNode.entrySet()) {
                    if (entry.getValue() instanceof IValueNode<?> valueNode && valueNode.getValue() != null) {
                        map.put(entry.getKey(), valueNode.getValue().toString());
                    }
                }
                configEntry.set(map);
            }
        }

        @Override
        public void valueToNetwork(RegistryFriendlyByteBuf buf, IStringMapConfigEntry configEntry, Map<String, String> value) {
            buf.writeMap(value, FriendlyByteBuf::writeUtf, (b, v) -> b.writeUtf(v));
        }

        @Override
        public Map<String, String> valueFromNetwork(RegistryFriendlyByteBuf buf) {
            return buf.readMap(FriendlyByteBuf::readUtf, b -> b.readUtf());
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, IStringMapConfigEntry configEntry) {
            buf.writeUtf(configEntry.getKey());
            buf.writeMap(configEntry.get(), FriendlyByteBuf::writeUtf, (b, v) -> b.writeUtf(v));
            buf.writeMap(configEntry.getDefaultValue(), FriendlyByteBuf::writeUtf, (b, v) -> b.writeUtf(v));
            buf.writeInt(configEntry.getMinLength());
            buf.writeInt(configEntry.getMaxLength());
            buf.writeUtf(configEntry.getPattern() == null ? "" : configEntry.getPattern());
            buf.writeCollection(configEntry.getValidValues(), FriendlyByteBuf::writeUtf);
            buf.writeCollection(configEntry.getComments().getComments(false), FriendlyByteBuf::writeUtf);
        }

        @Override
        public IStringMapConfigEntry fromNetwork(RegistryFriendlyByteBuf buf) {
            String key = buf.readUtf();
            Map<String, String> value = buf.readMap(FriendlyByteBuf::readUtf, b -> b.readUtf());
            Map<String, String> defaultValue = buf.readMap(FriendlyByteBuf::readUtf, b -> b.readUtf());
            int minLength = buf.readInt();
            int maxLength = buf.readInt();
            String pattern = buf.readUtf();
            List<String> validValues = buf.readList(FriendlyByteBuf::readUtf);
            StringMapConfigEntry configEntry = new StringMapConfigEntry(key, defaultValue, minLength, maxLength, pattern.isEmpty() ? null : pattern, validValues);
            configEntry.set(value);
            buf.readList(FriendlyByteBuf::readUtf).forEach(configEntry.getComments()::addComment);
            return configEntry;
        }
    }
}