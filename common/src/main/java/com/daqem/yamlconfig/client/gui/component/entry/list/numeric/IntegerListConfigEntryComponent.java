package com.daqem.yamlconfig.client.gui.component.entry.list.numeric;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.client.gui.component.entry.list.BaseListConfigEntryComponent;
import com.daqem.yamlconfig.impl.config.entry.list.numeric.IntegerListConfigEntry;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class IntegerListConfigEntryComponent extends BaseListConfigEntryComponent<IntegerListConfigEntryComponent, IntegerListConfigEntry> {

    public IntegerListConfigEntryComponent(String key, IntegerListConfigEntry configEntry) {
        super(key, configEntry, input -> {
            List<Component> list = new ArrayList<>();
            try {
                int value = Integer.parseInt(input);
                if (value < configEntry.getMinValue()) {
                    list.add(YamlConfig.translatable("gui.validation_error.min_value", configEntry.getMinValue()));
                }
                if (value > configEntry.getMaxValue()) {
                    list.add(YamlConfig.translatable("gui.validation_error.max_value", configEntry.getMaxValue()));
                }
            } catch (NumberFormatException e) {
                list.add(YamlConfig.translatable("gui.validation_error.invalid_number"));
            }
            return list;
        });
    }
}
