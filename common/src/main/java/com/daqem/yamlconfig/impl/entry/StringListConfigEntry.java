package com.daqem.yamlconfig.impl.entry;

import com.daqem.yamlconfig.api.IComments;
import com.daqem.yamlconfig.api.entry.IStringListConfigEntry;
import com.daqem.yamlconfig.api.exception.ConfigEntryValidationException;
import com.mojang.serialization.Codec;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.Nullable;
import org.snakeyaml.engine.v2.comments.CommentLine;
import org.snakeyaml.engine.v2.comments.CommentType;
import org.snakeyaml.engine.v2.nodes.AnchorNode;
import org.snakeyaml.engine.v2.nodes.Node;
import org.snakeyaml.engine.v2.nodes.NodeTuple;

import java.util.List;
import java.util.Optional;

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
            comments.addValidationParameter("Default value: " + getDefaultValue().stream().map(s -> "'" + s + "'").toList());
        }
        return comments;
    }
}
