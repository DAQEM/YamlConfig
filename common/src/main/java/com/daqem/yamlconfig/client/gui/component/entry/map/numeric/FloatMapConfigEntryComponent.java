package com.daqem.yamlconfig.client.gui.component.entry.map.numeric;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.client.gui.component.entry.map.BaseMapConfigEntryComponent;
import com.daqem.yamlconfig.impl.config.entry.map.numeric.FloatMapConfigEntry;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FloatMapConfigEntryComponent extends BaseMapConfigEntryComponent<FloatMapConfigEntryComponent, FloatMapConfigEntry> {

    public FloatMapConfigEntryComponent(String key, FloatMapConfigEntry configEntry) {
        super(key, configEntry, input -> {
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
        if (hasInputValidationErrors()) return;

        Map<String, Float> map = this.textBoxComponents.keySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getA().getValue(),
                        entry -> Float.parseFloat(entry.getB().getValue())
                ));

        this.getConfigEntry().set(map);
    }
}
