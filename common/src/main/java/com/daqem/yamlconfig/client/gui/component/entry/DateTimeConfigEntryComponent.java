package com.daqem.yamlconfig.client.gui.component.entry;

import com.daqem.uilib.client.gui.component.io.TextBoxComponent;
import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.api.config.entry.IDateTimeConfigEntry;
import com.daqem.yamlconfig.impl.config.entry.DateTimeConfigEntry;
import net.minecraft.network.chat.Component;

import java.time.LocalDateTime;
import java.util.List;

public class DateTimeConfigEntryComponent extends BaseConfigEntryComponent<DateTimeConfigEntryComponent, DateTimeConfigEntry> {

    private final TextBoxComponent textBoxComponent;

    public DateTimeConfigEntryComponent(String key, DateTimeConfigEntry configEntry) {
        super(key, configEntry, 0, 0, DEFAULT_HEIGHT);

        this.textBoxComponent = new TextBoxComponent(KEY_WIDTH + GAP_WIDTH, 0, VALUE_WIDTH, DEFAULT_HEIGHT, configEntry.get().format(IDateTimeConfigEntry.DATE_TIME_FORMATTER)) {
            @Override
            public List<Component> validateInput(String input) {
                List<Component> list = super.validateInput(input);
                try {
                    IDateTimeConfigEntry.DATE_TIME_FORMATTER.parse(input);
                } catch (Exception e) {
                    list.add(YamlConfig.translatable("gui.validation_error.invalid_date_time", "yyyy-MM-dd HH:mm:ss"));
                }
                return list;
            }
        };

        textBoxComponent.setMaxLength(19);
    }

    @Override
    public void startRenderable() {
        this.addChild(this.textBoxComponent);
        super.startRenderable();
    }

    @Override
    public boolean isOriginalValue() {
        return this.getConfigEntry().get().format(IDateTimeConfigEntry.DATE_TIME_FORMATTER).equals(this.textBoxComponent.getValue());
    }

    @Override
    public void resetValue() {
        this.textBoxComponent.setValue(this.getConfigEntry().get().format(IDateTimeConfigEntry.DATE_TIME_FORMATTER));
    }

    @Override
    public void applyValue() {
        if (this.textBoxComponent.hasInputValidationErrors()) return;
        this.getConfigEntry().set(LocalDateTime.parse(this.textBoxComponent.getValue(), IDateTimeConfigEntry.DATE_TIME_FORMATTER));
    }
}
