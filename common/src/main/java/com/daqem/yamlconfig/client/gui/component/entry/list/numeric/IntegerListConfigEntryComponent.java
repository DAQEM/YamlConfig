package com.daqem.yamlconfig.client.gui.component.entry.list.numeric;

import com.daqem.uilib.util.ValidationErrors;
import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.client.gui.component.entry.list.BaseListConfigEntryComponent;
import com.daqem.yamlconfig.impl.config.entry.list.numeric.IntegerListConfigEntry;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class IntegerListConfigEntryComponent extends BaseListConfigEntryComponent<IntegerListConfigEntry> {

    public IntegerListConfigEntryComponent(String key, IntegerListConfigEntry configEntry) {
        super(key, configEntry, input -> {
            List<Component> list = new ArrayList<>();
            try {
                int value = Integer.parseInt(input);
                if (value < configEntry.getMinValue()) {
                    list.add(ValidationErrors.minValue(configEntry.getMinValue()));
                }
                if (value > configEntry.getMaxValue()) {
                    list.add(ValidationErrors.maxValue(configEntry.getMaxValue()));
                }
            } catch (NumberFormatException e) {
                list.add(ValidationErrors.invalidNumber());
            }
            return list;
        });
    }

    @Override
    public void applyValue() {
        if (hasInputValidationErrors()) return;

        this.getConfigEntry().set(this.editBoxWidgets.keySet().stream()
                .map(input -> Integer.parseInt(input.getValue()))
                .toList()
        );
    }
}
