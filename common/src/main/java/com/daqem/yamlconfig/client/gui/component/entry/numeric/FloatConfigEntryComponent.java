package com.daqem.yamlconfig.client.gui.component.entry.numeric;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.impl.config.entry.numeric.FloatConfigEntry;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class FloatConfigEntryComponent extends BaseNumericConfigEntryComponent<FloatConfigEntryComponent, FloatConfigEntry, Float>{

    public FloatConfigEntryComponent(String key, FloatConfigEntry configEntry) {
        super(key, configEntry, configEntry.get(), input -> {
            List<Component> list = new ArrayList<>();
            try {
                double value = Float.parseFloat(input);
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
        this.getConfigEntry().setValue(Float.parseFloat(this.textBoxComponent.getValue()));
    }
}