package com.daqem.yamlconfig.client.gui.component.entry.numeric;

import com.daqem.uilib.util.ValidationErrors;
import com.daqem.yamlconfig.api.config.entry.numeric.ILongConfigEntry;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class LongConfigEntryComponent extends BaseNumericConfigEntryComponent<ILongConfigEntry, Long> {

    public LongConfigEntryComponent(String key, ILongConfigEntry configEntry) {
        super(key, configEntry, input -> {
            List<Component> list = new ArrayList<>();
            try {
                long value = Long.parseLong(input);
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
        this.getConfigEntry().set(Long.parseLong(this.editBoxWidget.getValue()));
    }
}