package com.daqem.yamlconfig.client.gui;

import com.daqem.uilib.gui.component.AbstractComponent;
import com.daqem.yamlconfig.api.config.IConfig;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.IStackConfigEntry;
import com.daqem.yamlconfig.client.gui.component.ConfigCategoryComponent;
import com.daqem.yamlconfig.client.gui.component.entry.YamlOnlyConfigEntryComponent;
import com.daqem.yamlconfig.client.gui.registry.ConfigEntryComponentRegistry;
import com.daqem.yamlconfig.client.gui.registry.IConfigEntryComponentFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfigEntryComponentBuilder {

    private final IConfig config;

    public ConfigEntryComponentBuilder(IConfig config) {
        this.config = config;
    }

    public ConfigCategoryComponent build() {
        return buildCategory(null, "", config.getContext());
    }

    private ConfigCategoryComponent buildCategory(String key, String prefix, IStackConfigEntry stackEntry) {
        List<AbstractComponent> children = new ArrayList<>();

        for (Map.Entry<String, IConfigEntry<?>> entry : stackEntry.get().entrySet()) {
            String entryKey = entry.getKey();
            IConfigEntry<?> configEntry = entry.getValue();
            String fullPath = prefix.isEmpty() ? entryKey : prefix + entryKey;

            if (configEntry instanceof IStackConfigEntry subStack) {
                children.add(buildCategory(entryKey, fullPath + ".", subStack));
            } else {
                children.add(createTypedEntryComponent(fullPath, configEntry));
            }
        }

        String labelKey = key == null ? null : (config.getModId() + "." + config.getName() + "." + prefix.substring(0, prefix.length() - 1));
        return new ConfigCategoryComponent(stackEntry, labelKey, children);
    }

    private <T> AbstractComponent createTypedEntryComponent(String key, IConfigEntry<T> entry) {
        String translationKey = config.getModId() + "." + config.getName() + "." + key;

        IConfigEntryComponentFactory<IConfigEntry<T>> factory = ConfigEntryComponentRegistry.getFactory(entry.getType());
        if (factory != null) {
            return (AbstractComponent) factory.create(translationKey, entry);
        }

        return new YamlOnlyConfigEntryComponent(translationKey, entry);
    }
}