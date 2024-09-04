package com.daqem.yamlconfig.client.gui.component.entry.map;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.impl.config.entry.map.StringMapConfigEntry;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class StringMapConfigEntryComponent extends BaseMapConfigEntryComponent<StringMapConfigEntryComponent, StringMapConfigEntry>{

    public StringMapConfigEntryComponent(String key, StringMapConfigEntry configEntry) {
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
