package com.daqem.yamlconfig.impl.entry.minecraft;

import com.daqem.yamlconfig.api.IComments;
import com.daqem.yamlconfig.api.entry.minecraft.IResourceLocationConfigEntry;
import com.daqem.yamlconfig.api.exception.ConfigEntryValidationException;
import com.daqem.yamlconfig.impl.entry.BaseConfigEntry;
import net.minecraft.resources.ResourceLocation;

public class ResourceLocationConfigEntry extends BaseConfigEntry<ResourceLocation> implements IResourceLocationConfigEntry {

    private final String pattern;

    public ResourceLocationConfigEntry(String key, ResourceLocation defaultValue) {
        this(key, defaultValue, null);
    }

    public ResourceLocationConfigEntry(String key, ResourceLocation defaultValue, String pattern) {
        super(key, defaultValue);
        this.pattern = pattern;
    }

    @Override
    public void validate(ResourceLocation value) throws ConfigEntryValidationException {
        if (pattern != null && !value.toString().matches(pattern)) {
            throw new ConfigEntryValidationException(getKey(), "Value does not match pattern: " + pattern);
        }
    }

    @Override
    public String getPattern() {
        return pattern;
    }

    @Override
    public IComments getComments() {
        IComments comments = super.getComments();
        if (comments.showValidationParameters()) {
            if (pattern != null) {
                comments.addValidationParameter("Pattern: " + pattern);
            }
        }
        if (comments.showDefaultValues()) {
            comments.addDefaultValues(getDefaultValue().toString());
        }
        return comments;
    }
}
