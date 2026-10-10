package com.daqem.yamlconfig.impl.config.entry.list;

import com.daqem.yamlconfig.api.config.entry.comment.IComments;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.list.IStringListConfigEntry;
import com.daqem.yamlconfig.api.config.entry.serializer.IConfigEntrySerializer;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.api.exception.ConfigEntryValidationException;
import com.daqem.yamlconfig.api.node.IConfigNode;
import com.daqem.yamlconfig.api.node.IListNode;
import com.daqem.yamlconfig.api.node.IMapNode;
import com.daqem.yamlconfig.api.node.IValueNode;
import com.daqem.yamlconfig.impl.config.entry.type.ConfigEntryTypes;
import com.daqem.yamlconfig.impl.node.ConfigListNode;
import com.daqem.yamlconfig.impl.node.ConfigValueNode;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
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

        private static final StreamCodec<ByteBuf, List<String>> LIST_CODEC =
                ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list());

        @Override
        public void toNode(IStringListConfigEntry configEntry, IMapNode parentMap) {
            ConfigListNode listNode = new ConfigListNode();
            for (String s : configEntry.get()) {
                listNode.add(new ConfigValueNode<>(s));
            }
            listNode.setComments(configEntry.getComments().getComments());
            parentMap.put(configEntry.getKey(), listNode);
        }

        @Override
        public void fromNode(IStringListConfigEntry configEntry, IMapNode parentMap) {
            IConfigNode node = parentMap.get(configEntry.getKey());
            if (node instanceof IListNode listNode) {
                List<String> list = new ArrayList<>();
                for (IConfigNode child : listNode.getValue()) {
                    if (child instanceof IValueNode<?> valueNode && valueNode.getValue() != null) {
                        list.add(valueNode.getValue().toString());
                    }
                }
                configEntry.set(list);
            }
        }

        @Override
        public void valueToNetwork(RegistryFriendlyByteBuf buf, IStringListConfigEntry configEntry, List<String> value) {
            LIST_CODEC.encode(buf, value);
        }

        @Override
        public List<String> valueFromNetwork(RegistryFriendlyByteBuf buf) {
            return LIST_CODEC.decode(buf);
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, IStringListConfigEntry configEntry) {
            buf.writeUtf(configEntry.getKey());
            LIST_CODEC.encode(buf, configEntry.get());
            LIST_CODEC.encode(buf, configEntry.getDefaultValue());
            buf.writeInt(configEntry.getMinLength());
            buf.writeInt(configEntry.getMaxLength());
            buf.writeUtf(configEntry.getPattern() == null ? "" : configEntry.getPattern());
            LIST_CODEC.encode(buf, configEntry.getValidValues());
            configEntry.getComments().toNetwork(buf);
        }

        @Override
        public IStringListConfigEntry fromNetwork(RegistryFriendlyByteBuf buf) {
            String key = buf.readUtf();
            List<String> value = LIST_CODEC.decode(buf);
            List<String> defaultValue = LIST_CODEC.decode(buf);
            int minLength = buf.readInt();
            int maxLength = buf.readInt();
            String pattern = buf.readUtf();
            List<String> validValues = LIST_CODEC.decode(buf);
            StringListConfigEntry configEntry = new StringListConfigEntry(key, defaultValue, minLength, maxLength, pattern.isEmpty() ? null : pattern, validValues);
            configEntry.set(value);
            configEntry.getComments().fromNetwork(buf);
            return configEntry;
        }
    }
}
