package com.daqem.yamlconfig.impl.entry.map;

import com.daqem.yamlconfig.api.IComments;
import com.daqem.yamlconfig.api.entry.map.IStringMapConfigEntry;
import com.daqem.yamlconfig.api.exception.ConfigEntryValidationException;

import java.util.List;
import java.util.Map;

public class StringMapConfigEntry extends BaseMapConfigEntry<String> implements IStringMapConfigEntry {

    private final String pattern;
    private final List<String> validValues;

    public StringMapConfigEntry(String key, Map<String, String> defaultValue) {
        this(key, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public StringMapConfigEntry(String key, Map<String, String> defaultValue, int minLength, int maxLength) {
        this(key, defaultValue, minLength, maxLength, null, null);
    }

    public StringMapConfigEntry(String key, Map<String, String> defaultValue, int minLength, int maxLength, String pattern) {
        this(key, defaultValue, minLength, maxLength, pattern, null);
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
            if (validValues != null && !validValues.contains(entry.getValue())) {
                throw new ConfigEntryValidationException(getKey(), "Value '" + entry.getValue() + "' is not a valid value");
            }
        }
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
}
