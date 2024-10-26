package com.daqem.yamlconfig.client.gui.component.entry.numeric;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.impl.config.entry.numeric.IntegerConfigEntry;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class IntegerConfigEntryComponent extends BaseNumericConfigEntryComponent<IntegerConfigEntryComponent, IntegerConfigEntry, Integer>{

    public IntegerConfigEntryComponent(String key, IntegerConfigEntry configEntry) {
        super(key, configEntry, configEntry.get(), input -> {
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

    @Override
    public void applyValue() {
        if (this.textBoxComponent.hasInputValidationErrors()) return;
        this.getConfigEntry().set(Integer.parseInt(this.textBoxComponent.getValue()));
    }
}
