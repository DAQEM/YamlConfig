package com.daqem.yamlconfig.impl.entry.minecraft;

import com.daqem.yamlconfig.api.IComments;
import com.daqem.yamlconfig.api.entry.minecraft.IRegistryConfigEntry;
import com.daqem.yamlconfig.api.exception.ConfigEntryValidationException;
import com.daqem.yamlconfig.impl.entry.BaseConfigEntry;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;

import java.util.Optional;

public class RegistryConfigEntry<T> extends BaseConfigEntry<T> implements IRegistryConfigEntry<T> {

    private final Registry<T> registry;

    public RegistryConfigEntry(String key, T defaultValue, Registry<T> registry) {
        super(key, defaultValue);
        this.registry = registry;
    }


    @Override
    public void validate(T value) throws ConfigEntryValidationException {
        if (registry.getKey(value) == null) {
            throw new ConfigEntryValidationException(getKey(), "Value is not in registry.");
        }
    }

    @Override
    public Registry<T> getRegistry() {
        return registry;
    }

    @Override
    public IComments getComments() {
        IComments comments = super.getComments();
        if (comments.showValidationParameters()) {
            comments.addValidationParameter("Registry: " + getRegistry().key().registry());
        }
        if (comments.showDefaultValues()) {
            comments.addDefaultValues(getDefaultValue().toString());
        }
        return comments;
    }
}
