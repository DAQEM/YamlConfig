package com.daqem.yamlconfig.client.gui;

import com.daqem.uilib.gui.component.AbstractComponent;
import com.daqem.yamlconfig.api.config.IConfig;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.IStackConfigEntry;
import com.daqem.yamlconfig.client.gui.component.ConfigCategoryComponent;
import com.daqem.yamlconfig.client.gui.component.entry.*;
import com.daqem.yamlconfig.client.gui.component.entry.list.*;
import com.daqem.yamlconfig.client.gui.component.entry.list.numeric.*;
import com.daqem.yamlconfig.client.gui.component.entry.map.*;
import com.daqem.yamlconfig.client.gui.component.entry.map.numeric.*;
import com.daqem.yamlconfig.client.gui.component.entry.minecraft.*;
import com.daqem.yamlconfig.client.gui.component.entry.numeric.*;
import com.daqem.yamlconfig.impl.config.entry.*;
import com.daqem.yamlconfig.impl.config.entry.list.*;
import com.daqem.yamlconfig.impl.config.entry.list.numeric.*;
import com.daqem.yamlconfig.impl.config.entry.map.*;
import com.daqem.yamlconfig.impl.config.entry.map.numeric.*;
import com.daqem.yamlconfig.impl.config.entry.minecraft.*;
import com.daqem.yamlconfig.impl.config.entry.numeric.*;

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

        // stackEntry.get() returns a LinkedHashMap, guaranteeing insertion order
        for (Map.Entry<String, IConfigEntry<?>> entry : stackEntry.get().entrySet()) {
            String entryKey = entry.getKey();
            IConfigEntry<?> configEntry = entry.getValue();
            String fullPath = prefix.isEmpty() ? entryKey : prefix + entryKey;

            if (configEntry instanceof IStackConfigEntry subStack) {
                children.add(buildCategory(entryKey, fullPath + ".", subStack));
            } else {
                children.add(createEntryComponent(fullPath, configEntry));
            }
        }

        String labelKey = key == null ? null : (config.getModId() + "." + config.getName() + "." + prefix.substring(0, prefix.length() - 1));
        return new ConfigCategoryComponent(stackEntry, labelKey, children);
    }

    private AbstractComponent createEntryComponent(String key, IConfigEntry<?> entry) {
        String translationKey = config.getModId() + "." + config.getName() + "." + key;

        return switch (entry) {
            case BooleanConfigEntry e -> new BooleanConfigEntryComponent(translationKey, e);
            case DateTimeConfigEntry e -> new DateTimeConfigEntryComponent(translationKey, e);
            case EnumConfigEntry<?> e -> new EnumConfigEntryComponent<>(translationKey, e);
            case StringConfigEntry e -> new StringConfigEntryComponent(translationKey, e);
            case StringListConfigEntry e -> new StringListConfigEntryComponent(translationKey, e);
            case DoubleListConfigEntry e -> new DoubleListConfigEntryComponent(translationKey, e);
            case FloatListConfigEntry e -> new FloatListConfigEntryComponent(translationKey, e);
            case IntegerListConfigEntry e -> new IntegerListConfigEntryComponent(translationKey, e);
            case StringMapConfigEntry e -> new StringMapConfigEntryComponent(translationKey, e);
            case DoubleMapConfigEntry e -> new DoubleMapConfigEntryComponent(translationKey, e);
            case FloatMapConfigEntry e -> new FloatMapConfigEntryComponent(translationKey, e);
            case IntegerMapConfigEntry e -> new IntegerMapConfigEntryComponent(translationKey, e);
            case RegistryConfigEntry<?> e -> new RegistryConfigEntryComponent<>(translationKey, e);
            case ResourceLocationConfigEntry e -> new ResourceLocationConfigEntryComponent(translationKey, e);
            case DoubleConfigEntry e -> new DoubleConfigEntryComponent(translationKey, e);
            case FloatConfigEntry e -> new FloatConfigEntryComponent(translationKey, e);
            case IntegerConfigEntry e -> new IntegerConfigEntryComponent(translationKey, e);
            case LongConfigEntry e -> new LongConfigEntryComponent(translationKey, e);
            default -> throw new UnsupportedOperationException("This entry does not support components: " + entry.getClass().getName());
        };
    }
}