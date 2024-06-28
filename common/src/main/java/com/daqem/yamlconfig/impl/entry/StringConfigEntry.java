package com.daqem.yamlconfig.impl.entry;

import com.daqem.yamlconfig.api.IComments;
import com.daqem.yamlconfig.api.entry.IStringConfigEntry;
import com.daqem.yamlconfig.api.exception.ConfigEntryValidationException;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.Nullable;

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
}
