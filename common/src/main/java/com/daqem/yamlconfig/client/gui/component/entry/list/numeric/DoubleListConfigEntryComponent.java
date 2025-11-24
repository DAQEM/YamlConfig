package com.daqem.yamlconfig.client.gui.component.entry.list.numeric;

import com.daqem.uilib.util.ValidationErrors;
import com.daqem.yamlconfig.api.config.entry.list.numeric.IDoubleListConfigEntry;
import com.daqem.yamlconfig.client.gui.component.entry.list.BaseListConfigEntryComponent;
import com.daqem.yamlconfig.impl.config.entry.list.numeric.DoubleListConfigEntry;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class DoubleListConfigEntryComponent extends BaseListConfigEntryComponent<IDoubleListConfigEntry> {

    public DoubleListConfigEntryComponent(String key, IDoubleListConfigEntry configEntry) {
        super(key, configEntry, input -> {
            List<Component> list = new ArrayList<>();
            try {
                double value = Double.parseDouble(input);
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
                .map(input -> Double.parseDouble(input.getValue()))
                .toList()
        );
    }
}
