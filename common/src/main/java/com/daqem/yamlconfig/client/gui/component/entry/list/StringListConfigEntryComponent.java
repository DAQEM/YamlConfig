package com.daqem.yamlconfig.client.gui.component.entry.list;

import com.daqem.uilib.gui.widget.EditBoxWidget;
import com.daqem.uilib.util.ValidationErrors;
import com.daqem.yamlconfig.impl.config.entry.list.StringListConfigEntry;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class StringListConfigEntryComponent extends BaseListConfigEntryComponent<StringListConfigEntry> {

    public StringListConfigEntryComponent(String key, StringListConfigEntry configEntry) {
        super(key, configEntry, input -> {
            List<Component> list = new ArrayList<>();
            if (configEntry.getPattern() != null && !input.matches(configEntry.getPattern())) {
                list.add(ValidationErrors.pattern(configEntry.getPattern()));
            }
            if (!configEntry.getValidValues().isEmpty() && !configEntry.getValidValues().contains(input)) {
                list.add(ValidationErrors.validValues(configEntry.getValidValues()));
            }
            return list;
        });
    }

    @Override
    public void applyValue() {
        if (hasInputValidationErrors()) return;

        this.getConfigEntry().set(this.editBoxWidgets.keySet().stream()
                .map(EditBoxWidget::getValue)
                .toList()
        );
    }
}
