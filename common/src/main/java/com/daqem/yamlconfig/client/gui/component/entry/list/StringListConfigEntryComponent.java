package com.daqem.yamlconfig.client.gui.component.entry.list;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.impl.config.entry.list.StringListConfigEntry;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class StringListConfigEntryComponent extends BaseListConfigEntryComponent<StringListConfigEntryComponent, StringListConfigEntry> {

    public StringListConfigEntryComponent(String key, StringListConfigEntry configEntry) {
        super(key, configEntry, input -> {
            List<Component> list = new ArrayList<>();
            if (configEntry.getPattern() != null && !input.matches(configEntry.getPattern())) {
                list.add(YamlConfig.translatable("gui.validation_error.pattern", configEntry.getPattern()));
            }
            if (!configEntry.getValidValues().isEmpty() && !configEntry.getValidValues().contains(input)) {
                list.add(YamlConfig.translatable("gui.validation_error.valid_values", configEntry.getValidValues()));
            }
            return list;
        });
    }
}
