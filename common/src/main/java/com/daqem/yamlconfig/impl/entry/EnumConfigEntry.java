package com.daqem.yamlconfig.impl.entry;

import com.daqem.yamlconfig.api.IComments;
import com.daqem.yamlconfig.api.entry.IEnumConfigEntry;
import com.daqem.yamlconfig.api.exception.ConfigEntryValidationException;

import java.util.stream.Stream;

public class EnumConfigEntry<E extends Enum<E>> extends BaseConfigEntry<E> implements IEnumConfigEntry<E> {

    private final Class<E> enumClass;

    public EnumConfigEntry(String key, E defaultValue, Class<E> enumClass) {
        super(key, defaultValue);
        this.enumClass = enumClass;
    }

    @Override
    public void validate(E value) throws ConfigEntryValidationException {
        if (value == null) {
            throw new ConfigEntryValidationException(getKey(), "Value cannot be null");
        }
    }

    @Override
    public Class<E> getEnumClass() {
        return enumClass;
    }

    @Override
    public IComments getComments() {
        IComments comments = super.getComments();
        if (comments.showValidationParameters()) {
            comments.addValidationParameter("Allowed values: " + Stream.of(getEnumClass().getEnumConstants()).map(s -> "'" + s + "'").toList());
        }
        if (comments.showDefaultValues()) {
            comments.addValidationParameter("Default value: " + getDefaultValue());
        }
        return comments;
    }
}
