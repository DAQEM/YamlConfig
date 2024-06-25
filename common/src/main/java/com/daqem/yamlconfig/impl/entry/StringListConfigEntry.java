package com.daqem.yamlconfig.impl.entry;

import com.daqem.yamlconfig.api.entry.IStringListConfigEntry;
import com.daqem.yamlconfig.api.exception.ConfigEntryParseException;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StringListConfigEntry extends BaseListConfigEntry<String> implements IStringListConfigEntry {

    private final @Nullable String pattern;
    private final List<String> validValues;

    public StringListConfigEntry(String key, List<String> defaultValue) {
        this(key, defaultValue, -1, -1);
    }
    public StringListConfigEntry(String key, List<String> defaultValue, int minLength, int maxLength) {
        this(key, defaultValue, minLength, maxLength, List.of());
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
    public void parse(List<String> value) throws ConfigEntryParseException {
        super.parse(value);
        for (String element : value) {
            if (pattern != null && !element.matches(pattern)) {
                throw new ConfigEntryParseException(getKey(), "Element (" + element + ") does not match the pattern (" + pattern + ")");
            }
            if (!validValues.isEmpty() && !validValues.contains(element)) {
                throw new ConfigEntryParseException(getKey(), "Element (" + element + ") is not a valid value");
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
}
