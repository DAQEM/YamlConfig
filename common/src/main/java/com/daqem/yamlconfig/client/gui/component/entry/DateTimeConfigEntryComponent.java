package com.daqem.yamlconfig.client.gui.component.entry;

import com.daqem.uilib.gui.widget.EditBoxWidget;
import com.daqem.uilib.util.ValidationErrors;
import com.daqem.yamlconfig.api.config.entry.IDateTimeConfigEntry;
import com.daqem.yamlconfig.impl.config.entry.DateTimeConfigEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.time.LocalDateTime;
import java.util.List;

public class DateTimeConfigEntryComponent extends BaseConfigEntryComponent<DateTimeConfigEntry> {

    private final EditBoxWidget editBoxWidget;

    public DateTimeConfigEntryComponent(String key, DateTimeConfigEntry configEntry) {
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
                try {
                    IDateTimeConfigEntry.DATE_TIME_FORMATTER.parse(input);
                } catch (Exception e) {
                    list.add(ValidationErrors.invalidDateTime("yyyy-MM-dd HH:mm:ss"));
                }
                return list;
            }
        };

        editBoxWidget.setValue(IDateTimeConfigEntry.DATE_TIME_FORMATTER.format(configEntry.get()));
        editBoxWidget.setMaxLength(19);

        this.addWidget(this.editBoxWidget);
    }

    @Override
    public boolean isOriginalValue() {
        return this.getConfigEntry().getDefaultValue().format(IDateTimeConfigEntry.DATE_TIME_FORMATTER).equals(this.editBoxWidget.getValue());
    }

    @Override
    public void resetValue() {
        this.editBoxWidget.setValue(this.getConfigEntry().getDefaultValue().format(IDateTimeConfigEntry.DATE_TIME_FORMATTER));
    }

    @Override
    public void applyValue() {
        if (this.editBoxWidget.hasInputValidationErrors()) return;
        this.getConfigEntry().set(LocalDateTime.parse(this.editBoxWidget.getValue(), IDateTimeConfigEntry.DATE_TIME_FORMATTER));
    }

    @Override
    public boolean hasValidationErrors() {
        return this.editBoxWidget.hasInputValidationErrors();
    }
}
