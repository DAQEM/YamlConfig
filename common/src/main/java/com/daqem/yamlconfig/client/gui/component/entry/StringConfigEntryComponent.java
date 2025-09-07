package com.daqem.yamlconfig.client.gui.component.entry;

import com.daqem.uilib.gui.widget.EditBoxWidget;
import com.daqem.uilib.util.ValidationErrors;
import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.impl.config.entry.StringConfigEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.List;

public class StringConfigEntryComponent extends BaseConfigEntryComponent<StringConfigEntry> {

    private final EditBoxWidget editBoxWidget;

    public StringConfigEntryComponent(String key, StringConfigEntry configEntry) {
        super(key, configEntry, 0, 0, DEFAULT_HEIGHT);

        this.editBoxWidget = new EditBoxWidget(
                Minecraft.getInstance().font,
                KEY_WIDTH + GAP_WIDTH,
                0,
                VALUE_WIDTH,
                DEFAULT_HEIGHT,
                Component.empty()
        ) {
            @Override
            public List<Component> validateInput(String input) {
                List<Component> list = super.validateInput(input);
                if (input.length() < configEntry.getMinLength()) {
                    list.add(ValidationErrors.minLength(configEntry.getMinLength()));
                }
                if (configEntry.getPattern() != null && !input.matches(configEntry.getPattern())) {
                    list.add(ValidationErrors.pattern(configEntry.getPattern()));
                }
                if (!configEntry.getValidValues().isEmpty() && !configEntry.getValidValues().contains(input)) {
                    list.add(ValidationErrors.validValues(configEntry.getValidValues()));
                }
                return list;
            }
        };

        editBoxWidget.setValue(configEntry.get());
        editBoxWidget.setMaxLength(configEntry.getMaxLength());

        this.addWidget(editBoxWidget);
    }

    @Override
    public boolean isOriginalValue() {
        return this.getConfigEntry().getDefaultValue().equals(this.editBoxWidget.getValue());
    }

    @Override
    public void resetValue() {
        this.editBoxWidget.setValue(this.getConfigEntry().getDefaultValue());
    }

    @Override
    public void applyValue() {
        if (this.editBoxWidget.hasInputValidationErrors()) return;
        this.getConfigEntry().set(this.editBoxWidget.getValue());
    }

    @Override
    public boolean hasValidationErrors() {
        return this.editBoxWidget.hasInputValidationErrors();
    }
}
