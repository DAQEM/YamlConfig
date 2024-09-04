package com.daqem.yamlconfig.impl.config.entry.list;

import com.daqem.yamlconfig.api.config.entry.comment.IComments;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.list.IStringListConfigEntry;
import com.daqem.yamlconfig.api.config.entry.serializer.IConfigEntrySerializer;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.api.exception.ConfigEntryValidationException;
import com.daqem.yamlconfig.api.gui.component.IConfigEntryComponent;
import com.daqem.yamlconfig.client.gui.component.entry.list.StringListConfigEntryComponent;
import com.daqem.yamlconfig.impl.config.entry.type.ConfigEntryTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.jetbrains.annotations.Nullable;
import org.snakeyaml.engine.v2.common.FlowStyle;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.*;

import java.util.List;

public class StringListConfigEntry extends BaseListConfigEntry<String> implements IStringListConfigEntry {

    private final @Nullable String pattern;
    private final List<String> validValues;

    public StringListConfigEntry(String key, List<String> defaultValue) {
        this(key, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public StringListConfigEntry(String key, List<String> defaultValue, int minLength, int maxLength) {
        this(key, defaultValue, minLength, maxLength, List.of());
    }

    public StringListConfigEntry(String key, List<String> defaultValue, @Nullable String pattern) {
        this(key, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE, pattern, List.of());
    }

    public StringListConfigEntry(String key, List<String> defaultValue, List<String> validValues) {
        this(key, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE, null, validValues);
    }

    public StringListConfigEntry(String key, List<String> defaultValue, @Nullable String pattern, List<String> validValues) {
        this(key, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE, pattern, validValues);
    }

    public StringListConfigEntry(String key, List<String> defaultValue, int minLength, int maxLength, @Nullable String pattern) {
        this(key, defaultValue, minLength, maxLength, pattern, List.of());
    }

    public StringListConfigEntry(String key, List<String> defaultValue, int minLength, int maxLength, List<String> validValues) {
        this(key, defaultValue, minLength, maxLength, null, validValues);
    }

    public StringListConfigEntry(String key, List<String> defaultValue, int minLength, int maxLength, @Nullable String pattern, List<String> validValues) {
        super(key, defaultValue, minLength, maxLength);
        this.pattern = pattern;
        this.validValues = validValues;
    }

    @Override
    public void validate(List<String> value) throws ConfigEntryValidationException {
        super.validate(value);
        for (String element : value) {
            if (pattern != null && !element.matches(pattern)) {
                throw new ConfigEntryValidationException(getKey(), "Element (" + element + ") does not match the pattern (" + pattern + ")");
            }
            if (!validValues.isEmpty() && !validValues.contains(element)) {
                throw new ConfigEntryValidationException(getKey(), "Element (" + element + ") is not a valid value");
            }
        }
    }

    @Override
    public IConfigEntryType<IConfigEntry<List<String>>, List<String>> getType() {
        //noinspection unchecked
        return (IConfigEntryType<IConfigEntry<List<String>>, List<String>>) (IConfigEntryType<?, ?>) ConfigEntryTypes.STRING_LIST;
    }

    @Override
    public IConfigEntryComponent<?, ?> createComponent(String key) {
        return new StringListConfigEntryComponent(key, this);
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
            if (pattern != null) {
                comments.addValidationParameter("Pattern: " + pattern);
            }
            if (!validValues.isEmpty()) {
                comments.addValidationParameter("Valid values: " + validValues);
            }
        }
        if (comments.showDefaultValues()) {
            comments.addDefaultValues(getDefaultValue().stream().map(s -> "'" + s + "'").toList().toString());
        }
        return comments;
    }

    public static class Serializer implements IConfigEntrySerializer<IStringListConfigEntry, List<String>> {

        @Override
        public void encodeNode(IStringListConfigEntry configEntry, NodeTuple nodeTuple) {
            if (nodeTuple.getValueNode() instanceof SequenceNode sequenceNode) {
                configEntry.setValue(sequenceNode.getValue().stream()
                        .filter(n -> n instanceof ScalarNode scalarNode && scalarNode.getTag().equals(Tag.STR))
                        .map(n -> ((ScalarNode) n).getValue())
                        .toList());
            }
        }

        @Override
        public NodeTuple decodeNode(IStringListConfigEntry configEntry) {
            ScalarNode keyNode = configEntry.createKeyNode();
            SequenceNode valueNode = new SequenceNode(Tag.SEQ, configEntry.get().stream()
                    .map(s -> (Node) new ScalarNode(Tag.STR, s, ScalarStyle.SINGLE_QUOTED))
                    .toList(), FlowStyle.BLOCK);
            return new NodeTuple(keyNode, valueNode);
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, IStringListConfigEntry configEntry, List<String> value) {
            buf.writeCollection(value, FriendlyByteBuf::writeUtf);
        }

        @Override
        public List<String> fromNetwork(RegistryFriendlyByteBuf buf) {
            return buf.readList(FriendlyByteBuf::readUtf);
        }
    }
}
