package com.daqem.yamlconfig.client.gui.component.entry;

import com.daqem.uilib.client.gui.component.io.TextBoxComponent;
import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.impl.config.entry.StringConfigEntry;
import net.minecraft.network.chat.Component;

import java.util.List;

public class StringConfigEntryComponent extends BaseConfigEntryComponent<StringConfigEntryComponent, StringConfigEntry> {

    private final TextBoxComponent textBoxComponent;

    public StringConfigEntryComponent(String key, StringConfigEntry configEntry) {
        super(key, configEntry, 0, 0, DEFAULT_HEIGHT);

        this.textBoxComponent = new TextBoxComponent(KEY_WIDTH + GAP_WIDTH, 0, VALUE_WIDTH, DEFAULT_HEIGHT, this.getConfigEntry().get()) {
            @Override
            public List<Component> validateInput(String input) {
                List<Component> list = super.validateInput(input);
                if (input.length() < configEntry.getMinLength()) {
                    list.add(YamlConfig.translatable("gui.validation_error.min_length", configEntry.getMinLength()));
                }
                if (configEntry.getPattern() != null && !input.matches(configEntry.getPattern())) {
                    list.add(YamlConfig.translatable("gui.validation_error.pattern", configEntry.getPattern()));
                }
                if (!configEntry.getValidValues().isEmpty() && !configEntry.getValidValues().contains(input)) {
                    list.add(YamlConfig.translatable("gui.validation_error.valid_values", configEntry.getValidValues()));
                }
                return list;
            }
        };

        textBoxComponent.setMaxLength(configEntry.getMaxLength());
    }

    @Override
    public void startRenderable() {
        this.addChild(this.textBoxComponent);
        super.startRenderable();
    }

    @Override
    public boolean isOriginalValue() {
        return this.getConfigEntry().get().equals(this.textBoxComponent.getValue());
    }

    @Override
    public void resetValue() {
        this.textBoxComponent.setValue(this.getConfigEntry().get());
    }
}
