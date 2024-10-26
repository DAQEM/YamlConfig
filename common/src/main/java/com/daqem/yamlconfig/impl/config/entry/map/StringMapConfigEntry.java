package com.daqem.yamlconfig.impl.config.entry.map;

import com.daqem.yamlconfig.api.config.entry.comment.IComments;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.map.IStringMapConfigEntry;
import com.daqem.yamlconfig.api.config.entry.serializer.IConfigEntrySerializer;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.api.exception.ConfigEntryValidationException;
import com.daqem.yamlconfig.api.gui.component.IConfigEntryComponent;
import com.daqem.yamlconfig.client.gui.component.entry.map.StringMapConfigEntryComponent;
import com.daqem.yamlconfig.impl.config.entry.type.ConfigEntryTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.snakeyaml.engine.v2.common.FlowStyle;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.MappingNode;
import org.snakeyaml.engine.v2.nodes.NodeTuple;
import org.snakeyaml.engine.v2.nodes.ScalarNode;
import org.snakeyaml.engine.v2.nodes.Tag;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public IConfigEntryComponent<?, ?> createComponent(String key) {
        return new StringMapConfigEntryComponent(key, this);
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
        public void encodeNode(IStringMapConfigEntry configEntry, NodeTuple nodeTuple) {
            if (nodeTuple.getValueNode() instanceof MappingNode mappingNode) {
                configEntry.setValue(mappingNode.getValue().stream()
                        .filter(n ->
                                n.getKeyNode() instanceof ScalarNode keyNode
                                        && n.getValueNode() instanceof ScalarNode valueNode
                                        && keyNode.getTag().equals(Tag.STR)
                                        && valueNode.getTag().equals(Tag.STR))
                        .collect(Collectors.toMap(
                                n -> ((ScalarNode) n.getKeyNode()).getValue(),
                                n -> ((ScalarNode) n.getValueNode()).getValue()
                        )));
            }
        }

        @Override
        public NodeTuple decodeNode(IStringMapConfigEntry configEntry) {
            ScalarNode keyNode = configEntry.createKeyNode();
            MappingNode valueNode = new MappingNode(Tag.MAP, configEntry.get().entrySet().stream()
                    .map(e -> new NodeTuple(
                            new ScalarNode(Tag.STR, e.getKey(), ScalarStyle.PLAIN),
                            new ScalarNode(Tag.STR, e.getValue(), ScalarStyle.SINGLE_QUOTED)
                    ))
                    .toList(), FlowStyle.BLOCK);
            return new NodeTuple(keyNode, valueNode);
        }

        @Override
        public void valueToNetwork(RegistryFriendlyByteBuf buf, IStringMapConfigEntry configEntry, Map<String, String> value) {
            buf.writeMap(value, FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeUtf);
        }

        @Override
        public Map<String, String> valueFromNetwork(RegistryFriendlyByteBuf buf) {
            return buf.readMap(FriendlyByteBuf::readUtf, FriendlyByteBuf::readUtf);
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, IStringMapConfigEntry configEntry) {
            buf.writeUtf(configEntry.getKey());
            buf.writeMap(configEntry.get(), FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeUtf);
            buf.writeInt(configEntry.getMinLength());
            buf.writeInt(configEntry.getMaxLength());
            buf.writeUtf(configEntry.getPattern() == null ? "" : configEntry.getPattern());
            buf.writeCollection(configEntry.getValidValues(), FriendlyByteBuf::writeUtf);
        }

        @Override
        public IStringMapConfigEntry fromNetwork(RegistryFriendlyByteBuf buf) {
            String key = buf.readUtf();
            Map<String, String> value = buf.readMap(FriendlyByteBuf::readUtf, FriendlyByteBuf::readUtf);
            int minLength = buf.readInt();
            int maxLength = buf.readInt();
            String pattern = buf.readUtf();
            List<String> validValues = buf.readList(FriendlyByteBuf::readUtf);
            StringMapConfigEntry configEntry = new StringMapConfigEntry(key, value, minLength, maxLength, pattern.isEmpty() ? null : pattern, validValues);
            configEntry.setValue(configEntry.getDefaultValue());
            return configEntry;
        }
    }
}
