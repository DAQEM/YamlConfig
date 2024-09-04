package com.daqem.yamlconfig.client.gui;

import com.daqem.yamlconfig.api.config.IConfig;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.gui.component.IConfigEntryComponent;
import com.daqem.yamlconfig.client.gui.component.ConfigCategoryComponent;
import com.daqem.yamlconfig.client.gui.component.entry.*;
import com.daqem.yamlconfig.client.gui.component.entry.list.StringListConfigEntryComponent;
import com.daqem.yamlconfig.client.gui.component.entry.list.numeric.DoubleListConfigEntryComponent;
import com.daqem.yamlconfig.client.gui.component.entry.list.numeric.FloatListConfigEntryComponent;
import com.daqem.yamlconfig.client.gui.component.entry.list.numeric.IntegerListConfigEntryComponent;
import com.daqem.yamlconfig.client.gui.component.entry.map.StringMapConfigEntryComponent;
import com.daqem.yamlconfig.client.gui.component.entry.map.numeric.DoubleMapConfigEntryComponent;
import com.daqem.yamlconfig.client.gui.component.entry.map.numeric.FloatMapConfigEntryComponent;
import com.daqem.yamlconfig.client.gui.component.entry.map.numeric.IntegerMapConfigEntryComponent;
import com.daqem.yamlconfig.client.gui.component.entry.minecraft.RegistryConfigEntryComponent;
import com.daqem.yamlconfig.client.gui.component.entry.minecraft.ResourceLocationConfigEntryComponent;
import com.daqem.yamlconfig.client.gui.component.entry.numeric.DoubleConfigEntryComponent;
import com.daqem.yamlconfig.client.gui.component.entry.numeric.FloatConfigEntryComponent;
import com.daqem.yamlconfig.client.gui.component.entry.numeric.IntegerConfigEntryComponent;
import com.daqem.yamlconfig.impl.config.entry.BooleanConfigEntry;
import com.daqem.yamlconfig.impl.config.entry.DateTimeConfigEntry;
import com.daqem.yamlconfig.impl.config.entry.EnumConfigEntry;
import com.daqem.yamlconfig.impl.config.entry.StringConfigEntry;
import com.daqem.yamlconfig.impl.config.entry.list.StringListConfigEntry;
import com.daqem.yamlconfig.impl.config.entry.list.numeric.DoubleListConfigEntry;
import com.daqem.yamlconfig.impl.config.entry.list.numeric.FloatListConfigEntry;
import com.daqem.yamlconfig.impl.config.entry.list.numeric.IntegerListConfigEntry;
import com.daqem.yamlconfig.impl.config.entry.map.StringMapConfigEntry;
import com.daqem.yamlconfig.impl.config.entry.map.numeric.DoubleMapConfigEntry;
import com.daqem.yamlconfig.impl.config.entry.map.numeric.FloatMapConfigEntry;
import com.daqem.yamlconfig.impl.config.entry.map.numeric.IntegerMapConfigEntry;
import com.daqem.yamlconfig.impl.config.entry.minecraft.RegistryConfigEntry;
import com.daqem.yamlconfig.impl.config.entry.minecraft.ResourceLocationConfigEntry;
import com.daqem.yamlconfig.impl.config.entry.numeric.DoubleConfigEntry;
import com.daqem.yamlconfig.impl.config.entry.numeric.FloatConfigEntry;
import com.daqem.yamlconfig.impl.config.entry.numeric.IntegerConfigEntry;

import java.util.*;
import java.util.stream.Collectors;

public class ConfigEntryComponentBuilder {

    private final IConfig config;

    public ConfigEntryComponentBuilder(IConfig config) {
        this.config = config;
    }

    public ConfigCategoryComponent build() {
        List<IConfigEntryComponent<?, ?>> components = createComponents("");
        List<ConfigCategoryComponent> categoryComponents = createCategories();

        return new ConfigCategoryComponent(null, components, categoryComponents);
    }

    private List<String> getCategories() {
        List<String> keys = new ArrayList<>(config.getEntries().keySet());

        Set<String> categoriesSet = keys.stream()
                .filter(s -> s.contains(".") && s.lastIndexOf('.') != 0)
                .map(s -> s.substring(0, s.lastIndexOf('.')))
                .collect(Collectors.toSet());

        for (String category : new ArrayList<>(categoriesSet)) {
            String[] parts = category.split("\\.");
            StringBuilder subCategory = new StringBuilder(parts[0]);

            categoriesSet.add(parts[0]);

            for (int i = 1; i < parts.length; i++) {
                subCategory.append(".").append(parts[i]);
                categoriesSet.add(subCategory.toString());
            }
        }

        return new ArrayList<>(categoriesSet);
    }

    private List<IConfigEntryComponent<?, ?>> createComponents(String category) {
        return config.getEntries().entrySet().stream()
                .filter(entry -> {
                    String key = entry.getKey();
                    if (category.isEmpty()) {
                        return !key.contains(".");
                    }
                    return key.startsWith(category) && key.lastIndexOf('.') == category.length();
                })
                .map(Map.Entry::getValue)
                .map(entry -> entry.createComponent(getPrefix(category) + entry.getKey()))
                .collect(Collectors.toList());
    }

    private String getPrefix(String category) {
        return getConfigPrefix() + category + ".";
    }

    private String getConfigPrefix() {
        return this.config.getModId() + "." + this.config.getName() + ".";
    }

    private List<ConfigCategoryComponent> createCategories() {
        Map<String, ConfigCategoryComponent> categoryComponents = new HashMap<>();

        for (String category : getCategories()) {
            List<IConfigEntryComponent<?, ?>> components = createComponents(category);
            categoryComponents.put(category, new ConfigCategoryComponent(getConfigPrefix() + category, components));
        }

        appendSubCategories(categoryComponents);

        return categoryComponents.entrySet().stream()
                .filter(entry -> !entry.getKey().contains(".")) // Only return top level categories
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    private void appendSubCategories(Map<String, ConfigCategoryComponent> categoryComponents) {
        for (Map.Entry<String, ConfigCategoryComponent> entry : categoryComponents.entrySet()) {
            String category = entry.getKey();
            ConfigCategoryComponent component = entry.getValue();

            if (category.contains(".")) {
                String parentCategory = category.substring(0, category.lastIndexOf('.'));
                ConfigCategoryComponent parentComponent = categoryComponents.get(parentCategory);

                if (parentComponent != null) {
                    parentComponent.addSubCategory(component);
                }
            }
        }
    }
}
