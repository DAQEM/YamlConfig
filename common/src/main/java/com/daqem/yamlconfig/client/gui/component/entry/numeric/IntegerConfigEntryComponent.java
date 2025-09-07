package com.daqem.yamlconfig.client.gui.component.entry.numeric;

import com.daqem.uilib.util.ValidationErrors;
import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.impl.config.entry.numeric.IntegerConfigEntry;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class IntegerConfigEntryComponent extends BaseNumericConfigEntryComponent<IntegerConfigEntry, Integer>{

    public IntegerConfigEntryComponent(String key, IntegerConfigEntry configEntry) {
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
        if (this.editBoxWidget.hasInputValidationErrors()) return;
        this.getConfigEntry().set(Integer.parseInt(this.editBoxWidget.getValue()));
    }
}
