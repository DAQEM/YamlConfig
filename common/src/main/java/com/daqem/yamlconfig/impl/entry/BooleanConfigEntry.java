package com.daqem.yamlconfig.impl.entry;

import com.daqem.yamlconfig.api.IComments;
import com.daqem.yamlconfig.api.entry.IBooleanConfigEntry;
import com.daqem.yamlconfig.api.exception.ConfigEntryValidationException;

import java.util.List;

public class BooleanConfigEntry extends BaseConfigEntry<Boolean> implements IBooleanConfigEntry {

    public BooleanConfigEntry(String key, Boolean defaultValue) {
        super(key, defaultValue);
    }

    @Override
    public void validate(Boolean value) throws ConfigEntryValidationException {
    }

    @Override
    public IComments getComments() {
        IComments comments = super.getComments();
        if (comments.showDefaultValues()) {
            comments.addValidationParameter("Default value: " + getDefaultValue());
        }
        return comments;
    }
}
