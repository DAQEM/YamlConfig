package com.daqem.yamlconfig.client.gui;

import com.daqem.yamlconfig.api.config.IConfig;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.IStackConfigEntry;
import com.daqem.yamlconfig.api.gui.component.IConfigEntryComponent;
import com.daqem.yamlconfig.client.gui.component.ConfigCategoryComponent;
import com.daqem.yamlconfig.impl.config.entry.StackConfigEntry;

import java.util.*;
import java.util.stream.Collectors;

public class ConfigEntryComponentBuilder {

    private final IConfig config;

    public ConfigEntryComponentBuilder(IConfig config) {
        this.config = config;
    }

    public ConfigCategoryComponent build() {
        List<IConfigEntryComponent<?>> components = createComponents("");
        List<ConfigCategoryComponent> categoryComponents = createCategories();

        return new ConfigCategoryComponent(null, null, components, categoryComponents);
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

    private List<IConfigEntryComponent<?>> createComponents(String category) {
        return config.getEntries().entrySet().stream()
                .filter(entry -> {
                    String key = entry.getKey();
                    if (category.isEmpty()) {
                        return !key.contains(".");
                    }
                    return key.startsWith(category) && key.lastIndexOf('.') == category.length();
                })
                .map(Map.Entry::getValue)
                .map(entry -> {
                    return entry.createComponent(getPrefix(category) + entry.getKey());
                })
                .collect(Collectors.toList());
    }

    private String getPrefix(String category) {
        if (category.isEmpty()) {
            return getConfigPrefix();
        }
        return getConfigPrefix() + category + ".";
    }

    private String getConfigPrefix() {
        return this.config.getModId() + "." + this.config.getName() + ".";
    }

    private List<ConfigCategoryComponent> createCategories() {
        Map<String, ConfigCategoryComponent> categoryComponents = new HashMap<>();

        for (String category : getCategories()) {
            List<IConfigEntryComponent<?>> components = createComponents(category);
            IStackConfigEntry configEntry = getConfigEntry(category, config.getContext().get());
            categoryComponents.put(category, new ConfigCategoryComponent(configEntry, getConfigPrefix() + category, components));
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

    public IStackConfigEntry getConfigEntry(String path, Map<String, IConfigEntry<?>> entries) {
        if (path == null || path.isEmpty() || entries == null) {
            return null;
        }

        String[] keys = path.split("\\.");
        return getConfigEntryRecursive(keys, 0, entries);
    }

    private IStackConfigEntry getConfigEntryRecursive(String[] keys, int index, Map<String, IConfigEntry<?>> entries) {
        IConfigEntry<?> currentEntry = entries.get(keys[index]);

        if (currentEntry instanceof IStackConfigEntry stackConfigEntry) {
            if (index == keys.length - 1) {
                return stackConfigEntry;
            }

            Map<String, IConfigEntry<?>> nextEntries = stackConfigEntry.get();
            if (nextEntries != null) {
                return getConfigEntryRecursive(keys, index + 1, nextEntries);
            }
        }

        return null;
    }
}
