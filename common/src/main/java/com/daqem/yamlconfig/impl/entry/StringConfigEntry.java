package com.daqem.yamlconfig.impl.entry;

import com.daqem.yamlconfig.api.entry.IStringConfigEntry;
import com.daqem.yamlconfig.api.exception.ConfigEntryParseException;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StringConfigEntry extends BaseConfigEntry<String> implements IStringConfigEntry {

    private final int minLength;
    private final int maxLength;
    private final @Nullable String pattern;
    private final List<String> validValues;

    public StringConfigEntry(String key, String defaultValue) {
        this(key, defaultValue, -1, -1);
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
    public void parse(String value) throws ConfigEntryParseException {
        if (minLength != -1 && value.length() < minLength) {
            throw new ConfigEntryParseException(getKey(), "String length (" + value.length() + ") is less than the minimum length (" + minLength + ")");
        }
        if (maxLength != -1 && value.length() > maxLength) {
            throw new ConfigEntryParseException(getKey(), "String length (" + value.length() + ") is greater than the maximum length (" + maxLength + ")");
        }
        if (pattern != null && !value.matches(pattern)) {
            throw new ConfigEntryParseException(getKey(), "String (" + value + ") does not match the pattern (" + pattern + ")");
        }
        if (!validValues.isEmpty() && !validValues.contains(value)) {
            throw new ConfigEntryParseException(getKey(), "String (" + value + ") is not a valid value");
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
}
